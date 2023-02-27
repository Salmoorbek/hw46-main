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
    public static void writeBooks(ArrayList<Book> books){
        Path path = Paths.get("./books.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Book[] result = books.stream().toArray(Book[]::new);
        String json = gson.toJson(result);
        try{
            byte[] arr = json.getBytes();
            Files.write(path, arr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
