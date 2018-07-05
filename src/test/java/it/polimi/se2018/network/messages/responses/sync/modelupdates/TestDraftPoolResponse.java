package it.polimi.se2018.network.messages.responses.sync.modelupdates;

import it.polimi.se2018.GameInstance;
import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.DraftPool;
import it.polimi.se2018.network.messages.responses.sync.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static junit.framework.TestCase.fail;

public class TestDraftPoolResponse {
    private DraftPoolResponse response;
    private List<Die> draftPool;

    @Before
    public void init() {
        GameInstance gameInstance = new GameInstance();
        gameInstance.createGame();
        Board board = gameInstance.getManager().getModel();
        draftPool = board.getDraftPool().modelViewCopy();
        response = new DraftPoolResponse(1,board);
    }

    @Test
    public void testGetters() {
        Assert.assertEquals(1, response.getPlayerID());
        Assert.assertEquals(draftPool,response.getDraftPool());
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
