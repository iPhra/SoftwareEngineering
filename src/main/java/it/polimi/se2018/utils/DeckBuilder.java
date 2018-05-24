package it.polimi.se2018.utils;

import it.polimi.se2018.mvc.model.objectives.privateobjectives.*;
import it.polimi.se2018.mvc.model.objectives.publicobjectives.*;
import it.polimi.se2018.mvc.model.toolcards.*;

import java.util.*;

public class DeckBuilder {
    private final List<ToolCard> toolCards;
    private final List<PublicObjective> publicObjectives;
    private final List<PrivateObjective> privateObjectives;

    public DeckBuilder() {
        toolCards = new ArrayList<>();
        publicObjectives = new ArrayList<>();
        privateObjectives = new ArrayList<>();
        toolCards.add(CopperFoilBurnisher.instance());
        toolCards.add(CorkBackedStraightedge.instance());
        toolCards.add(EglomiseBrush.instance());
        toolCards.add(FluxBrush.instance());
        toolCards.add(FluxRemover.instance());
        toolCards.add(GlazingHammer.instance());
        toolCards.add(GrindingStone.instance());
        toolCards.add(GrozingPliers.instance());
        toolCards.add(Lathekin.instance());
        toolCards.add(LensCutter.instance());
        toolCards.add(RunningPliers.instance());
        toolCards.add(TapWheel.instance());
        publicObjectives.add(ColorDiagonalsObjective.instance("Color Diagonals"));
        publicObjectives.add(ColorVarietyObjective.instance("Color Variety"));
        publicObjectives.add(ColumnColorVarietyObjective.instance("Column Color Variety"));
        publicObjectives.add(ColumnShadeVarietyObjective.instance("Colum Shade Variety"));
        publicObjectives.add(DeepShadesObjective.instance("Deep Shades"));
        publicObjectives.add(LightShadesObjective.instance("Light Shades"));
        publicObjectives.add(RowColorVarietyObjective.instance("Row Color Variety"));
        publicObjectives.add(RowShadeVarietyObjective.instance("Row Shade Variety"));
        publicObjectives.add(ShadeVarietyObjective.instance("Shade Variety"));
        privateObjectives.add(ShadesOfBlueObjective.instance("Shades of Blue"));
        privateObjectives.add(ShadesOfGreenObjective.instance("Shades of Green"));
        privateObjectives.add(ShadesOfPurpleObjective.instance("Shades of Purple"));
        privateObjectives.add(ShadesOfRedObjective.instance("Shades of Red"));
        privateObjectives.add(ShadesOfYellowObjective.instance("Shades of Yellow"));
    }

    public List<ToolCard> extractToolCards(int number) {
        List<ToolCard> result = new ArrayList<>();
        Collections.shuffle(toolCards);
        for(int i=0; i<number; i++) {
            result.add(toolCards.get(i));
        }
        return result;
    }

    public List<PublicObjective> extractPublicObjectives(int number) {
        List<PublicObjective> result = new ArrayList<>();
        Collections.shuffle(publicObjectives);
        for(int i=0; i<number; i++) {
            result.add(publicObjectives.get(i));
        }
        return result;
    }

    public List<PrivateObjective> extractPrivateObjectives(int number) {
        List<PrivateObjective> result = new ArrayList<>();
        Collections.shuffle(privateObjectives);
        for(int i=0; i<number; i++) {
            result.add(privateObjectives.get(i));
        }
        return result;
    }
}
