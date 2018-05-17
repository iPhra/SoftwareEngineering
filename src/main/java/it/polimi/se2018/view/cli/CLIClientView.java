package it.polimi.se2018.view.cli;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.ModelView;
import it.polimi.se2018.model.objectives.privateobjectives.PrivateObjective;
import it.polimi.se2018.model.objectives.publicobjectives.PublicObjective;
import it.polimi.se2018.model.toolcards.ToolCard;
import it.polimi.se2018.network.connections.ClientConnection;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.*;
import it.polimi.se2018.network.messages.responses.*;
import it.polimi.se2018.view.ClientView;

import java.rmi.RemoteException;
import java.util.*;

public class CLIClientView implements ResponseHandler, ClientView {
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
        toolCardPlayerInput = new ToolCardPlayerInput();
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

    private List<Integer> actionPossible() {
        List<Integer> choosable = new ArrayList<>();
        if (!board.hasDraftedDie()) {
            choosable.add(1);
        }
        if (board.hasDieInHand()) {
            choosable.add(2);
        }
        if (!board.hasUsedCard()) {
            choosable.add(3);
        }
        choosable.add(4);
        choosable.add(5);
        return choosable;
    }

    public void printActionPermitted(List<Integer> choosable) {
        for (int i : choosable) {
            if (i == 1) {
                cliInput.print("1: Draft a die");
            }
            if (i == 2) {
                cliInput.print("2: Place the drafted die");
            }
            if (i == 3) {
                cliInput.print("3: Select a toolcard");
            }
            cliInput.print("4: Pass turn");
            cliInput.print("5: Ask for information of the game");
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
        if (choice == 1) {
            draftDie();
        } else if (choice == 2) {
            placeDie();
        } else if (choice == 3) {
            selectToolcard();
        } else if (choice == 4) {
            passTurn();
        } else if (choice == 5) {
            askInformation();
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
            cliInput.print("1: Print your window");
            cliInput.print("2: Print window of a player");
            cliInput.print("3: Print draft pool");
            cliInput.print("4: Print Round Tracker");
            cliInput.print("5: Print toolcard");
            cliInput.print("6: Print public objective");
            cliInput.print("7: Print your private objecgive");
            cliInput.print("8: Print your favor points");
            choice = scanner.nextInt();
        }
        switch (choice) {
            case 1: cliInput.printYourMap();
                    break;
            case 2: cliInput.printPlayerMap();
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
