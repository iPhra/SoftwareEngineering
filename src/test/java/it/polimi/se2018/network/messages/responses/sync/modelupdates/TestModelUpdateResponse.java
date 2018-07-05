package it.polimi.se2018.network.messages.responses.sync.modelupdates;

import it.polimi.se2018.GameInstance;
import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Color;
import it.polimi.se2018.network.messages.responses.sync.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.fail;

public class TestModelUpdateResponse {
    private ModelUpdateResponse response;
    private Board board;

    @Before
    public void init() {
        GameInstance gameInstance = new GameInstance();
        gameInstance.createGame();
        board = gameInstance.getManager().getModel();
        response = new ModelUpdateResponse(1,0,board);
    }

    @Test
    public void testGetters() {
        int id = board.getRound().getCurrentPlayerID();
        Assert.assertEquals(1, response.getPlayerID());
        Assert.assertEquals(id,response.getCurrentPlayerID());
        Assert.assertEquals(board.getPlayerByID(id).getFavorPoints(),response.getFavorPoints());
        Assert.assertEquals(0,response.getStateID());
        Assert.assertEquals(null,response.getDieInHand());
        Assert.assertEquals(false,response.hasDieInHand());
        Assert.assertEquals(false,response.hasDraftedDie());
        Assert.assertEquals(false,response.hasUsedCard());
    }

    @Test
    public void testHandle() {
        response.handle(new SyncResponseHandler() {
            @Override
            public void handleResponse(ModelViewResponse modelViewResponse) {
                fail();
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
            public void handleResponse(InputResponse inputMessage) {
                fail();
            }

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
            }
        });
    }
}
