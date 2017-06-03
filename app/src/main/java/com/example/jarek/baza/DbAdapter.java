package com.example.jarek.baza;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter {
    private static final String DEBUG_TAG = "SqLiteTodoManager";
    private SQLiteDatabase db;
    private Context context;
    private DatabaseHelper dbHelper;

    private static final String DB_NAME = "database.db";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE = "contacts";
    static String DB_TODO_TABLE;

    public static final String KEY_ID = "id";
    public static final String ID_OPTIONS = "INTEGER PRIMART KEY AUTOINCREMENT";
    public static final int ID_COLUMN = 0;
    public static final String KEY_DESCRIPTION = "description";
    public static final String DESCRIPTION_OPTIONS = "TEXT NOT NULL";
    public static final int DESCRIPTION_COLUMN = 1;
    public static final String KEY_NUMBER = "number";
    public static final String NUMBER_OPTIONS = "INTEGER DEFAULT 0";
    public static final int NUMBER_COLUMN = 2;


    private static final String DB_CREATE_TABLE = "CREATE TABLE "
            + DB_TABLE + "(" + KEY_ID + " " + ID_OPTIONS + "," + KEY_DESCRIPTION + " "
            + DESCRIPTION_OPTIONS + "," + KEY_NUMBER + " " + NUMBER_OPTIONS + ");";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + DB_TABLE;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_TABLE);
            Log.d(DEBUG_TAG, "Database creating...");
            Log.d(DEBUG_TAG, "Table " + DB_TABLE + " ver." + DB_VERSION + " created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TABLE);
            Log.d(DEBUG_TAG, "Database upgrading...");
            Log.d(DEBUG_TAG, "Table " + DB_TABLE + " updated from ver." + oldVersion + " to ver." + newVersion);
            Log.d(DEBUG_TAG, "All data is lost.");
            onCreate(db);
        }
    }

    public DbAdapter(Context context) {
        this.context = context;
    }

    public DbAdapter open() {
        dbHelper = new DatabaseHelper(context,DB_NAME,null,DB_VERSION);
        try {
            db = dbHelper.getWritableDatabase();
        }
        catch (SQLException e) {
            db = dbHelper.getReadableDatabase();
        }
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insertContact(int number, String description) {
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_DESCRIPTION, description);
        newValues.put(KEY_NUMBER, number);
        return db.insert(DB_TABLE, null, newValues);
    }

    public boolean updateData(Contacts contact) {
        long id = contact.getId();
        String description = contact.getDescription();
        Integer number = contact.getNumber();
        return updateData(id,description,number);
    }

    private boolean updateData(long id, String description, Integer number) {
        String where = KEY_ID + "=" + id;
        ContentValues updateValues = new ContentValues();
        updateValues.put(KEY_DESCRIPTION, description);
        updateValues.put(KEY_NUMBER, number);
        return db.update(DB_TODO_TABLE, updateValues, where, null) > 0;
    }

    public boolean deleteData(long id) {
        String where = KEY_ID + "=" + id;
        return db.delete(DB_TABLE,where,null) > 0;
    }

    public Cursor getAllData(long id) {
        String[] columns = {KEY_ID, KEY_DESCRIPTION, KEY_NUMBER};
        return db.query(DB_TABLE,columns,null,null,null,null,null);
    }

    public Contacts getData(long id) {
        String[] columns = {KEY_ID, KEY_DESCRIPTION, KEY_NUMBER};
        String where = KEY_ID + "=" + id;
        Cursor cursor = db.query(DB_TABLE,columns,null,null,null,null,null);
        Contacts contact = null;
        if (cursor != null && cursor.moveToFirst()) {
            String description = cursor.getString(DESCRIPTION_COLUMN);
            int number = cursor.getInt(NUMBER_COLUMN);
            contact = new Contacts(id,description,number);
        }
        return contact;

    }
}
