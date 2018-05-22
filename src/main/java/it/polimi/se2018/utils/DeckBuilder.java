package it.polimi.se2018.utils;

import it.polimi.se2018.model.objectives.privateobjectives.*;
import it.polimi.se2018.model.objectives.publicobjectives.*;
import it.polimi.se2018.model.toolcards.*;

import java.util.*;

public class DeckBuilder {
    private static DeckBuilder instance;
    private List<ToolCard> toolCards;
    private List<PublicObjective> publicObjectives;
    private List<PrivateObjective> privateObjectives;

    private DeckBuilder() {
        toolCards = new ArrayList<>();
        publicObjectives = new ArrayList<>();
        privateObjectives = new ArrayList<>();
        toolCards.add(new CopperFoilBurnisher());
        toolCards.add(new CorkBackedStraightedge());
        toolCards.add(new EglomiseBrush());
        toolCards.add(new FluxBrush());
        toolCards.add(new FluxRemover());
        toolCards.add(new GlazingHammer());
        toolCards.add(new GrindingStone());
        toolCards.add(new GrozingPliers());
        toolCards.add(new Lathekin());
        toolCards.add(new LensCutter());
        toolCards.add(new RunningPliers());
        toolCards.add(new TapWheel());
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

    private static synchronized DeckBuilder createInstance(){
        if (instance==null) instance = new DeckBuilder();
        return instance;
    }

    public static DeckBuilder instance(){
        if (instance==null) createInstance();
        return instance;
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
