package kz.attractor.java.lesson44;

import java.util.List;

public class Employee {
    private String mail;
    private List<String> currentBooks;
    private List<String> issuedBooks;
    private String employeeName;
    private String password;


    public Employee(String mail, List<String> currentBooks, List<String> issuedBooks, String employeeName, String password) {
        this.mail = mail;
        this.currentBooks = currentBooks;
        this.issuedBooks = issuedBooks;
        this.employeeName = employeeName;
        this.password = password;
    }

    public Employee(String mail, String employeeName, String password) {
        this(mail, null, null, employeeName, password);
    }
    public Employee(){
        this(null, null, null, null,null);
    }

    public List<String> getCurrentBooks() {
        return currentBooks;
    }

    public List<String> getIssuedBooks() {
        return issuedBooks;
    }

    public String getEmployeeName() {
        return employeeName;
    }


    public void setCurrentBooks(List<String> currentBooks) {
        this.currentBooks = currentBooks;
    }

    public void setIssuedBooks(List<String> issuedBooks) {
        this.issuedBooks = issuedBooks;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
