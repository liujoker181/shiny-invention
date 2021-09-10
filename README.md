### 1、httpshttp请求方法

```java
String url = "https://abc";//支持https或http
String response = httpPost(url, jsonobj.toJSONString());//获得返回值

/**
 * post请求，能够成功发起post请求，且请求报文为JSON格式的请求
 * @param url url地址
 * @param jsonParam 参数,可以为JSONObect格式通过.toJSONString()转化而来
 * @return
 */
public static String httpPost(String url,String jsonParam){
    //post请求
    HttpClient httpClient = new DefaultHttpClient();
    
    httpClient = wrapClient(url);//这行代码用于处理分析url地址，特别处理https
    
    HttpPost method = new HttpPost(url);
    String str = "";
    try {
        if (null != jsonParam) {
            //解决中文乱码问题
            StringEntity entity = new StringEntity(jsonParam, "utf-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            method.setEntity(entity);
        }
        HttpResponse result = httpClient.execute(method);
        url = URLDecoder.decode(url, "UTF-8");
        /**请求发送成功，并得到响应**/
        if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            try {
                /**读取服务器返回过来的json字符串数据**/
                str = EntityUtils.toString(result.getEntity());
                System.out.println("str");

            } catch (Exception e) {
                return String.valueOf(e);
            }
        }
        else
        {
            return String.valueOf(result.getStatusLine().getStatusCode());
        }
    } catch (Exception e) {
        return String.valueOf(e);
    }
    return str;
}


/**
 * 解决避免HttpClient的”SSLPeerUnverifiedException: peer not authenticated”异常
 * 不用导入SSL证书
 * @date 2021-09-02
 */
public static HttpClient wrapClient(String host) {
    HttpClient httpClient = new DefaultHttpClient();
    if (host.startsWith("https")){
        return sslClient(httpClient);
    }
    return httpClient;
}

private static HttpClient sslClient(HttpClient httpClient){
    try {
        SSLContext ctx = SSLContext.getInstance("TLS");
        X509TrustManager tm = new X509TrustManager(){
            @Override
            public void checkClientTrusted(X509Certificate[] arg0,
                    String arg1) throws CertificateException {
                // TODO Auto-generated method stub
            }
            @Override
            public void checkServerTrusted(X509Certificate[] arg0,
                    String arg1) throws CertificateException {
                // TODO Auto-generated method stub
            }
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                // TODO Auto-generated method stub
                return null;
            }
        };
        ctx.init(null, new TrustManager[]{tm}, null);
        SSLSocketFactory ssf = new SSLSocketFactory(ctx,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("https",443,ssf));
        ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager(registry);
        return new DefaultHttpClient(mgr, httpClient.getParams());

    } catch (Exception e) {
        return null;
        // TODO: handle exception
    }
}
```



### 2、oql的方式进行数据库条目录入

```java
FundPropertyInfo fundPropertyInfo = new FundPropertyInfo();//用于存储资金项目
BOSUuid id = BOSUuid.create(fundPropertyInfo.getBOSType());
fundPropertyInfo.setId(id);//基础设置
fundPropertyInfo.setNumber(tmp.get("code").toString());//编码
fundPropertyInfo.setName(tmp.get("name").toString());//名称
fundPropertyInfo.setLevel(Integer.parseInt(tmp.get("intlevel").toString()));//层级 1-5//无法存储，但可用于校验是否有父节点
//是否启用
if (tmp.get("status").toString().equals("1")){//启用
    fundPropertyInfo.setDeletedStatus(EnableStatusEnum.DISABLE);
}else if(tmp.get("status").toString().equals("0")){//不启用
    fundPropertyInfo.setDeletedStatus(EnableStatusEnum.DISABLE);
}
//						fundPropertyInfo.setIsLeaf();//叶子 0-否；1-是//无效
//夫结点
if (fundPropertyInfo.getLevel() != 1){
    oql_parent = "select *,parent.number,parent.name where number = '"+ tmp.get("parentcode") +"' and name = '"+ tmp.get("parentname") +"'";//查找父结点
    fundpropertycol_p = FundPropertyFactory.getLocalInstance(ctx).getFundPropertyCollection(oql_parent);
    if (fundpropertycol_p.size()>0){
        FundPropertyInfo parentitem = fundpropertycol_p.get(0);
        fundPropertyInfo.setParent(parentitem);//父级
    }else{//没有找到该夫结点
//								return "没有找到结点（"+ tmp.get("code") +"）的父节点（"+ tmp.get("parentcode") +"）";
        //中断该功能，并跳出提示框报错
        //为程序运行流畅，暂时更改为跳过增加父节点信息，导致其层级会比较高
    }
}
//收支标识
fundPropertyInfo.setRevenueAndExpensesMark(RevenueAndExpensesMarkEnum.REVENUE);//收入标识
//						fundPropertyInfo.setRevenueAndExpensesMark(RevenueAndExpensesMarkEnum.EXPENSES);//支出标识

fundPropertyInfo.setDescription("123");//用于标识测试数据

IObjectPK pk = FundPropertyFactory.getLocalInstance(ctx).addnew(fundPropertyInfo);
if (pk == null){
    map.put("error","新增款项计划（资金项目）失败");
}else{
    map.put("success", "true");
    map.put("message", "新增款项计划（资金项目）成功");
}
//						fundPropertyInfo.clear();

//更新的方式来更改禁用状态,可行
if("1".equals(String.valueOf(tmp.get("status")))){
    fundPropertyInfo.setDeletedStatus(EnableStatusEnum.ENABLE);
    FundPropertyFactory.getLocalInstance(ctx).update(pk, fundPropertyInfo);
}
```



### 3、在BOS平台直接使用sql语句的操作

```java
/*
 * 直接sql更新数据库?
 */
String sql_upstatus = "";
if(tmp.get("status").toString().equals("0")){
    sql_upstatus = "update T_PS_FundProperty SET FDELETEDSTATUS = '2' where FNUMBER = '"+ tmp.get("code") +"' and FNAME_L2 = '"+ tmp.get("name") +"'";
}else if(tmp.get("status").toString().equals("1")){
    sql_upstatus = "update T_PS_FundProperty SET FDELETEDSTATUS = '1' where FNUMBER = '"+ tmp.get("code") +"' and FNAME_L2 = '"+ tmp.get("name") +"'";
}
FMIsqlFacadeFactory.getLocalInstance(ctx).executeSql(sql_upstatus);
```



### 4、json格式的数据的使用

```java
//传入参数设置
JSONObject jsonobj = new JSONObject();//构建接口请求所需参数，json格式
JSONObject c1 = new JSONObject();
//参数：消费方系统标识设定，暂定为FMS
String syscode = null;
syscode = "FMS";
c1.put("syscode", syscode);
//参数：获取当前时间
SimpleDateFormat sdf = new SimpleDateFormat();
sdf.applyPattern("yyyy-MM-dd HH:mm:ss");//时间格式
Date date = new Date();
String dateString = sdf.format(date).toString();
c1.put("reqdate", dateString);//获得现在的时间，示例2021-08-01 10:00:00
JSONObject c2 = new JSONObject();
//参数：年份暂定本年
String year = null;
year = "2021";
c2.put("year", year);
c2.put("reference1", "");
c2.put("reference2", "");
c2.put("reference3", "");
JSONObject b = new JSONObject();
b.put("head", c1);
b.put("body", c2);
jsonobj.put("data", b);

/*
 * 以下代码包含于map，hashmap与list类型的转换
 */
HashMap<String, List> alldata = new HashMap<String, List>();//全部数据以map形式存储
List<Map<String, String>> leveldata[] = new ArrayList[5];//每级以list形式存储
for (int i = 0; i < leveldata.length; i++) {
    leveldata[i] = new ArrayList<Map<String,String>>();
}
JSONObject jsonInput = null;
JSONObject jsonData = null;//该行为接口文档中，当外层有"data"时,需要添加
JSONObject paramHead = null;
List<Map<String, String>> parameter = new ArrayList<Map<String,String>>();
try {
    jsonInput = JSONObject.parseObject(response);//对返回的数据处理，大整体转成json
    jsonData = jsonInput.getJSONObject("data");//获得其中的key为data的value值，也是一个JSONObject格式//接口文档中，当外层有"data"时,需要添加
    paramHead = jsonData.getJSONObject("head");//存储小信息
    parameter = (List) jsonData.get("body");//存储其资金项目

} catch (Exception e) {
    // TODO: handle exception
    System.out.println("获得的报文出错");
}
str += String.valueOf(paramHead)+"\n";//测试用，返回返回头信息弹窗
if ("N".equals(paramHead.getString("sucflag"))){//返回失败
    str = "接口返回失败\n";
}else if("Y".equals(paramHead.getString("sucflag")) && parameter!=null){//获取data内list后，将list内数据根据层级添加至map
    for (int i = 0; i < parameter.size(); i++) {
        Map tmp = new HashMap();
        tmp = (Map)parameter.get(i);
//				System.out.println(tmp);
        String a =  tmp.get("intlevel").toString();
        int level = Integer.parseInt(tmp.get("intlevel").toString());//获取该条信息的层级
        leveldata[level-1].add(tmp);
    }
    alldata.put("1", leveldata[0]);
    alldata.put("2", leveldata[1]);
    alldata.put("3", leveldata[2]);
    alldata.put("4", leveldata[3]);
    alldata.put("5", leveldata[4]);
    System.out.println(alldata);//打印map
    str += String.valueOf(leveldata[0].size())+" "+String.valueOf(leveldata[1].size())+" " +
            ""+String.valueOf(leveldata[2].size())+" "+String.valueOf(leveldata[3].size())+" " +
                    ""+String.valueOf(leveldata[4].size())+" ";
}
```

