package it.polimi.se2018.Model.Moves;

import it.polimi.se2018.Model.Player;

public class DraftMessage {
    private final Player player;
    private int draftPoolPosition;

    public DraftMessage(Player player, int draftPoolPosition) {
        this.player = player;
        this.draftPoolPosition = draftPoolPosition;
    }

    public Player getPlayer() {
        return player;
    }

    public int getDraftPoolPosition() {
        return draftPoolPosition;
    }

    public void setDraftPoolPosition(int draftPoolPosition) {
        this.draftPoolPosition = draftPoolPosition;
    }
}
