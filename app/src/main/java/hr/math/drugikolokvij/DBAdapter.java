package hr.math.drugikolokvij;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by aivekov on 1/26/18.
 */

public class DBAdapter {

    static final String TAG = "DBAdapter";

    static final String DATABASE_NAME = "MyDatabase";
    static final int DATABASE_VERSION = 3;

    static final String KEY_ID = "ID";

    static final String KEY_ADRESA = "ADRESA";
    static final String KEY_VRSTA = "VRSTA";

    static final String KEY_ID_VLASNIKA = "ID_VLASNIKA";
    static final String KEY_ID_STRANICE = "ID_STRANICE";

    static final String DATABASE_TABLE_STRANICE = "stranice";
    static final String DATABASE_TABLE_VLASNICI = "vlasnici";

    static final String CREATE_TABLE_STRANICE = "create table stranice (ID integer primary key autoincrement," + "ADRESA text not null, VRSTA text not null);";

    static final String CREATE_TABLE_VLASNICI = "create table vlasnici (ID_VLASNIKA integer primary key autoincrement," + "ID_STRANICE integer);";

    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(CREATE_TABLE_STRANICE);
                db.execSQL(CREATE_TABLE_VLASNICI);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading db from" + oldVersion + "to"
                    + newVersion );
            db.execSQL("DROP TABLE IF EXISTS stranice");
            db.execSQL("DROP TABLE IF EXISTS vlasnici");
            onCreate(db);
        }
    }

    //---opens the database---
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }

    //---insert a site into the database---
    public long insertSite(String adresa, String vrsta)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ADRESA, adresa);
        initialValues.put(KEY_VRSTA, vrsta);
        return db.insert(DATABASE_TABLE_STRANICE, null, initialValues);
    }

    //---insert an owner into the database---
    public long insertOwner(int stranica)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID_STRANICE, stranica);
        return db.insert(DATABASE_TABLE_VLASNICI, null, initialValues);
    }

    //---deletes a particular site--
    public boolean deleteSite(int siteId)
    {
        return db.delete(DATABASE_TABLE_STRANICE, KEY_ID + "=" + siteId, null) > 0;
    }

    //---deletes a particular owner---
    public boolean deleteOwner(int rowId)
    {
        return db.delete(DATABASE_TABLE_VLASNICI, KEY_ID_VLASNIKA + "=" + rowId, null) > 0;
    }

    //---retrieves all the sites---
    public Cursor getAllSites()
    {
        return db.query(DATABASE_TABLE_STRANICE, new String[] {KEY_ID, KEY_ADRESA, KEY_VRSTA}, null, null, null, null, null);
    }

    //---retrieves all the owners---
    public Cursor getAllOwners()
    {
        return db.query(DATABASE_TABLE_VLASNICI, new String[] {KEY_ID_VLASNIKA, KEY_ID_STRANICE},
                null, null, null, null, null);
    }

    public Cursor getSite(int siteId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE_STRANICE, new String[] {KEY_ID,
                                KEY_ADRESA, KEY_VRSTA}, KEY_ID + "=" + siteId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
}
