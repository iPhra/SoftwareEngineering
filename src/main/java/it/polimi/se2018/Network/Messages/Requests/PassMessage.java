package it.polimi.se2018.Network.Messages.Requests;

import it.polimi.se2018.Model.Player;

/**
 * This class represents a message from a player whishing to end his turn
 * @Author Francesco Lorenzo
 */
public class PassMessage extends Message {

    public PassMessage(Player player) {
        super(player);
    }

    /**
     * Uses the handler to handle this specific pass request
     * @param handler is the object who will handle this request
     */
    @Override
    public void handle(MessageHandler handler) {
        handler.performMove(this);
    }

}
