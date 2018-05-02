package it.polimi.se2018.Model;

import it.polimi.se2018.Model.Objectives.PublicObjectives.PublicObjective;
import it.polimi.se2018.Model.ToolCards.ToolCard;

import java.util.ArrayList;

//updates from model to view
public class ModelView {
    private final ArrayList<Player> players; //contiene la mappa di ciascun giocatore
    private final DraftPool draftPool; //draft pool
    private final ToolCard[] toolCards; // sono le toolCards sulla board (il gioco prevede da regolamento che siano 3, ma noi lo facciamo parametrico e quindi estendibile)
    private final PublicObjective[] publicObjectives; //array che contiene le carte degli obbiettivi pubblici
    private final RoundTracker roundTracker; //ha il riferimento al roundTracker

    public ModelView(ArrayList<Player> players, DraftPool draftPool, ToolCard[] toolCards, PublicObjective[] publicObjectives, RoundTracker roundTracker) {
        this.players = players;
        this.draftPool = draftPool;
        this.toolCards = toolCards;
        this.publicObjectives = publicObjectives;
        this.roundTracker = roundTracker;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public DraftPool getDraftPool() {
        return draftPool;
    }

    public ToolCard[] getToolCards() {
        return toolCards;
    }

    public PublicObjective[] getPublicObjectives() {
        return publicObjectives;
    }

    public RoundTracker getRoundTracker() {
        return roundTracker;
    }
}
