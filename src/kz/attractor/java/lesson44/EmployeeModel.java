package kz.attractor.java.lesson44;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EmployeeModel {
    private static ArrayList<Employee> employees;
    private static Path PATH = Paths.get("./employee.json");
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public EmployeeModel() {
        employees = new ArrayList<>(List.of(readEmployee()));
    }

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public static Employee[] readEmployee() {
        String json = "";
        try {
            json = Files.readString(PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return GSON.fromJson(json, Employee[].class);
    }
    public static void writeNewEmployee(ArrayList<Employee> employee){
        Employee[] result = employee.toArray(Employee[]::new);
        String json = GSON.toJson(result);
        try{
            byte[] jsonBytes = json.getBytes();
            Files.write(PATH, jsonBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setEmployees(ArrayList<Employee> employees) {
        EmployeeModel.employees = employees;
    }
}