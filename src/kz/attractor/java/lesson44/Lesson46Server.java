package kz.attractor.java.lesson44;

import com.sun.net.httpserver.HttpExchange;
import kz.attractor.java.server.Cookie;
import kz.attractor.java.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Lesson46Server extends Lesson45Server{


    public Lesson46Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/lesson46", this::lesson46Handler);
        registerGet("/take", this::takeBook);
        registerGet("/return", this::returnBook);
        registerGet("/logout", this::logout);
    }

    private void lesson46Handler(HttpExchange exchange) {
//        Cookie sessionCookie = Cookie.make("userId", "123");
//        exchange.getResponseHeaders()
//                .add("Set-Cookie", sessionCookie.toString());
//
//        Map<String, Object> data = new HashMap<>();
//        int times = 42;
//        data.put("times", times);
//
//        Cookie c1 = Cookie.make("user%Id", "456");
//        setCookie(exchange, c1);
//
//        Cookie c2 = Cookie.make("restricted()<>@,;:\\\\\\\"/[]?={}", "()<>@,;:\\\\\\\"/[]?={}");
//        setCookie(exchange, c2);
//
//        Cookie c3 = Cookie.make("user=mail", "example@mail.com");
//        setCookie(exchange, c3);
//
//        String cookieString = getCookies(exchange);
//        Map<String, String> cookies = Cookie.parse(cookieString);
//        data.put("cookies", cookies);

        Map<String, Object> data = new HashMap<>();
        String name = "times";

        String cookieStr = getCookies(exchange);
        Map<String, String> cookies = Cookie.parse(cookieStr);

        String cookieValue = cookies.getOrDefault(name, "0");
        int times = Integer.parseInt(cookieValue)+1;
        Cookie response = new Cookie(name, times);
        setCookie(exchange,response);
        data.put(name, times);
        data.put("cookies", cookies);
        renderTemplate(exchange, "cookie.html", data);
    }
    private void takeBook(HttpExchange exchange) {
        String queryParams = exchange.getRequestURI().getQuery();
        if(queryParams != null) {
            ArrayList<Employee> employees = new EmployeeModel().getEmployees();
            ArrayList<Book> books = new BookModel().getBooks();
            Map<String, String> parsed = Utils.parseUrlEncoded(queryParams, "&");
            if (parsed.get("bookid") != null) {
                for (var e : employees) {
                    if (e.getMail().equalsIgnoreCase(parsed.get("userid")) && e.getCurrentBooks().size() <2) {
                        e.getCurrentBooks().add(parsed.get("bookid"));
                    }
                }
                for (var b : books) {
                    if (b.getId().equalsIgnoreCase(parsed.get("bookid"))) {
                        b.setEmployeeId(parsed.get("userid"));
                        b.setIssued(true);
                    }
                }
                EmployeeModel.writeNewEmployee(employees);
                BookModel.writeBooks(books);
            } else {
                respond404(exchange);
            }
            redirect303(exchange, "/books");
        }
    }
    private void returnBook(HttpExchange exchange) {
        String queryParams = exchange.getRequestURI().getQuery();
        if(queryParams != null) {
            ArrayList<Employee> employees = new EmployeeModel().getEmployees();
            ArrayList<Book> books = new BookModel().getBooks();
            Map<String, String> parsed = Utils.parseUrlEncoded(queryParams, "&");
            if (parsed.get("bookid") != null) {
                for (var e : employees) {
                    if (e.getMail().equalsIgnoreCase(parsed.get("userid"))) {
                        e.getCurrentBooks().remove(parsed.get("bookid"));
                        e.getIssuedBooks().add(parsed.get("bookid"));
                    }
                }
                for (var b : books) {
                    if (b.getId().equalsIgnoreCase(parsed.get("bookid"))) {
                        b.setEmployeeId("");
                        b.setIssued(false);
                    }
                }
                EmployeeModel.writeNewEmployee(employees);
                BookModel.writeBooks(books);
            } else {
                respond404(exchange);
            }

            redirect303(exchange, "/profile");
        }
    }
    private void logout(HttpExchange exchange) {
        Cookie sessionCookie = Cookie.make("userId", "");
        sessionCookie.setMaxAge(0);
        setCookie(exchange, sessionCookie);
        getSomeEmployee().setIdentify("");
        setCheckLogin(false);
        redirect303(exchange, "/");
    }

}
