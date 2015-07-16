package uni.android.md.muc_coursework;

import java.io.Serializable;

/**
 * Created by Mark on 22/11/2014.
 */
public class RSSDataItem implements Serializable {
    //Class which hold information parsed from rss feed
    private String itemTitle;
    private String itemDesc;
    private String itemLink;

    public RSSDataItem() {
        this.itemTitle = "";
        this.itemDesc = "";
        this.itemLink = "";
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemLink() {
        return itemLink;
    }

    public void setItemLink(String itemLink) {
        this.itemLink = itemLink;
    }

    @Override
    public String toString() {
        String bookReviewData;
        bookReviewData = "mcRSSDataItem [itemTitle=" + itemTitle;
        bookReviewData = ", itemDesc=" + itemDesc;
        bookReviewData = ", itemLink=" + itemLink + "]";
        return bookReviewData;
    }
}
