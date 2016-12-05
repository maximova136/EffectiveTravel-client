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

public class AppSQliteDb implements ILocalStorage {
    private AppDbHelper helper;
    private SQLiteDatabase db;

    public AppSQliteDb(Context context) {
        helper = new AppDbHelper(context);
        db = helper.getWritableDatabase();
    }

    public void close() {
        db.close();
        helper.close();
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
        String[] columnNames = cursor.getColumnNames();
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
}
