package com.et.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.et.routes.RouteStorage;
import com.et.routes.RouteType;
import com.et.stations.StationsStorage;
import com.et.stats.personal.PersonalStatsStorage;


public class AppDbHelper extends SQLiteOpenHelper {
    // DB
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "EffectiveTravel_001.db";

    // =============================================================================================
    // Table for routes
    // =============================================================================================
    public static abstract class RouteEntry implements BaseColumns {
        public static final String TABLE_NAME            = RouteStorage.COLLECTION_NAME;
        public static final String COLUMN_R_ID           = RouteStorage.R_ID_KEY;
        public static final String COLUMN_TITLE          = RouteStorage.TITLE_KEY;
        public static final String COLUMN_COST           = RouteStorage.COST_KEY;
        public static final String COLUMN_TRANSPORT_TYPE = RouteStorage.TYPE_KEY;
    }
    // SQL Create query
    public static final String SQL_CREATE_ROUE_ENTRIES = "CREATE TABLE " + RouteEntry.TABLE_NAME + " ( " +
//            RouteEntry._ID + " INTEGER PRIMARY KEY, " +
            RouteEntry.COLUMN_R_ID  + " TEXT, " +
            RouteEntry.COLUMN_TITLE + " TEXT, " +
            RouteEntry.COLUMN_COST  + " TEXT, " +
            RouteEntry.COLUMN_TRANSPORT_TYPE + " TEXT )";

    public static final String SQL_DROP_ROUTE_ENTRIES = "DROP TABLE IF EXISTS " + RouteEntry.TABLE_NAME;
    // End of routes table
    // =============================================================================================




    // =============================================================================================
    // Table for stations
    // =============================================================================================
    public static abstract class StationEntry implements BaseColumns {
        public static final String TABLE_NAME            = StationsStorage.COLLECTION_NAME;
        public static final String COLUMN_S_ID           = StationsStorage.S_ID_KEY;
        public static final String COLUMN_TITLE          = StationsStorage.TITLE_KEY;
    }
    // SQL Create query
    public static final String SQL_CREATE_STATION_ENTRIES = "CREATE TABLE " + StationEntry.TABLE_NAME + " ( " +
//            StationEntry._ID + " INTEGER PRIMARY KEY, " +
            StationEntry.COLUMN_S_ID  + " TEXT, " +
            StationEntry.COLUMN_TITLE + " TEXT )";
    // SQL Drop query
    public static final String SQL_DROP_STATION_ENTRIES = "DROP TABLE IF EXISTS " + StationEntry.TABLE_NAME;
    // =============================================================================================




    // =============================================================================================
    // Table for personal stats
    // =============================================================================================
    public static abstract class PersonalStatsEntry implements BaseColumns {
        public static final String TABLE_NAME            = PersonalStatsStorage.COLLECTION_NAME;
        public static final String BUS_COLUMN            = RouteType.BUS;
        public static final String TRAM_COLUMN           = RouteType.TRAM;
        public static final String TROLLEY_COLUMN        = RouteType.TROLLEY;
        public static final String SHUTTLE_COLUMN        = RouteType.SHUTTLE;
    }
    // SQL Create entry
    public static final String SQL_CREATE_PERSONAL_STATS_ENTRY = "CREATE TABLE " + PersonalStatsEntry.TABLE_NAME + " ( " +
            PersonalStatsEntry.BUS_COLUMN       + " TEXT, " +
            PersonalStatsEntry.TRAM_COLUMN      + " TEXT, " +
            PersonalStatsEntry.TROLLEY_COLUMN   + " TEXT, " +
            PersonalStatsEntry.SHUTTLE_COLUMN   + " TEXT )";
    // SQL Drop query
    public static String SQL_DROP_PERSONAL_STATS_ENTRY = "DROP TABLE IF EXISTS " + PersonalStatsEntry.TABLE_NAME;
    // End of personal stats
    // =============================================================================================




    public AppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ROUE_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_STATION_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_PERSONAL_STATS_ENTRY);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DROP_ROUTE_ENTRIES);
        sqLiteDatabase.execSQL(SQL_DROP_STATION_ENTRIES);
        sqLiteDatabase.execSQL(SQL_DROP_PERSONAL_STATS_ENTRY);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
