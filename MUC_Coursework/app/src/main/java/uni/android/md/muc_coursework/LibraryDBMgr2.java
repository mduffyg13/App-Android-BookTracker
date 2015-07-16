package uni.android.md.muc_coursework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Mark on 21/11/2014.
 */
public class LibraryDBMgr2 extends SQLiteOpenHelper {
//Same functions as Commit DBMgr class

    private static final int DB_VER = 1;
    private static final String DB_PATH = "/data/data/uni.android.md.muc_coursework/databases/";
    private static final String DB_NAME = "sqlLibrary2.s3db";

    //Books Table
    private static final String TBL_BOOKS = "Books";

    public static final String COL_ID = "id";
    public static final String COL_TITLE = "Title";
    public static final String COL_AUTHOR = "Author";
    public static final String COL_MYRATING = "MyRating";
    public static final String COL_NUMBEROFPAGES = "NumberOfPages";

    public static final String COL_READING = "READING";
    public static final String COL_CURRENTPAGE = "CURRENT_PAGE";
    public static final String COL_STARTDATE = "START_DATE";
    public static final String COL_ENDDATE = "END_DATE";

    //Commits Table
    // private static final String TBL_COMMITS = "Commits";
    // public static final String COL_COMMITDATE = "COMMIT_DATE";
    //  public static final String COL_PAGECOUNT = "PAGE_COUNT";

    private final Context appContext;

    public LibraryDBMgr2(Context context, String name,
                         SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.appContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_BOOKS_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TBL_BOOKS + "("
                + COL_ID + " TEXT,"
                + COL_TITLE + " TEXT," + COL_AUTHOR
                + " TEXT," + COL_MYRATING + " TEXT," + COL_NUMBEROFPAGES + " TEXT,"
                + COL_READING + " TEXT," + COL_CURRENTPAGE + " TEXT,"
                + COL_STARTDATE + " TEXT," + COL_ENDDATE +
                " TEXT" + ")";
        try {
            db.execSQL(CREATE_BOOKS_TABLE);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TBL_BOOKS);
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
            throw new Error("Problems /**/copying DB!");
        }
    }

    public Book[] loadBooks() {
      /*  try {
            this.copyDBFromAssets();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        String query = "Select * FROM " + TBL_BOOKS;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Book[] dbLibrary = new Book[cursor.getCount()];
        SimpleDateFormat format = new SimpleDateFormat("yyyy-dd-MM");

        int j = 0;
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {

            dbLibrary[i] = new Book();
            dbLibrary[i].setBookID(Integer.parseInt(cursor.getString(0)));
            dbLibrary[i].setTitle(cursor.getString(1));
            dbLibrary[i].setAuthor(cursor.getString(2));
            dbLibrary[i].setRating(Integer.parseInt(cursor.getString(3)));
            dbLibrary[i].setNumberOfPages(Integer.parseInt(cursor.getString(4)));

            if (cursor.getString(5) != null) {
                String fi = cursor.getString(5).toUpperCase();
                dbLibrary[i].setReading(Boolean.valueOf(cursor.getString(5)));
            }
            if (cursor.getString(6) != null) {
                dbLibrary[i].setCurrentPage(Integer.parseInt(cursor.getString(6)));
            }

            if (cursor.getString(7) != null) {
                try {
                    dbLibrary[i].setStartDate(format.parse(cursor.getString(7)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (cursor.getString(8) != null) {

                try {
                    dbLibrary[i].setEndDate(format.parse(cursor.getString(8)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            cursor.moveToNext();
            j++;
        }
        cursor.close();
        return dbLibrary;
    }

    public Book[] getReading() {
        //  String query = "Select * FROM " + TBL_STARSIGNSINFO + " WHERE " + COL_STARSIGN + " =  \"" + sStarSign + "\"";

        String query = "Select * FROM " + TBL_BOOKS + " WHERE " + COL_READING + " = \"" + "true" + "\"";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Book[] dbLibrary = new Book[cursor.getCount()];
        SimpleDateFormat format = new SimpleDateFormat("yyyy-dd-MM");

        int j = 0;
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {

            dbLibrary[i] = new Book();
            dbLibrary[i].setBookID(Integer.parseInt(cursor.getString(0)));
            dbLibrary[i].setTitle(cursor.getString(1));
            dbLibrary[i].setAuthor(cursor.getString(2));
            dbLibrary[i].setRating(Integer.parseInt(cursor.getString(3)));
            dbLibrary[i].setNumberOfPages(Integer.parseInt(cursor.getString(4)));

            if (cursor.getString(5) != null) {
                String fi = cursor.getString(5).toUpperCase();
                dbLibrary[i].setReading(Boolean.valueOf(cursor.getString(5)));
            }
            if (cursor.getString(6) != null) {
                dbLibrary[i].setCurrentPage(Integer.parseInt(cursor.getString(6)));
            }

            if (cursor.getString(7) != null) {
                try {
                    dbLibrary[i].setStartDate(format.parse(cursor.getString(7)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (cursor.getString(8) != null) {

                try {
                    dbLibrary[i].setEndDate(format.parse(cursor.getString(8)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            cursor.moveToNext();
            j++;
        }
        cursor.close();
        return dbLibrary;


    }

    public Book findBook(String sBookTitle) {
        String query = "Select * FROM " + TBL_BOOKS + " WHERE " + COL_TITLE + " =  \"" + sBookTitle + "\"";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Book aBook = new Book();


        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            aBook.setBookID(Integer.parseInt(cursor.getString(0)));
            aBook.setTitle(cursor.getString(1));
            aBook.setAuthor(cursor.getString(2));
            //StarSignsInfo.setStarSignImg(cursor.getString(2));
            //StarSignsInfo.setStarSignDates(cursor.getString(3));
            //StarSignsInfo.setStarSignCharacteristics(cursor.getString(4));
            cursor.close();
        } else {
            aBook = null;
        }
        db.close();
        return aBook;
    }

    public boolean removeBook(String sBookTitle) {

        boolean result = false;

        String query = "Select * FROM " + TBL_BOOKS + " WHERE " + COL_TITLE + " =  \"" + sBookTitle + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Book StarSignsInfo = new Book();
        String[] whereArgs = new String[]{String.valueOf(sBookTitle)};
        if (cursor.moveToFirst()) {
            // StarSignsInfo.setStarSignID(Integer.parseInt(cursor.getString(0)));
            db.delete(TBL_BOOKS, COL_TITLE + " = ?",
                    whereArgs);
            cursor.close();
            result = true;
        }

        db.close();
        return result;
    }

    public boolean readBook(int iBookID) {
        boolean result = false;
        String sBookID = Integer.toString(iBookID);

        String query = "Select * FROM " + TBL_BOOKS + " WHERE " + COL_ID + " =  \"" + sBookID + "\"";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        ContentValues cv = new ContentValues();
        cv.put(COL_READING, "true");

        String[] whereArgs = new String[]{String.valueOf(sBookID)};

        if (cursor.moveToFirst()) {
            // StarSignsInfo.setStarSignID(Integer.parseInt(cursor.getString(0)));

            db.update(TBL_BOOKS, cv, COL_ID + " = ?",
                    whereArgs);

            cursor.close();
            result = true;
        }

        db.close();


        return result;
    }

    public void addUpdateBook(Book aBook) {


        //SimpleDateFormat format = new SimpleDateFormat("yyyy-dd-MM");

        String sBookID = String.valueOf(aBook.getBookID());
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * FROM " + TBL_BOOKS + " WHERE " + COL_ID + " =  \"" + sBookID + "\"";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            //If book id exists, update existing book


            ContentValues values = new ContentValues();
            values.put(COL_TITLE, aBook.getTitle());
            values.put(COL_AUTHOR, aBook.getAuthor());
            values.put(COL_NUMBEROFPAGES, String.valueOf(aBook.getNumberOfPages()));
            values.put(COL_READING, String.valueOf(aBook.isReading()));
            values.put(COL_CURRENTPAGE, String.valueOf(aBook.getCurrentPage()));

            String[] whereArgs = new String[]{String.valueOf(sBookID)};

            db.update(TBL_BOOKS, values, COL_ID + " = ?",
                    whereArgs);

            //cursor.close();


        } else {
            //if not create new book entry
            ContentValues values = new ContentValues();
            // values.put(COL_ID, aBook.getBookID());
            values.put(COL_TITLE, aBook.getTitle());
            values.put(COL_AUTHOR, aBook.getAuthor());
            values.put(COL_NUMBEROFPAGES, String.valueOf(aBook.getNumberOfPages()));
            values.put(COL_MYRATING, aBook.getRating());
            values.put(COL_CURRENTPAGE, aBook.getCurrentPage());
            values.put(COL_READING, "false");
            //values.put(COL_STARTDATE, aBook.getStartDate());
            //  values.put(COL_ENDDATE, aBook.getEndDate());
            //rest of entries
//WHAT ABOUT AUTO INCREMENT????????????????????

            db.insert(TBL_BOOKS, null, values);

        }
        cursor.close();
        db.close();
    }
}
