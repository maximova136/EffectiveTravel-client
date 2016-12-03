package com.et.routes;

import com.et.api.IApiClient;
import com.et.exception.api.InsuccessfulResponseException;
import com.et.exception.api.RequestFailedException;
import com.et.response.object.RouteObject;

import java.util.List;


public class RouteFetcher implements IRouteFetcher {
    private IApiClient client;

    public RouteFetcher(IApiClient client) {
        this.client = client;
    }

    @Override
    public List<RouteObject> fetchAll() throws RequestFailedException, InsuccessfulResponseException {
        return client.routes();
    }
}
