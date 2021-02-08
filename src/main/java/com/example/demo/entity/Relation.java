package com.example.demo.entity;

import java.sql.Timestamp;

public class Relation {
    private int id;
    private int textMainId;
    private int relatedTextId;
    private Timestamp createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTextMainId() {
        return textMainId;
    }

    public void setTextMainId(int textMainId) {
        this.textMainId = textMainId;
    }

    public int getRelatedTextId() {
        return relatedTextId;
    }

    public void setRelatedTextId(int relatedTextId) {
        this.relatedTextId = relatedTextId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Relation{" +
                "id=" + id +
                ", textMainId=" + textMainId +
                ", relatedTextId=" + relatedTextId +
                ", createTime=" + createTime +
                '}';
    }
}
