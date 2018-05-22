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
import it.polimi.se2018.utils.exceptions.TimeOutException;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.time.Duration;
import java.util.*;

public class CLIClientView implements ResponseHandler, ClientView, Serializable, Timing {
    private final transient int playerID;
    private transient ClientConnection clientConnection;
    private final transient Scanner scanner;
    private transient CLIInput cliInput;
    private transient ToolCardPlayerInput toolCardPlayerInput;
    WaitingThread clock;

    public CLIClientView(int playerID) {
        this.playerID = playerID;
        scanner = new Scanner(System.in);
        cliInput = new CLIInput(playerID);
        toolCardPlayerInput = new ToolCardPlayerInput(playerID, cliInput);
        clock = null;
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
    //aggiorno il modelview e stampo a schermo la tua e il roundtracker
    public void handleResponse(ModelViewResponse modelViewResponse) {
        cliInput.setBoard(modelViewResponse.getModelView());
        cliInput.printDraftPool();
        checkIsYourTurn();
    }

    //prints the text message
    @Override
    //eccezioni da printare
    public void handleResponse(TextResponse textResponse) {
        cliInput.print(textResponse.getMessage());
        checkIsYourTurn();
    }

    @Override
    public void handleResponse(InputResponse inputResponse) {
        cliInput.print("Color of the die is " + inputResponse.getColor());
        try {
            int choice = cliInput.getValueDie();
            try {
                clientConnection.sendMessage(new InputMessage(playerID, choice));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        catch (TimeOutException e) {
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
            clientConnection.sendMessage(new SetupMessage(playerID,setupResponse.getWindows().get(windowNumber)));
            cliInput.print("Window sent. Waiting for other players to choose.");
        }
        catch (RemoteException e) {
        }
    }

    private void checkIsYourTurn() {
        if (playerID == cliInput.getBoard().getCurrentPlayerID()) {
            Duration timeout = Duration.ofSeconds(60);
            clock = new WaitingThread(timeout, this);
            clock.start();
            try {
                chooseAction();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        else  {
            clock = null;
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
        //Choose the action to do DraftDie, UseToolcard, PlaceDie, PassTurn
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
                    selectToolcard();
                    break;
                case 5:
                    passTurn();
                    break;
                default:
                    break;
            }
        }
        catch (TimeOutException e) {
        }
    }

    private int selectWindow(List<Window> windows) {
        int choice = -1;
        while (choice < 1 || choice > 4) {
            cliInput.print("Select your Window");
            int i = 1;
            for(Window window : windows) {
                cliInput.print("Press [" + i + "] to select this window");
                cliInput.print(window.getTitle());
                cliInput.printPlayerWindow(window.modelViewCopy());
                cliInput.print("The level of the window is " + window.getLevel() + "\n");
                i++;
            }
            choice = scanner.nextInt();
        }
        return choice;
    }

    private void passTurn() throws RemoteException{
        clock.interrupt();
        clientConnection.sendMessage(new PassMessage(playerID));
    }

    private void draftDie() throws RemoteException, TimeOutException {
        cliInput.print("Choose the die to draft.");
        int index = cliInput.getDraftPoolPosition();
        if (index != -1) clientConnection.sendMessage(new DraftMessage(playerID, index));
        else chooseAction();
    }

    private void selectToolcard() throws RemoteException, TimeOutException {
        int toolCard = cliInput.getToolCard();
        //if you click 3, you choose to go for another action
        if (toolCard != 3)
            clientConnection.sendMessage(new ToolCardRequestMessage(playerID, toolCard));
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
        catch (TimeOutException e) {}
    }

    private void placeDie() throws RemoteException, TimeOutException {
        cliInput.print("Choose the position where you want to put the drafted die");
        Coordinate coordinate = cliInput.getCoordinate();
        if (!coordinate.equals(new Coordinate(-1, -1))) {
            clientConnection.sendMessage(new PlaceMessage(playerID, coordinate));
        }
        else { chooseAction(); }
    }

    @Override
    public void onTimesUp() {
        clock = null;
        cliInput.getBoard().setCurrentPlayerID(0);
        cliInput.print("Time is up, press any number to continue");
    }
}
