package it.polimi.se2018.Network.Messages.Responses;

public interface ResponseHandler {

    public void handleResponse(ModelViewResponse modelViewResponse);
    public void handleResponse(TextResponse textResponse);
    public void handleResponse(TurnStartResponse turnStartResponse);
}
