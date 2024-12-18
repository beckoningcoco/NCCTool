package com.zht.testjavafx2.func;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zht.testjavafx2.vo.DBConnVO;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SuppressWarnings("all")
public class NetPayTestFuncProcessor {

    private static final String app_id = "CQ1";
    private static final String secret_code = "VWPytKD5RDxhwtaC45OtUcq1";
    private static String transaction_type = "push_instruct_status";
    private static final String action_type = "write";
    private static final String version = "1.0";
    private static final String charset = "utf-8";
    private static  String timestamp = "2024-11-26 11:10:00";
    private static  String package_code = "20241126CQ700000007";
    private static  String biz_response_content = "[{\"apply_code\":\"20241126CQ7000008\",\"status_no\":\"8\"," +
            "\"status_dec\":\"成功\",\"trans_no\":\"1259811399241126144901008\",\"client_code\":\"01-C0170\"," +
            "\"currency_code\":\"RMB\",\"office_code\":\"01\",\"deal_time\":\"2024-11-26 15:11:00\"}]";




    public static String postSuccessSend(String billcode , DBConnVO dbConnVO) throws SQLException {

        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        // 定义格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 格式化当前时间
        timestamp = now.format(formatter);

        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonArray.add(jsonObject);
        jsonObject.addProperty("apply_code","");
        jsonObject.addProperty("status_no","8");
        jsonObject.addProperty("status_dec","成功");
        jsonObject.addProperty("trans_no","");
        jsonObject.addProperty("client_code","");
        jsonObject.addProperty("currency_code","RMB");
        jsonObject.addProperty("office_code","01");
        jsonObject.addProperty("deal_time",timestamp);
        biz_response_content = jsonArray.toString();

        if (dbConnVO == null) {
            throw new SQLException("获取数据库连接失败！");
        }

        StringBuffer buf = new StringBuffer();
        buf.append(" select a.headpackageid , a.bankaccnum ,a.obmdef1 headno,c.obmdef1 itemno ");
        buf.append(" from ebank_paylog_h a  ");
        buf.append(" inner join cmp_settlement b on a.srcbillcode  = b.billcode ");
        buf.append(" inner join ebank_paylog c on a.pk_ebank_paylog_h = c.pk_ebank_paylog_h ");
        buf.append(" where a.srcbillcode = ? ");
        buf.append(" and a.dr = 0  and b.dr = 0 ");

        Connection conn = FunctionProcessor.connDatabase(dbConnVO.getIp(), dbConnVO.getPort(), dbConnVO.getServername(), dbConnVO.getUsername(), dbConnVO.getPassword(), dbConnVO.getDbType());
        PreparedStatement pstmt = conn.prepareStatement(buf.toString());
        // 设置参数
        pstmt.setString(1, billcode);
        String urlStr = null;
        // 执行查询
        try (ResultSet rs = pstmt.executeQuery()) {
            // 获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            List<Map<String, Object>> resultList = new ArrayList<>();
            // 遍历结果集
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = rsmd.getColumnName(i);
                    Object value = rs.getObject(i);
                    row.put(columnName, value);
                }
                resultList.add(row);
            }

            if(resultList.size() ==0){
                return null;
            }
            Map<String, Object> map = resultList.get(0);
            String headno = (String)map.get("HEADNO");
            package_code = headno ;

            String itemno = (String) map.get("ITEMNO");
            jsonObject.addProperty("apply_code",itemno);

            String headpackageid = (String) map.get("HEADPACKAGEID");
            jsonObject.addProperty("trans_no",headpackageid);

            String bankaccnum = (String) map.get("BANKACCNUM");
            String[] arr = bankaccnum.split("-");
            if(arr.length == 4){
              String clint_id =   arr[0]+"-"+ arr[2];
              jsonObject.addProperty("client_code",clint_id);
            }

            // 对 biz_response_content 进行 urlencode 编码
            String encodedData = URLEncoder.encode(jsonArray.toString(), "UTF-8");
            biz_response_content = encodedData ;


        //拼接 调用路径
              urlStr = "http://127.0.0.1:8082/api/gateway?"
                +       "app_id="+app_id+"&"
                +       "secret_code="+secret_code+"&"
                +       "timestamp="+timestamp+"&"
                +       "transaction_type="+transaction_type+"&"
                +       "action_type="+action_type+"&"
                +       "version="+version+"&"
                +       "charset="+charset+"&"
                +       "package_code="+package_code+"&"
                +       "biz_response_content="+biz_response_content ;

       // sendPost(urlStr,"");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return urlStr;

    }


    public static String sendPost(String strUrl, String param) {
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            // connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            // connection.setRequestProperty("contentType", "utf-8");
            connection.setConnectTimeout(30000);// 设置请求超时时间
            connection.setReadTimeout(300000);// 设置响应超时时间
            connection.setUseCaches(false);
            connection.connect();

            PrintWriter out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "utf-8"));
            out.println(param);
            out.flush();
            out.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

    }

    public static String postFailSend(String billcode, DBConnVO dbConnVO) throws SQLException {
       transaction_type = "public_payment";
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        // 定义格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 格式化当前时间
        timestamp = now.format(formatter);

        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonArray.add(jsonObject);
        jsonObject.addProperty("apply_code","");
        jsonObject.addProperty("status_no","4");
        jsonObject.addProperty("status_dec","失败");
        biz_response_content = jsonArray.toString();

        if (dbConnVO == null) {
            throw new SQLException("获取数据库连接失败！");
        }

        StringBuffer buf = new StringBuffer();
        buf.append(" select a.headpackageid , a.bankaccnum ,a.obmdef1 headno,c.obmdef1 itemno ");
        buf.append(" from ebank_paylog_h a  ");
        buf.append(" inner join cmp_settlement b on a.srcbillcode  = b.billcode ");
        buf.append(" inner join ebank_paylog c on a.pk_ebank_paylog_h = c.pk_ebank_paylog_h ");
        buf.append(" where a.srcbillcode = ? ");
        buf.append(" and a.dr = 0  and b.dr = 0 ");

        Connection conn = FunctionProcessor.connDatabase(dbConnVO.getIp(), dbConnVO.getPort(), dbConnVO.getServername(), dbConnVO.getUsername(), dbConnVO.getPassword(), dbConnVO.getDbType());
        PreparedStatement pstmt = conn.prepareStatement(buf.toString());
        // 设置参数
        pstmt.setString(1, billcode);
        String urlStr = null;
        // 执行查询
        try (ResultSet rs = pstmt.executeQuery()) {
            // 获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            List<Map<String, Object>> resultList = new ArrayList<>();
            // 遍历结果集
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = rsmd.getColumnName(i);
                    Object value = rs.getObject(i);
                    row.put(columnName, value);
                }
                resultList.add(row);
            }

            if(resultList.size() ==0){
                return null;
            }
            Map<String, Object> map = resultList.get(0);
            String headno = (String)map.get("HEADNO");
            package_code = headno ;

            String itemno = (String) map.get("ITEMNO");
            jsonObject.addProperty("apply_code",itemno);

            jsonObject.addProperty("status_no","4");
            jsonObject.addProperty("status_dec","指令保存失败！业务申请编号:"+itemno+"重复！");

            // 对 biz_response_content 进行 urlencode 编码
            String encodedData = URLEncoder.encode(jsonArray.toString(), "UTF-8");
            biz_response_content = encodedData ;


            //拼接 调用路径
            urlStr = "http://127.0.0.1:8082/api/gateway?"
                    +       "app_id="+app_id+"&"
                    +       "secret_code="+secret_code+"&"
                    +       "timestamp="+timestamp+"&"
                    +       "transaction_type="+transaction_type+"&"
                    +       "action_type="+action_type+"&"
                    +       "version="+version+"&"
                    +       "charset="+charset+"&"
                    +       "package_code="+package_code+"&"
                    +       "biz_response_content="+biz_response_content ;

            // sendPost(urlStr,"");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return urlStr;
    }
}
