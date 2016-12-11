package com.et.api;


import com.et.exception.api.InsuccessfulResponseException;
import com.et.exception.api.LoginFailedException;
import com.et.exception.api.RequestFailedException;
import com.et.exception.api.SignupFailedException;
import com.et.response.StatisticsResponse;
import com.et.response.object.RouteObject;
import com.et.response.object.StationObject;

import java.util.List;

public interface IApiClient {

    public String login(String login, String password) throws LoginFailedException;


    public String signup(String login, String password) throws SignupFailedException;


    public List<RouteObject> routes() throws RequestFailedException, InsuccessfulResponseException;


    public List<StationObject> stations() throws RequestFailedException, InsuccessfulResponseException;


    public StatisticsResponse statistics(int s_id, int r_id) throws RequestFailedException, InsuccessfulResponseException;


    public void submitNote(int s_id, int r_id, String time) throws RequestFailedException, InsuccessfulResponseException;
}
