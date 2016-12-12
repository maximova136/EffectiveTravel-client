package com.et.tests;

import com.et.api.ApiClient;
import com.et.api.IApiClient;
import com.et.auth.Auth;
import com.et.exception.api.InsuccessfulResponseException;
import com.et.exception.api.LoginFailedException;
import com.et.exception.api.RequestFailedException;
import com.et.exception.api.SignupFailedException;
import com.et.exception.storage.DeleteObjectFailed;
import com.et.exception.storage.LoadCollectionFailed;
import com.et.exception.storage.PutObjectFailed;
import com.et.exception.storage.UpdateObjectFailed;
import com.et.response.StatisticsObject;
import com.et.response.object.RouteObject;
import com.et.response.object.StationObject;
import com.et.routes.RouteStorage;
import com.et.routes.TransportRoutes;
import com.et.storage.ILocalStorage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TestRoutes {

    private class MockApiClient implements IApiClient {
        private int failVariant = 0;
        private ArrayList<RouteObject> list;

        public MockApiClient() {
            list = new ArrayList<RouteObject>();
        }

        public void clear() {
            list.clear();
        }

        public void put(RouteObject route) {
            list.add(route);
        }

        public void setFailVariant(int v) {
            failVariant = v;
        }

        @Override
        public String login(String login, String password) throws LoginFailedException {
            throw new LoginFailedException("Mock");
        }

        @Override
        public String signup(String login, String password) throws SignupFailedException {
            throw new SignupFailedException("Mock");
        }

        @Override
        public StatisticsObject statistics(int s_id, int r_id) throws RequestFailedException, InsuccessfulResponseException {
            throw new SignupFailedException("Mock");
        }

        @Override
        public void submitNote(int s_id, int r_id, String time) throws RequestFailedException, InsuccessfulResponseException {
            throw new SignupFailedException("Mock");
        }

        @Override
        public List<RouteObject> routes() throws RequestFailedException, InsuccessfulResponseException {
            switch (failVariant) {
                case 1:
                    throw new InsuccessfulResponseException("Mock");
                case 2:
                    throw new RequestFailedException("Mock");

                default:
                    return list;
            }
        }

        @Override
        public List<StationObject> stations() throws RequestFailedException, InsuccessfulResponseException {
            throw new InsuccessfulResponseException("Mock");
        }
    }

    private class MockLocalStorage implements ILocalStorage {
        public ArrayList<RouteObject> list;
        public ArrayList<RouteObject> savedList;

        public boolean clearDbCalled = false;

        public MockLocalStorage() {
            savedList = new ArrayList<>();
            list = new ArrayList<>();
        }

        public void clear() {
            savedList.clear();
            list.clear();
            clearDbCalled = false;
        }

        public void put(RouteObject route) {
            list.add(route);
        }

        @Override
        public void putObject(String collection, HashMap<String, String> values) throws PutObjectFailed {
            if(collection != RouteStorage.COLLECTION_NAME)
                throw new PutObjectFailed("Incorrect collection name");

            RouteObject r = new RouteObject();
            r.setR_id(Integer.parseInt(values.get(RouteStorage.R_ID_KEY)));
            r.setTransport_type(values.get(RouteStorage.TYPE_KEY));
            r.setTitle(values.get(RouteStorage.TITLE_KEY));
            r.setCost(Integer.parseInt(values.get(RouteStorage.COST_KEY)));

            savedList.add(r);
        }

        @Override
        public void updateObject(String collection, int objectId, HashMap<String, String> values) throws UpdateObjectFailed {
            if(collection != RouteStorage.COLLECTION_NAME)
                throw new UpdateObjectFailed("Incorrect collection name");
        }

        @Override
        public void deleteObject(String collection, int objectId) throws DeleteObjectFailed {
            if(collection != RouteStorage.COLLECTION_NAME)
                throw new DeleteObjectFailed("Incorrect collection name");

        }

        @Override
        public void clearCollection(String collection) throws DeleteObjectFailed {
            list.clear();
            savedList.clear();
            clearDbCalled = true;
        }

        @Override
        public List<HashMap<String, String>> loadCollection(String collection) throws LoadCollectionFailed {
            if(collection != RouteStorage.COLLECTION_NAME)
                throw new LoadCollectionFailed("Incorrect collection name");

            ArrayList< HashMap<String, String> > collectionList = new ArrayList<>();

            for (RouteObject r : list ) {
                HashMap<String, String> item = new HashMap<>();
                item.put(RouteStorage.R_ID_KEY,  "" + r.getR_id());
                item.put(RouteStorage.TYPE_KEY,  "" + new String(r.getTransport_type()));
                item.put(RouteStorage.TITLE_KEY, "" + new String(r.getTitle()));
                item.put(RouteStorage.COST_KEY,  "" + r.getCost());
                collectionList.add(item);
            }

            return collectionList;
        }
    }


    private MockLocalStorage mockLocalStorage;
    private MockApiClient mockApiClient;


    public TestRoutes() {
        mockLocalStorage = new MockLocalStorage();
        mockApiClient = new MockApiClient();
    }

    @BeforeClass
    public static void setUpSuite() {

    }

    @Before
    public void setUpCase() {
        mockLocalStorage.clear();
        mockLocalStorage.clear();
    }





    @Test
    public void loadFromLocalStorage() {
        TransportRoutes routes = new TransportRoutes(mockApiClient, mockLocalStorage);

        // Set mock to fail if provider tries to fetch data from server
        mockApiClient.setFailVariant(1);

        // Put test route to mock storage
        RouteObject r = new RouteObject();
        r.setR_id(123);
        r.setTransport_type("hell_rider");
        r.setTitle("x77x");
        r.setCost(33);
        mockLocalStorage.put(r);

        assertFalse("Local routes list should not be cleared", mockLocalStorage.clearDbCalled);

        assertTrue("Failed to load routes from local storage", routes.load());
        assertTrue("One route", routes.getAll().size() == 1);

        RouteObject loadedRoute = routes.getAll().get(0);

        assertTrue("The same r_id",  loadedRoute.getR_id() == r.getR_id());
        assertTrue("The same cost",  loadedRoute.getCost() == r.getCost());
        assertTrue("The same title", loadedRoute.getTitle().equals(r.getTitle()));
        assertTrue("The same type",  loadedRoute.getTransport_type().equals(r.getTransport_type()));
    }




    @Test
    public void fetchLocallyAndSave() {
        TransportRoutes routes = new TransportRoutes(mockApiClient, mockLocalStorage);

        // Put test route to mock storage
        RouteObject r = new RouteObject();
        r.setR_id(444);
        r.setTransport_type("local_redneck");
        r.setTitle("foo");
        r.setCost(44);
        mockApiClient.put(r);
        mockApiClient.setFailVariant(0);

        assertTrue("Failed to fetch routes from mock client", routes.load());
        assertTrue("Local routes list should be cleaned before save", mockLocalStorage.clearDbCalled);
        assertTrue("One route fetched", routes.getAll().size() == 1);
        assertTrue("One route saved", mockLocalStorage.savedList.size() == 1);

        RouteObject loadedRoute = routes.getAll().get(0);
        RouteObject savedRoute = mockLocalStorage.savedList.get(0);

        assertTrue("Not the same r_id",  loadedRoute.getR_id() == r.getR_id());
        assertTrue("Not the same cost",  loadedRoute.getCost() == r.getCost());
        assertTrue("Not the same title", loadedRoute.getTitle().equals(r.getTitle()));
        assertTrue("Not the same type",  loadedRoute.getTransport_type().equals(r.getTransport_type()));

        assertTrue("Not the same r_id",  savedRoute.getR_id() == r.getR_id());
        assertTrue("Not the same cost",  savedRoute.getCost() == r.getCost());
        assertTrue("Not the same title", savedRoute.getTitle().equals(r.getTitle()));
        assertTrue("Not the same type",  savedRoute.getTransport_type().equals(r.getTransport_type()));
    }



    @Test
    public void fetchFromServer() {
        TransportRoutes routes = new TransportRoutes(ApiClient.instance(), mockLocalStorage);
        // Clear mock storage to force provider to fetch routes from server
        mockLocalStorage.clear();

        assertTrue("can not to log in", Auth.login("admin", "admin"));

        assertTrue("failed to load", routes.load());

        assertTrue("no routes", routes.getAll().size() >= 0);

        System.out.println();
        System.out.println("#####################################################################");
        System.out.println("# FETCHED ROUTES                                                    #");
        System.out.println("#####################################################################");
        System.out.printf("%-5s%-15s%-6s%n", "ID", "Type", "Number");
        for (RouteObject route : routes.getAll()) {
            System.out.printf("%-5d%-15s%-6s%n", route.getR_id(), route.getTransport_type(), route.getTitle());
            assertTrue("empty title", route.getTitle().length() > 0);
            assertTrue("empty type", route.getTransport_type().length() > 0);
            assertTrue("incorrect rid", route.getR_id() > 0);
            assertTrue("incorrect cost", route.getCost() >= 0);
        }
        System.out.println("#####################################################################");
        System.out.println();
    }
}