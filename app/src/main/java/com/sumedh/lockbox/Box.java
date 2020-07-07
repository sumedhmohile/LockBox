package com.sumedh.lockbox;

import android.net.Uri;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Box {
    public String getBoxId() {
        return boxId;
    }

    private String boxId;
    private String ownerName;
    private String name;
    private List<String> files;

    public String getOwnerName() {
        return ownerName;
    }

    public String getName() {
        return name;
    }

    public List<String> getFiles() {
        return files;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public CheckInFrequency getCheckInFrequency() {
        return checkInFrequency;
    }

    private Date creationDate;
    private CheckInFrequency checkInFrequency;

    public Box() {

    }

    public Box(String ownerName, String name, CheckInFrequency checkInFrequency) {
        this.boxId = UUID.randomUUID().toString();
        this.ownerName = ownerName;
        this.name = name;
        this.creationDate = new Date();
        this.checkInFrequency = checkInFrequency;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return String.format("<Box: boxId: " + boxId + " | name: " + name + ">");
    }

}
