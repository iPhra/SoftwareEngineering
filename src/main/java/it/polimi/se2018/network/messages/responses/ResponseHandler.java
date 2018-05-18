package it.polimi.se2018.network.messages.responses;

public interface ResponseHandler {

    void handleResponse(ModelViewResponse modelViewResponse);

    void handleResponse(TextResponse textResponse);

    void handleResponse(TurnStartResponse turnStartResponse);

    void handleResponse(ToolCardResponse toolCardResponse);

    void handleResponse(SetupResponse setupResponse);

    void handleResponse(InputResponse inputResponse);
}
