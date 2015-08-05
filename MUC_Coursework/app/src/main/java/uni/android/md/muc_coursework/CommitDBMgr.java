package uni.android.md.muc_coursework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Mark on 04/12/2014.
 */
public class CommitDBMgr extends SQLiteOpenHelper {

//Class which handles access to the Commit table of the database
//Contains methods for loading, adding, updating and deleting fields

    private static final int DB_VER = 1;
    private static final String DB_PATH = "/data/data/uni.android.md.muc_coursework/databases/";
    private static final String DB_NAME = "sqlLibrary2.s3db";


    // Commits Table
    private static final String TBL_COMMITS = "Commits";
    public static final String COL_COMMITDATE = "COMMIT_DATE";
    public static final String COL_PAGECOUNT = "PAGE_COUNT";

    private final Context appContext;

    public CommitDBMgr(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.appContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //First time app is run virtual database is constructed

        String CREATE_COMMITS_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TBL_COMMITS + "("
                + COL_COMMITDATE + " TEXT,"
                + COL_PAGECOUNT + " TEXT" + ")";
        try {
            db.execSQL(CREATE_COMMITS_TABLE);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TBL_COMMITS);
            onCreate(db);
        }
    }

    // ================================================================================
    // Creates a empty database on the system and rewrites it with your own database.
    // ================================================================================
    public void dbCreate() throws IOException {

        boolean dbExist = dbCheck();

        if (!dbExist) {
            //By calling this method an empty database will be created into the default system path
            //of your application so we can overwrite that database with our database.
            this.getReadableDatabase();

            try {

                //Actual database is loaded into constructed version
                copyDBFromAssets();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    // ============================================================================================
    // Check if the database already exist to avoid re-copying the file each time you open the application.
    // @return true if it exists, false if it doesn't
    // ============================================================================================
    private boolean dbCheck() {

        SQLiteDatabase db = null;

        //check if constructed database exists
        try {
            String dbPath = DB_PATH + DB_NAME;
            db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
            db.setLocale(Locale.getDefault());
            db.setVersion(1);

        } catch (SQLiteException e) {

            Log.e("SQLHelper", "Database not Found!");

        }

        if (db != null) {

            db.close();

        }

        return db != null ? true : false;
    }

    // ============================================================================================
    // Copies your database from your local assets-folder to the just created empty database in the
    // system folder, from where it can be accessed and handled.
    // This is done by transfering bytestream.
    // ============================================================================================
    private void copyDBFromAssets() throws IOException {

        InputStream dbInput = null;
        OutputStream dbOutput = null;
        String dbFileName = DB_PATH + DB_NAME;

        try {

            dbInput = appContext.getAssets().open(DB_NAME);
            dbOutput = new FileOutputStream(dbFileName);
            //transfer bytes from the dbInput to the dbOutput
            byte[] buffer = new byte[1024];
            int length;
            while ((length = dbInput.read(buffer)) > 0) {
                dbOutput.write(buffer, 0, length);
            }

            //Close the streams
            dbOutput.flush();
            dbOutput.close();
            dbInput.close();
        } catch (IOException e) {
            throw new Error("Problems copying DB!");
        }
    }

    public Commit[] loadCommits() {

        //Load all rows from commit table

   /*

    try {
            this.copyDBFromAssets();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        String query = "Select * FROM " + TBL_COMMITS;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Commit[] dbCommit = new Commit[cursor.getCount()];
        SimpleDateFormat format = new SimpleDateFormat("yyyy-dd-MM");

        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {

            dbCommit[i] = new Commit();
            try {
                dbCommit[i].setDate(format.parse(cursor.getString(0)));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            dbCommit[i].setPages(Integer.parseInt(cursor.getString(1)));

            cursor.moveToNext();

        }
        cursor.close();
        return dbCommit;
    }


    public void addCommit(Commit aCommit) {

        //Add commit to table
        SimpleDateFormat format = new SimpleDateFormat("yyyy-dd-MM");

        String sDate = format.format(aCommit.getDate());
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * FROM " + TBL_COMMITS + " WHERE " + COL_COMMITDATE + " =  \"" + sDate + "\"";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {

            //If commit date exists, add to existing count
            int totalPageCount = cursor.getInt(1) + aCommit.getPages();
            ContentValues values = new ContentValues();

            values.put(COL_PAGECOUNT, totalPageCount);
            String[] whereArgs = new String[]{String.valueOf(sDate)};

            db.update(TBL_COMMITS, values, COL_COMMITDATE + " = ?",
                    whereArgs);

            cursor.close();


        } else {
            //if not create new commit
            ContentValues values = new ContentValues();
            values.put(COL_COMMITDATE, format.format(aCommit.getDate()));
            values.put(COL_PAGECOUNT, aCommit.getPages());


            db.insert(TBL_COMMITS, null, values);
        }
        db.close();
    }


}
