package com.example.jarek.baza;

public class Contacts {
    private long id;
    private String description;
    private int number;

    public Contacts(long id, String description, int number) {
        this.id = id;
        this.description = description;
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
