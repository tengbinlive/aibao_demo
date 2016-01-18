#aibao

[download](http://fir.im/5xv4)

服务器url:

    "http://114.215.108.49/" /** 正式环境OpenAPI地址 */
    "http://10.0.1.15/" /** 调试环境OpenAPI地址 */

appkey - aibao.jks

    storePassword "aibaoaibao"
    keyAlias "aibao"
    keyPassword "aibaoaibao"
    
sharesdk

    AppKey = "dd995e0d9818"
    App Secret : 79225ccb8d3db5d604757f4d2d491ebf
    
Wechat

    AppId="wx25f63c29962b8d0b"
    AppSecret="5697971f7f49ba14c9e9f2672a1bd6b5"

SinaWeibo 
    
    AppKey="884925751"
    AppSecret="b834ce49cf265035cdb515b05553b318"
    RedirectUrl="http://www.mytian.com.cn"
    
QQ

    AppId="1105046638"
    AppKey="nHG1UCvmLyVNjelM"

##lib_greendao

lib_greendao为本地数据库dao-生成项目
用于生成greendao，生成app本地缓存用javabean （app配置属性，用户登录属性，约定项目数据，习惯项目数据）


##*注意*

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