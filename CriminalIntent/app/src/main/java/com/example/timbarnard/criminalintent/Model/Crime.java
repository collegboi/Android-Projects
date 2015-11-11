package com.example.timbarnard.criminalintent.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Timbarnard on 23/10/2015.
 */
public class Crime {

    private UUID id;
    private String title;
    private Date discoveredOn;
    private boolean solved;

    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_DISCOVERED_ON = "discovered_on";
    private static final String JSON_SOLVED = "solved";

    public Crime() {
        id = UUID.randomUUID();
        discoveredOn = new Date();
    }

    public Crime(JSONObject json) throws JSONException {
        this.id = UUID.fromString(json.getString(JSON_ID));
        this.title = json.getString(JSON_TITLE);
        this.solved = json.getBoolean(JSON_SOLVED);
        this.discoveredOn = new Date(json.getLong(JSON_DISCOVERED_ON));
    }

    public UUID getId() {
        return this.id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public Date getDiscoveredOn() {
        return discoveredOn;
    }

    public void setDiscoveredOn(Date discoveredOn) {
        this.discoveredOn = discoveredOn;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    @Override
    public String toString() {
        return title;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_ID, this.id.toString());
        jsonObject.put(JSON_TITLE, this.title);
        jsonObject.put(JSON_SOLVED, this.solved);
        jsonObject.put(JSON_DISCOVERED_ON, this.discoveredOn.getTime());

        return jsonObject;
    }
}
