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
        toolCards.add(CopperFoilBurnisher.instance("/toolcards/copper_foil_burnisher.png"));
        toolCards.add(CorkBackedStraightedge.instance("/toolcards/cork_backed_straightedge.png"));
        toolCards.add(EglomiseBrush.instance("/toolcards/eglomise_brush.png"));
        toolCards.add(FluxBrush.instance("/toolcards/flux_brush.png"));
        toolCards.add(FluxRemover.instance("/toolcards/flux_remover.png"));
        toolCards.add(GlazingHammer.instance("/toolcards/glazing_hammer.png"));
        toolCards.add(GrindingStone.instance("/toolcards/grinding_stone.png"));
        toolCards.add(GrozingPliers.instance("/toolcards/grozling_pliers.png"));
        toolCards.add(Lathekin.instance("/toolcards/lathekin.png"));
        toolCards.add(LensCutter.instance("/toolcards/lens_cutter.png"));
        toolCards.add(RunningPliers.instance("/toolcards/running_pliers.png"));
        toolCards.add(TapWheel.instance("/toolcards/tap_wheel.png"));
        publicObjectives.add(ColorDiagonalsObjective.instance("/objectives/public_objectives/color_diagonals.png"));
        publicObjectives.add(ColorVarietyObjective.instance("/objectives/public_objectives/color_variety.png"));
        publicObjectives.add(ColumnColorVarietyObjective.instance("/objectives/public_objectives/column_color_variety.png"));
        publicObjectives.add(ColumnShadeVarietyObjective.instance("/objectives/public_objectives/column_shade_variety.png"));
        publicObjectives.add(DeepShadesObjective.instance("/objectives/public_objectives/deep_shades.png"));
        publicObjectives.add(LightShadesObjective.instance("/objectives/public_objectives/light_shades.png"));
        publicObjectives.add(MediumShadesObjective.instance("/objectives/public_objectives/medium_shades.png"));
        publicObjectives.add(RowColorVarietyObjective.instance("/objectives/public_objectives/row_color_variety.png"));
        publicObjectives.add(RowShadeVarietyObjective.instance("/objectives/public_objectives/row_shade_variety.png"));
        publicObjectives.add(ShadeVarietyObjective.instance("/objectives/public_objectives/shade_variety.png"));
        privateObjectives.add(ShadesOfBlueObjective.instance("/objectives/private_objectives/shades_of_blue.png"));
        privateObjectives.add(ShadesOfGreenObjective.instance("/objectives/private_objectives/shades_of_green.png"));
        privateObjectives.add(ShadesOfPurpleObjective.instance("/objectives/private_objectives/shades_of_purple.png"));
        privateObjectives.add(ShadesOfRedObjective.instance("/objectives/private_objectives/shades_of_red.png"));
        privateObjectives.add(ShadesOfYellowObjective.instance("/objectives/private_objectives/shades_of_yellow.png"));
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
