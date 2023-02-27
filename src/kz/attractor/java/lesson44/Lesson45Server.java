package kz.attractor.java.lesson44;

import com.sun.net.httpserver.HttpExchange;
import kz.attractor.java.server.Cookie;
import kz.attractor.java.utils.Utils;

import java.io.IOException;
import java.util.*;

public class Lesson45Server extends Lesson44Server{
    private boolean checkLogin = false;
    private static Employee someEmployee = new Employee("", new ArrayList<>(), new ArrayList<>(),"", "password");

    public boolean isCheckLogin() {
        return checkLogin;
    }

    public void setCheckLogin(boolean checkLogin) {
        this.checkLogin = checkLogin;
    }

    public static Employee getSomeEmployee() {
        return someEmployee;
    }

    public static void setSomeEmployee(Employee someEmployee) {
        Lesson45Server.someEmployee = someEmployee;
    }

    public Lesson45Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/", this::homeGetHandler);
        registerGet("/books", this::booksHandler);
        registerGet("/employee", this::employeeHandler);
        registerGet("/login", this::loginGet);
        registerPost("/login", this::loginPost);
        registerGet("/register", this::regGet);
        registerPost("/register", this::regPost);
        registerGet("/profile", this::profileGet);
    }
    private void homeGetHandler(HttpExchange exchange) {
        registerGet("/", this::homeGetHandler);
        Map<String,Object> data = new HashMap<>();
        if(!checkCookie(exchange)) {
            renderTemplate(exchange, "index.html", data);
        }else {
            data.put("login", isCheckLogin());
            data.put("employee", getSomeEmployee());
            renderTemplate(exchange, "index.html", data);
        }
    }

    private void employeeHandler(HttpExchange exchange) {
        Map<String,Object> data = new HashMap<>();
        if(checkCookie(exchange)) {
            data.put("employees", new EmployeeModel().getEmployees());
            data.put("books", new BookModel().getBooks());
            data.put("login", checkLogin);
            data.put("currentEmployee", getSomeEmployee());
            renderTemplate(exchange, "employee.html", data);
        } else {
            data.put("employees", new EmployeeModel().getEmployees());
            data.put("books", new BookModel().getBooks());
            renderTemplate(exchange, "employee.html", data);
        }
    }
    private void booksHandler(HttpExchange exchange) {
        Map<String,Object> data = new HashMap<>();
        String raw = getBody(exchange);
        Map<String, String> params = Utils.parseUrlEncoded(raw, "&");
        if(params.get("id") != null){
            var books = new BookModel().getBooks();
            for (Book book : books){
                if(book.getId().equalsIgnoreCase(params.get("id"))){
                    data.put("book", book);
                    break;
                }
            }
            if(data.get("book") != null){
                renderTemplate(exchange, "book.html", data);
            } else {
                respond404(exchange);
            }

        }else if(checkCookie(exchange)) {
            data.put("books", new BookModel().getBooks());
            data.put("login", checkLogin);
            data.put("employee", getSomeEmployee());
            renderTemplate(exchange, "books.html", data);
        } else {
            renderTemplate(exchange, "books.html", new BookModel().getBooks());
        }
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
                    if (e.getCurrentBooks() == null) {
                        e.setCurrentBooks(new ArrayList<>());
                    }
                    if (e.getIssuedBooks() == null) {
                        e.setIssuedBooks(new ArrayList<>());
                    }
                    e.makeIdentify();
                    employee = e;
                }
            }
        }
        EmployeeModel.writeNewEmployee(employees);
        if(check){
            Cookie sessionCookie = Cookie.make("userId", employee.getIdentify());
            someEmployee = employee;
            checkLogin = true;
            sessionCookie.setHttpOnly(true);
            sessionCookie.setMaxAge(600);
            setCookie(exchange, sessionCookie);
            redirect303(exchange, "/profile");
        } else {
            checkLogin = false;
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
            Employee newEmployee = new Employee(parsed.get("mail"), new ArrayList<>(), new ArrayList<>(), parsed.get("employeeName"), parsed.get("password"));
            employees.add(newEmployee);
            EmployeeModel.writeNewEmployee(employees);
        }
        renderTemplate(exchange, "register.html", new Register(startReg, status));
    }
    private void profileGet(HttpExchange exchange) {
        Map<String,Object> data = new HashMap<>();
        if(!checkCookie(exchange)) {
            data.put("employee", new Employee("some@email.ru","Некий сотрудник", "password"));
            data.put("sample", true);
            data.put("books", new BookModel().getBooks());
            checkLogin = false;
            renderTemplate(exchange, "profile.html", data);
        } else {
            data.put("books", new BookModel().getBooks());
            data.put("employee", someEmployee);
            data.put("login", checkLogin);
            renderTemplate(exchange, "profile.html", data);
        }
    }
}
