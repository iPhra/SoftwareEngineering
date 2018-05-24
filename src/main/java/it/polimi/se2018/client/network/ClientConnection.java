package it.polimi.se2018.client.network;

import it.polimi.se2018.network.messages.requests.Message;


public interface ClientConnection {

    void sendMessage(Message message);
    void stop();
}
