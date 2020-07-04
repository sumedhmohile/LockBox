package com.sumedh.lockbox;

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
    private List<File> files;

    public String getOwnerName() {
        return ownerName;
    }

    public String getName() {
        return name;
    }

    public List<File> getFiles() {
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
}
