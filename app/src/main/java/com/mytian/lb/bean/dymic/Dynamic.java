package com.mytian.lb.bean.dymic;

import com.core.openapi.OpenApiSimpleResult;
import com.mytian.lb.bean.user.CommentResult;

import java.util.ArrayList;

/**
 * Created by bin.teng on 2015/10/28.
 */
public class Dynamic extends OpenApiSimpleResult {

    public boolean isExpandable; //评论列表状态 true 展开，false 关闭

    private String id;
    private DynamicBaseInfo baseInfo;
    private DynamicContent content;
    private CommentResult comment = CommentResult.testData(0);
    private ArrayList<CommentResult> commentArray = CommentResult.testData();

    public boolean isExpandable() {
        return isExpandable;
    }

    public void setIsExpandable(boolean isExpandable) {
        this.isExpandable = isExpandable;
    }

    public CommentResult getComment() {
        return comment;
    }

    public void setComment(CommentResult comment) {
        this.comment = comment;
    }

    public ArrayList<CommentResult> getCommentArray() {
        return commentArray;
    }

    public void setCommentArray(ArrayList<CommentResult> commentArray) {
        this.commentArray = commentArray;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DynamicBaseInfo getBaseInfo() {
        return baseInfo;
    }

    public void setBaseInfo(DynamicBaseInfo baseInfo) {
        this.baseInfo = baseInfo;
    }

    public DynamicContent getContent() {
        return content;
    }

    public void setContent(DynamicContent content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Dynamic{" +
                "isExpandable=" + isExpandable +
                ", id='" + id + '\'' +
                ", baseInfo=" + baseInfo +
                ", content=" + content +
                ", comment=" + comment +
                ", commentArray=" + commentArray +
                '}';
    }
}
