package it.polimi.se2018.client.view.cli;

import it.polimi.se2018.model.Window;
import it.polimi.se2018.model.toolcards.ToolCard;
import it.polimi.se2018.client.network.ClientConnection;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.*;
import it.polimi.se2018.network.messages.responses.*;
import it.polimi.se2018.client.view.ClientView;
import it.polimi.se2018.utils.Timing;
import it.polimi.se2018.utils.WaitingThread;
import it.polimi.se2018.utils.exceptions.TimeoutException;

import java.rmi.RemoteException;
import java.time.Duration;
import java.util.*;

public class CLIClientView implements ResponseHandler, ClientView, Timing {
    private final int playerID;
    private ClientConnection clientConnection;
    private CLIInput cliInput;
    private ToolCardPlayerInput toolCardPlayerInput;
    private WaitingThread clock;

    public CLIClientView(int playerID) {
        this.playerID = playerID;
        cliInput = new CLIInput(playerID);
        toolCardPlayerInput = new ToolCardPlayerInput(playerID, cliInput);
    }

    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    @Override
    //receives input from the network, called by class clientConnection
    public void handleNetworkInput(Response response) {
        response.handle(this);
    }

    //updates the board
    @Override
    public void handleResponse(ModelViewResponse modelViewResponse) {
        cliInput.setBoard(modelViewResponse.getModelView());
        cliInput.printDraftPool();
        playTurn();
    }

    //prints the text message
    @Override
    //eccezioni da printare
    public void handleResponse(TextResponse textResponse) {
        cliInput.print(textResponse.getMessage());
        playTurn();
    }

    @Override
    public void handleResponse(InputResponse inputResponse) {
        cliInput.print("Color of the die is " + inputResponse.getColor());
        try {
            int choice = cliInput.getValueDie();
            try {
                clientConnection.sendMessage(new InputMessage(playerID, cliInput.getBoard().getStateID(), choice));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        catch (TimeoutException e) {
        }
    }

    @Override
    public void handleResponse(ToolCardResponse toolCardResponse) {
        try {
            useToolCard(toolCardResponse.getToolCardNumber());
        }
        catch (RemoteException ignored) {
        }
    }

    //Set the objective and toolcard copy to the value. Ask the window to select
    @Override
    public void handleResponse(SetupResponse setupResponse) {
        cliInput.setPlayersName(setupResponse.getPlayerNames());
        cliInput.setPrivateObjective(setupResponse.getPrivateObjective());
        cliInput.setPublicObjectives(setupResponse.getPublicObjectives());
        cliInput.setToolCards(setupResponse.getToolCards());
        cliInput.printPrivateObjective();
        cliInput.printPublicObjective();
        int windowNumber = selectWindow(setupResponse.getWindows())-1;
        try {
            clientConnection.sendMessage(new SetupMessage(playerID,0,setupResponse.getWindows().get(windowNumber)));
            cliInput.print("Window sent. Waiting for other players to choose.");
        }
        catch (RemoteException e) {
        }
    }

    @Override
    public void handleResponse(ScoreBoardResponse scoreBoardResponse){
        for (int i = 0; i < scoreBoardResponse.getSortedPlayersNames().size(); i++) {
            cliInput.print(i+1 + "  player: " + scoreBoardResponse.getSortedPlayersNames().get(i) + "  score: " + scoreBoardResponse.getSortedPlayersScores().get(i));
        }
        clientConnection.stop();
    }

    private void playTurn() {
        if (playerID == cliInput.getBoard().getCurrentPlayerID()) {
            Duration timeout = Duration.ofSeconds(6000);
            clock = new WaitingThread(timeout, this);
            clock.start();
            try {
                chooseAction();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        else  {
            cliInput.print("It's not your turn. You can't do anything!");
        }
    }

    private List<Integer> actionPossible() {
        List<Integer> choosable = new ArrayList<>();
        choosable.add(1);
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
        for (int i : choosable) {
            if (i == 1) cliInput.print("[1] Ask an information on the game");
            if (i == 2) {
                cliInput.print("[2] Draft a die");
            }
            if (i == 3) {
                cliInput.print("[3] Place the drafted die");
            }
            if (i == 4) {
                cliInput.print("[4] Select a Tool Card");
            }
            if (i == 5) cliInput.print("[5] Pass");
        }
    }


    private void chooseAction() throws RemoteException{
        try {
            int choice = -1;
            List<Integer> choosable = actionPossible();
            cliInput.print("It's your turn, choose your action");
            if (cliInput.getBoard().hasDieInHand()) {
                cliInput.print("You have this die in your hand:");
                cliInput.printDieExtended(cliInput.getBoard().getDieInHand());
            }
            cliInput.print("");
            while (!choosable.contains(choice) || choice == 1) {
                printActionPermitted(choosable);
                choice = cliInput.takeInput();
                if (choice == 1) cliInput.askInformation();
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
                    break;
            }
        }
        catch (TimeoutException e) {
        }
    }

    private int selectWindow(List<Window> windows) {
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        boolean iterate = true;
        cliInput.print("Select your Window");
        for(int i=1; i<windows.size(); i++) {
            cliInput.print("Press [" + i + "] to select this window");
            cliInput.print(windows.get(i).getTitle());
            cliInput.printPlayerWindow(windows.get(i).modelViewCopy());
            cliInput.print("The level of the window is " + windows.get(i).getLevel() + "\n");
        }
        do {
            try {
                choice = scanner.nextInt();
                if (choice<1 || choice>4) cliInput.print("Type a number between 1 and 4");
                else iterate = false;
            } catch (InputMismatchException e) {
                cliInput.print("Input is invalid");
                scanner.nextLine();
            }
        }
        while(iterate);
        return choice;
    }

    private void passTurn() throws RemoteException{
        clock.interrupt();
        clientConnection.sendMessage(new PassMessage(playerID,cliInput.getBoard().getStateID()));
    }

    private void draftDie() throws RemoteException, TimeoutException {
        cliInput.print("Choose the die to draft.");
        int index = cliInput.getDraftPoolPosition();
        if (index != -1) clientConnection.sendMessage(new DraftMessage(playerID, cliInput.getBoard().getStateID(),index));
        else chooseAction();
    }

    private void selectToolCard() throws RemoteException, TimeoutException {
        int toolCard = cliInput.getToolCard();
        //if you click 3, you choose to go for another action
        if (toolCard != 3)
            clientConnection.sendMessage(new ToolCardRequestMessage(playerID, cliInput.getBoard().getStateID(), toolCard));
        else{
            try{
                chooseAction();
            }catch(RemoteException e){
                System.err.println(e.getMessage());
            }
        }
    }

    private void useToolCard(int indexOfToolCard) throws RemoteException {
        ToolCard toolCard = cliInput.getToolCards().get(indexOfToolCard);
        try {
            ToolCardMessage toolCardMessage = toolCard.handleView(toolCardPlayerInput, indexOfToolCard);
            if (!toolCardMessage.isToDismiss()) clientConnection.sendMessage(toolCardMessage);
            else chooseAction();
        }
        catch (TimeoutException e) {}
    }

    private void placeDie() throws RemoteException, TimeoutException {
        cliInput.print("Choose the position where you want to put the drafted die");
        Coordinate coordinate = cliInput.getCoordinate();
        if (!coordinate.equals(new Coordinate(-1, -1))) {
            clientConnection.sendMessage(new PlaceMessage(playerID, cliInput.getBoard().getStateID(), coordinate));
        }
        else { chooseAction(); }
    }

    @Override
    public void wakeUp() {
        clock = null;
        cliInput.getBoard().setCurrentPlayerID(0);
        cliInput.print("Time is up, press any number to continue");
    }
}
