package com.example.seley.assignmentorganizer;

import java.util.Date;
import java.util.UUID;

public class Assignment {
    private UUID mId;
    private String mTitle;
    private Date mDueDate;
    private String mSubject;
    private boolean mCompleted;

    public Assignment()
    {
        mId = UUID.randomUUID();
        mDueDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Date getDueDate() {
        return mDueDate;
    }

    public void setDueDate(Date mDueDate) {
        this.mDueDate = mDueDate;
    }

    public String getSubject() {
        return mSubject;
    }

    public void setSubject(String mSubject) {
        this.mSubject = mSubject;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public void setCompleted(boolean mCompleted) {
        this.mCompleted = mCompleted;
    }
}
