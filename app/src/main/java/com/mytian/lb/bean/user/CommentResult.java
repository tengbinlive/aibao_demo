package com.mytian.lb.bean.user;

import com.core.openapi.OpenApiSimpleResult;

import java.util.ArrayList;

/**
 * 评论返回数据
 * Created by bin.teng on 2015/10/28.
 */
public class CommentResult extends OpenApiSimpleResult {

    private String name;

    private String content;

    private String headUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public static ArrayList<CommentResult> testData() {
        ArrayList<CommentResult> arrayList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            CommentResult commentResult = new CommentResult();
            commentResult.headUrl = "https://raw.githubusercontent.com/tengbinlive/aibao_demo/master/app/src/main/res/mipmap/ic_launcher.png";
            commentResult.name = i==0?"newest ":"bin.teng " + i;
            commentResult.content = "this is a comment ";
            arrayList.add(commentResult);
        }
        return arrayList;
    }


    @Override
    public String toString() {
        return "CommentResult{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", headUrl='" + headUrl + '\'' +
                '}';
    }
}
