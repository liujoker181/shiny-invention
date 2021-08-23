package com.wx;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.sql.Date;
import java.util.*;

@RestController
@SpringBootApplication
public class ControllerText {
    public ControllerText() throws Exception {
    }

    @RequestMapping("getUser")
    public Map<String,Object> getUser(){
        System.out.println("微信小程序正在调用...");
        Map<String,Object> map = new HashMap<String, Object>();
        List<String> list = new ArrayList<String>();
        list.add("Amy");
        list.add("Cathy");
        map.put("list",list);
        System.out.println("微信小程序调用完成...");
        return map;
    }

    static final String JDBC_Driver = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/lose?serverTimezone=UTC";
    static final String USER = "root";
    static final String PASS = "54174740";
//    Properties pro = new Properties();
//    InputStream in = ControllerText.class.getClassLoader().getResourceAsStream("jdbc.properties");
//    DataSource dataSource = DruidDataSourceFactory.createDataSource(pro);
//    Connection connection = dataSource.getConnection();

    @RequestMapping("getTest1")
    public Map<String,Object> getTest1(){
        Map<String,Object> map = new HashMap<String, Object>();
        System.out.println("微信小程序正在调用...");
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_Driver);
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT username, password FROM user";
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            int i = 0;
            while(rs.next()){
                // 通过字段检索
                i++;
                Map<String,Object> map_1 = new HashMap<String, Object>();
                String username  = rs.getString("username");
                String password = rs.getString("password");
                map_1.put("username",username);
                map_1.put("password",password);
                map.put("list"+i,map_1);
            }
            rs.close();
            stmt.close();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return map;
    }

    @RequestMapping("getuser")
    public Map<String,Object> select_getuser(String username1, HttpServletResponse response){
        Map<String,Object> map = new HashMap<String, Object>();
        System.out.println("微信小程序正在调用...");
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_Driver);
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM user where username = '"+username1+"'";
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            int i = 0;
            while(rs.next()){
                // 通过字段检索
                i++;
                Map<String,Object> map_1 = new HashMap<String, Object>();
                String username  = rs.getString("username");
                String password = rs.getString("password");
                String tele = rs.getString("tele");
                String email = rs.getString("email");
                map_1.put("username",username);
                map_1.put("password",password);
                map_1.put("tele",tele);
                map_1.put("email",email);


                map = map_1;
            }
            rs.close();
            stmt.close();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return map;
    }

    @RequestMapping("getpeopleinfor")
    public Map<String,Object> select_getuserinfor(String username1, HttpServletResponse response){
        Map<String,Object> map = new HashMap<String, Object>();
        System.out.println("微信小程序正在调用...");
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_Driver);
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT tele,email FROM user where username = '"+username1+"'";
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            int i = 0;
            while(rs.next()){
                // 通过字段检索
                i++;
                Map<String,Object> map_1 = new HashMap<String, Object>();
                String tele = rs.getString("tele");
                String email = rs.getString("email");
                map_1.put("tele",tele);
                map_1.put("email",email);

                map = map_1;
            }
            rs.close();
            stmt.close();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return map;
    }

    @RequestMapping("getlose")
    public List<Object> select_getlose0(HttpServletResponse response){
        List<Object> list = new ArrayList<>();
        System.out.println("微信小程序正在调用...");
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_Driver);
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM lostthing where state != 2";
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            int i = 0;
            while(rs.next()){
                // 通过字段检索
                i++;
                Map<String,Object> map_0 = new HashMap<String, Object>();
                String lose_id  = rs.getString("lose_id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                String place = rs.getString("place");
                String state = rs.getString("state");
                String lose_people = rs.getString("lose_people");
                String find_people = rs.getString("find_people");
                Date date = rs.getDate("date");
                map_0.put("lose_id",lose_id);
                map_0.put("title",title);
                map_0.put("content",content);
                map_0.put("place",place);
                map_0.put("state",state);
                map_0.put("lose_people",lose_people);
                map_0.put("find_people",find_people);
                map_0.put("date",date);
                map_0.put("isShow", true);

                list.add(map_0);
            }
            rs.close();
            stmt.close();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @RequestMapping("getmylose")
    public List<Object> select_getmylose0(String username1, HttpServletResponse response){
        List<Object> list = new ArrayList<>();
        System.out.println("微信小程序正在调用...");
        Connection conn = null;
        Statement stmt = null;
        String str ="";
        if (username1 == str){
            return list;
        }
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_Driver);
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM lostthing where state != 2 AND (lose_people = '"+username1+"' OR find_people = '"+username1+"')";
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            int i = 0;
            while(rs.next()){
                // 通过字段检索
                i++;
                Map<String,Object> map_0 = new HashMap<String, Object>();
                String lose_id  = rs.getString("lose_id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                String place = rs.getString("place");
                String state = rs.getString("state");
                String lose_people = rs.getString("lose_people");
                String find_people = rs.getString("find_people");
                Date date = rs.getDate("date");
                map_0.put("lose_id",lose_id);
                map_0.put("title",title);
                map_0.put("content",content);
                map_0.put("place",place);
                map_0.put("state",state);
                map_0.put("lose_people",lose_people);
                map_0.put("find_people",find_people);
                map_0.put("date",date);
                map_0.put("isShow", true);

                list.add(map_0);
            }
            rs.close();
            stmt.close();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @RequestMapping("getmyalllose")
    public List<Object> select_getmyalllose0(String username1, HttpServletResponse response){
        List<Object> list = new ArrayList<>();
        System.out.println("微信小程序正在调用...");
        Connection conn = null;
        Statement stmt = null;
        String str ="";
        if (username1 == str){
            return list;
        }
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_Driver);
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM lostthing where (lose_people = '"+username1+"' OR find_people = '"+username1+"')";
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            int i = 0;
            while(rs.next()){
                // 通过字段检索
                i++;
                Map<String,Object> map_0 = new HashMap<String, Object>();
                String lose_id  = rs.getString("lose_id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                String place = rs.getString("place");
                String state = rs.getString("state");
                String lose_people = rs.getString("lose_people");
                String find_people = rs.getString("find_people");
                Date date = rs.getDate("date");
                map_0.put("lose_id",lose_id);
                map_0.put("title",title);
                map_0.put("content",content);
                map_0.put("place",place);
                map_0.put("state",state);
                map_0.put("lose_people",lose_people);
                map_0.put("find_people",find_people);
                map_0.put("date",date);
                map_0.put("isShow", true);

                list.add(map_0);
            }
            rs.close();
            stmt.close();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @RequestMapping("getalllose")
    public List<Object> select_getalllose0(HttpServletResponse response){
        List<Object> list = new ArrayList<>();
        System.out.println("微信小程序正在调用...");
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_Driver);
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM lostthing";
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            int i = 0;
            while(rs.next()){
                // 通过字段检索
                i++;
                Map<String,Object> map_0 = new HashMap<String, Object>();
                String lose_id  = rs.getString("lose_id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                String place = rs.getString("place");
                String state = rs.getString("state");
                String lose_people = rs.getString("lose_people");
                String find_people = rs.getString("find_people");
                Date date = rs.getDate("date");
                map_0.put("lose_id",lose_id);
                map_0.put("title",title);
                map_0.put("content",content);
                map_0.put("place",place);
                map_0.put("state",state);
                map_0.put("lose_people",lose_people);
                map_0.put("find_people",find_people);
                map_0.put("date",date);
                map_0.put("isShow", true);

                list.add(map_0);
            }
            rs.close();
            stmt.close();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @RequestMapping("register")
    public int insert_register(String username1, String password1, HttpServletResponse response){
        System.out.println("微信小程序正在调用...");
        Connection conn = null;
        PreparedStatement pstmt = null;
        int flag = 0;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_Driver);
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // 执行查询
            System.out.println(" 实例化Statement对象...");
            String sql;
            sql = "INSERT INTO user (username, password) VALUES (?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,username1);
            pstmt.setString(2,password1);
            System.out.println(sql);
            flag = pstmt.executeUpdate();
            System.out.println(flag);
            pstmt.close();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
        }

        return flag;
    }

    @RequestMapping("insertlose_find")
    public int insert_lose_find(String title1, String content1, String place1,String find_people1,String date1, HttpServletResponse response){
        System.out.println("微信小程序正在调用...");
        Connection conn = null;
        PreparedStatement pstmt = null;
        int flag = 0;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_Driver);
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // 执行查询
            System.out.println(" 实例化Statement对象...");
            String sql;
            sql = "INSERT INTO lostthing (title, content, place, state, find_people, date) VALUES (?, ?, ?, '0', ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,title1);
            pstmt.setString(2,content1);
            pstmt.setString(3,place1);
            pstmt.setString(4,find_people1);
            Date sqldate = Date.valueOf(date1);
            pstmt.setDate(5,sqldate);
            System.out.println(sql);
            flag = pstmt.executeUpdate();
            System.out.println(flag);
            pstmt.close();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
        }

        return flag;
    }

    @RequestMapping("insertlose_lose")
    public int insert_lose_lose(String title1, String content1, String place1,String lose_people1,String date1, HttpServletResponse response){
        System.out.println("微信小程序正在调用...");
        Connection conn = null;
        PreparedStatement pstmt = null;
        int flag = 0;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_Driver);
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // 执行查询
            System.out.println(" 实例化Statement对象...");
            String sql;
            sql = "INSERT INTO lostthing (title, content, place, state, lose_people, date) VALUES (?, ?, ?, '1', ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,title1);
            pstmt.setString(2,content1);
            pstmt.setString(3,place1);
            pstmt.setString(4,lose_people1);
            Date sqldate = Date.valueOf(date1);
            pstmt.setDate(5,sqldate);
            System.out.println(sql);
            flag = pstmt.executeUpdate();
            System.out.println(flag);
            pstmt.close();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
        }

        return flag;
    }

    @RequestMapping("upsetting")
    public int update_upsetting(String username1, String tele1, String email1, HttpServletResponse response){
        System.out.println("微信小程序正在调用...");
        Connection conn = null;
        PreparedStatement pstmt = null;
        int flag = 0;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_Driver);
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // 执行查询
            System.out.println(" 实例化Statement对象...");
            String sql;
            sql = "UPDATE user SET tele=?, email=? WHERE username = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,tele1);
            pstmt.setString(2,email1);
            pstmt.setString(3,username1);
            System.out.println(sql);
            flag = pstmt.executeUpdate();
            System.out.println(flag);
            pstmt.close();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
        }

        return flag;
    }

    @RequestMapping("upfindstate")
    public int update_upfindstate(String anotheruser1, int lose_id1, HttpServletResponse response){
        System.out.println("微信小程序正在调用...");
        Connection conn = null;
        PreparedStatement pstmt = null;
        int flag = 0;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_Driver);
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // 执行查询
            System.out.println(" 实例化Statement对象...");
            String sql;
            sql = "UPDATE lostthing SET state='2',find_people=? WHERE lose_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,anotheruser1);
            pstmt.setInt(2,lose_id1);
            System.out.println(sql);
            flag = pstmt.executeUpdate();
            System.out.println(flag);
            pstmt.close();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
        }

        return flag;
    }

    @RequestMapping("uplosestate")
    public int update_uplosestate(String anotheruser1, int lose_id1, HttpServletResponse response){
        System.out.println("微信小程序正在调用...");
        Connection conn = null;
        PreparedStatement pstmt = null;
        int flag = 0;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_Driver);
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // 执行查询
            System.out.println(" 实例化Statement对象...");
            String sql;
            sql = "UPDATE lostthing SET state='2',lose_people=? WHERE lose_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,anotheruser1);
            pstmt.setInt(2,lose_id1);
            System.out.println(sql);
            flag = pstmt.executeUpdate();
            System.out.println(flag);
            pstmt.close();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
        }

        return flag;
    }

    @RequestMapping("login")
    public int select_login(String username1, String password1, HttpServletResponse response){
        System.out.println("微信小程序正在调用...");
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_Driver);
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT count(*) FROM user where username = '"+username1 +"' and password = '"+ password1+ "'";
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            while(rs.next()){
                int count  = rs.getInt("count(*)");
                return count;
            }
            rs.close();
            stmt.close();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
