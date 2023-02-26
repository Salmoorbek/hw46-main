package kz.attractor.java.lesson44;

import com.sun.net.httpserver.HttpExchange;
import kz.attractor.java.utils.Utils;

import java.io.IOException;
import java.util.*;

public class Lesson45Server extends Lesson44Server{
    public Lesson45Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/login", this::loginGet);
        registerPost("/login", this::loginPost);
        registerGet("/register", this::regGet);
        registerPost("/register", this::regPost);
        registerGet("/profile", this::profileGet);
    }

    private void loginPost(HttpExchange exchange) {
        var model = new EmployeeModel();
        boolean check = false;
        Employee employee = new Employee();
        var employees = model.getEmployees();
        String raw = getBody(exchange);

        Map<String, String> parsed = Utils.parseUrlEncoded(raw, "&");
        for (Employee e : employees) {
            if(e.getMail().equalsIgnoreCase(parsed.get("mail"))){
                if(e.getPassword().equalsIgnoreCase(parsed.get("password"))){
                    check = true;
                    employee = e;
                }
            }
        }
        if(check){
            Map<String,Object> data = new HashMap<>();
            data.put("employee", new Employee(employee.getMail(),employee.getCurrentBooks(), employee.getIssuedBooks(), employee.getEmployeeName(), employee.getPassword()));
            data.put("books", getBooksModel().getBooks());
            renderTemplate(exchange, "profile.html", data);
        } else {
            renderTemplate(exchange, "login.html", new Login(true));
        }

    }

    private void loginGet(HttpExchange exchange) {
        renderTemplate(exchange, "login.html", new Login(false));
    }

    private void regGet(HttpExchange exchange){
        renderTemplate(exchange, "register.html", new Register(false, false));
    }
    private void regPost(HttpExchange exchange){
        boolean startReg = true;
        boolean status = false;
        boolean checkEmail = false;
        EmployeeModel employeeModel = new EmployeeModel();
        ArrayList<Employee> employees = employeeModel.getEmployees();
        String raw = getBody(exchange);
        Map<String, String> parsed = Utils.parseUrlEncoded(raw, "&");
        for (Employee e : employees) {
            if(e.getMail().equals(parsed.get("mail"))){
                checkEmail = true;
            }
        }
        if(!checkEmail){
            status = true;
            Employee newEmployee = new Employee(parsed.get("mail"), parsed.get("employeeName"), parsed.get("password"));
            employees.add(newEmployee);
            EmployeeModel.writeNewEmployee(employees);
        }
        renderTemplate(exchange, "register.html", new Register(startReg, status));
    }
    private void profileGet(HttpExchange exchange) {
        Map<String,Object> data = new HashMap<>();
        data.put("employee", new Employee("some@email.ru", Collections.singletonList(""), Collections.singletonList(""),"Некий сотрудник", "password"));
        data.put("books", getBooksModel().getBooks());
        renderTemplate(exchange, "profile.html", data);
    }
}
