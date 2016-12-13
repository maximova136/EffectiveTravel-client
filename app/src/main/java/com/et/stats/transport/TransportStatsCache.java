package com.et.stats.transport;


import android.content.ContentValues;
import android.database.Cursor;

import com.et.response.StatisticsObject;
import com.et.response.object.FreqObject;
import com.et.storage.ISQLiteDb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransportStatsCache implements ITransportStatsCache {
    public static final String TABLE_NAME  = "statistics";
    public static final String R_ID_COLUMN = "r_id";
    public static final String R_ID_COLUMN_TYPE = "INT";
    public static final String S_ID_COLUMN = "s_id";
    public static final String S_ID_COLUMN_TYPE = "INT";
    public static final String WEEKDAY_STATS_COLUMN = "weekday_stats";
    public static final String WEEKDAY_STATS_COLUMN_TYPE = "TEXT";
    public static final String FRIDAY_STATS_COLUMN  = "friday_stats";
    public static final String FRIDAY_STATS_COLUMN_TYPE  = "TEXT";
    public static final String WEEKEND_STATS_COLUMN = "weekend_stats";
    public static final String WEEKEND_STATS_COLUMN_TYPE = "TEXT";
    public static final String CACHE_EXP_TIME_COLUMN = "cache_expires";
    public static final String CACHE_EXP_TIME_COLUMN_TYPE = "TEXT";

    public static final String FREQ_OBJECT_SEP = ";";
    public static final String TIME_COUNT_SEP = "=";

    public static final SimpleDateFormat CACHE_EXPIRES_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    ISQLiteDb db;

    public TransportStatsCache(ISQLiteDb db) {
        this.db = db;
    }

    @Override
    public StatisticsObject load(int s_id, int r_id) {
        Integer sId = new Integer(s_id);
        Integer rId = new Integer(r_id);

//        Cursor result = db.query(
//                TABLE_NAME,
//                new String[] { WEEKDAY_STATS_COLUMN, FRIDAY_STATS_COLUMN, WEEKEND_STATS_COLUMN, CACHE_EXP_TIME_COLUMN },
//                S_ID_COLUMN + " = ? AND " + R_ID_COLUMN + " = ?",
//                new String[] { sId.toString(), rId.toString() });

        Cursor result = db.rawQuery("SELECT " +
                WEEKDAY_STATS_COLUMN + ", " + FRIDAY_STATS_COLUMN + ", " +
                WEEKEND_STATS_COLUMN + ", " + CACHE_EXP_TIME_COLUMN + " FROM " + TABLE_NAME + " WHERE " + S_ID_COLUMN + " = " + sId.toString() +
                " AND " + R_ID_COLUMN + " = " + rId.toString() + " AND " + CACHE_EXP_TIME_COLUMN + " > datetime('now', '-1 minutes')", null);

        if(result.getCount() <= 0)
            return null;

        result.moveToFirst();

        StatisticsObject stats = new StatisticsObject();

        stats.setWeekdaysFreq(stringToStatsList(result.getString(0)));
        stats.setFridayFreq(stringToStatsList(result.getString(1)));
        stats.setWeekendFreq(stringToStatsList(result.getString(2)));

//        Date lastUpdated = CACHE_EXPIRES_FORMAT.parse(result.getString(4));

        return stats;
    }

    @Override
    public void save(int s_id, int r_id, StatisticsObject statistics) {
        Integer sId = new Integer(s_id);
        Integer rId = new Integer(r_id);

        ContentValues values = new ContentValues();
        values.put(WEEKDAY_STATS_COLUMN, statListToString(statistics.getWeekdaysFreq()));
        values.put(WEEKEND_STATS_COLUMN, statListToString(statistics.getWeekendFreq()));
        values.put(FRIDAY_STATS_COLUMN,  statListToString(statistics.getFridayFreq()));
        values.put(CACHE_EXP_TIME_COLUMN, CACHE_EXPIRES_FORMAT.format(new Date()));

        int updated = db.update(TABLE_NAME, values, S_ID_COLUMN + " = ? AND " + R_ID_COLUMN + " = ?", new String[] { rId.toString(), sId.toString() });

        // If entry already cached then update
        if(updated != 1) {
            removeEntry(s_id, r_id);

            values.put(S_ID_COLUMN, s_id);
            values.put(R_ID_COLUMN, r_id);
            db.insert(TABLE_NAME, values);
        }
    }

    @Override
    public void clear() {
        db.delete(TABLE_NAME, null, null);
    }

    private void removeEntry(int s_id, int r_id) {
        Integer sId = new Integer(s_id);
        Integer rId = new Integer(r_id);

        db.delete(TABLE_NAME, S_ID_COLUMN + " = ? AND " + R_ID_COLUMN + " = ?", new String[] {sId.toString(), rId.toString()});
    }


    private String statListToString(List<FreqObject> stats) {
        StringBuilder builder = new StringBuilder();

        for(FreqObject freq : stats) {
            builder.append(freqToString(freq));
            builder.append(FREQ_OBJECT_SEP);
        }

        return builder.toString();
    }


    private List<FreqObject> stringToStatsList(String statsString) {
        ArrayList<FreqObject> list = new ArrayList<>();
        String[] freqStrings = statsString.split(FREQ_OBJECT_SEP);

        for(String freqString : freqStrings) {
            if(freqString.length() <= 0)
                continue;

            System.out.println(freqString);

            FreqObject freqObject = stringToFreq(freqString);
            list.add(freqObject);
        }

        return list;
    }


    private String freqToString(FreqObject freq) {
        StringBuilder builder = new StringBuilder();

        builder.append(freq.getTime().trim());
        builder.append(TIME_COUNT_SEP);
        builder.append(freq.getCount());

        return builder.toString();
    }

    private FreqObject stringToFreq(String freqString) {
        String[] parts = freqString.split(TIME_COUNT_SEP);
        FreqObject freq = new FreqObject();
        freq.setTime(parts[0].trim());
        //freq.setCount(Double.parseDouble(parts[1].trim()));
        freq.setCount(Float.parseFloat(parts[1].trim()));
        return freq;
    }
}
