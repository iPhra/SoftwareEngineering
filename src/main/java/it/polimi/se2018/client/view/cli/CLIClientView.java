package it.polimi.se2018.client.view.cli;

import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;
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
        if (playerID == cliInput.getBoard().getCurrentPlayerID()) {
            startTimer(6000);
            try {
                chooseAction();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        else  {
            cliInput.print(modelViewResponse.getDescription());
            cliInput.print("It's not your turn. You can't do anything!");
        }
    }

    //prints the text message
    @Override
    //eccezioni da printare
    public void handleResponse(TextResponse textResponse) {
        cliInput.print(textResponse.getMessage());
        try {
            chooseAction();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleResponse(InputResponse inputResponse) {
        cliInput.print("Color of the die is " + inputResponse.getColor());
        try {
            int choice = cliInput.getValueDie();
            clientConnection.sendMessage(new InputMessage(playerID, cliInput.getBoard().getStateID(), choice));
        }
        catch (TimeoutException e) {
            cliInput.setHasToChange(false);
            e.printStackTrace();
        }
    }

    @Override
    public void handleResponse(ToolCardResponse toolCardResponse) {
        try {
            useToolCard(toolCardResponse.getToolCardNumber());
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //Set the objective and toolcard copy to the value. Ask the window to select
    @Override
    public void handleResponse(SetupResponse setupResponse) {
        startTimer(20);
        cliInput.setPlayersName(setupResponse.getPlayerNames());
        cliInput.setPrivateObjective(setupResponse.getPrivateObjective());
        cliInput.setPublicObjectives(setupResponse.getPublicObjectives());
        cliInput.setToolCards(setupResponse.getToolCards());
        cliInput.printPrivateObjective();
        cliInput.printPublicObjective();
        int windowNumber = 0;
        try {
            windowNumber = selectWindow(setupResponse.getWindows())-1;
            clientConnection.sendMessage(new SetupMessage(playerID,0,setupResponse.getWindows().get(windowNumber)));
            cliInput.print("Window sent. Waiting for other players to choose.");
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleResponse(ScoreBoardResponse scoreBoardResponse){
        for (int i = 0; i < scoreBoardResponse.getSortedPlayersNames().size(); i++) {
            cliInput.print(i+1 + "  player: " + scoreBoardResponse.getSortedPlayersNames().get(i) + "  score: " + scoreBoardResponse.getSortedPlayersScores().get(i));
        }
        clientConnection.stop();
    }

    private List<String> actionPossible() {
        List<String> choosable = new ArrayList<>();
        if (!cliInput.getBoard().hasDraftedDie()) {
            choosable.add("d");
        }
        if (cliInput.getBoard().hasDieInHand()) {
            choosable.add("m");
        }
        if (!cliInput.getBoard().hasUsedCard()) {
            choosable.add("t");
        }
        choosable.add("p");
        return choosable;
    }

    private void printActionPermitted(List<String> choosable) {
        cliInput.print("[I] Ask an information on the game"); //i
        for (String i : choosable) {
            if (i.equals("d")) {
                cliInput.print("[D] Draft a die"); //d
            }
            if (i.equals("m")) {
                cliInput.print("[M] Place the drafted die");
            }
            if (i.equals("t")) {
                cliInput.print("[T] Select a Tool Card"); //t
            }
            if (i.equals("p")) cliInput.print("[P] Pass"); //p
        }
    }
    
    private void chooseAction() throws RemoteException{
        try {
            String choice = "";
            List<String> choosable = actionPossible();
            cliInput.print("It's your turn, choose your action");
            if (cliInput.getBoard().hasDieInHand()) {
                cliInput.print("You have this die in your hand:");
                cliInput.printDieExtended(cliInput.getBoard().getDieInHand());
            }
            cliInput.print("");
            while (!choosable.contains(choice)) {
                printActionPermitted(choosable);
                choice = cliInput.takeAction().toLowerCase();
                if (choice.equals("i") || choice.equals("I")) cliInput.askInformation();
            }
            switch (choice) {
                case "d":
                    draftDie();

                    break;
                case "m":
                    placeDie();

                    break;
                case "t":
                    selectToolCard();

                    break;
                case "p":
                    passTurn();

                    break;
                default:
                    cliInput.print("Error!");
                    break;
            }
        }
        catch (TimeoutException e) {
            cliInput.setHasToChange(false);
            e.printStackTrace();
        }
    }

    private int selectWindow(List<Window> windows) throws TimeoutException {
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        boolean iterate = true;
        cliInput.print("Select your Window");
        for(int i=0; i<windows.size(); i++) {
            cliInput.print("Press [" + (i+1) + "] to select this window");
            cliInput.print(windows.get(i).getTitle());
            cliInput.printPlayerWindow(windows.get(i).modelViewCopy());
            cliInput.print("The level of the window is " + windows.get(i).getLevel() + "\n");
        }
        do {
                choice = cliInput.takeInput();
                if (choice<1 || choice>4) cliInput.print("Type a number between 1 and 4");
                else iterate = false;
        }
        while(iterate);
        return choice;
    }

    private void passTurn() {
        clock.interrupt();
        clientConnection.sendMessage(new PassMessage(playerID,cliInput.getBoard().getStateID()));
    }

    private void draftDie() throws RemoteException, TimeoutException {
        cliInput.print("Choose the die to draft.");
        int index = cliInput.getDraftPoolPosition();
        if (index != -1) clientConnection.sendMessage(new DraftMessage(playerID, cliInput.getBoard().getStateID(),index));
        else chooseAction();
    }

    private void selectToolCard() throws TimeoutException {
        int toolCard = cliInput.getToolCard();
        //if you click 3, you choose to go for another action
        if (toolCard != 3)
            clientConnection.sendMessage(new ToolCardRequestMessage(playerID, cliInput.getBoard().getStateID(), toolCard));
        else{
            try{
                chooseAction();
            }catch(RemoteException e){
                e.printStackTrace();
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
        catch (TimeoutException e) {
            cliInput.setHasToChange(false);
            e.printStackTrace();
        }
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
        cliInput.setHasToChange(true);
        cliInput.print("Time is up, press 1 to continue");
    }
}
