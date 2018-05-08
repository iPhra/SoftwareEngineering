package it.polimi.se2018.Model.Messages;

public interface ResponseHandler {

    public void handleResponse(ModelViewResponse modelViewResponse);
    public void handleResponse(TextResponse textResponse);
    public void handleResponse(TurnStartResponse turnStartResponse);
}
