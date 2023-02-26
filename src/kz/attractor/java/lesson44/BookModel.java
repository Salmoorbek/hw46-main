package kz.attractor.java.lesson44;

import com.google.gson.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BookModel {
    private ArrayList<Book> books;


    public BookModel() {
        books = new ArrayList<>(List.of(readBooks()));
    }

    public ArrayList<Book> getBooks() {
        return books;
    }
    public Book getBook() {
        return books.get(2);
    }

    public static Book[] readBooks(){
        Path path = Paths.get("./books.json");
        String json = "";
        try{
            json = Files.readString(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(json, Book[].class);
    }
}
