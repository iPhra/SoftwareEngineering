package it.polimi.se2018.utils;

import it.polimi.se2018.mvc.model.objectives.privateobjectives.*;
import it.polimi.se2018.mvc.model.objectives.publicobjectives.*;
import it.polimi.se2018.mvc.model.toolcards.*;

import java.util.*;

/**
 * This class creates all the Private Objectives, Public Objectives and Tool Cards
 * Pattern Singleton prevents to create more than one istance of each card, the same one is always returned
 */
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
        publicObjectives.add(ColorDiagonalsObjective.instance());
        publicObjectives.add(ColorVarietyObjective.instance());
        publicObjectives.add(ColumnColorVarietyObjective.instance());
        publicObjectives.add(ColumnShadeVarietyObjective.instance());
        publicObjectives.add(DeepShadesObjective.instance());
        publicObjectives.add(LightShadesObjective.instance());
        publicObjectives.add(MediumShadesObjective.instance());
        publicObjectives.add(RowColorVarietyObjective.instance());
        publicObjectives.add(RowShadeVarietyObjective.instance());
        publicObjectives.add(ShadeVarietyObjective.instance());
        privateObjectives.add(ShadesOfBlueObjective.instance());
        privateObjectives.add(ShadesOfGreenObjective.instance());
        privateObjectives.add(ShadesOfPurpleObjective.instance());
        privateObjectives.add(ShadesOfRedObjective.instance());
        privateObjectives.add(ShadesOfYellowObjective.instance());
    }

    /**
     * Extracts a given number of Tool Cards
     * @param number is the number of cards to extract
     * @return a list containing the extracted cards
     */
    public List<ToolCard> extractToolCards(int number) {
        List<ToolCard> result = new ArrayList<>();
        Collections.shuffle(toolCards);
        for(int i=0; i<number; i++) {
            result.add(toolCards.get(i));
        }
        return result;
    }

    /**
     * Extracts a given number of Public Objectives
     * @param number is the number of cards to extract
     * @return a list containing the extracted cards
     */
    public List<PublicObjective> extractPublicObjectives(int number) {
        List<PublicObjective> result = new ArrayList<>();
        Collections.shuffle(publicObjectives);
        for(int i=0; i<number; i++) {
            result.add(publicObjectives.get(i));
        }
        return result;
    }

    /**
     * Extracts a given number of Private Objectives
     * @param number is the number of cards to extract
     * @return a list containing the extracted cards
     */
    public List<PrivateObjective> extractPrivateObjectives(int number) {
        List<PrivateObjective> result = new ArrayList<>();
        Collections.shuffle(privateObjectives);
        for(int i=0; i<number; i++) {
            result.add(privateObjectives.get(i));
        }
        return result;
    }
}
