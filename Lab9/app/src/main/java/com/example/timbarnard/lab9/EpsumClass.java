package com.example.timbarnard.lab9;

/**
 * Created by Timbarnard on 11/11/15.
 */
public class EpsumClass {

    private int id;
    private int userID;
    private String title;
    private boolean completed;

    public EpsumClass() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
