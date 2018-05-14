package it.polimi.se2018.network.messages.responses;

import java.io.Serializable;

public interface ResponseHandler extends Serializable{

    void handleResponse(ModelViewResponse modelViewResponse);

    void handleResponse(TextResponse textResponse);

    void handleResponse(TurnStartResponse turnStartResponse);

    void handleResponse(ToolCardResponse toolCardResponse);
}
