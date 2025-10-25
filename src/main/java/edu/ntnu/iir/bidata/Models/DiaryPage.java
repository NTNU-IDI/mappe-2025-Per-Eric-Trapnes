package edu.ntnu.iir.bidata.Models;

public class DiaryPage {
    private String DiaryID;
    private String CreatedTime;
    private String EditedTime = "";

    public DiaryPage(String DiaryID, String CreatedTime) {
        this.DiaryID = DiaryID;
        this.CreatedTime = CreatedTime;
    }

    public String getDiaryID() {
        return DiaryID;
    }

    public String getCreatedTime() {
        return CreatedTime;
    }

    public String getEditedTime() {
        return EditedTime;
    }

    public void setEditedTime(String EditedTime) {
        this.EditedTime = EditedTime;
    }
}
