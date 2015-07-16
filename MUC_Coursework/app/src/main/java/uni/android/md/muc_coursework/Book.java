package uni.android.md.muc_coursework;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Mark on 21/11/2014.
 */
public class Book {//implements Parcelable {

    //Class which holds book objects which are loaded from the database
    //contains getters and setters for each data field and an overloaded constructor


    private int bookID;
    private String title;
    private String author;
    private int rating; //not used
    private int numberOfPages;

    private boolean reading;
    private int currentPage;
    private Date startDate; //not used
    private Date endDate;//not used


    public Book() {
        this.bookID = 0;
        this.title = "";
        this.author = "";
        this.rating = 0;
        this.numberOfPages = 0;
        this.reading = false;
        this.currentPage = 0;
        this.startDate = null;
        this.endDate = null;


    }

    // public Book(String title, String author, int iSBN, int iSBN13, int rating, String publisher, int numberOfPages, int publicationYear) {
    public Book(int id, String title, String author, int rating, int numberOfPages, boolean reading, int currentPage, Date startDate, Date endDate) {
        this.bookID = id;
        this.title = title;
        this.author = author;
        this.rating = rating;
        this.numberOfPages = numberOfPages;
        this.reading = reading;
        this.currentPage = currentPage;
        this.startDate = startDate;
        this.endDate = endDate;


    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public boolean isReading() {
        return reading;
    }

    public void setReading(boolean reading) {
        this.reading = reading;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }



/*    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }*/
}
