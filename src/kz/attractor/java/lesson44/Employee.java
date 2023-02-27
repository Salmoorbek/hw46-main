package kz.attractor.java.lesson44;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Employee {
    private String mail;
    private List<String> currentBooks;
    private List<String> issuedBooks;
    private String employeeName;
    private String password;
    private String identify;


    public Employee(String mail, List<String> currentBooks, List<String> issuedBooks, String employeeName, String password) {
        this.mail = mail;
        this.currentBooks = currentBooks;
        this.issuedBooks = issuedBooks;
        this.employeeName = employeeName;
        this.password = password;
    }

    public Employee(String mail, String employeeName, String password) {
        this.mail = mail;
        this.currentBooks = new ArrayList<>();
        this.issuedBooks = new ArrayList<>();
        this.employeeName = employeeName;
        this.password = password;
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

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }
    public void makeIdentify() {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            setIdentify(convertToString(md.digest(employeeName.getBytes())));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    private String convertToString(byte[] array) {
        return IntStream.range(0, array.length / 4)
                .map(i -> array[i])
                .map(i -> (i < 0) ? i + 127 : i)
                .mapToObj(Integer::toHexString)
                .collect(Collectors.joining());
    }
}
