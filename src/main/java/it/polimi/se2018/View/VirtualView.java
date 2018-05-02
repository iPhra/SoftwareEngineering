package it.polimi.se2018.View;

import it.polimi.se2018.Connections.ServerConnection;
import it.polimi.se2018.Model.ModelView;
import it.polimi.se2018.Model.Moves.MoveMessage;
import it.polimi.se2018.Model.Player;
import it.polimi.se2018.Utils.Observable;
import it.polimi.se2018.Utils.Observer;

public class VirtualView extends Observable<MoveMessage> implements Observer<ModelView>{
    private Player player;
    private ServerConnection serverConnection;

    protected VirtualView(Player player, ServerConnection serverConnection) {
        this.player = player;
        this.serverConnection=serverConnection;
    }

    public ServerConnection getServerConnection() {
        return serverConnection;
    }

    protected Player getPlayer(){
        return player;
    }

    @Override
    public void update(ModelView message) {}

}
