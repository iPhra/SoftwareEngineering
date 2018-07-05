package it.polimi.se2018.network.messages.responses.sync.modelupdates;

import it.polimi.se2018.GameInstance;
import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.network.messages.responses.sync.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.TestCase.fail;

public class TestModelViewResponse {
    private ModelViewResponse response;
    private Board board;

    @Before
    public void init() {
        GameInstance gameInstance = new GameInstance();
        gameInstance.createGame();
        board = gameInstance.getManager().getModel();
        response = new ModelViewResponse(board,new ModelView(board),1);
    }

    @Test
    public void testGetters() {
        Assert.assertEquals(1, response.getPlayerID());
        Assert.assertEquals(board.getPlayerByID(1).getPrivateObjective(),response.getPrivateObjective());
        Assert.assertEquals(Arrays.asList(board.getPublicObjectives()),response.getPublicObjectives());
        Assert.assertEquals(Arrays.asList(board.getToolCards()),response.getToolCards());
        Assert.assertEquals(1,response.getModelView().getStateID());
        Assert.assertEquals(board.getRound().getCurrentPlayerID(),response.getModelView().getCurrentPlayerID());
        Assert.assertEquals(board.getRoundTracker().modelViewCopy(),response.getModelView().getRoundTracker());
        Assert.assertEquals(board.getDraftPool().modelViewCopy(),response.getModelView().getDraftPool());
        Assert.assertFalse(response.getModelView().hasUsedCard());
        Assert.assertFalse(response.getModelView().hasDieInHand());
        Assert.assertFalse(response.getModelView().hasDraftedDie());
        Assert.assertNull(response.getModelView().getDieInHand());
    }

    @Test
    public void testHandle() {
        response.handle(new SyncResponseHandler() {
            @Override
            public void handleResponse(ModelViewResponse modelViewResponse) {
            }

            @Override
            public void handleResponse(TextResponse textResponse) {
                fail();
            }

            @Override
            public void handleResponse(ToolCardResponse toolCardResponse) {
                fail();
            }

            @Override
            public void handleResponse(SetupResponse setupResponse) {fail();}

            @Override
            public void handleResponse(InputResponse inputMessage) {fail();}

            @Override
            public void handleResponse(ScoreBoardResponse scoreBoardResponse) {
                fail();
            }

            @Override
            public void handleResponse(ReconnectionResponse reconnectionResponse) {
                fail();
            }

            @Override
            public void handleResponse(DraftPoolResponse draftPoolResponse) {
                fail();
            }

            @Override
            public void handleResponse(RoundTrackerResponse roundTrackerResponse) {
                fail();
            }

            @Override
            public void handleResponse(WindowResponse windowResponse) {
                fail();
            }

            @Override
            public void handleResponse(ModelUpdateResponse modelUpdateResponse) {
                fail();
            }
        });
    }
}
