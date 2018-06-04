package it.polimi.se2018.network.messages.responses;

import it.polimi.se2018.network.messages.responses.sync.SyncResponse;

public interface ResponseHandler {


    void handleResponse(DisconnectionResponse disconnectionResponse);

    void handleResponse(ReconnectionNotificationResponse reconnectionNotificationResponse);

    void handleResponse(TimeUpResponse timeUpResponse);

    void handleResponse(SyncResponse syncResponse);
}
