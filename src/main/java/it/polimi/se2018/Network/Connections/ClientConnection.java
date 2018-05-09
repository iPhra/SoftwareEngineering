package it.polimi.se2018.Network.Connections;

import it.polimi.se2018.Network.Messages.Requests.Message;

public interface ClientConnection {
    public void sendMessage (Message message);
}
