package it.polimi.se2018.Model.Messages;

public interface MessageHandler {

    public void performMove(ToolCardMessage toolCardMessage);
    public void performMove(PassMessage passMessage);
    public void performMove(PlaceMessage placeMessage);
    public void performMove(DraftMessage draftMessage);

}
