package com.example.demo.entity;

import java.sql.Timestamp;

public class Comment {
    private int id;
    private int textMainId;
    private String content;
    private Timestamp  createTime;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
