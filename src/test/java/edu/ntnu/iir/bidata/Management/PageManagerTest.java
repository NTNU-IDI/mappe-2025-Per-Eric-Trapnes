package edu.ntnu.iir.bidata.Management;

import edu.ntnu.iir.bidata.Manager.EncryptionManager;
import edu.ntnu.iir.bidata.Manager.FileManager;
import edu.ntnu.iir.bidata.Manager.PageManager;
import edu.ntnu.iir.bidata.Models.Author;
import edu.ntnu.iir.bidata.Models.DiaryPage;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class PageManagerTest {

    private static Path tempPagesDir;
    private static File draftFile;
    private final String testUID = "testuid";
    private final String testUsername = "testuser";

    @BeforeAll
    public static void setupTempDir() throws Exception {
        tempPagesDir = Files.createTempDirectory("testpages");
        draftFile = new File(tempPagesDir.toFile(), "draft.txt");
        draftFile.createNewFile();

        // Use public setters in PageManager to override paths
        PageManager.setPagesDir(tempPagesDir.toFile());
        PageManager.setDraftFile(draftFile);
    }

    @BeforeEach
    public void setupAuthor() {
        Author author = new Author(testUID);
        try {
            FileManager.saveAuthor(author);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWritePageCreatesEncryptedFile() throws Exception {

        Scanner scanner = new Scanner("MyTestPage\n\n");
        PageManager.writePage(scanner, testUsername, testUID, true);

        String diaryID = testUsername + "MyTestPage";
        String encryptedID = EncryptionManager.encrypt(diaryID, testUID);
        String safeEncryptedID = Base64.getUrlEncoder().encodeToString(encryptedID.getBytes(StandardCharsets.UTF_8));
        File encryptedFile = new File(tempPagesDir.toFile(), safeEncryptedID + ".txt");

        assertTrue(encryptedFile.exists(), "Encrypted file should be created");
        String encryptedContent = Files.readString(encryptedFile.toPath());
        assertFalse(encryptedContent.isBlank(), "Encrypted file should contain content");
    }

    @Test
    public void testAuthorMetadataIsUpdated() throws IOException {
        Author author = FileManager.findAuthor(testUID);
        assertNotNull(author, "Author should not be null");
        assertFalse(author.getPages().isEmpty(), "Author should have at least one page");

        DiaryPage page = author.getPages().get(0);
        assertEquals(testUsername + "MyTestPage", page.getDiaryID(testUID), "Diary ID should match");
        assertNotNull(page.getCreatedTime(testUID), "Created time should be set");
    }

    @AfterAll
    public static void cleanup() throws Exception {
        Files.walk(tempPagesDir)
                .map(Path::toFile)
                .forEach(File::delete);
    }
}