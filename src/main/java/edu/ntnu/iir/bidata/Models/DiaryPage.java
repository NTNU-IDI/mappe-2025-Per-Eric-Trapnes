package edu.ntnu.iir.bidata.Models;

import edu.ntnu.iir.bidata.Manager.EncryptionManager;

public class DiaryPage {
    private String DiaryID;
    private String CreatedTime;
    private String EditedTime = "";

    public DiaryPage(String UID, String DiaryID, String CreatedTime) {
        this.DiaryID = DiaryID;

        try {
            this.CreatedTime = EncryptionManager.encrypt(CreatedTime, UID);
        } catch (Exception errorMessage) {
            errorMessage.printStackTrace();
        }
    }

    public String getDiaryID(String UID) {
        try {
            return EncryptionManager.decrypt(DiaryID, UID);
        } catch (Exception errorMessage) {
            errorMessage.printStackTrace();
        }
        return DiaryID;
    }

    public String getCreatedTime(String UID) {
        try {
            return EncryptionManager.decrypt(CreatedTime, UID);
        } catch (Exception errorMessage) {
            errorMessage.printStackTrace();
        }
        return CreatedTime;
    }

    public String getEditedTime(String UID) {
        try {
            return EncryptionManager.decrypt(EditedTime, UID + UID);
        } catch (Exception errorMessage) {
            errorMessage.printStackTrace();
        }
        return EditedTime;
    }

    public void setEditedTime(String UID, String EditedTime) {
        try {
            this.EditedTime = EncryptionManager.encrypt(EditedTime, UID + UID);
        } catch (Exception errorMessage) {
            errorMessage.printStackTrace();
        }
    }
}
