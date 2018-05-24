package it.polimi.se2018.network.messages.responses;

import it.polimi.se2018.mvc.controller.ModelView;

/**
 * This is the class containing a response from the Server containing an updated copy of the state of the game
 * @author Francesco Lorenzo
 */
public class ModelViewResponse extends Response {
    /**
     * This is the object containing the updated copy of the model
     */
    private final ModelView modelView;

    public ModelViewResponse(ModelView modelView) {
        super(modelView.getCurrentPlayerID());
        this.modelView = modelView;
    }

    /**
     * @return the updated copy of the model
     */
    public ModelView getModelView() {
        return modelView;
    }

    /**
     * Uses the handler to handle this specific {@link ModelView} response
     * @param responseHandler is the object who will handle this response
     */
    @Override
    public void handle(ResponseHandler responseHandler) { responseHandler.handleResponse(this);}


}
