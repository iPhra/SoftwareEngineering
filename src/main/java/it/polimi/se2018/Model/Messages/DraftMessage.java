package it.polimi.se2018.Model.Messages;

import it.polimi.se2018.Model.Player;

public class DraftMessage extends Message {
    private int draftPoolPosition;

    public DraftMessage(Player player, int draftPoolPosition) {
        super(player);
        this.draftPoolPosition = draftPoolPosition;
    }

    public int getDraftPoolPosition() {
        return draftPoolPosition;
    }

    public void setDraftPoolPosition(int draftPoolPosition) {
        this.draftPoolPosition = draftPoolPosition;
    }
}
