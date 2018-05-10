package it.polimi.se2018.Network.Messages.Responses;

public interface ResponseHandler {

    void handleResponse(ModelViewResponse modelViewResponse);

    void handleResponse(TextResponse textResponse);

    void handleResponse(TurnStartResponse turnStartResponse);

    void handleResponse(ToolCardResponse toolCardResponse);
}
