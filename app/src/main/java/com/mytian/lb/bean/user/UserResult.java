package com.mytian.lb.bean.user;

import com.alibaba.fastjson.JSON;
import com.core.openapi.OpenApiSimpleResult;
import com.dao.Parent;

/**
 * 登录返回数据
 * Created by bin.teng on 2015/10/28.
 */
public class UserResult extends OpenApiSimpleResult {

    public final static int WOMAN = 0;
    public final static int MAN = 1;

    private Parent parent;

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public static UserResult testData() {
        String jsonStr = "{\"result\":1,\"description\":\"登录成功\",\"parent\":{\"uid\":50000001,\"createTime\":1453369278947,\"birthday\":0,\"sex\":1,\"phone\":\"13120979267\",\"appointId\":0,\"password\":\"\",\"sysThumbId\":100400,\"id\":41,\"isOnline\":1,\"obj3\":\"\",\"obj4\":\"\",\"time\":0,\"thumbType\":1,\"obj5\":\"\",\"obj6\":\"\",\"token\":\"8d090f04-9503-4ed0-a0b2-bfeb8960a096\",\"obj1\":\"\",\"name\":\"\",\"obj2\":\"\",\"verificationCode\":\"\",\"headThumb\":\"http://mytianimg.oss-cn-shanghai.aliyuncs.com/896C8F81BD156B92FF0260ACC2A901F2.jpg\",\"idCard\":\"\",\"updateTime\":0,\"status\":1,\"channelId\":\"\",\"alias\":\"嘘嘘\",\"obj7\":\"\",\"babyUid\":0,\"appointTime\":0,\"cmd\":0,\"category\":0,\"isIos\":0,\"realName\":\"\"}}";
        UserResult result = JSON.parseObject(jsonStr, UserResult.class);
        return result;
    }

    @Override
    public String toString() {
        return "UserResult{" +
                "parent=" + parent +
                '}';
    }
}
