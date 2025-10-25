package edu.ntnu.iir.bidata.Manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.ntnu.iir.bidata.Models.Author;

public class FileManager {
    public static Author findAuthor(String UID) throws IOException {

        Author author = new Author(UID, new ArrayList<>());
        String filePath = "src/main/java/edu/ntnu/iir/bidata/Database/Users.JSON";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(filePath);

        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        List<Author> authors = new ArrayList<>();

        // Load existing data if file has content
        if (file.exists() && file.length() > 0) {
            Reader reader = new FileReader(file);
            Author[] existing = gson.fromJson(reader, Author[].class);
            reader.close();

            if (existing != null) {
                authors.addAll(Arrays.asList(existing));
            }
        }

        // Check if UID exists
        boolean found = false;
        for (Author a : authors) {
            if (a.getUID().equals(UID)) {
                author = a;
                found = true;
                break;
            }
        }

        if (!found) {
            authors.add(author);
        }

        // Save updated data
        Writer writer = new FileWriter(file);
        gson.toJson(authors, writer);
        writer.close();
        return author;
    }

}
