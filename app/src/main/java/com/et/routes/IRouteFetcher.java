package com.et.routes;

import com.et.exception.api.InsuccessfulResponseException;
import com.et.exception.api.RequestFailedException;
import com.et.response.object.RouteObject;

import java.util.List;



public interface IRouteFetcher {
    public List<RouteObject> fetchAll() throws RequestFailedException, InsuccessfulResponseException;
}
