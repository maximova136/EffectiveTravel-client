package com.et.storage;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

public interface ISQLiteDb {
    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs) throws SQLiteException;
    public int delete(String table, String where, String[] whereArgs);
    public long insert(String table, ContentValues values);
    public int update(String table, ContentValues values, String where, String[] whereArgs);
    public void exec(String sqlQuery);
    public Cursor rawQuery(String query, String[] queryParams);
}
