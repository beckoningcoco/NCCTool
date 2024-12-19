package com.zht.testjavafx2.func;

import com.zht.testjavafx2.nomal.ConfigSysintParam;
import com.zht.testjavafx2.openapi.FileUtil;
import com.zht.testjavafx2.openapi.XmlConverter;
import com.zht.testjavafx2.vo.DBConnVO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

@SuppressWarnings("all")
public class SQLScriptFuncProcessor {
    private static final String targetFilePath = "D:/Program Files/ncc/dbconn.xml";

    public static DBConnVO ctrateGrid() {
        DBConnVO ret = new DBConnVO();
        ;
        // 创建一个自定义的Dialog
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("新增数据连接");

        // 设置对话框的按钮
        ButtonType loginButtonType = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        Map<String, TextField> tempMap = new HashMap<>(100);
        // 创建对话框的内容区域
        GridPane grid = new GridPane();
        grid.setPrefHeight(300);
        grid.setPrefWidth(400);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        //新增连接 名称
        Label connName = new Label("数据源命名 ");
        TextField connField = new TextField();
        grid.add(connName, 0, 0);
        grid.add(connField, 1, 0);
        //数据库类型
        Label databaselabel = new Label("数据库类型 ");
        databaselabel.setPrefWidth(100);
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPromptText("选择数据库的类型");


        // 创建数据源
        ObservableList<String> options = FXCollections.observableArrayList("ORACLE", "DM");

        // 设置 ComboBox 的项目
        comboBox.setItems(options);

        // 可选：设置默认选中的项
        comboBox.setValue("ORACLE");
        grid.add(databaselabel, 0, 1);
        grid.add(comboBox, 1, 1);

        //数据库ip
        Label iplabel = new Label("数据库 IP ");
        TextField ipField = new TextField();
        ipField.setPromptText("例如:192.168.3.7");
        grid.add(iplabel, 0, 2);
        grid.add(ipField, 1, 2);

        //端口号
        Label portLabel = new Label("端口号 ");
        TextField portTextField = new TextField();
        portTextField.setPromptText("例如:1521");
        grid.add(portLabel, 0, 3);
        grid.add(portTextField, 1, 3);

        //服务名
        Label dbLabel = new Label("DB/ODBC名称 ");
        dbLabel.setPrefWidth(140);
        TextField dbTextField = new TextField();
        dbTextField.setPromptText("例如:orcl");
        grid.add(dbLabel, 0, 4);
        grid.add(dbTextField, 1, 4);

        //用户名
        Label userLabel = new Label("用户名");
        TextField userTextField = new TextField();
        userTextField.setPromptText("用户名");
        grid.add(userLabel, 0, 5);
        grid.add(userTextField, 1, 5);

        //密码
        Label pwdLabel = new Label("用户密码 ");
        TextField pwdTextField = new TextField();
        pwdTextField.setPromptText("用户密码");
        grid.add(pwdLabel, 0, 6);
        grid.add(pwdTextField, 1, 6);

        Button testConn = new Button("测试连接");
        testConn.setStyle("-fx-background-color: #f1f1f1; " + // 蓝色背景
                "-fx-text-fill: #242424; " + // 白色文本颜色
                "-fx-font-size: 14px; " + // 字体大小
                "-fx-padding: 4 15 4 15; " + // 内边距（上 右 下 左）
                "-fx-border-radius: 5; " + // 圆角半径
                "-fx-background-radius: 5; " + // 背景圆角半径
                "-fx-border-width: 1; " + // 边框宽度
                "-fx-border-color: #ffffff; " + // 无边框颜色
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 0); " + // 轻微阴影效果

                // 悬停时的样式变化
                "-fx-cursor: hand;" // 鼠标悬停时变为手型指针
        );

        testConn.setOnAction(event -> {
            String name = connField.getText().trim();
            String dbType = comboBox.getValue();
            String ip = ipField.getText().trim();
            String port = portTextField.getText().trim();
            String db = dbTextField.getText().trim();
            String user = userTextField.getText().trim();
            String pwd = pwdTextField.getText().trim();
            //测试连接
            try {
                FunctionProcessor.connDatabase(ip, port, db, user, pwd, dbType);
                ret.setConnName(name);
                ret.setDbType(dbType);
                ret.setIp(ip);
                ret.setPort(port);
                ret.setServername(db);
                ret.setUsername(user);
                ret.setPassword(pwd);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("测试连接成功！");
                alert.showAndWait();
                //将这个数据库连接信息保存到 xml
                saveConnDataToxXml(ret);
            } catch (SQLException e) {
                e.fillInStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("测试连接失败:" + e.getMessage());
                alert.showAndWait();
            } finally {
                FunctionProcessor.closeConn();
            }
        });

        grid.add(testConn, 1, 9);
        // 将GridPane设置为对话框的内容
        dialog.getDialogPane().setContent(grid);

        // 请求焦点
        Platform.runLater(connField::requestFocus);

        // 转换对话框的结果
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                String name = connField.getText().trim();
                String dbType = comboBox.getValue();
                String ip = ipField.getText().trim();
                String port = portTextField.getText().trim();
                String db = dbTextField.getText().trim();
                String user = userTextField.getText().trim();
                String pwd = pwdTextField.getText().trim();
                ret.setConnName(name);
                ret.setDbType(dbType);
                ret.setIp(ip);
                ret.setPort(port);
                ret.setServername(db);
                ret.setUsername(user);
                ret.setPassword(pwd);
                System.out.println(ret.toString());
            }
            return null;
        });

        // 显示对话框并等待结果
        Optional<String> result = dialog.showAndWait();
        return ret;
    }

    private static void saveConnDataToxXml(DBConnVO ret) {

        try {
            String xmlStr = XmlConverter.convertToXml(ret);
            xmlStr = XmlConverter.formatXml(xmlStr);
            FileUtil.copyResourceToFileIfNotExists(targetFilePath);
            InputStream resourceAsStream = FileUtil.getInputStreamForFile(targetFilePath);
            Reader reader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);
            InputSource is = new InputSource(reader);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            // 将新的 XML 字符串解析为 DocumentFragment
            Document newDoc = dBuilder.parse(new ByteArrayInputStream(xmlStr.getBytes()));
            Element newElement = (Element) newDoc.getDocumentElement().cloneNode(true);
            // 获取根元素并插入新元素
            Element root = doc.getDocumentElement();
            NodeList conns = root.getElementsByTagName("conns");
            conns.item(0).appendChild(doc.importNode(newElement, true));

            // 使用 Transformer 将修改后的文档写回文件
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            File file = new File(targetFilePath);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static DBConnVO ctrateLoadGrid() throws IOException, ParserConfigurationException, SAXException {
        DBConnVO ret = new DBConnVO();
        // 创建一个自定义的Dialog
        Dialog<DBConnVO> dialog = new Dialog<>();
        dialog.setTitle("加载数据库连接");

        // 设置对话框的按钮
        ButtonType loginButtonType = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // 创建对话框的内容区域
        GridPane grid = new GridPane();
        grid.setPrefHeight(300);
        grid.setPrefWidth(400);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        Label lab = new Label("选择一个数据连接");
        ListView<DBConnVO> listView = new ListView<>();

        //加载xml中的所有VO
        List<DBConnVO> list = SQLScriptFuncProcessor.transferXML2VO();
        listView.getItems().setAll(list);

        grid.add(lab, 0, 0);
        grid.add(listView, 0, 1);

        // 将GridPane设置为对话框的内容
        dialog.getDialogPane().setContent(grid);


        // 请求焦点
//        Platform.runLater(connField::requestFocus);

        // 转换对话框的结果
        dialog.setResultConverter(new Callback<>() {
            @Override
            public DBConnVO call(ButtonType dialogButton) {
                if (dialogButton == loginButtonType) {
                    DBConnVO selectedItem = listView.getSelectionModel().getSelectedItem();
                    if (selectedItem != null) {
                        // 如果你希望返回选中的项本身，可以返回 selectedItem
                        return selectedItem;
                    }
                }
                return null;
            }
        });

        // 显示对话框并等待结果
        Optional<DBConnVO> result = dialog.showAndWait();

        ret = listView.getSelectionModel().getSelectedItem();
        return ret;
    }

    // 将数据库连接的xml信息转换为VO
    private static List<DBConnVO> transferXML2VO() throws IOException, ParserConfigurationException, SAXException {
        List<DBConnVO> ret = new ArrayList<>();
        FileUtil.copyResourceToFileIfNotExists(targetFilePath);
        InputStream resourceAsStream = FileUtil.getInputStreamForFile(targetFilePath);
        Reader reader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);
        InputSource is = new InputSource(reader);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(is);
        doc.getDocumentElement().normalize();

        System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
        //xml 里至少有一个conns标签
        // 获取所有 <conn> 元素
        NodeList connNodes = doc.getElementsByTagName("conn");
        if (connNodes.getLength() == 0) {
            return ret;
        }
        for (int i = 0; i < connNodes.getLength(); i++) {
            Node connNode = connNodes.item(i);
            if (connNode.getNodeType() == Node.ELEMENT_NODE) {
                Element connElement = (Element) connNode;
                DBConnVO vo = new DBConnVO();
                vo.setConnName(getElementValue(connElement, "connName"));
                vo.setDbType(getElementValue(connElement, "dbType"));
                vo.setServername(getElementValue(connElement, "servername"));
                vo.setIp(getElementValue(connElement, "ip"));
                vo.setPort(getElementValue(connElement, "port"));
                vo.setUsername(getElementValue(connElement, "username"));
                vo.setPassword(getElementValue(connElement, "password"));

                vo.setDef1(getElementValue(connElement, "def1"));
                vo.setDef2(getElementValue(connElement, "def2"));
                vo.setDef3(getElementValue(connElement, "def3"));
                vo.setDef4(getElementValue(connElement, "def4"));
                vo.setDef5(getElementValue(connElement, "def5"));

                ret.add(vo);
            }
        }
        return ret;
    }


    private static String getElementValue(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                return node.getTextContent();
            }
        }
        return null;
    }

    //导出业务插件的脚本，参数是  全类名，是否导出事件源
    public static void exceListenerSql(String clazzName, boolean selected, DBConnVO dbConnVO) throws SQLException, IOException {

        if (dbConnVO == null) {
            throw new SQLException("获取数据库连接失败！");
        }
        // SELECT 语句
        String selectSQL = " select * from PUB_EVENTLISTENER WHERE dr = 0 and implclassname = '" + clazzName + "'";

        // INSERT 语句模板
        StringBuffer insertSQLTemplate = new StringBuffer();
        insertSQLTemplate.append(" INSERT INTO PUB_EVENTLISTENER  ");
        insertSQLTemplate.append(" (COMP,DR,ENABLED,IMPLCLASSNAME,INDUSTRYTYPE,LISTENERTYPE,LOCALTYPE,NAME,NAME2,NAME3,NAME4,NAME5,NAME6,NOTE,OPERINDEX,OWNER,PK_EVENTLISTENER,PK_EVENTTYPE,TS)   ");
        insertSQLTemplate.append(" VALUES (%s) ");
        List<String> insertStatements = selectToInsert(selectSQL, dbConnVO, insertSQLTemplate.toString());
        //导出事件源
        if (selected) {
            StringBuffer buf = new StringBuffer();
            buf.append(" SELECT * FROM PUB_EVENTTYPE pe WHERE PK_EVENTTYPE IN (SELECT PK_EVENTTYPE FROM PUB_EVENTLISTENER  ");
            buf.append(" WHERE dr = 0 and  implclassname = '" + clazzName + "' ) ");

            StringBuffer listenerTypeTemplate = new StringBuffer();
            listenerTypeTemplate.append(" INSERT INTO PUB_EVENTTYPE  ");
            listenerTypeTemplate.append(" (COMP,DR,EVENTTYPECODE,EVENTTYPENAME,EVENTTYPENAME2,EVENTTYPENAME3,EVENTTYPENAME4,EVENTTYPENAME5,EVENTTYPENAME6,NOTE,OWNER,PK_EVENTTYPE,SOURCEID,SOURCENAME,SOURCENAME2,SOURCENAME3,SOURCENAME4,SOURCENAME5,SOURCENAME6,TS)  ");
            listenerTypeTemplate.append(" VALUES (%s) ");

            List<String> eventypeStatements = selectToInsert(buf.toString(), dbConnVO, listenerTypeTemplate.toString());
            if (eventypeStatements != null && eventypeStatements.size() > 0) {
                insertStatements.addAll(eventypeStatements);
            }
        }
        //输出到 桌面目录
        String tarurl = ConfigSysintParam.SQLTARGETURL + "/listenersSQL.sql";
        // 将所有的 INSERT 语句写入到 .sql 文件中
        writeInsertStatementsToFile(insertStatements, tarurl);
        System.out.println("导出业务插件脚本成功！");
    }

    private static void writeInsertStatementsToFile(List<String> insertStatements, String filePath) throws IOException {
        // 确保输出目录存在
        Files.createDirectories(Paths.get(filePath).getParent());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            for (String statement : insertStatements) {
                writer.write(statement);
                writer.newLine(); // 添加换行符以分隔每个语句
            }
        }
    }

    private static List<String> selectToInsert(String selectSQL, DBConnVO dbConnVO, String sqlTemplate) {
        List<String> insertStatements = new ArrayList<>();
        try (Connection conn = FunctionProcessor.connDatabase(dbConnVO.getIp(), dbConnVO.getPort(), dbConnVO.getServername(), dbConnVO.getUsername(), dbConnVO.getPassword(), dbConnVO.getDbType()); Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {
            // 获取列元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (rs.next()) {
                StringBuilder values = new StringBuilder();
                for (int i = 1; i <= columnCount; i++) {
                    if (i > 1) values.append(", ");

                    Object value = rs.getObject(i);
                    if (value == null) {
                        values.append("NULL");
                    } else {
                        switch (rsmd.getColumnType(i)) {
                            case Types.VARCHAR:
                            case Types.CHAR:
                            case Types.LONGVARCHAR:
                            case Types.NVARCHAR:
                            case Types.NCHAR:
                            case Types.LONGNVARCHAR:
                                values.append("'").append(escapeSingleQuotes(value.toString())).append("'");
                                break;
                            default:
                                values.append(value.toString());
                        }
                    }
                }
                // 将生成的值插入到 INSERT 模板中
                String insertStatement = String.format(sqlTemplate, values.toString());
                insertStatements.add(insertStatement);
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return insertStatements;
    }


    // 转义单引号
    private static String escapeSingleQuotes(String input) {
        return input.replace("'", "''");
    }

    //导出自定义档案的脚本
    public static void execDefdocSQL(String code, DBConnVO nowDbConnVO) throws SQLException, IOException {

        List<String> sqlList = new ArrayList<>();

        String sql1 = "select * from bd_defdoc where pk_defdoclist in (select pk_defdoclist from bd_defdoclist where code in ('" + code + "'))";
        String sql2 = "select * from bd_mode_selected where mdclassid in(select pk_defdoclist from bd_defdoclist where code in ('" + code + "'))";
        String sql3 = "select * from bd_mode_all where mdclassid in (select pk_defdoclist from bd_defdoclist where code in ('" + code + "'))";
        String sql4 = "select * from bd_defdoclist where code in ('" + code + "')";
        String sql5 = "select * from bd_refinfo  where reserv3 in ('" + code + "')";

        HashMap<String, String> map = new HashMap<>();
        map.put("BD_DEFDOC", sql1);
        map.put("BD_MODE_SELECTED", sql2);
        map.put("BD_MODE_ALL", sql3);
        map.put("BD_DEFDOCLIST", sql4);
        map.put("BD_REFINFO", sql5);

        Connection conn = FunctionProcessor.connDatabase(nowDbConnVO.getIp(), nowDbConnVO.getPort(), nowDbConnVO.getServername(), nowDbConnVO.getUsername(), nowDbConnVO.getPassword(), nowDbConnVO.getDbType());

        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            sqlList.addAll(generateInsertStatements(conn, entry));
        }

        //输出到 桌面目录
        String tarurl = ConfigSysintParam.SQLTARGETURL + "/defdocSQL.sql";
        // 将所有的 INSERT 语句写入到 .sql 文件中
        writeInsertStatementsToFile(sqlList, tarurl);

    }

    private static List<String> generateInsertStatements(Connection conn, Map.Entry<String, String> entry) throws SQLException {
        List<String> ret = new ArrayList<>();
        String tableName = entry.getKey();
        String sql = entry.getValue();

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();

        while (rs.next()) {
            StringBuilder insertStmt = new StringBuilder("INSERT INTO " + tableName + " (");
            List<String> values = new ArrayList<>();

            for (int i = 1; i <= columnCount; i++) {
                if (i > 1) {
                    insertStmt.append(", ");
                }
                insertStmt.append(rsmd.getColumnName(i));

                Object value = rs.getObject(i);
                String formattedValue = formatValue(value, rsmd.getColumnType(i));
                values.add(formattedValue);
            }

            insertStmt.append(") VALUES (").append(String.join(", ", values)).append(");");
            ret.add(insertStmt.toString());
        }
        return ret;
    }

    private static String formatValue(Object value, int sqlType) {
        if (value == null) return "NULL";

        switch (sqlType) {
            case Types.VARCHAR:
            case Types.CHAR:
            case Types.LONGVARCHAR:
                return "'" + value.toString().replace("'", "''") + "'";
            case Types.DATE:
            case Types.TIMESTAMP:
                return "TIMESTAMP '" + ((Timestamp) value).toLocalDateTime() + "'";
            case Types.NUMERIC:
            case Types.DECIMAL:
            case Types.INTEGER:
            case Types.SMALLINT:
            case Types.BIGINT:
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
                return value.toString();
            default:
                // Handle other types as needed
                return "'" + value.toString().replace("'", "''") + "'";
        }
    }

    //导出后台任务的脚本
    public static void execPluginsSQL(String code, DBConnVO nowDbConnVO) throws SQLException, IOException {
        List<String> pluginsList = new ArrayList<>();

        Map<String, String> map = new HashMap<>();
        String sql1 = "select * from pub_alerttypeconfig where pk_alertregistry in(select pk_alertregistry from pub_alertregistry where pk_alerttype in(select pk_alerttype from pub_alerttype where busi_plugin in('" + code + "')))";
        String sql2 = "select * from pub_alertregistry where pk_alerttype in(select pk_alerttype from pub_alerttype where busi_plugin in('" + code + "'))";
        String sql3 = "select * from pub_alerttype_b where pk_alerttype in(select pk_alerttype from pub_alerttype where busi_plugin in('" + code + "'))";
        String sql4 = "select * from pub_alerttype  where busi_plugin in('" + code + "')";

        map.put("pub_alerttypeconfig", sql1);
        map.put("pub_alertregistry", sql2);
        map.put("pub_alerttype_b", sql3);
        map.put("pub_alerttype", sql4);

        Connection conn = FunctionProcessor.connDatabase(nowDbConnVO.getIp(), nowDbConnVO.getPort(), nowDbConnVO.getServername(), nowDbConnVO.getUsername(), nowDbConnVO.getPassword(), nowDbConnVO.getDbType());

        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            pluginsList.addAll(generateInsertStatements(conn, entry));
        }

        //输出到 桌面目录
        String tarurl = ConfigSysintParam.SQLTARGETURL + "/pluginSQL.sql";
        // 将所有的 INSERT 语句写入到 .sql 文件中
        writeInsertStatementsToFile(pluginsList, tarurl);
    }
}
