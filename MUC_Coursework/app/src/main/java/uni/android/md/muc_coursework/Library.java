package uni.android.md.muc_coursework;

import android.content.Context;

import java.io.IOException;

/**
 * Created by Mark on 21/11/2014.
 */
public class Library {
//Class which handles access to library database.

    private Book[] library;
    private LibraryDBMgr2 dbMgr;
    private Book bookTest;

    public Library(Context context) {


        dbMgr = new LibraryDBMgr2(context, "sqlLibrary2.s3db", null, 1);

        try {
            dbMgr.dbCreate();

        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    public Book[] getLibrary() {
        return library;
    }

    public void loadLibrary() {
       /* try {
            dbMgr.dbCreate();

        } catch (IOException e) {
            e.printStackTrace();
        }*/

        library = dbMgr.loadBooks();
    }

    public Book[] getReading() {
        return dbMgr.getReading();
    }

    public Book loadBook() {


        try {
            dbMgr.dbCreate();

        } catch (IOException e) {
            e.printStackTrace();
        }
        bookTest = new Book();
        bookTest = dbMgr.findBook("Moby Dick");
        return bookTest;

    }

    public void DeleteBook(int id) {
        dbMgr.removeBook(library[id].getTitle());
    }

    public void ReadBook(int id) {
        dbMgr.readBook(library[id].getBookID());
        library = dbMgr.loadBooks();
    }

    public void AddUpdateBook(Book aBook) {
        dbMgr.addUpdateBook(aBook);
        library = dbMgr.loadBooks();
    }
}
