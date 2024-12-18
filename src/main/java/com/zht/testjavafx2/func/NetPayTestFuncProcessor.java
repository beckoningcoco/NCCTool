package com.zht.testjavafx2.func;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zht.testjavafx2.vo.DBConnVO;
import java.io.IOException;
import java.io.OutputStream;
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
    private static final String transaction_type = "push_instruct_status";
    private static final String action_type = "write";
    private static final String version = "1.0";
    private static final String charset = "utf-8";
    private static  String timestamp = "2024-11-26 11:10:00";
    private static  String package_code = "20241126CQ700000007";
    private static  String biz_response_content = "[{\"apply_code\":\"20241126CQ7000008\",\"status_no\":\"8\"," +
            "\"status_dec\":\"成功\",\"trans_no\":\"1259811399241126144901008\",\"client_code\":\"01-C0170\"," +
            "\"currency_code\":\"RMB\",\"office_code\":\"01\",\"deal_time\":\"2024-11-26 15:11:00\"}]";




    public static void postSuccessSend(String billcode , DBConnVO dbConnVO) throws SQLException {

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
                return;
            }
            Map<String, Object> map = resultList.get(0);
            String headno = (String)map.get("headno");
            package_code = headno ;

            String itemno = (String) map.get("itemno");
            jsonObject.addProperty("apply_code",itemno);

            String headpackageid = (String) map.get("headpackageid");
            jsonObject.addProperty("trans_no",headpackageid);

            String bankaccnum = (String) map.get("bankaccnum");
            String[] arr = bankaccnum.split("-");
            if(arr.length == 4){
              String clint_id =   arr[0]+"-"+ arr[2];
              jsonObject.addProperty("client_code",clint_id);
            }

            // 对 biz_response_content 进行 urlencode 编码
            String encodedData = URLEncoder.encode(jsonArray.toString(), "UTF-8");
            biz_response_content = encodedData ;


        //拼接 调用路径
        String urlStr = "http://127.0.0.1:8082/api/gateway?"
                +       "app_id="+app_id+"&"
                +       "secret_code="+secret_code+"&"
                +       "timestamp="+timestamp+"&"
                +       "transaction_type="+transaction_type+"&"
                +       "action_type="+action_type+"&"
                +       "version="+version+"&"
                +       "charset="+charset+"&"
                +       "package_code="+package_code+"&"
                +       "biz_response_content="+biz_response_content ;

        sendPost(urlStr);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }


    private static void  sendPost(String urlStr) throws IOException {

            // 创建URL对象
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // 设置请求方法为POST
            conn.setRequestMethod("POST");
            // 允许发送（输出）和读取（输入）
            conn.setDoOutput(true);
            // 设置内容类型为 application/json
            conn.setRequestProperty("Content-Type", "application/json");

            // 准备POST请求的JSON数据
//            String jsonInputString = "{\"key1\":\"value1\", \"key2\":\"value2\"}";

            // 发送POST请求
           OutputStream os = conn.getOutputStream();

            // 获取响应码
            int responseCode = conn.getResponseCode();
            System.out.println("POST Response Code :: " + responseCode);

            // 处理响应...

    }
}
