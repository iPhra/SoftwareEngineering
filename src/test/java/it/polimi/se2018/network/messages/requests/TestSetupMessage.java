package it.polimi.se2018.network.messages.requests;

import it.polimi.se2018.MapDatabase;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.utils.DeckBuilder;
import it.polimi.se2018.utils.WindowBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static junit.framework.TestCase.fail;

public class TestSetupMessage {
    private SetupMessage setupMessage;
    private Window window;

    @Before
    public void init() {
        window = new MapDatabase().generateWindowByTitle("GRAVITAS");
        setupMessage = new SetupMessage(0,0,window);
    }

    @Test
    public void testGetWindow() {
        Assert.assertEquals(window,setupMessage.getWindow());
    }

    @Test
    public void testHandle() {
        setupMessage.handle(new MessageHandler() {
            @Override
            public void handleMove(ToolCardMessage toolCardMessage) {
                fail();
            }

            @Override
            public void handleMove(PassMessage passMessage) {
                fail();
            }

            @Override
            public void handleMove(PlaceMessage placeMessage) {
                fail();
            }

            @Override
            public void handleMove(DraftMessage draftMessage) {
                fail();
            }

            @Override
            public void handleMove(ToolCardRequestMessage toolCardRequestMessage) {
                fail();
            }

            @Override
            public void handleMove(SetupMessage setupMessage) {
            }

            @Override
            public void handleMove(InputMessage inputMessage) {
                fail();
            }
        });
    }
}
