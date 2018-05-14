package it.polimi.se2018.network.messages.responses;

public class TextResponse extends Response {
    private final String message;

    public TextResponse(int player, String message){
        super(player);
        this.message=message;
    }

    public void handle(ResponseHandler responseHandler) {responseHandler.handleResponse(this);}

    public String getMessage() {return message;}
}
