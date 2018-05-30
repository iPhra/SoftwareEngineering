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

public class CLIClientView implements ResponseHandler, Observer<Response>, Stopper {
    private final int playerID;

    private final CLIInput cliInput;
    private final ToolCardPlayerInput toolCardPlayerInput;
    private WaitingThread clock;

    public CLIClientView(int playerID) {
        this.playerID = playerID;
        cliInput = new CLIInput(playerID);
        toolCardPlayerInput = new ToolCardPlayerInput(playerID, cliInput);
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
        cliInput.setBoard(modelViewResponse.getModelView());
        cliInput.printDraftPool();
        if (playerID == cliInput.getBoard().getCurrentPlayerID()) {
            startTimer(6000);
            try {
                chooseAction();
            } catch (RemoteException e) {
                Logger logger = Logger.getAnonymousLogger();
                logger.log(Level.ALL,e.getMessage());
            }
        }
        else  {
            cliInput.print("\n" + modelViewResponse.getDescription());
            cliInput.print("It's not your turn. You can't do anything!");
        }
    }

    //prints the text message
    @Override
    public void handleResponse(TextResponse textResponse) {
        cliInput.print(textResponse.getMessage()+"\n");
        try {
            chooseAction();
        } catch (RemoteException e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }

    @Override
    public void handleResponse(InputResponse inputResponse) {
        cliInput.print("Color of the die is " + inputResponse.getColor());
        try {
            int choice = cliInput.getDieValue();
            cliInput.handleNetworkOutput(new InputMessage(playerID, cliInput.getBoard().getStateID(), choice));
        }
        catch (HaltException e) {
            cliInput.setStopAction(false);
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
        cliInput.setPrivateObjective(setupResponse.getPrivateObjective());
        cliInput.setPublicObjectives(setupResponse.getPublicObjectives());
        cliInput.setToolCards(setupResponse.getToolCards());
        cliInput.print("");
        cliInput.printPrivateObjective();
        cliInput.printPublicObjective();
        int windowNumber;
        try {
            windowNumber = selectWindow(setupResponse.getWindows())-1;
            cliInput.handleNetworkOutput(new SetupMessage(playerID,0,setupResponse.getWindows().get(windowNumber)));
            clock.interrupt();
            cliInput.print("Window sent. Waiting for other players to choose." + "\n");
        } catch (HaltException e) {
            cliInput.setStopAction(false);
        }
    }

    @Override
    public void handleResponse(ScoreBoardResponse scoreBoardResponse){
        cliInput.print("Final score:");
        for (int i = 0; i < scoreBoardResponse.getSortedPlayersNames().size(); i++) {
            cliInput.print(i+1 + "  Player: " + scoreBoardResponse.getSortedPlayersNames().get(i) + "     Score: " + scoreBoardResponse.getSortedPlayersScores().get(i));
        }
        cliInput.stopConnection();
    }

    /**
     * This method is used by the Server to notify that a player has disconnected
     *
     * @param disconnectionResponse contains a notification message
     */
    @Override
    public void handleResponse(DisconnectionResponse disconnectionResponse) {
        new Thread(new AsyncPrinter(cliInput,this,disconnectionResponse)).start();
    }

    private List<Integer> actionPossible() {
        List<Integer> choosable = new ArrayList<>();
        if (!cliInput.getBoard().hasDraftedDie()) {
            choosable.add(2);
        }
        if (cliInput.getBoard().hasDieInHand()) {
            choosable.add(3);
        }
        if (!cliInput.getBoard().hasUsedCard()) {
            choosable.add(4);
        }
        choosable.add(5);
        return choosable;
    }

    private void printActionPermitted(List<Integer> choosable) {
        cliInput.print("[1] Print the state of the game");
        for (int i : choosable) {
            if (i==2) {
                cliInput.print("[2] Draft a die");
            }
            if (i==3) {
                cliInput.print("[3] Place the drafted die");
            }
            if (i==4) {
                cliInput.print("[4] Select a Tool Card");
            }
            if (i==5) cliInput.print("[5] Pass");
        }
    }
    
    private void chooseAction() throws RemoteException{
        try {
            int choice = -1;
            List<Integer> choosable = actionPossible();
            cliInput.print("It's your turn, choose your action");
            if (cliInput.getBoard().hasDieInHand()) {
                cliInput.print("You have this die in your hand:");
                cliInput.printDraftPoolDice(cliInput.getBoard().getDieInHand());
            }
            cliInput.print("\n");
            while (!choosable.contains(choice)) {
                printActionPermitted(choosable);
                choice = cliInput.takeInput(0, 10);
                if (choice == 1) cliInput.printModel();
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
                    cliInput.print("Error!");
                    break;
            }
        }
        catch (HaltException e) {
            cliInput.setStopAction(false);
        }
    }

    private int selectWindow(List<Window> windows) throws HaltException {
        int choice;
        boolean iterate = true;
        cliInput.printSetupWindows(windows);
        do {
            choice = cliInput.takeInput(1, 4);
            if (choice<1 || choice>4) cliInput.print("Type a number between 1 and 4");
            else iterate = false;
        }
        while(iterate);
        cliInput.print("\n");
        return choice;
    }

    private void passTurn() {
        clock.interrupt();
        cliInput.handleNetworkOutput(new PassMessage(playerID,cliInput.getBoard().getStateID()));
    }

    private void draftDie() throws RemoteException, HaltException {
        cliInput.print("Choose the die to draft.");
        int index = cliInput.getDraftPoolPosition();
        if (index != -1) cliInput.handleNetworkOutput(new DraftMessage(playerID, cliInput.getBoard().getStateID(),index));
        else chooseAction();
    }

    private void selectToolCard() throws HaltException {
        int toolCard = cliInput.getToolCard();
        if (toolCard != 3)
            cliInput.handleNetworkOutput(new ToolCardRequestMessage(playerID, cliInput.getBoard().getStateID(), toolCard));
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
        ToolCard toolCard = cliInput.getToolCards().get(indexOfToolCard);
        try {
            ToolCardMessage toolCardMessage = toolCard.handleView(toolCardPlayerInput, indexOfToolCard);
            if (!toolCardMessage.isToDismiss()) cliInput.handleNetworkOutput(toolCardMessage);
            else chooseAction();
        }
        catch (HaltException e) {
            cliInput.setStopAction(false);
        }
    }

    private void placeDie() throws RemoteException, HaltException {
        cliInput.print("Choose the position where you want to put the drafted die");
        Coordinate coordinate = cliInput.getCoordinate();
        if (!coordinate.equals(new Coordinate(-1, -1))) {
            cliInput.handleNetworkOutput(new PlaceMessage(playerID, cliInput.getBoard().getStateID(), coordinate));
        }
        else { chooseAction(); }
    }

    @Override
    public void halt(String message) {
        cliInput.setStopAction(true);
        cliInput.print(message + ", press 1 to continue" + "\n");
    }

}
