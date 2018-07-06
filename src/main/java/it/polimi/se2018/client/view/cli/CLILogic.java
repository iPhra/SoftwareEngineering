package it.polimi.se2018.client.view.cli;

import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.*;
import it.polimi.se2018.network.messages.responses.sync.*;
import it.polimi.se2018.network.messages.responses.sync.modelupdates.*;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.utils.exceptions.ChangeActionException;
import it.polimi.se2018.utils.exceptions.HaltException;

import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class handle inputs for player and from the server
 */
public class CLILogic implements SyncResponseHandler, Observer<SyncResponse> {
    private final int playerID;
    private final CLIView cliView;
    private final CLIData cliModel;
    private final ToolCardCLI toolCardCLI;

    public CLILogic(CLIView cliView, CLIData cliModel, int playerID) {
        this.playerID = playerID;
        this.cliView = cliView;
        this.cliModel = cliModel;
        toolCardCLI = new ToolCardCLI(playerID, cliView, cliModel);
    }

    /**
     * This method create a list of integer that represents the action possible
     * @return a list di integer. Every integer represent an action possible
     */
    private List<Integer> actionPossible() {
        List<Integer> choosable = new ArrayList<>();
        if (!cliModel.getBoard().hasDraftedDie()) choosable.add(2);
        if (cliModel.getBoard().hasDieInHand()) choosable.add(3);
        if (!cliModel.getBoard().hasUsedCard()) choosable.add(4);
        choosable.add(5);
        return choosable;
    }

    /**
     * This method print to the player the list of action possible and the number
     * to press to do that action
     * @param choosable list of number. Every number represent an action possible
     */
    private void printActionPermitted(List<Integer> choosable) {
        cliView.print("[1] Ask for informations\n");
        for (int i : choosable) {
            if (i==2) cliView.print("[2] Draft a die\n");
            if (i==3) cliView.print("[3] Place the drafted die\n");
            if (i==4) cliView.print("[4] Select a Tool Card\n");
            if (i==5) cliView.print("[5] Pass\n");
        }
    }

    /**
     * This method allow the player to do an action
     * @throws RemoteException is called if RMI connection falls
     */
    private void chooseAction() throws RemoteException{
        try {
            int choice = -1;
            List<Integer> choosable = actionPossible();
            cliModel.showYourWindow();
            cliView.print("It's your turn, choose your action\n");
            if (cliModel.getBoard().hasDieInHand()) {
                showDieInHand();
            }
            cliView.print("\n");
            while (!choosable.contains(choice)) {
                printActionPermitted(choosable);
                choice = cliView.takeInput(0, 10);
                if (choice == 1) askInformation();
            }
            switch (choice) {
                case 2: draftDie(); break;
                case 3: placeDie(); break;
                case 4: selectToolCard(); break;
                case 5: passTurn(); break;
                default: cliView.print("Error!"); break;
            }
        }
        catch (HaltException ignored) {
            //now i can stop the method
        }
    }

    /**
     * This method print to the player his die in hand
     */
    private void showDieInHand() {
        cliView.print("\nYou have this die in your hand: ");
        cliModel.showExtendedDice(cliModel.getBoard().getDieInHand());
    }

    /**
     * This method allow the player to ask information about the state of the game
     * @throws HaltException is called if the timer of the turn is up and client receive the message from the server
     */
    private void askInformation() throws HaltException {
        int choice;
        int top = 8;
        StringBuilder result = new StringBuilder();
        result.append("\n[1] Print the entire state of the game\n");
        result.append("[2] Show all Windows\n");
        result.append("[3] Show your Favor Points\n");
        result.append("[4] Show all Tool Cards\n");
        result.append("[5] Show your Private Objective\n");
        result.append("[6] Show all Public Ojectives\n");
        result.append("[7] Show the Draft Pool\n");
        result.append("[8] Show the Round Tracker\n");
        if (cliModel.getBoard().hasDieInHand()) {
            result.append("[9] Show the die in your hand\n");
            top = 9;
        }
        cliView.print(result.toString());
        choice = cliView.takeInput(1, top);
        switch (choice) {
            case 1: cliModel.showModel(); break;
            case 2: cliModel.showPlayersWindows(); break;
            case 3: cliModel.showFavorPoints(); break;
            case 4: cliModel.showToolCards(); break;
            case 5: cliModel.showPrivateObjective(); break;
            case 6: cliModel.showPublicObjectives(); break;
            case 7: cliModel.showDraftPool(); break;
            case 8: cliModel.showRoundTracker(); break;
            case 9: cliModel.showExtendedDice(cliModel.getBoard().getDieInHand()); break;
            default: break;
        }
    }

    /**
     * This method allow the player to choose the window that he will use for the game
     * @param windows list of windows
     * @return the number of the window chosen
     * @throws HaltException is throwed if the timer is up
     */
    private int selectWindow(List<Window> windows) throws HaltException {
        int choice;
        boolean iterate = true;
        cliModel.showSetupWindows(windows);
        do {
            choice = cliView.takeInput(1, 4);
            if (choice<1 || choice>4) cliView.print("Type a value between 1 and 4\n");
            else iterate = false;
        }
        while(iterate);
        cliView.print("\n");
        return choice;
    }


    private void passTurn() {
        cliView.handleNetworkOutput(new PassMessage(playerID, cliModel.getBoard().getStateID(),false));
    }

    private void draftDie() throws RemoteException, HaltException {
        cliModel.showYourWindow();
        cliView.print("Choose the die to draft\n");
        try {
            int index = cliView.getDraftPoolPosition();
            cliView.handleNetworkOutput(new DraftMessage(playerID, cliModel.getBoard().getStateID(), index));
        }
        catch (ChangeActionException e) {
            chooseAction();
        }
    }

    private int getToolCard() throws HaltException, ChangeActionException {
        int choice = -1;
        boolean loop = true;
        while (loop) {
            cliModel.showToolCards();
            cliView.print("Select the tool card\n");
            cliView.print("[3] Choose another action\n");
            cliView.print("[4] Print the state of the game\n");
            choice = cliView.takeInput(0, 4);
            if (choice == 3) throw new ChangeActionException();
            else if (choice == 4) askInformation();
            else if (!cliModel.getBoard().getToolCardUsability().get(choice))
                cliView.print("You can't use the chosen tool card. Please choose another one.\n");
            else loop = false;
        }
        return choice;
    }

    private void selectToolCard() throws RemoteException, HaltException {
        cliModel.showYourWindow();
        try {
            int toolCard = getToolCard();
            cliView.handleNetworkOutput(new ToolCardRequestMessage(playerID, cliModel.getBoard().getStateID(), toolCard));
        }
        catch(ChangeActionException e) {
            chooseAction();
        }
    }

    private void useToolCard(int toolCardIndex) throws RemoteException {
        ToolCard toolCard = cliModel.getToolCards().get(toolCardIndex);
        try {
            ToolCardMessage toolCardMessage = toolCard.handleView(toolCardCLI, toolCardIndex);
            cliView.handleNetworkOutput(toolCardMessage);
        }
        catch (HaltException ignored) {
            //now i can stop the method
        }
        catch (ChangeActionException e) {
            chooseAction();
        }
    }

    private void placeDie() throws RemoteException, HaltException {
        showDieInHand();
        cliView.print("\nChoose the position where you want to put the drafted die\n\n");
        try {
            Coordinate coordinate = cliView.getCoordinate();
            cliView.handleNetworkOutput(new PlaceMessage(playerID, cliModel.getBoard().getStateID(), coordinate));
        }
        catch (ChangeActionException e) {
            chooseAction();
        }
    }

    private void checkTurn(String description) {
        cliView.print("\n"+description+"\n");
        if (playerID == cliModel.getBoard().getCurrentPlayerID()) {
            try {
                chooseAction();
            } catch (RemoteException e) {
                Logger logger = Logger.getAnonymousLogger();
                logger.log(Level.ALL,e.getMessage());
            }
        }
        else  {
            cliModel.showDraftPool();
            cliModel.showPlayersWindows();
            cliView.print("It's not your turn. You can't do anything!\n");
        }
    }

    @Override
    //receives input from the network, called by class clientConnection
    public void update(SyncResponse syncResponse) {
        syncResponse.handle(this);
    }

    //updates the board
    @Override
    public void handleResponse(ModelViewResponse modelViewResponse) {
        cliModel.setBoard(modelViewResponse.getModelView());
        cliModel.setPrivateObjective(modelViewResponse.getPrivateObjective());
        cliModel.setPublicObjectives(modelViewResponse.getPublicObjectives());
        cliModel.setToolCards(modelViewResponse.getToolCards());
        cliModel.showPrivateObjective();
        cliModel.showPublicObjectives();
        checkTurn(modelViewResponse.getDescription());
    }

    //prints the text message
    @Override
    public void handleResponse(TextResponse textResponse) {
        cliView.print(textResponse.getDescription()+"\n");
        try {
            chooseAction();
        } catch (RemoteException e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }

    @Override
    public void handleResponse(InputResponse inputResponse) {
        cliView.print("Color of the die is " + inputResponse.getColor()+"\n");
        try {
            int choice = cliView.getDieValue();
            cliView.handleNetworkOutput(new InputMessage(playerID, cliModel.getBoard().getStateID(), choice));
        }
        catch (HaltException ignored) {
            //now i can stop the method
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
        cliModel.setPrivateObjective(setupResponse.getPrivateObjective());
        cliModel.setPlayersNumber(setupResponse.getPlayersNumber());
        cliView.print("");
        cliModel.showPrivateObjective();
        try {
            int windowNumber = selectWindow(setupResponse.getWindows())-1;
            cliView.print("Window sent. Waiting for other players to choose.\n");
            cliView.handleNetworkOutput(new SetupMessage(playerID,0,setupResponse.getWindows().get(windowNumber)));
        } catch (HaltException ignored) {
            //now i can stop the method
        }
    }

    @Override
    public void handleResponse(ScoreBoardResponse scoreBoardResponse){
        if(!scoreBoardResponse.isLastPlayer()) {
            cliView.print("\nFinal score:\n");
            for (int i = 0; i < scoreBoardResponse.getSortedPlayersNames().size(); i++) {
                cliView.print(i + 1 + "  Player: " + scoreBoardResponse.getSortedPlayersNames().get(i) + "     Score: " + scoreBoardResponse.getSortedPlayersScores().get(i) + "\n");
            }
        }
        cliView.endGame();
    }

    @Override
    public void handleResponse(ReconnectionResponse reconnectionResponse) {
        cliModel.setPlayersNumber(reconnectionResponse.getPlayersNumber());
        if(reconnectionResponse.isWindowSelectionOver()) {
            ModelViewResponse response = reconnectionResponse.getModelViewResponse();
            response.setDescription("Reconnected, wait for your turn\n");
            handleResponse(response);
        }
        else cliView.print("\nReconnected, wait for other players to choose their Windows\n\n");
    }

    @Override
    public void handleResponse(DraftPoolResponse draftPoolResponse) {
        ModelView modelView = cliModel.getBoard();
        modelView.setToolCardUsage(draftPoolResponse.getToolCardUsage());
        modelView.setHasDieInHand(draftPoolResponse.hasDieInHand());
        modelView.setHasDraftedDie(draftPoolResponse.hasDraftedDie());
        modelView.setHasUsedCard(draftPoolResponse.hasUsedCard());
        modelView.setDieInHand(draftPoolResponse.getDieInHand());
        modelView.setToolCardUsability(draftPoolResponse.getToolCardUsability());
        modelView.setPlayerFavorPoint(modelView.getPlayerIDs().indexOf(draftPoolResponse.getCurrentPlayerID()),draftPoolResponse.getFavorPoints());
        modelView.setStateID(draftPoolResponse.getStateID());
        modelView.setCurrentPlayerID(draftPoolResponse.getCurrentPlayerID());
        modelView.setDraftPool(draftPoolResponse.getDraftPool());
        checkTurn(draftPoolResponse.getDescription());
    }

    @Override
    public void handleResponse(RoundTrackerResponse roundTrackerResponse) {
        ModelView modelView = cliModel.getBoard();
        modelView.setToolCardUsage(roundTrackerResponse.getToolCardUsage());
        modelView.setHasDieInHand(roundTrackerResponse.hasDieInHand());
        modelView.setHasDraftedDie(roundTrackerResponse.hasDraftedDie());
        modelView.setHasUsedCard(roundTrackerResponse.hasUsedCard());
        modelView.setDieInHand(roundTrackerResponse.getDieInHand());
        modelView.setToolCardUsability(roundTrackerResponse.getToolCardUsability());
        modelView.setPlayerFavorPoint(modelView.getPlayerIDs().indexOf(roundTrackerResponse.getCurrentPlayerID()),roundTrackerResponse.getFavorPoints());
        modelView.setStateID(roundTrackerResponse.getStateID());
        modelView.setCurrentPlayerID(roundTrackerResponse.getCurrentPlayerID());
        modelView.setRoundTracker(roundTrackerResponse.getRoundTracker());
        checkTurn(roundTrackerResponse.getDescription());
    }

    @Override
    public void handleResponse(WindowResponse windowResponse) {
        ModelView modelView = cliModel.getBoard();
        modelView.setToolCardUsage(windowResponse.getToolCardUsage());
        modelView.setHasDieInHand(windowResponse.hasDieInHand());
        modelView.setHasDraftedDie(windowResponse.hasDraftedDie());
        modelView.setHasUsedCard(windowResponse.hasUsedCard());
        modelView.setDieInHand(windowResponse.getDieInHand());
        modelView.setToolCardUsability(windowResponse.getToolCardUsability());
        modelView.setPlayerFavorPoint(modelView.getPlayerIDs().indexOf(windowResponse.getCurrentPlayerID()),windowResponse.getFavorPoints());
        modelView.setStateID(windowResponse.getStateID());
        modelView.setCurrentPlayerID(windowResponse.getCurrentPlayerID());
        modelView.setPlayerWindow(modelView.getPlayerIDs().indexOf(windowResponse.getCurrentPlayerID()),windowResponse.getWindow());
        checkTurn(windowResponse.getDescription());
    }

    @Override
    public void handleResponse(ModelUpdateResponse modelUpdateResponse) {
        ModelView modelView = cliModel.getBoard();
        modelView.setToolCardUsage(modelUpdateResponse.getToolCardUsage());
        modelView.setHasDieInHand(modelUpdateResponse.hasDieInHand());
        modelView.setHasDraftedDie(modelUpdateResponse.hasDraftedDie());
        modelView.setHasUsedCard(modelUpdateResponse.hasUsedCard());
        modelView.setDieInHand(modelUpdateResponse.getDieInHand());
        modelView.setToolCardUsability(modelUpdateResponse.getToolCardUsability());
        modelView.setPlayerFavorPoint(modelView.getPlayerIDs().indexOf(modelUpdateResponse.getCurrentPlayerID()),modelUpdateResponse.getFavorPoints());
        modelView.setStateID(modelUpdateResponse.getStateID());
        modelView.setCurrentPlayerID(modelUpdateResponse.getCurrentPlayerID());
        checkTurn(modelUpdateResponse.getDescription());
    }
}
