package nl.inholland.librarysystemjavafx.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    private int itemCode;
    private boolean isAvailable;
    private String title;
    private String author;
    private LocalDate lendingDate;

    public Item() {
    }

    public Item(int itemCode, boolean isAvailable, String title, String author) {
        this.itemCode = itemCode;
        this.isAvailable = isAvailable;
        this.title = title;
        this.author = author;
    }

    public int getItemCode() {
        return itemCode;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean available) {
        this.isAvailable = available;
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

    public LocalDate getLendingDate() {
        return lendingDate;
    }

    public void setLendingDate(LocalDate lendingDate) {
        this.lendingDate = lendingDate;
    }

}
