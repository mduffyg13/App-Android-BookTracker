package uni.android.md.muc_coursework;

import java.util.Date;

/**
 * Created by Mark on 04/12/2014.
 */
public class Commit {
    //Class which holds commit objects which are loaded from the database
    //contains getters and setters for each data field and an overloaded constructor


    Date date;
    int pages;

    public Commit() {
        this.date = null;
        this.pages = 0;
    }

    public Commit(Date date, int pages) {
        this.date = date;
        this.pages = pages;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
