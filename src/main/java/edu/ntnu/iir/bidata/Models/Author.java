package edu.ntnu.iir.bidata.Models;

import java.util.ArrayList;
import java.util.List;

public class Author {
    private String UID;
    private List<DiaryPage> Pages;

    public Author() {
        this.Pages = new ArrayList<>();
    }

    public Author(String UID, List<DiaryPage> Pages) {
        this.UID = UID;
        this.Pages = new ArrayList<>(Pages);
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public List<DiaryPage> getPages() {
        return Pages;
    }

    public void setPages(List<DiaryPage> Pages) {
        // Always store a mutable list
        this.Pages = new ArrayList<>(Pages);
    }

    @Override
    public String toString() {
        return "Author{" +
                "UID='" + UID + '\'' +
                ", Pages=" + Pages +
                '}';
    }
}
