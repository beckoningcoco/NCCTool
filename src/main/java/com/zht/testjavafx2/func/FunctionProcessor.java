package com.zht.testjavafx2.func;

import com.zht.testjavafx2.ui.ExportNodeScenceCreator;
import com.zht.testjavafx2.ui.UICreator;
import org.apache.commons.lang3.StringUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class FunctionProcessor {
    private static Connection conn = null;


    //测试数据库连接
    public static Connection connDatabase(String ip, String port, String sid, String username, String userpassword, String databaseType) throws SQLException {

        if ("ORACLE".equals(databaseType)) {

            // 定义数据库连接参数
            String url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + sid.toLowerCase(); // 根据实际情况调整
            String user = username;
            String password = userpassword;

            try {
                // 从JDBC 4.0开始，不再需要显式加载驱动程序
//                Class.forName("oracle.jdbc.driver.OracleDriver"); // 可选，通常不需要

                // 获取数据库连接
                conn = DriverManager.getConnection(url, user, password);
                if (conn != null
                ) {
                    System.out.println("成功连接到数据库！"
                    );
                }
            } catch
            (Exception e) {
                e.printStackTrace();
                System.err.println("数据库连接失败："
                        + e.getMessage());
                throw new SQLException("数据库连接失败: " + e.getMessage());
            }
        } else if ("DM".equals(databaseType)) {
            // 定义数据库连接参数
            String url = "jdbc:dm://" + ip + ":" + port + "/" + username + "?useUnicode=true&characterEncoding=GBK"; // 根据实际情况调整
            String user = username;
            String password = userpassword;

            try {
                Class.forName("dm.jdbc.driver.DmDriver"); // 可选，通常不需要
                // 获取数据库连接
                conn = DriverManager.getConnection(url, user, password);
                if (conn != null
                ) {
                    System.out.println("成功连接到数据库！"
                    );
                }
            } catch
            (Exception e) {
                e.printStackTrace();
                System.err.println("数据库连接失败："
                        + e.getMessage());
                throw new SQLException("数据库连接失败: " + e.getMessage());
            }
        }
        return conn;
    }

    //导出新节点脚本
    public static void exportNodeSQL(String appCode, Connection con) throws Exception {

        if (StringUtils.isBlank(appCode)) {
            throw new SQLException("传入的应用编码为空，导出sql脚本失败！");
        }
        conn = con;
        String dirUrl = ExportNodeScenceCreator.getDirUrl();

        String tablename = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter(dirUrl + "\\" + appCode + ".sql");

            Map<String, String> mapSql = getBDSelectMapSql();
            for (String key : mapSql.keySet()) {
                tablename = key;
                getInsertSql(key, ((String) mapSql.get(key)).replace("NODECODE", appCode), fw);
            }

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            closeConn();
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException ex) {
                    ex.getMessage();
                }
            }
        }
    }

    public static void closeConn() {
        // 关闭连接
        try {
            if (conn != null
                    && !conn.isClosed()) {
                conn.close();
                System.out.println("数据库连接已关闭。"
                );
            }
        } catch
        (SQLException ex) {
            ex.printStackTrace();
        }
    }


    private static Map<String, String> getBDSelectMapSql() {
        Map<String, String> mapSql = new HashMap<String, String>();
        mapSql.put("pub_bcr_candiattr",
                "select * from   pub_bcr_candiattr where pk_nbcr in (select pk_nbcr from pub_bcr_nbcr where code in (select pk_billtypecode from bd_billtype where  nodecode in ('NODECODE')))");
        mapSql.put("pub_bcr_nbcr",
                "select * from   pub_bcr_nbcr where code in (select pk_billtypecode from bd_billtype where  nodecode in ('NODECODE'))");
        mapSql.put("pub_bcr_elem",
                "select * from   pub_bcr_elem where pk_billcodebase in (select pk_billcodebase from pub_bcr_rulebase where nbcrcode in (select pk_billtypecode from bd_billtype where  nodecode in ('NODECODE')))");
        mapSql.put("pub_bcr_orgrela",
                "select * from   pub_bcr_orgrela where pk_billcodebase in (select pk_billcodebase from pub_bcr_rulebase where nbcrcode in (select pk_billtypecode from bd_billtype where  nodecode in ('NODECODE')))");
        mapSql.put("pub_bcr_rulebase",
                "select * from   pub_bcr_rulebase where nbcrcode in (select pk_billtypecode from bd_billtype where  nodecode in ('NODECODE'))");
        mapSql.put("pub_eventlistener",
                "select * from   pub_eventlistener where pk_eventtype in (select pk_eventtype from pub_eventtype where sourceid in (select distinct mdid from sm_funcregister where funcode in ('NODECODE')))");
        mapSql.put("pub_eventtype",
                "select * from   pub_eventtype where sourceid in (select distinct mdid from sm_funcregister where funcode in ('NODECODE'))");
        mapSql.put("pub_vochange_b",
                "select * from   pub_vochange_b where pk_vochange in ( select pk_vochange from pub_vochange where src_billtype in ( select pk_billtypecode from bd_billtype where  nodecode in ('NODECODE')))");
        mapSql.put("pub_vochange_b_upgrade",
                "select * from   pub_vochange_b_upgrade where pk_vochange in ( select pk_vochange from pub_vochange where src_billtype in ( select pk_billtypecode from bd_billtype where  nodecode in ('NODECODE')))");
        mapSql.put("pub_vochange_s",
                "select * from  pub_vochange_s where pk_vochange in ( select pk_vochange from pub_vochange where src_billtype in ( select pk_billtypecode from bd_billtype where  nodecode in ('NODECODE')))");
        mapSql.put("pub_vochange_s_upgrade",
                "select * from   pub_vochange_s_upgrade where pk_vochange in ( select pk_vochange from pub_vochange where src_billtype in ( select pk_billtypecode from bd_billtype where  nodecode in ('NODECODE')))");
        mapSql.put("pub_vochange",
                "select * from   pub_vochange where src_billtype in ( select pk_billtypecode from bd_billtype where  nodecode in ('NODECODE'))");
        mapSql.put("pub_vochange_b",
                "select * from   pub_vochange_b where pk_vochange in ( select pk_vochange from pub_vochange where dest_billtype in ( select pk_billtypecode from bd_billtype where  nodecode in ('NODECODE')))");
        mapSql.put("pub_vochange_b_upgrade",
                "select * from   pub_vochange_b_upgrade where pk_vochange in ( select pk_vochange from pub_vochange where src_billtype in ( select pk_billtypecode from bd_billtype where  nodecode in ('NODECODE')))");
        mapSql.put("pub_vochange_s",
                "select * from   pub_vochange_s where pk_vochange in ( select pk_vochange from pub_vochange where src_billtype in ( select pk_billtypecode from bd_billtype where  nodecode in ('NODECODE')))");
        mapSql.put("pub_vochange_s_upgrade",
                "select * from   pub_vochange_s_upgrade where pk_vochange in ( select pk_vochange from pub_vochange where src_billtype in ( select pk_billtypecode from bd_billtype where  nodecode in ('NODECODE')))");
        mapSql.put("pub_vochange",
                "select * from   pub_vochange where dest_billtype in ( select pk_billtypecode from bd_billtype where  nodecode in ('NODECODE'))");
        mapSql.put("pub_systemplate_base", "select * from   pub_systemplate_base where funnode in ('NODECODE')");
        mapSql.put("pub_systemplate", "select * from   pub_systemplate where funnode in ('NODECODE')");
        mapSql.put("pub_billtemplet_b",
                "select * from   pub_billtemplet_b where pk_billtemplet in (select pk_billtemplet from pub_billtemplet where nodecode in ('NODECODE'))");
        mapSql.put("pub_billtemplet_t",
                "select * from   pub_billtemplet_t where pk_billtemplet in (select pk_billtemplet from pub_billtemplet where nodecode in ('NODECODE'))");
        mapSql.put("pub_billtemplet", "select * from   pub_billtemplet where  nodecode in ('NODECODE')");
        mapSql.put("pub_query_condition",
                "select * from   pub_query_condition where pk_templet in (select ID from pub_query_templet where node_code in ('NODECODE'))");
        mapSql.put("pub_query_templet", "select * from   pub_query_templet where node_code in ('NODECODE')");
        mapSql.put("pub_print_cell",
                "select * from   pub_print_cell where ctemplateid in (select ctemplateid from pub_print_template where vnodecode in ('NODECODE'))");
        mapSql.put("pub_print_line",
                "select * from   pub_print_line where ctemplateid in (select ctemplateid from pub_print_template where vnodecode in ('NODECODE'))");
        mapSql.put("pub_print_variable",
                "select * from   pub_print_variable where ctemplateid in (select ctemplateid from pub_print_template where vnodecode in ('NODECODE'))");
        mapSql.put("pub_print_datasource",
                "select * from   pub_print_datasource where ctemplateid in (select ctemplateid from pub_print_template where vnodecode in ('NODECODE'))");
        mapSql.put("pub_print_template", "select * from   pub_print_template where vnodecode in ('NODECODE')");
        mapSql.put("bd_fwdbilltype",
                "select * from   bd_fwdbilltype where  pk_billtypeid in ( select pk_billtypeid from bd_billtype where  nodecode in ('NODECODE'))");
        mapSql.put("pub_function",
                "select * from   pub_function where   pk_billtypeid in ( select pk_billtypeid from bd_billtype where  nodecode in ('NODECODE'))");
        mapSql.put("pub_billAction",
                "select * from   pub_billAction where pk_billtype in (select pk_billtypecode from bd_billtype where  nodecode in ('NODECODE'))");
        mapSql.put("pub_busiclass",
                "select * from   pub_busiclass where pk_billtype in (select pk_billtypecode from bd_billtype where  nodecode in ('NODECODE'))");
        mapSql.put("sm_paramregister",
                "select * from   sm_paramregister where parentid in ( select cfunid from sm_funcregister where funcode in ('NODECODE'))");
        mapSql.put("sm_butnRegister",
                "select * from   sm_butnRegister where parent_id in (select CFUNID from sm_funcregister where funcode in ('NODECODE'))");
        mapSql.put("sm_menuitemreg", "select * from  sm_menuitemreg where funcode in ('NODECODE')");
        mapSql.put("bd_billtype2",
                "select * from   bd_billtype2 where  pk_billtypeid in ( select pk_billtypeid from bd_billtype where  nodecode in ('NODECODE'))");
        mapSql.put("bd_billtype", "select * from   bd_billtype where  nodecode in ('NODECODE')");
        mapSql.put("sm_funcregister", "select * from   sm_funcregister where funcode in ('NODECODE')");
        return mapSql;
    }

    private static void getInsertSql(String tableName, String sql, FileWriter fw)
            throws ClassNotFoundException, SQLException, IOException, IOException {

//		Class.forName("oracle.jdbc.driver.OracleDriver");
        List<String> column = new ArrayList<String>();
        List<String> list = new ArrayList<String>();
        PreparedStatement prst = conn.prepareStatement(sql);
        ResultSet rs = prst.executeQuery();
        ResultSetMetaData rmd = rs.getMetaData();
        for (int i = 1; i <= rmd.getColumnCount(); i++) {
            column.add(rmd.getColumnName(i));
        }
        while (rs.next()) {
            for (String n : column) {
                if ("pub_query_templet".equals(tableName) && "FIXQUERYTREE".equals(n)) {
                    list.add("''");
                    continue;
                }
                if ("pub_alertregistry".equals(tableName) && "REPORTLIKEWORK".equals(n)) {
                    list.add("''");
                    continue;
                }
                n = rs.getString(n);
                if (n != null) {
                    list.add("'" + n + "'");
                    continue;
                }
                list.add("''");
            }
        }
        String insertsql = "insert into " + tableName + "  (";
        for (int j = 1; j <= rmd.getColumnCount(); j++) {
            if (j == rmd.getColumnCount()) {
                insertsql = String.valueOf(insertsql) + rmd.getColumnName(j) + ")  ";
            } else {
                insertsql = String.valueOf(insertsql) + rmd.getColumnName(j) + ",";
            }
        }
        String str = "";
        List<String> st = null;
        int num = column.size();
        int r = 0;
        while (r < list.size()) {
            st = list.subList(r, r + num);
            StringBuffer sb = new StringBuffer();
            for (int k = 0; k < st.size(); k++) {
                String ss = st.get(k);
                if (k == st.size() - 1) {
                    sb.append(ss);
                } else {
                    sb.append(String.valueOf(ss) + ",");
                }
                str = String.valueOf(insertsql) + " values(" + sb + ");";
            }
            fw.write(str);
            fw.write("\r\n");
            r += num;
        }
        rs.close();
        prst.close();
    }


}
