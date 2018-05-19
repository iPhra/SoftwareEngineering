package it.polimi.se2018.view.cli;

import it.polimi.se2018.model.ModelView;
import it.polimi.se2018.model.Window;
import it.polimi.se2018.model.objectives.privateobjectives.PrivateObjective;
import it.polimi.se2018.model.objectives.publicobjectives.PublicObjective;
import it.polimi.se2018.model.toolcards.ToolCard;
import it.polimi.se2018.client.network.ClientConnection;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.*;
import it.polimi.se2018.network.messages.responses.*;
import it.polimi.se2018.view.ClientView;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;

public class CLIClientView implements ResponseHandler, ClientView, Serializable {
    private final transient int playerID;
    private transient List<String> playersName;
    private transient ClientConnection clientConnection;
    private transient ModelView board;
    private final transient Scanner scanner;
    private transient List<ToolCard> toolCards;
    private transient PrivateObjective privateObjective;
    private transient List<PublicObjective> publicObjectives;
    private transient CLIInput cliInput;
    private transient ToolCardPlayerInput toolCardPlayerInput;

    public CLIClientView(int playerID) {
        this.playerID = playerID;
        scanner = new Scanner(System.in);
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

    @Override
    public void handleNetworkInput(Message message) {
        //not implemented client-side
    }

    //updates the board
    @Override
    //aggiorno il modelview e stampo a schermo la tua e il roundtracker
    public void handleResponse(ModelViewResponse modelViewResponse) {
        this.board = modelViewResponse.getModelView();
        cliInput.printRoundTracker();
    }

    //prints the text message
    @Override
    //eccezioni da printare
    public void handleResponse(TextResponse textResponse) {
        cliInput.print(textResponse.getMessage());
    }

    @Override
    public void handleResponse(InputResponse inputResponse) {
        cliInput.print("Color of the die is " + inputResponse.getColor());
        int choice = cliInput.getValueDie();
        try {
            clientConnection.sendMessage(new InputMessage(playerID, choice));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    //chamo inizio turno
    public void handleResponse(TurnStartResponse turnStartResponse) {
        try {
            chooseAction();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleResponse(ToolCardResponse toolCardResponse) {
        try {
            useToolcard(toolCardResponse.getToolCardNumber());
        }
        catch (RemoteException ignored) {
        }
    }

    @Override
    public void handleResponse(SetupResponse setupResponse) {
        //cliInput.setPlayersName(setupResponse.PlayersName);
        cliInput.setPrivateObjective(setupResponse.getPrivateObjective());
        cliInput.setPublicObjectives(setupResponse.getPublicObjectives());
        cliInput.setToolCards(setupResponse.getToolCards());
        selectWindow(setupResponse.getWindows());
    }

    private List<Integer> actionPossible() {
        List<Integer> choosable = new ArrayList<>();
        choosable.add(1);
        if (!board.hasDraftedDie()) {
            choosable.add(2);
        }
        if (board.hasDieInHand()) {
            choosable.add(3);
        }
        if (!board.hasUsedCard()) {
            choosable.add(4);
        }
        choosable.add(5);
        return choosable;
    }

    private void printActionPermitted(List<Integer> choosable) {
        for (int i : choosable) {
            cliInput.print("[1] Ask information of the game");
            if (i == 1) {
                cliInput.print("[2] Draft a die");
            }
            if (i == 2) {
                cliInput.print("[3] Place the drafted die");
            }
            if (i == 3) {
                cliInput.print("[4] Select a toolcard");
            }
            cliInput.print("[5] Pass turn");
        }
    }


    private void chooseAction() throws RemoteException{
        //Choose the action to do DraftDie, UseToolcard, PlaceDie, PassTurn
        int choice = -1;
        List<Integer> choosable = actionPossible();
        cliInput.print("It's your turn, choose your action");
        while (!choosable.contains(choice)) {
            printActionPermitted(choosable);
            choice = scanner.nextInt();
        }
        switch (choice) {
            case 1 : askInformation(); break;
            case 2 : draftDie(); break;
            case 3 : placeDie(); break;
            case 4 : selectToolcard(); break;
            case 5 : passTurn(); break;
            default : break;
        }
    }

    private void selectWindow(List<Window> windows) {
        int choice = -1;
        while (choice < 0 || choice > 5) {
            cliInput.print("Select your window");
            int i = 1;
            for(Window window : windows) {
                cliInput.print("Press [" + i + "] to select this window");
                cliInput.printPlayerWindow(window.modelViewCopy());
                cliInput.print("The level of the window is\b" + window.getLevel());
                i++;
            }
            cliInput.print("[5] Ask information");
            choice = scanner.nextInt();
            if (choice == 1) {
                this.askInformation();
            }
        }
    }

    private void passTurn() throws RemoteException{
        clientConnection.sendMessage(new PassMessage(playerID));
    }

    private void draftDie() throws RemoteException {
        cliInput.print("Choose the die to draft.");
        int index = cliInput.getPositionDraftPool();
        clientConnection.sendMessage(new DraftMessage(playerID, index));
    }

    private void selectToolcard() throws RemoteException {
        int toolCard = cliInput.getToolCard();
        clientConnection.sendMessage(new ToolCardRequestMessage(playerID, toolCard));
    }

    private void useToolcard(int indexOfToolCard) throws RemoteException {
        ToolCard toolCard = cliInput.getToolCards().get(indexOfToolCard);
        ToolCardMessage toolCardMessage = toolCard.handleView(toolCardPlayerInput, indexOfToolCard);
        clientConnection.sendMessage(toolCardMessage);
    }

    private void placeDie() throws RemoteException {
        cliInput.print("Choose the position where put the drafted die");
        Coordinate coordinate = cliInput.getCoordinate();
        clientConnection.sendMessage(new PlaceMessage(playerID, coordinate));
    }

    private void askInformation() {
        int choice = -1;
        cliInput.print("Choose the information you need.");
        while (choice < 1 || choice > 9) {
            cliInput.print("[1] Print your window");
            cliInput.print("[2] Print window of a player");
            cliInput.print("[3] Print draft pool");
            cliInput.print("[4] Print round tracker");
            cliInput.print("[5] Print toolcard");
            cliInput.print("[6] Print public objective");
            cliInput.print("[7] Print your private objecgive");
            cliInput.print("[8] Print your favor points");
            choice = scanner.nextInt();
        }
        switch (choice) {
            case 1: cliInput.printYourWindow();
                    break;
            case 2: cliInput.getPlayerWindow();
                    break;
            case 3: cliInput.printDraftPool();
                    break;
            case 4: cliInput.printRoundTracker();
                    break;
            case 5: cliInput.printToolcard();
                    break;
            case 6: cliInput.printPublicObjective();
                    break;
            case 7: cliInput.printPrivateObjective();
                    break;
            case 8: cliInput.printYourFavorPoint();
                    break;
            default: break;
        }
    }
}
