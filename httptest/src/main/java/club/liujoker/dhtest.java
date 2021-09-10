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

public class dhtest {

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
        //0、用于返回的数据
        String resultString = "";//最终返回值
        String response="";//功能1得到后给功能2的值


        /* 1、调用接口部分，带参数（消费方系统标识+查询请求日期（当前））的post亲求
         * 接口地址：测试环境：https://fssctest.dahuahome.com:9081/dfssc/dhfssc/syncEASForm/queryfundproj
         * 方法类型：HTTP/HTTPS POST
         * ContentType：application/json; charset=utf-8
         *
         */
        //自己部署的接口，暂时用于直接返回大华接口2021的返回值，绕过java版本导致的https访问失败问题：peer not authenticated
        String url_test = "http://liujoker.club/myapi/dahua";

        String str= "";//用于最终传值

        //接口地址存储于InterfaceAddress，获取地址，地址编码为dahua_zijin
        String url = "http://121.4.139.128/myapi/dahua";
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
        JSONObject jsonData = null;//该行为接口文档中，当外层有"data"时,需要添加
        JSONObject paramHead = null;
        List<Map<String, String>> parameter = new ArrayList<Map<String,String>>();
        try {
            jsonInput = JSONObject.parseObject(response);//对返回的数据处理，大整体转成json
            jsonData = jsonInput.getJSONObject("data");//获得其中的key为data的value值，也是一个JSONObject格式//接口文档中，当外层有"data"时,需要添加
            paramHead = jsonData.getJSONObject("head");
            parameter = (List) jsonData.get("body");

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("获得的报文出错");
        }
        str += String.valueOf(paramHead)+"\n";//测试用，返回返回头信息弹窗
        str += String.valueOf(response);

        System.out.println(str);

    }
}
