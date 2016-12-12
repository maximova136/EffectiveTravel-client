package com.et.storage;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.et.exception.storage.DeleteObjectFailed;
import com.et.exception.storage.LoadCollectionFailed;
import com.et.exception.storage.PutObjectFailed;
import com.et.exception.storage.UpdateObjectFailed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppSQliteDb implements ILocalStorage, ISQLiteDb {
    private static AppSQliteDb inst = null;

    private AppDbHelper helper;
    private SQLiteDatabase db;

    private boolean isOpened = false;

    public static AppSQliteDb init(Context context) {
        if(inst == null)
            inst = new AppSQliteDb(context);

        return inst;
    }

    public static AppSQliteDb getInstance() {
        return inst;
    }


    private AppSQliteDb(Context context) {
        helper = new AppDbHelper(context);
        db = helper.getWritableDatabase();
        isOpened = true;
    }

    public void open() {
        if(!isOpened) {
            db = helper.getWritableDatabase();
            isOpened = true;
        }
    }

    public void close() {
        if(isOpened) {
            db.close();
            isOpened = false;
        }
    }

    @Override
    public void clearCollection(String collection) throws DeleteObjectFailed {
        db.delete(collection, null, null);
    }

    @Override
    public void putObject(String collection, HashMap<String, String> values) throws PutObjectFailed {
        ContentValues contentValues = new ContentValues();
        for (Map.Entry<String, String> pair : values.entrySet() ) {
            contentValues.put(pair.getKey(), pair.getValue());
        }
        long newRowId = db.insert(collection, null, contentValues);
        if(newRowId < 0) {
            throw new PutObjectFailed("Failed to insert row to SQLite DB");
        }
    }

    @Override
    public void updateObject(String collection, int objectId, HashMap<String, String> values) throws UpdateObjectFailed {
        throw new UpdateObjectFailed("Will not be implemented.");
    }

    @Override
    public void deleteObject(String collection, int objectId) throws DeleteObjectFailed {
        throw new DeleteObjectFailed("Will not be implemented.");
    }

    @Override
    public List<HashMap<String, String>> loadCollection(String collection) throws LoadCollectionFailed {
        ArrayList< HashMap<String, String> > collectionItems = new ArrayList<>();

        Cursor cursor = db.query(collection, null, null, null, null, null, null);
        if(cursor.getCount() <= 0)
            return collectionItems;

        String[] columnNames = cursor.getColumnNames();
        cursor.moveToFirst();
        do {
            HashMap<String, String> item = new HashMap<>();
            for (String column : columnNames ) {
                int columnIndex = cursor.getColumnIndex(column);
                if(columnIndex < 0) {
                    throw new LoadCollectionFailed("No such column: '" + column + "'");
                }
                item.put(column, cursor.getString(columnIndex));
            }
            collectionItems.add(item);
        }
        while(cursor.moveToNext());

        return collectionItems;
    }


    // Adapter
    @Override
    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs) {
        return db.query(table, columns, selection, selectionArgs, null, null, null);
    }


    @Override
    public int delete(String table, String where, String[] whereArgs) {
        return db.delete(table, where, whereArgs);
    }


    @Override
    public long insert(String table, ContentValues values) {
        return db.insert(table, null, values);
    }


    @Override
    public int update(String table, ContentValues values, String where, String[] whereArgs) {
        return db.update(table, values, where, whereArgs);
    }
}
