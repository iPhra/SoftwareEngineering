package it.polimi.se2018.client.view.cli;

import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.*;
import it.polimi.se2018.network.messages.responses.*;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.utils.Stopper;
import it.polimi.se2018.utils.WaitingThread;
import it.polimi.se2018.utils.exceptions.HaltException;

import java.rmi.RemoteException;
import java.time.Duration;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CLIController implements ResponseHandler, Observer<Response>, Stopper {
    private final int playerID;
    private final CLIView cliView;
    private final ToolCardPlayerInput toolCardPlayerInput;
    private WaitingThread clock;

    public CLIController(int playerID, CLIView cliView) {
        this.playerID = playerID;
        this.cliView = cliView;
        toolCardPlayerInput = new ToolCardPlayerInput(playerID, cliView);
    }

    private void startTimer(int seconds) {
        Duration timeout = Duration.ofSeconds(seconds);
        clock = new WaitingThread(timeout, this);
        clock.start();
    }

    @Override
    //receives input from the network, called by class clientConnection
    public void update(Response response) {
        response.handle(this);
    }

    //updates the board
    @Override
    public void handleResponse(ModelViewResponse modelViewResponse) {
        cliView.setBoard(modelViewResponse.getModelView());
        if (playerID == cliView.getBoard().getCurrentPlayerID()) {
            startTimer(6000);
            try {
                chooseAction();
            } catch (RemoteException e) {
                Logger logger = Logger.getAnonymousLogger();
                logger.log(Level.ALL,e.getMessage());
            }
        }
        else  {
            cliView.print("\n" + modelViewResponse.getDescription());
            cliView.printDraftPool();
            cliView.printPlayersWindows();
            cliView.print("It's not your turn. You can't do anything!");
        }
    }

    //prints the text message
    @Override
    public void handleResponse(TextResponse textResponse) {
        cliView.print(textResponse.getMessage()+"\n");
        try {
            chooseAction();
        } catch (RemoteException e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }

    @Override
    public void handleResponse(InputResponse inputResponse) {
        cliView.print("Color of the die is " + inputResponse.getColor());
        try {
            int choice = cliView.getDieValue();
            cliView.handleNetworkOutput(new InputMessage(playerID, cliView.getBoard().getStateID(), choice));
        }
        catch (HaltException e) {
            cliView.setStopAction(false);
        }
    }

    @Override
    public void handleResponse(ToolCardResponse toolCardResponse) {
        try {
            useToolCard(toolCardResponse.getToolCardNumber());
        }
        catch (RemoteException e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }

    //Set the objective and tool card copy to the value. Ask the window to select
    @Override
    public void handleResponse(SetupResponse setupResponse) {
        startTimer(20);
        cliView.setPrivateObjective(setupResponse.getPrivateObjective());
        cliView.setPublicObjectives(setupResponse.getPublicObjectives());
        cliView.setToolCards(setupResponse.getToolCards());
        cliView.setPlayersNumber(setupResponse.getPlayersNumber());
        cliView.print("");
        cliView.printPrivateObjective();
        cliView.printPublicObjective();
        int windowNumber;
        try {
            windowNumber = selectWindow(setupResponse.getWindows())-1;
            clock.interrupt();
            cliView.print("Window sent. Waiting for other players to choose." + "\n");
            cliView.handleNetworkOutput(new SetupMessage(playerID,0,setupResponse.getWindows().get(windowNumber)));
        } catch (HaltException e) {
            cliView.setStopAction(false);
        }
    }

    @Override
    public void handleResponse(ScoreBoardResponse scoreBoardResponse){
        cliView.print("Final score:");
        for (int i = 0; i < scoreBoardResponse.getSortedPlayersNames().size(); i++) {
            cliView.print(i+1 + "  Player: " + scoreBoardResponse.getSortedPlayersNames().get(i) + "     Score: " + scoreBoardResponse.getSortedPlayersScores().get(i));
        }
        cliView.stopConnection();
    }

    @Override
    public void handleResponse(ReconnectionResponse reconnectionResponse) {
        cliView.setPrivateObjective(reconnectionResponse.getPrivateObjective());
        cliView.setPublicObjectives(reconnectionResponse.getPublicObjectives());
        cliView.setToolCards(reconnectionResponse.getToolCards());
        cliView.setPlayersNumber(reconnectionResponse.getPlayersNumber());
        handleResponse(new ModelViewResponse(reconnectionResponse.getModelView()));
    }

    private List<Integer> actionPossible() {
        List<Integer> choosable = new ArrayList<>();
        if (!cliView.getBoard().hasDraftedDie()) {
            choosable.add(2);
        }
        if (cliView.getBoard().hasDieInHand()) {
            choosable.add(3);
        }
        if (!cliView.getBoard().hasUsedCard()) {
            choosable.add(4);
        }
        choosable.add(5);
        return choosable;
    }

    private void printActionPermitted(List<Integer> choosable) {
        cliView.print("[1] Ask for informations");
        for (int i : choosable) {
            if (i==2) {
                cliView.print("[2] Draft a die");
            }
            if (i==3) {
                cliView.print("[3] Place the drafted die");
            }
            if (i==4) {
                cliView.print("[4] Select a Tool Card");
            }
            if (i==5) cliView.print("[5] Pass");
        }
    }
    
    private void chooseAction() throws RemoteException{
        try {
            int choice = -1;
            List<Integer> choosable = actionPossible();
            cliView.printYourWindow();
            cliView.print("It's your turn, choose your action");
            if (cliView.getBoard().hasDieInHand()) {
                cliView.print("You have this die in your hand:");
                cliView.printDraftPoolDice(cliView.getBoard().getDieInHand());
            }
            cliView.print("\n");
            while (!choosable.contains(choice)) {
                printActionPermitted(choosable);
                choice = cliView.takeInput(0, 10);
                if (choice == 1) askInformation();
            }
            switch (choice) {
                case 2:
                    draftDie();
                    break;
                case 3:
                    placeDie();
                    break;
                case 4:
                    selectToolCard();
                    break;
                case 5:
                    passTurn();
                    break;
                default:
                    cliView.print("Error!");
                    break;
            }
        }
        catch (HaltException e) {
            cliView.setStopAction(false);
        }
    }

    private void askInformation() throws HaltException {
        int choice = -1;
        int top = 8;
        StringBuilder result = new StringBuilder();
        result.append("\n[1] Print the entire state of the game\n");
        result.append("[2] Show your window\n");
        result.append("[3] Show all Windows\n");
        result.append("[4] Show all Tool Cards\n");
        result.append("[5] Show your Private Objective\n");
        result.append("[6] Show all Public Ojectives\n");
        result.append("[7] Show the Draft Pool\n");
        result.append("[8] Show your Favor Points");
        if (cliView.getBoard().hasDieInHand()) {
            result.append("[9] Show the die in your hand");
            top = 9;
        }
        cliView.print(String.valueOf(result));
        choice = cliView.takeInput(1, top);
        switch (choice) {
            case 1: cliView.printModel(); break;
            case 2: cliView.printYourWindow(); break;
            case 3: cliView.printPlayersWindows(); break;
            case 4: cliView.printToolCards(); break;
            case 5: cliView.printPrivateObjective(); break;
            case 6: cliView.printPublicObjective(); break;
            case 7: cliView.printDraftPool(); break;
            case 8: cliView.printFavorPoints(); break;
            case 9: cliView.printDraftPoolDice(cliView.getBoard().getDieInHand()); break;
            default: break;
        }
    }

    private int selectWindow(List<Window> windows) throws HaltException {
        int choice;
        boolean iterate = true;
        cliView.printSetupWindows(windows);
        do {
            choice = cliView.takeInput(1, 4);
            if (choice<1 || choice>4) cliView.print("Type a number between 1 and 4");
            else iterate = false;
        }
        while(iterate);
        cliView.print("\n");
        return choice;
    }

    private void passTurn() {
        clock.interrupt();
        cliView.handleNetworkOutput(new PassMessage(playerID, cliView.getBoard().getStateID()));
    }

    private void draftDie() throws RemoteException, HaltException {
        cliView.printYourWindow();
        cliView.print("Choose the die to draft.");
        int index = cliView.getDraftPoolPosition();
        if (index != -1) cliView.handleNetworkOutput(new DraftMessage(playerID, cliView.getBoard().getStateID(),index));
        else chooseAction();
    }

    int getToolCard() throws HaltException {
        int choice = 4;
        while (choice == 4 || (choice != 3 && !cliView.getBoard().getToolCardUsability().get(choice))) {
            cliView.printToolCards();
            cliView.print("Select the tool card");
            cliView.print("[3] Choose another action");
            cliView.print("[4] Print the state of the game");
            choice = cliView.takeInput(0, 4);
            if (choice == 4) askInformation();
            if (choice >= 0 && choice < 3 && !cliView.getBoard().getToolCardUsability().get(choice)) {
                cliView.print("You can't use the chosen tool card. Please choose another one.");
            }
        }
        return  choice;
    }

    private void selectToolCard() throws HaltException {
        cliView.printYourWindow();
        int toolCard = getToolCard();
        if (toolCard != 3)
            cliView.handleNetworkOutput(new ToolCardRequestMessage(playerID, cliView.getBoard().getStateID(), toolCard));
        else{
            try{
                chooseAction();
            }catch(RemoteException e){
                Logger logger = Logger.getAnonymousLogger();
                logger.log(Level.ALL,e.getMessage());
            }
        }
    }

    private void useToolCard(int indexOfToolCard) throws RemoteException {
        cliView.printYourWindow();
        ToolCard toolCard = cliView.getToolCards().get(indexOfToolCard);
        try {
            ToolCardMessage toolCardMessage = toolCard.handleView(toolCardPlayerInput, indexOfToolCard);
            if (!toolCardMessage.isToDismiss()) cliView.handleNetworkOutput(toolCardMessage);
            else chooseAction();
        }
        catch (HaltException e) {
            cliView.setStopAction(false);
        }
    }

    private void placeDie() throws RemoteException, HaltException {
        cliView.print("Choose the position where you want to put the drafted die");
        Coordinate coordinate = cliView.getCoordinate();
        if (!coordinate.equals(new Coordinate(-1, -1))) {
            cliView.handleNetworkOutput(new PlaceMessage(playerID, cliView.getBoard().getStateID(), coordinate));
        }
        else { chooseAction(); }
    }

    @Override
    public void halt(String message) {
        cliView.setStopAction(true);
        cliView.print(message + ", press 1 to continue" + "\n");
    }

}
