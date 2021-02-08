package com.example.demo.entity;

import java.util.List;

public class CobwebData {
    private Text text;
    private List<Text> relationNoteDatas;
    private List<String> labelStrList;
    private List<Comment> commentDatas;

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public List<Text> getRelationNoteDatas() {
        return relationNoteDatas;
    }

    public void setRelationNoteDatas(List<Text> relationNoteDatas) {
        this.relationNoteDatas = relationNoteDatas;
    }

    public List<String> getLabelStrList() {
        return labelStrList;
    }

    public void setLabelStrList(List<String> labelStrList) {
        this.labelStrList = labelStrList;
    }

    public List<Comment> getCommentDatas() {
        return commentDatas;
    }

    public void setCommentDatas(List<Comment> commentDatas) {
        this.commentDatas = commentDatas;
    }

    @Override
    public String toString() {
        return "CobwebData{" +
                "text=" + text +
                ", relationNoteDatas=" + relationNoteDatas +
                ", labelStrList=" + labelStrList +
                ", commentDatas=" + commentDatas +
                '}';
    }
}
