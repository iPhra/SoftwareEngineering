package it.polimi.se2018.network.messages.responses;

import it.polimi.se2018.model.*;


//updates from model to view
public class ModelViewResponse extends Response {
    private final ModelView modelView;

    public ModelViewResponse(ModelView modelView) {
        super(0);
        this.modelView = modelView;
    }

    public ModelView getModelView() {
        return modelView;
    }

    public void handle(ResponseHandler responseHandler) { responseHandler.handleResponse(this);}


}
