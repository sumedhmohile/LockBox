package com.sumedh.lockbox;

import java.util.Calendar;
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
    private Date creationDate;
    private CheckInFrequency checkInFrequency;
    private Date lastCheckInDate;

    private Date unlockDate;

    public String getOwnerName() {
        return ownerName;
    }

    public String getName() {
        return name;
    }

    public Date getLastCheckInDate() {
        return lastCheckInDate;
    }

    public Date getUnlockDate() {
        return unlockDate;
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


    public Box() {

    }

    public Box(String ownerName, String name, CheckInFrequency checkInFrequency) {
        this.boxId = UUID.randomUUID().toString();
        this.ownerName = ownerName;
        this.name = name;
        this.creationDate = new Date();
        this.lastCheckInDate = new Date();
        this.checkInFrequency = checkInFrequency;
        this.unlockDate = getDateFromFrequency(checkInFrequency);
    }

    private Date getDateFromFrequency(CheckInFrequency checkInFrequency) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        switch (checkInFrequency) {
            case DAILY: c.add(Calendar.DATE, 2);break;
            case WEEKLY: c.add(Calendar.DATE, 7);break;
            case MONTHLY: c.add(Calendar.MONTH, 2);break;
        }
        return c.getTime();
    }
    public void setFiles(List<String> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "<Box: boxId: " + boxId + " | name: " + name + ">";
    }

}
