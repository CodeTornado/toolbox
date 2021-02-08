package com.example.demo;

import com.example.demo.dao.*;
import com.example.demo.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Secured("ADMIN")
@RequestMapping("cobweb")
@Controller
public class WebNoteApplication {
    @Autowired
    private CommentDto commentDto;
    @Autowired
    private FormatDictDto formatDictDto;
    @Autowired
    private LabelDictDto labelDictDto;
    @Autowired
    private RelationDto relationDto;
    @Autowired
    private TextDto textDto;
    @Autowired
    private TextLabelDto textLabelDto;

    @RequestMapping("cobweb_note")

    public String cobwebNote(@RequestParam("id") Integer id, Model model) {
        model.addAttribute("id", id);
        return "cobweb_note";
    }

    @RequestMapping("selectTextById")
    @ResponseBody
    public CobwebData selectTextById(@RequestParam("id") Integer id) {
        CobwebData cobwebData = new CobwebData();

        List<Text> textList = textDto.selectById(id);
        if (textList.size() <= 0) {
            return null;
        }
        Text text = textList.get(0);


        List<Relation> relationList = relationDto.selectByTextMainId(id);
        String textIds = "";
        for (int i = 0; i < relationList.size(); i++) {
            int relatedTextId = relationList.get(i).getRelatedTextId();
            if (i == 0) {
                textIds = relatedTextId + "";
            } else {
                textIds = textIds + ", " + relatedTextId;
            }
        }
        List<Text> texts = "".equals(textIds) ? null : textDto.selectByIds(textIds);


        List<TextLabel> textLabels = textLabelDto.selectByTextMainId(id);
        String labelIdStr = "";
        for (int i = 0; i < textLabels.size(); i++) {
            int labelId = textLabels.get(i).getLabelId();
            if (i == 0) {
                labelIdStr = labelId + "";
            } else {
                labelIdStr = labelIdStr + ", " + labelId;
            }
        }
        List<LabelDict> labelDicts = "".equals(labelIdStr) ? null : labelDictDto.selectByIds(labelIdStr);
        List<String> labelStrings = new ArrayList<>();
        for (int i = 0; labelDicts != null && i < labelDicts.size(); i++) {
            labelStrings.add(labelDicts.get(i).getName());
        }


        List<Comment> comments = commentDto.selectByTextMainId(id);

        cobwebData.setText(text);
        cobwebData.setRelationNoteDatas(texts);
        cobwebData.setLabelStrList(labelStrings);
        cobwebData.setCommentDatas(comments);
        return cobwebData;
    }

    @RequestMapping("selectByLikeContent")
    @ResponseBody
    public List<Text> selectByLikeContent(@RequestParam("type") Integer type, @RequestParam("content") String content) {
        List<Text> textList = new ArrayList<Text>();
        if (type == 1) {//标签
            List<LabelDict> labelDicts = labelDictDto.selectByLikeName(content);
            if (labelDicts.size() > 0) {
                String labelIds = "";
                for (int i = 0; i < labelDicts.size(); i++) {
                    int labelId = labelDicts.get(i).getId();
                    if (i == 0) {
                        labelIds = labelId + "";
                    } else {
                        labelIds = labelIds + ", " + labelId;
                    }
                }
                List<TextLabel> textLabels = textLabelDto.selectByLabelIds(labelIds);
                if (textLabels.size() > 0) {
                    String textIds = "";
                    for (int i = 0; i < textLabels.size(); i++) {
                        int textId = textLabels.get(i).getTextMainId();
                        if (i == 0) {
                            textIds = textId + "";
                        } else {
                            textIds = textIds + ", " + textId;
                        }
                    }
                    textList = textDto.selectByIds(textIds);
                }
            }
        } else if (type == 2) {//标题
            textList = textDto.selectByTitle(content);
        } else {//内容
            textList = textDto.selectByContent(content);
        }
        return textList;
    }

    @RequestMapping("associatedNote")
    @ResponseBody
    public int associatedNote(@RequestParam("main_id") Integer main_id, @RequestParam("addTextId") Integer addTextId) {
        return relationDto.addRelation(main_id, addTextId);
    }

    @RequestMapping("updateNoteById")
    @ResponseBody
    public int updateNoteById(@RequestParam("id") Integer id, @RequestParam("title") String title, @RequestParam("content") String content) {
        Text text = new Text();
        text.setId(id);
        text.setTitle(title);
        text.setContent(content);
        return textDto.updateById(text);
    }

    @RequestMapping("deleteNoteById")
    @ResponseBody
    public int deleteNoteById(@RequestParam("id") Integer id) {
        int delNum = textDto.delById(id);
        //删除text 记得删除干净Relation TextLabel关联表  Comment评论表
        relationDto.delByTextMainId(id);
        textLabelDto.delByTextMainId(id);
        commentDto.delByTextMainId(id);
        return delNum;
    }

    @RequestMapping("addNewNoteAndRelevance")
    @ResponseBody
    public int addNewNoteAndRelevance(@RequestParam("main_text_id") Integer main_text_id, @RequestParam("title") String title, @RequestParam("content") String content) {
        Text text = new Text();
        text.setTitle(title);
        text.setContent(content);
        int newtTextId = textDto.addText(text);
        relationDto.addRelation(main_text_id, newtTextId);
        return newtTextId;
    }

    @RequestMapping("disengage")
    @ResponseBody
    public int disengage(@RequestParam("main_text_id") Integer main_text_id, @RequestParam("relatedTextId") Integer relatedTextId) {
        return relationDto.delRelation(main_text_id, relatedTextId);
    }

    @RequestMapping("addNewTagAndRelevance")
    @ResponseBody
    public int addNewTagAndRelevance(@RequestParam("textMainId") Integer textMainId, @RequestParam("tagName") String tagName) {
        //查这个标签字典里有没有
        Integer labelId = null;
        List<LabelDict> labelDicts = labelDictDto.selectByName(tagName);
        if (labelDicts.size() > 0) {
            labelId = labelDicts.get(0).getId();
        } else {
            //没有插入一条新的
            labelId = labelDictDto.addLabelDict(tagName);
        }

        //label字典表id 关联到 textLabel
        TextLabel textLabel = new TextLabel();
        textLabel.setTextMainId(textMainId);
        textLabel.setLabelId(labelId);

        return textLabelDto.addTextLabel(textLabel);
    }

    @RequestMapping("deleteLabelAssociation")
    @ResponseBody
    public int deleteLabelAssociation(@RequestParam("textMainId") Integer textMainId, @RequestParam("labelName") String labelName) {
        List<LabelDict> labelDictList = labelDictDto.selectByName(labelName);
        if (labelDictList.size() > 0) {
            LabelDict labelDict = labelDictList.get(0);

            TextLabel textLabel = new TextLabel();
            textLabel.setTextMainId(textMainId);
            textLabel.setLabelId(labelDict.getId());
            return textLabelDto.delByTextMainIdAndLabelId(textLabel);
        } else {
            return -1;
        }
    }

    @RequestMapping("addNewComment")
    @ResponseBody
    public int addNewComment(@RequestParam("main_text_id") Integer main_text_id, @RequestParam("content") String content) {
        Comment comment = new Comment();
        comment.setTextMainId(main_text_id);
        comment.setContent(content);
        return commentDto.addComment(comment);
    }

    @RequestMapping("updateComment")
    @ResponseBody
    public int updateComment(@RequestParam("commentId") Integer commentId, @RequestParam("content") String content) {
        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setContent(content);
        return commentDto.updateById(comment);
    }

    @RequestMapping("delComment")
    @ResponseBody
    public int updateComment(@RequestParam("commentId") Integer commentId) {
        Comment comment = new Comment();
        comment.setId(commentId);
        return commentDto.delById(comment);
    }
}