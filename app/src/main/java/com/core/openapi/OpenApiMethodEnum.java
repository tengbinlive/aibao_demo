package com.core.openapi;

/**
 * OpenAPI方法名称枚举类.
 * <p/>
 * 说明:
 * 对以前的OpenAPI几个类进行解耦, 一般只需要在该类中添加和修改接口的方法名称即可.
 *
 * @author bin.teng
 */
public enum OpenApiMethodEnum {

    /**
     * 置顶消息详情查询
     */
    TOP_MESSAGE_DEATAIL("/topMessage/queryDetail.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 用户所有群置顶消息查询
     */
    TOP_MESSAGE_LIST("/topMessage/queryAllList.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 批量获取用户信息群信息
     */
    BATCH_INFO("/user/batchInfo.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 发布悬赏
     */
    PUBLISH_REWARD("/reward/publish.action", "openapi_json", OpenApi.URL_TYPE_DATA),


    /**
     * 发布活动
     */
    PUBLISH_ACT("/active/publish.action", "openapi_json", OpenApi.URL_TYPE_DATA),


    /**
     * 发布资讯
     */
    PUBLISH_INFO("/info/publish.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 七牛上传token获取
     */
    QINIU_UPLOAD("/user/getUpToken.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 广场活动列表
     */
    LOAD_ACT_LIST("/active/list.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 查询悬赏
     */
    QUERY_REWARDS("/reward/queryRewards.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 获取验证码
     */
    LOAD_GET_CODE("/user/getAuthCode.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 用户注册
     */
    LOAD_REGIST("/user/regist.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 重置密码
     */
    RESET_PASSWD("/user/resetPasswd.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 收藏
     */
    LOAD_STORE_MSG("/user/storeMsg.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 采纳
     */
    LOAD_ADOPTION("/user/storeMsg.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 购买资讯
     */
    LOAD_BUY_INFO("/info/buyInfo.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 账号查询
     */
    LOAD_ACCOUNT("/account/queryById.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 打赏
     */
    RECORD_REWARDS("/info/recordRewards.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 活动报名
     */
    LOAD_ACT_SIGN("/active/apply.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 资讯查询
     */
    QUERY_INFOS("/info/queryInfos.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 订阅人资讯列表
     */
    QUERY_SUBSCRIBE("/info/subscribe.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 修改用户信息
     */
    MODIFY_USER_INFO("/user/update.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 评论资讯/回复悬赏
     */
    REPLY_DISCUSS("/reply/discuss.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 回复查询
     */
    QUERY_REPLYS("/reply/queryReplys.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 资讯详情
     */
    QUERY_INFO_DETAIL("/info/queryById.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 活动详情
     */
    QUERY_ACT_DETAIL("/active/detail.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 活动 参与人员信息
     */
    QUERY_ACT_JOIN_USER("/active/jionUserList.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 获取和我相关的群组列表
     */
    QUERY_USERGROUPS("/group/getGroupsByUserId.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 查询粉丝
     */
    QUERY_USERFANS("/subscribe/querySubMyscribers.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 我的关注
     */
    QUERY_FOLLOW("/subscribe/queryMySubscribers.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 取消关注
     */
    CANCEL_FOLLOW("/subscribe/canel.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 上传头像
     */
    UPLOAD_HEAD("/user/updateAvatar.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 查询他人信息
     */
    QUERY_OTHERINFO("/center/getOtherInfo.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 关注他人
     */
    FOLLOW_OTHERINFO("/subscribe/addSubscriber.action", "openapi_json", OpenApi.URL_TYPE_DATA),


    /**
     * 原创资讯
     */
    LOAD_ORIGINAL_INFO("/info/queryInfos.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 删除原创资讯
     */
    DELETE_ORIGINAL_INFOS("/center/batchDelInfos.action", "openapi_json", OpenApi.URL_TYPE_DATA),
    /**
     * 购买资讯
     */
    LOAD_PURCHASE_INFO("/center/myBuyInfos.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 删除购买资讯
     */
    DELETE_PURCHASE_INFOS("/center/batchDelBuyInfos.action", "openapi_json", OpenApi.URL_TYPE_DATA),
    /**
     * 我发布的活动
     */
    LOAD_MYRELEASE_ACT("/center/myActivity.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 删除我发布的活动
     */
    DELETE_MYRELEASE_ACT("/center/batchDelActivitys.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 我参加的活动
     */
    LOAD_MYPARTAKE_ACT("/center/myJoinedActivity.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 删除我发布的活动
     */
    DELETE_MYPARTAKE_ACT("/center/batchDelJoinedAcitvitys.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 我发布的悬赏
     */
    LOAD_MYRELEASE_OFFER("/reward/queryRewards.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 删除我发布的悬赏
     */
    DELETE_MYRELEASE_OFFER("/center/batchDelRewards.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 我参与的悬赏
     */
    LOAD_MYPARTAKE_OFFER("/center/myJoinedRewards.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 删除我参与的悬赏
     */
    DELETE_MYPARTAKE_OFFER("/center/batchDelJoinedRewards.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 获取可能认识的人列表
     */
    LOAD_MAYKNOW_FRIEND("/user/recomUsers.action", "openapi_json", OpenApi.URL_TYPE_DATA),


    /**
     * 获取热门群组
     */
    LOAD_HOTGROUP("/group/recomGroups.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 创建群组
     */
    CREATE_GROUP("/group/regist.action", "openapi_json", OpenApi.URL_TYPE_DATA),


    /**
     * 查询已收藏资讯列表
     */
    QUERY_COLLECTED_INFOS("/center/myStoreInfos.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 查询已收藏活动列表
     */
    QUERY_COLLECTED_ACTS("/center/myStoreActivity.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 查询已收藏悬赏
     */
    QUERY_COLLECTED_REWARDS("/center/myStoreRewards.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 删除已收藏资讯列表
     */
    DELETE_COLLECTED_INFOS("/center/batchDelMsgs.action", "openapi_json", OpenApi.URL_TYPE_DATA),
    /**
     * 删除好友列表
     */
    QUERY_USER_FRIENDS("/user/friends.action", "openapi_json", OpenApi.URL_TYPE_DATA),
    /**
     * 搜索用户
     */
    SEARCH_USER("/user/searchUser.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 申请认证
     */
    APPLY_VIP("/user/applyVip.action", "openapi_json", OpenApi.URL_TYPE_DATA),

    /**
     * 通讯录好友
     */
    QUERY_CONTACT_FRIENDS("/user/relation.action", "openapi_json", OpenApi.URL_TYPE_DATA),
    /**
     * 邀请通讯录好友
     */
    INVITE_CONTACT_FRIENDS("/user/invite.action", "openapi_json", OpenApi.URL_TYPE_DATA),
    /**
     * 登陆
     */
    LOAD_LOGIN("/user/login.action", "openapi_json", OpenApi.URL_TYPE_DATA);


    private String code;
    private String format;

    private String type;

    private OpenApiMethodEnum(String code, String format, String type) {
        this.code = code;
        this.format = format;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * 根据方法代码获得对应枚举对象.
     *
     * @param code 方法代码,例如:
     * @return 对应枚举对象
     */
    public static OpenApiMethodEnum getEnumByCode(String code) {
        for (OpenApiMethodEnum item : values()) {
            if (item.code.equals(code)) return item;
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(super.toString());
        buf.append(",code=").append(this.getCode());
        buf.append(",format=").append(this.getFormat());
        return buf.toString();
    }
}
