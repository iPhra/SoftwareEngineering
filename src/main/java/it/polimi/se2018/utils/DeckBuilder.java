package it.polimi.se2018.utils;

import it.polimi.se2018.model.objectives.privateobjectives.*;
import it.polimi.se2018.model.objectives.publicobjectives.*;
import it.polimi.se2018.model.toolcards.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeckBuilder {
    private static DeckBuilder instance;
    private List<ToolCard> toolCards;
    private List<PublicObjective> publicObjectives;
    private List<PrivateObjective> privateObjectives;

    private DeckBuilder() {
        toolCards.add(new CopperFoilBurnisher(null));
        toolCards.add(new CorkBackedStraightedge(null));
        toolCards.add(new EglomiseBrush(null));
        toolCards.add(new FluxBrush(null));
        toolCards.add(new FluxRemover(null));
        toolCards.add(new GlazingHammer(null));
        toolCards.add(new GrindingStone(null));
        toolCards.add(new GrozingPliers(null));
        toolCards.add(new Lathekin(null));
        toolCards.add(new LensCutter(null));
        toolCards.add(new RunningPliers(null));
        toolCards.add(new TapWheel(null));
        publicObjectives.add(ColorDiagonalsObjective.instance(null, "Color Diagonals"));
        publicObjectives.add(ColorVarietyObjective.instance(null, "Color Variety"));
        publicObjectives.add(ColumnColorVarietyObjective.instance(null, "Column Color Variety"));
        publicObjectives.add(ColumnShadeVarietyObjective.instance(null, "Colum Shade Variety"));
        publicObjectives.add(DeepShadesObjective.instance(null, "Deep Shades"));
        publicObjectives.add(LightShadesObjective.instance(null,"Light Shades"));
        publicObjectives.add(RowColorVarietyObjective.instance(null, "Row Color Variety"));
        publicObjectives.add(RowShadeVarietyObjective.instance(null, "Row Shade Variety"));
        publicObjectives.add(ShadeVarietyObjective.instance(null, "Shade Variety"));
        privateObjectives.add(ShadesOfBlueObjective.instance(null, "Shades of Blue"));
        privateObjectives.add(ShadesOfGreenObjective.instance(null, "Shades of Green"));
        privateObjectives.add(ShadesOfPurpleObjective.instance(null, "Shades of Purple"));
        privateObjectives.add(ShadesOfRedObjective.instance(null, "Shades of Red"));
        privateObjectives.add(ShadesOfYellowObjective.instance(null, "Shades of Purple"));
    }

    private static synchronized DeckBuilder createInstance(){
        if (instance==null) instance = new DeckBuilder();
        return instance;
    }

    public static DeckBuilder instance(String imagePath, String title){
        if (instance==null) createInstance();
        return instance;
    }

    public List<ToolCard> extractToolCards(int number) {
        List<ToolCard> result = new ArrayList<>();
        Random random = new Random();
        for(int i=0; i<number; i++) {
            result.add(toolCards.get(random.nextInt(i)));
        }
        return result;
    }

    public List<PublicObjective> extractPublicObjectives(int number) {
        List<PublicObjective> result = new ArrayList<>();
        Random random = new Random();
        for(int i=0; i<number; i++) {
            result.add(publicObjectives.get(random.nextInt(i)));
        }
        return result;
    }

    public List<PrivateObjective> extractPrivateObjectives(int number) {
        List<PrivateObjective> result = new ArrayList<>();
        Random random = new Random();
        for(int i=0; i<number; i++) {
            result.add(privateObjectives.get(random.nextInt(i)));
        }
        return result;
    }
}
