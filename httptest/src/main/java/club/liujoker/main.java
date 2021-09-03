package club.liujoker;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URLDecoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;

public class main {

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

                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
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

    /**
     * post请求，能够成功发起post请求，且请求报文为JSON格式的请求
     * @param url url地址
     * @param jsonParam 参数,可以为JSONObect格式通过.toJSONString()转化而来
     * @return
     */
    public static String httpPost(String url,String jsonParam){
        //post请求
        HttpClient httpClient = new DefaultHttpClient();
        httpClient = wrapClient(url);
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
                    return null;
                }
            }
            else
            {
                return null;
            }
        } catch (Exception e) {
            return e.toString();
        }
        return str;
    }

    public static void main(String[] args) {
        String response = null;
        String url = "https://fssctest.dahuahome.com:9081/dfssc/dhfssc/syncEASForm/queryfundproj";
        //传入参数设置
        JSONObject jsonobj = new JSONObject();//构建接口请求所需参数，json格式
        JSONObject c1 = new JSONObject();
        //消费方系统标识设定，暂定为FMS
        String syscode = null;
        syscode = "FMS";
        c1.put("syscode", syscode);

        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");//时间格式
        Date date = new Date();
        String dateString = sdf.format(date).toString();
        c1.put("reqdate", dateString);//获得现在的时间，示例2021-08-01 10:00:00
        JSONObject c2 = new JSONObject();
        //年份暂定本年
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

        response = httpPost(url, jsonobj.toJSONString());//返回值给到下一块用于处理
        System.out.println(response);


        HashMap<String, List> alldata = new HashMap<String, List>();//全部数据以map形式存储
        List<Map<String, String>> leveldata[] = new ArrayList[5];//每级以list形式存储
        for (int i = 0; i < leveldata.length; i++) {
            leveldata[i] = new ArrayList<Map<String,String>>();
        }
        JSONObject jsonInput = null;
//		JSONObject jsonData = null;//该行为接口文档中，当外层有"data"时,需要添加
        JSONObject paramHead = null;
        List<Map<String, String>> parameter = new ArrayList<Map<String,String>>();
        try {
            jsonInput = JSONObject.parseObject(response);//对返回的数据处理，大整体转成json
//			jsonData = jsonInput.getJSONObject("data");//获得其中的key为data的value值，也是一个JSONObject格式//接口文档中，当外层有"data"时,需要添加
            paramHead = jsonInput.getJSONObject("head");
            parameter = (List) jsonInput.get("body");

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("获得的报文出错");
        }
        if (paramHead.getString("sucflag") == "N"){//返回失败

        }else if(paramHead.getString("sucflag").equals("Y") && parameter!=null){//获取data内list后，将list内数据根据层级添加至map
            for (int i = 0; i < parameter.size(); i++) {
                Map tmp = new HashMap();
                tmp = (Map)parameter.get(i);
                System.out.println(tmp);
                String a =  tmp.get("intlevel").toString();
                int level = Integer.parseInt(tmp.get("intlevel").toString());//获取该条信息的层级
                leveldata[level-1].add(tmp);
            }
        }
        alldata.put("1", leveldata[0]);
        alldata.put("2", leveldata[1]);
        alldata.put("3", leveldata[2]);
        alldata.put("4", leveldata[3]);
        alldata.put("5", leveldata[4]);
        System.out.println(alldata);//打印map

    }
}
