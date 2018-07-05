package it.polimi.se2018.network.messages.responses.sync;

import it.polimi.se2018.mvc.model.objectives.privateobjectives.ShadesOfBlueObjective;
import it.polimi.se2018.network.messages.responses.sync.modelupdates.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static junit.framework.TestCase.fail;

public class TestSetupResponse {
    private SetupResponse response;
    private int playerID;

    @Before
    public void init() {
        playerID = new Random().nextInt();
        response = new SetupResponse(playerID,new ArrayList<>(), ShadesOfBlueObjective.instance(),4);
    }

    @Test
    public void testGetters() {
        Assert.assertEquals(playerID, response.getPlayerID());
        Assert.assertEquals(4,response.getPlayersNumber());
        Assert.assertEquals(new ArrayList<>(),response.getWindows());
        Assert.assertEquals(ShadesOfBlueObjective.instance(),response.getPrivateObjective());
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
            public void handleResponse(SetupResponse setupResponse) {
            }

            @Override
            public void handleResponse(InputResponse inputMessage) {fail();}

            @Override
            public void handleResponse(ScoreBoardResponse scoreBoardResponse) {fail();}

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
