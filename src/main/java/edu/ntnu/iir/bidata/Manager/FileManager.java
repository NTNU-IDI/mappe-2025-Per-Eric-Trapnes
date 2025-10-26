package edu.ntnu.iir.bidata.Manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.ntnu.iir.bidata.Models.Author;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileManager {
    private static final File jsonFile = new File("src/main/java/edu/ntnu/iir/bidata/Database/Users.JSON");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static List<Author> loadAuthors() throws IOException {
        if (!jsonFile.exists() || jsonFile.length() == 0)
            return new ArrayList<>();
        try (Reader reader = new FileReader(jsonFile)) {
            Author[] array = gson.fromJson(reader, Author[].class);
            return array == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(array));
        }
    }

    public static Author findAuthor(String UID) throws IOException {
        List<Author> authors = loadAuthors();
        for (Author author : authors) {
            if (author.getUID().equals(UID))
                return author;
        }
        Author newAuthor = new Author(UID);
        authors.add(newAuthor);
        saveAuthors(authors);
        return newAuthor;
    }

    public static void saveAuthors(List<Author> authors) throws IOException {
        try (FileWriter writer = new FileWriter(jsonFile)) {
            gson.toJson(authors, writer);
        }
    }

    public static void saveAuthor(Author updatedAuthor) throws IOException {
        List<Author> authors = loadAuthors();
        for (int i = 0; i < authors.size(); i++) {
            if (authors.get(i).getUID().equals(updatedAuthor.getUID())) {
                authors.set(i, updatedAuthor);
                saveAuthors(authors);
                return;
            }
        }
        authors.add(updatedAuthor);
        saveAuthors(authors);
    }
}