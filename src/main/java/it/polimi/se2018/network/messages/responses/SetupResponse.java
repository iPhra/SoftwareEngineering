package it.polimi.se2018.network.messages.responses;

import it.polimi.se2018.model.Window;

import java.util.List;

public class SetupResponse extends Response{
    private final List<Window> windows;

    public SetupResponse(int player, List<Window> windows) {
        super(player);
        this.windows = windows;
    }

    public List<Window> getWindows() {
        return windows;
    }

    @Override
    public void handle(ResponseHandler handler){
        handler.handleResponse(this);
    }
}
