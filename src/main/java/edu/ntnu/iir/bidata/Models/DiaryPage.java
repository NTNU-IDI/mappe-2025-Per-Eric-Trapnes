package edu.ntnu.iir.bidata.models;

import edu.ntnu.iir.bidata.manager.EncryptionManager;

/**
 * Represents a diary page in the digital diary application.
 * Stores the page identifier, creation time, and last edited time,
 * with values encrypted for security.
 *
 * <p>This class provides methods to retrieve and update
 * encrypted diary metadata using the {@link EncryptionManager}.
 *
 * @author Per Eric
 */
public class DiaryPage {
  private String diaryId;
  private String createdTime;
  private String editedTime = "";

  /**
   * Creates a new diary page with an encrypted creation time.
   *
   * @param id          the encryption key or user identifier
   * @param diaryId     the unique identifier of the diary page
   * @param createdTime the creation time of the page
   */
  public DiaryPage(String id, String diaryId, String createdTime) {
    this.diaryId = diaryId;
    try {
      this.createdTime = EncryptionManager.encrypt(createdTime, id);
    } catch (Exception errorMessage) {
      errorMessage.printStackTrace();
    }
  }

  /**
   * Returns the decrypted diary page identifier.
   *
   * @param id the encryption key or user identifier
   * @return the decrypted diary ID
   */
  public String getDiaryId(String id) {
    try {
      return EncryptionManager.decrypt(diaryId, id);
    } catch (Exception errorMessage) {
      errorMessage.printStackTrace();
    }
    return diaryId;
  }

  /**
   * Returns the decrypted creation time of the diary page.
   *
   * @param id the encryption key or user identifier
   * @return the decrypted creation time
   */
  public String getCreatedTime(String id) {
    try {
      return EncryptionManager.decrypt(createdTime, id);
    } catch (Exception errorMessage) {
      errorMessage.printStackTrace();
    }
    return createdTime;
  }

  /**
   * Returns the decrypted last edited time of the diary page.
   *
   * @param id the encryption key or user identifier
   * @return the decrypted edited time
   */
  public String getEditedTime(String id) {
    try {
      return EncryptionManager.decrypt(editedTime, id + id);
    } catch (Exception errorMessage) {
      errorMessage.printStackTrace();
    }
    return editedTime;
  }

  /**
   * Updates the last edited time of the diary page.
   *
   * @param id         the encryption key or user identifier
   * @param editedTime the new edited time to set
   */
  public void setEditedTime(String id, String editedTime) {
    try {
      this.editedTime = EncryptionManager.encrypt(editedTime, id + id);
    } catch (Exception errorMessage) {
      errorMessage.printStackTrace();
    }
  }
}