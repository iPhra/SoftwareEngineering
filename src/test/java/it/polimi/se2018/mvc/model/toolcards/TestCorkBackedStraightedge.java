package it.polimi.se2018.mvc.model.toolcards;

import it.polimi.se2018.client.view.cli.ToolCardCLIHandler;
import it.polimi.se2018.client.view.gui.ToolCardGUIHandler;
import it.polimi.se2018.mvc.controller.ToolCardCheckerHandler;
import it.polimi.se2018.mvc.controller.ToolCardHandler;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.mvc.model.objectives.privateobjectives.ShadesOfGreenObjective;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.utils.exceptions.ChangeActionException;
import it.polimi.se2018.utils.exceptions.HaltException;
import it.polimi.se2018.utils.exceptions.ToolCardException;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.fail;

public class TestCorkBackedStraightedge {
    private CorkBackedStraightedge corkBackedStraightedge;

    @Before
    public void init(){
        corkBackedStraightedge = CorkBackedStraightedge.instance();
    }

    @Test
    public void testHandle(){
        try {
            corkBackedStraightedge.handle(new ToolCardHandler() {
                @Override
                public void useCard(CopperFoilBurnisher toolCard, ToolCardMessage toolCardMessage)  {
                    fail();
                }

                @Override
                public void useCard(CorkBackedStraightedge toolCard, ToolCardMessage toolCardMessage){

                }

                @Override
                public void useCard(EglomiseBrush toolCard, ToolCardMessage toolCardMessage) {
                    fail();
                }

                @Override
                public void useCard(FluxBrush toolCard, ToolCardMessage toolCardMessage) {
                    fail();
                }

                @Override
                public void useCard(FluxRemover toolCard, ToolCardMessage toolCardMessage)  {
                    fail();
                }

                @Override
                public void useCard(GlazingHammer toolCard, ToolCardMessage toolCardMessage) {
                    fail();
                }

                @Override
                public void useCard(GrindingStone toolCard, ToolCardMessage toolCardMessage) {
                    fail();
                }

                @Override
                public void useCard(GrozingPliers toolCard, ToolCardMessage toolCardMessage) {
                    fail();
                }

                @Override
                public void useCard(Lathekin toolCard, ToolCardMessage toolCardMessage) {
                    fail();
                }

                @Override
                public void useCard(LensCutter toolCard, ToolCardMessage toolCardMessage) {
                    fail();
                }

                @Override
                public void useCard(RunningPliers toolCard, ToolCardMessage toolCardMessage) {
                    fail();
                }

                @Override
                public void useCard(TapWheel toolCard, ToolCardMessage toolCardMessage) {
                    fail();
                }
            }, new ToolCardMessage(1, 1, 1));
        }
        catch(ToolCardException e){
            fail();
        }
    }

    @Test
    public void testHandleView(){
        try {
            corkBackedStraightedge.handleView(new ToolCardCLIHandler() {
                @Override
                public ToolCardMessage getPlayerRequests(CopperFoilBurnisher toolCard, int toolCardNumber) {
                    fail();
                    return null;
                }

                @Override
                public ToolCardMessage getPlayerRequests(CorkBackedStraightedge toolCard, int toolCardNumber)  {
                    return null;
                }

                @Override
                public ToolCardMessage getPlayerRequests(EglomiseBrush toolCard, int toolCardNumber) {
                    fail();
                    return null;
                }

                @Override
                public ToolCardMessage getPlayerRequests(FluxBrush toolCard, int toolCardNumber) {
                    fail();
                    return null;
                }

                @Override
                public ToolCardMessage getPlayerRequests(FluxRemover toolCard, int toolCardNumber) {
                    fail();
                    return null;
                }

                @Override
                public ToolCardMessage getPlayerRequests(GlazingHammer toolCard, int toolCardNumber) {
                    fail();
                    return null;
                }

                @Override
                public ToolCardMessage getPlayerRequests(GrindingStone toolCard, int toolCardNumber) {
                    fail();
                    return null;
                }

                @Override
                public ToolCardMessage getPlayerRequests(GrozingPliers toolCard, int toolCardNumber)  {
                    fail();
                    return null;
                }

                @Override
                public ToolCardMessage getPlayerRequests(Lathekin toolCard, int toolCardNumber) {
                    fail();
                    return null;
                }

                @Override
                public ToolCardMessage getPlayerRequests(LensCutter toolCard, int toolCardNumber) {
                    fail();
                    return null;
                }

                @Override
                public ToolCardMessage getPlayerRequests(RunningPliers toolCard, int toolCardNumber) {
                    fail();
                    return null;
                }

                @Override
                public ToolCardMessage getPlayerRequests(TapWheel toolCard, int toolCardNumber) {
                    fail();
                    return null;
                }
            }, 2);
        }
        catch (HaltException | ChangeActionException e){
            fail();
        }
    }

    @Test
    public void testHandleCheck(){
        corkBackedStraightedge.handleCheck(new ToolCardCheckerHandler() {
            @Override
            public boolean checkUsability(CopperFoilBurnisher toolCard, boolean isUsed, Player player) {
                fail();
                return false;
            }

            @Override
            public boolean checkUsability(CorkBackedStraightedge toolCard, boolean isUsed, Player player) {
                return false;
            }

            @Override
            public boolean checkUsability(EglomiseBrush toolCard, boolean isUsed, Player player) {
                fail();
                return false;
            }

            @Override
            public boolean checkUsability(FluxBrush toolCard, boolean isUsed, Player player) {
                fail();
                return false;
            }

            @Override
            public boolean checkUsability(FluxRemover toolCard, boolean isUsed, Player player) {
                fail();
                return false;
            }

            @Override
            public boolean checkUsability(GlazingHammer toolCard, boolean isUsed, Player player) {
                fail();
                return false;
            }

            @Override
            public boolean checkUsability(GrindingStone toolCard, boolean isUsed, Player player) {
                fail();
                return false;
            }

            @Override
            public boolean checkUsability(GrozingPliers toolCard, boolean isUsed, Player player) {
                fail();
                return false;
            }

            @Override
            public boolean checkUsability(Lathekin toolCard, boolean isUsed, Player player) {
                fail();
                return false;
            }

            @Override
            public boolean checkUsability(LensCutter toolCard, boolean isUsed, Player player) {
                fail();
                return false;
            }

            @Override
            public boolean checkUsability(RunningPliers toolCard, boolean isUsed, Player player) {
                fail();
                return false;
            }

            @Override
            public boolean checkUsability(TapWheel toolCard, boolean isUsed, Player player) {
                fail();
                return false;
            }
        }, false, new Player("name", 3, new Window("title",4,new Square[3][4],"levelPath"), ShadesOfGreenObjective.instance()));
    }

    @Test
    public void testHandleGUI(){
        corkBackedStraightedge.handleGUI(new ToolCardGUIHandler() {
            @Override
            public void getPlayerRequests(CopperFoilBurnisher toolCard, int toolCardNumber) {
                fail();
            }

            @Override
            public void getPlayerRequests(CorkBackedStraightedge toolCard, int toolCardNumber) {

            }

            @Override
            public void getPlayerRequests(EglomiseBrush toolCard, int toolCardNumber) {
                fail();
            }

            @Override
            public void getPlayerRequests(FluxBrush toolCard, int toolCardNumber) {
                fail();
            }

            @Override
            public void getPlayerRequests(FluxRemover toolCard, int toolCardNumber) {
                fail();
            }

            @Override
            public void getPlayerRequests(GlazingHammer toolCard, int toolCardNumber) {
                fail();
            }

            @Override
            public void getPlayerRequests(GrindingStone toolCard, int toolCardNumber) {
                fail();
            }

            @Override
            public void getPlayerRequests(GrozingPliers toolCard, int toolCardNumber) {
                fail();
            }

            @Override
            public void getPlayerRequests(Lathekin toolCard, int toolCardNumber) {
                fail();
            }

            @Override
            public void getPlayerRequests(LensCutter toolCard, int toolCardNumber) {
                fail();
            }

            @Override
            public void getPlayerRequests(RunningPliers toolCard, int toolCardNumber) {
                fail();
            }

            @Override
            public void getPlayerRequests(TapWheel toolCard, int toolCardNumber) {
                fail();
            }
        }, 1);
    }
}
