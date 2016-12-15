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
import com.et.response.object.StatisticsObject;
import com.et.response.object.RouteObject;
import com.et.response.object.StationObject;
import com.et.stations.StationsList;
import com.et.stations.StationsStorage;
import com.et.storage.ILocalStorage;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;



public class TestStations {

    private class MockApiClient implements IApiClient {
        private int failVariant = 0;
        private ArrayList<StationObject> list;

        public MockApiClient() {
            list = new ArrayList<>();
        }

        public void clear() {
            list.clear();
        }

        public void put(StationObject station) {
            list.add(station);
        }

        public void setFailVariant(int v) {
            failVariant = v;
        }

        @Override
        public String login(String login, String password) throws LoginFailedException {
            throw new LoginFailedException("Mock API login");
        }

        @Override
        public String signup(String login, String password) throws SignupFailedException {
            throw new SignupFailedException("Mock API signup");
        }

        @Override
        public List<RouteObject> routes() throws RequestFailedException, InsuccessfulResponseException {
            throw new InsuccessfulResponseException("Mock API routes");
        }

        @Override
        public List<StationObject> stations() throws RequestFailedException, InsuccessfulResponseException {
            switch (failVariant) {
                case 1:
                    throw new InsuccessfulResponseException("Mock API stations");
                case 2:
                    throw new RequestFailedException("Mock API stations");

                default:
                    return list;
            }
        }

        @Override
        public StatisticsObject statistics(int s_id, int r_id) throws RequestFailedException, InsuccessfulResponseException {
            throw new SignupFailedException("Mock");
        }

        @Override
        public void submitNote(int s_id, int r_id, String time) throws RequestFailedException, InsuccessfulResponseException {
            throw new SignupFailedException("Mock");
        }
    }

    private class MockLocalStorage implements ILocalStorage {
        public ArrayList<StationObject> list;
        public ArrayList<StationObject> savedList;

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

        public void put(StationObject station) {
            list.add(station);
        }

        @Override
        public void putObject(String collection, HashMap<String, String> values) throws PutObjectFailed {
            if(collection != StationsStorage.COLLECTION_NAME)
                throw new PutObjectFailed("Incorrect collection name");

            StationObject r = new StationObject();
            r.setS_id(Integer.parseInt(values.get(StationsStorage.S_ID_KEY)));
            r.setTitle(values.get(StationsStorage.TITLE_KEY));
            r.setRoutes(StationsStorage.stringToRoutesList(values.get(StationsStorage.ROUTES_KEY)));


            savedList.add(r);
        }

        @Override
        public void updateObject(String collection, int objectId, HashMap<String, String> values) throws UpdateObjectFailed {
            if(collection != StationsStorage.COLLECTION_NAME)
                throw new UpdateObjectFailed("Incorrect collection name");
        }

        @Override
        public void deleteObject(String collection, int objectId) throws DeleteObjectFailed {
            if(collection != StationsStorage.COLLECTION_NAME)
                throw new DeleteObjectFailed("Incorrect collection name");

        }

        @Override
        public List<HashMap<String, String>> loadCollection(String collection) throws LoadCollectionFailed {
            if(collection != StationsStorage.COLLECTION_NAME)
                throw new LoadCollectionFailed("Incorrect collection name");

            ArrayList< HashMap<String, String> > collectionList = new ArrayList<>();

            for (StationObject s : list ) {
                HashMap<String, String> item = new HashMap<>();
                item.put(StationsStorage.S_ID_KEY,  "" + s.getS_id());
                item.put(StationsStorage.TITLE_KEY, "" + new String(s.getTitle()));
                item.put(StationsStorage.ROUTES_KEY, StationsStorage.routesListToString(s.getRoutes()));
                collectionList.add(item);
            }

            return collectionList;
        }

        @Override
        public void clearCollection(String collection) throws DeleteObjectFailed {
            list.clear();
            savedList.clear();
            clearDbCalled = true;
        }
    }

    private MockLocalStorage mockLocalStorage;
    private MockApiClient mockApiClient;


    public TestStations() {
        mockLocalStorage = new MockLocalStorage();
        mockApiClient = new MockApiClient();
    }

    @BeforeClass
    public static void setUpSuite() {
        assertTrue(Auth.login("admin", "admin"));
    }

    @Before
    public void setUpCase() {
        mockLocalStorage.clear();
        mockLocalStorage.clear();
    }





    @Test
    public void loadFromLocalStorage() {
        StationsList stations = new StationsList(mockApiClient, mockLocalStorage);

        // Set mock to fail if provider tries to fetch data from server
        mockApiClient.setFailVariant(1);

        // Put test route to mock storage
        StationObject s = new StationObject();
        s.setTitle("x77x");
        s.setS_id(222);
        ArrayList<Integer> routes = new ArrayList<>();
        routes.add(31);
        routes.add(42);
        routes.add(56);
        s.setRoutes(routes);
        mockLocalStorage.put(s);

        assertFalse("Local stations list should not be cleared", mockLocalStorage.clearDbCalled);

        assertTrue("Failed to load stations from local storage", stations.load());
        assertEquals("One route", 1, stations.getAll().size());

        StationObject loadedStation = stations.getAll().get(0);

        assertTrue("The same r_id",  loadedStation.getS_id() == s.getS_id());
        assertTrue("The same title", loadedStation.getTitle().equals(s.getTitle()));
    }




    @Test
    public void fetchLocallyAndSave() {
        StationsList stations = new StationsList(mockApiClient, mockLocalStorage);

        // Put test route to mock storage
        StationObject s = new StationObject();
        s.setS_id(444);
        s.setTitle("foo");
        ArrayList<Integer> routes = new ArrayList<>();
        routes.add(31);
        routes.add(42);
        routes.add(56);
        s.setRoutes(routes);
        mockApiClient.put(s);
        mockApiClient.setFailVariant(0);

        assertTrue("Failed to fetch stations from mock client", stations.load());
        assertTrue("Local stations list should be cleaned before save", mockLocalStorage.clearDbCalled);
        assertEquals("One route fetched", 1, stations.getAll().size());
        assertEquals("One route saved",   1, mockLocalStorage.savedList.size());

        StationObject loadedStation = stations.getAll().get(0);
        StationObject savedStation = mockLocalStorage.savedList.get(0);

        assertTrue("Not the same s_id",  loadedStation.getS_id() == s.getS_id());
        assertTrue("Not the same title", loadedStation.getTitle().equals(s.getTitle()));
        assertTrue("Not the same title", loadedStation.getRoutes().equals(s.getRoutes()));

        assertTrue("Not the same s_id",  savedStation.getS_id() == s.getS_id());
        assertTrue("Not the same title", savedStation.getTitle().equals(s.getTitle()));
        assertTrue("Not the same title", savedStation.getRoutes().equals(s.getRoutes()));
    }



    @Test
    public void fetchFromServer() {
        StationsList stations = new StationsList(ApiClient.instance(), mockLocalStorage);
        // Clear mock storage to force provider to fetch routes from server
        mockLocalStorage.clear();

        assertTrue("Failed to fetch stations from server", stations.load());

        assertTrue("No stations on server", stations.getAll().size() >= 0);

        System.out.println();
        System.out.println("#####################################################################");
        System.out.println("# FETCHED STATIONS                                                  #");
        System.out.println("#####################################################################");
        System.out.printf("%-5s%-15s%n", "ID", "Title");
        for (StationObject station : stations.getAll()) {
            System.out.printf("%-5d%-15s%n", station.getS_id(), station.getTitle());
            assertTrue("empty title", station.getTitle().length() > 0);
            assertTrue("incorrect sid", station.getS_id() >= 0);
        }
        System.out.println("#####################################################################");
        System.out.println();
    }



    @Test
    public void fetchFail() {
        StationsList stations = new StationsList(mockApiClient, mockLocalStorage);
        mockApiClient.setFailVariant(2);

        assertFalse("Load should fail", stations.load());
    }
}
