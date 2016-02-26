#aibao

[download](http://fir.im/5xv4)

Monkey

事件之间延时500毫秒 压力10000

前提：已经完成登录，app启动后可直接进入主界面
    
    adb shell monkey -v -v -v -p com.mytian.lb 
    --monitor-native-crashes --ignore-security-exceptions 
    --kill-process-after-error --pct-trackball 0 --pct-nav 0 
    --pct-syskeys 0 --pct-anyevent 0 --pct-appswitch 0 
    --pct-flip 0 --pct-majornav 0 
    --pct-touch 75 --pct-motion 25 --throttle 500 10000 
    > D:\monkey_log\java_monkey_log_aibao.txt 
    
1.修改UserFragment exitAccount(),防止点击退出
    
2.手机屏幕超时设置为30分钟（或者更长），防止手机进入锁屏状态

3.插入SIM卡和存储卡后将手机开机，开启ADB模式（即USB调试模式），连接到PC（存储卡剩余空间尽量留大，建议 1G以上）

4.在PC上执行 运行—>cmd,在弹出的命令窗口中，输入adb devices，检查设备是否连接

5.输入Monkey命令，回车
  
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
    配置web联调
    buildConfigField "boolean", "STETHO_DEBUG", "false"// stetho info
    配置渠道信息输出
    buildConfigField "boolean", "CHANNEL_DEBUG", "false"// channel info

服务器url:

    "http://www.mytian.com.cn/"

appkey - aibao.jks

    storePassword "aibaoaibao"
    keyAlias "aibao"
    keyPassword "aibaoaibao"
    
getui
    
    manifestPlaceholders = [
                    GETUI_APP_ID    : "kvuUWGiv0c6sHXaYA8JzT7",
                    GETUI_APP_KEY   : "PO0la0IdOd8vTKnFl4Ftq2",
                    GETUI_APP_SECRET: "bGqQIpUdU69rpr74CTX3t2",
                    PACKAGE_NAME    : applicationId
            ]
    
sharesdk

    AppKey = "dd995e0d9818"
    App Secret : 79225ccb8d3db5d604757f4d2d491ebf
    
Wechat  如果分享类型跟数据填充不匹配，将无法调启微信客服端，如（url ="",分享类型为 = SHARE_WEBPAGE,会无法调启客服端）

    AppId="wxa91c0e9b1fcdea23"
    AppSecret="2ce1257f1ebd4e63405af586ba85e337"

SinaWeibo 
    
    AppKey="884925751"
    AppSecret="b834ce49cf265035cdb515b05553b318"
    RedirectUrl="http://www.mytian.com.cn"
    
QQ

    AppId="1105046638"
    AppKey="nHG1UCvmLyVNjelM"

##lib_greendao

lib_greendao为本地数据库dao-生成项目

用于生成greendao，生成app本地缓存用javabean 

（app配置属性，用户登录属性，约定项目数据，习惯项目数据）


##*注意事项*

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

3.生成class写的是决定路径需要自行修改

    new DaoGenerator().generateAll(schema, "D:\\tengbin\\project\\kndle\\lib_greendao\\src\\main\\java\\dao");
    
4.WXEntryActivity 必须在包名+wxapi目录下 且必须

    android:exported="true"
    
5.发布APP时，三星系列需要

    debuggable false

 