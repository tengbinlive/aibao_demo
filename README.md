#aibao

[download  apk](http://fir.im/5xv4)

[download  apk](https://github.com/tengbinlive/aibao_demo/blob/master/app/app-alpha-unaligned.apk)

[视频演示](http://yun.baidu.com/share/link?shareid=82978705&uk=3809381021)

build

    debug 调试版本
    alpha 测试版本
    release 发布版本
    
    配置api
    buildConfigField "String", "API_HOST", "\"http://www.mytian.com.cn/\""//API Host 
    配置debug
    buildConfigField "boolean", "CONFIG_DEBUG", "false"// config info
    配置日志输出
    buildConfigField "boolean", "LOG_DEBUG", "false"// log info
    配置渠道信息输出
    buildConfigField "boolean", "CHANNEL_DEBUG", "false"// channel info

服务器 url:

    "http://www.mytian.com.cn/"
    
Wechat share:

    如果分享类型跟数据填充不匹配，将无法调启微信客服端，如（url ="",分享类型为 = SHARE_WEBPAGE,会无法调启客服端）


##lib_greendao

lib_greendao为本地数据库dao-生成项目

用于生成greendao，生成app本地缓存用javabean 

（app配置属性，用户登录属性，约定项目数据，习惯项目数据）


#注意事项

1.每次生成新dao时需要提升数据版本version

    private int version = 7 ;

    Schema schema = new Schema(version, "com.example");

2.由lib_greendao 生成的UserAction.class 不可以直接用于app项目中 

需添加 用于本地状态判断

    /**
     * 扩展
     *
     */
    public final static String GREAT = "1";
    public final static String BAD = "0";
    private String record;

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

3.生成class写的是绝对路径需要自行修改

    new DaoGenerator().generateAll(schema, "D:\\tengbin\\project\\kndle\\lib_greendao\\src\\main\\java\\dao");
    
4.WXEntryActivity 必须在包名+wxapi目录下 且必须

    android:exported="true"
    
5.发布APP时，三星系列需要

    debuggable false

 