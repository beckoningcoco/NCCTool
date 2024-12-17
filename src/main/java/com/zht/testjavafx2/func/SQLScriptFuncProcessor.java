package com.zht.testjavafx2.func;

import com.zht.testjavafx2.vo.DBConnVO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SQLScriptFuncProcessor {

        public static DBConnVO ctrateGrid(){
            DBConnVO ret =  new DBConnVO();;
            // 创建一个自定义的Dialog
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("新增应用");

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
            grid.add(connName , 0 ,0);
            grid.add(connField , 1 ,0);
            //数据库类型
            Label databaselabel = new Label("数据库类型 ");
            databaselabel.setPrefWidth(100);
            ComboBox<String> comboBox = new ComboBox<>();
            comboBox.setPromptText("选择数据库的类型");


            // 创建数据源
            ObservableList<String> options = FXCollections.observableArrayList(
                    "ORACLE", "DM"
            );

            // 设置 ComboBox 的项目
            comboBox.setItems(options);

            // 可选：设置默认选中的项
            comboBox.setValue("ORACLE");
            grid.add(databaselabel , 0 ,1);
            grid.add(comboBox , 1 ,1);

            //数据库ip
            Label iplabel = new Label("数据库 IP ");
            TextField ipField = new TextField();
            ipField.setPromptText("例如:192.168.3.7");
            grid.add(iplabel , 0 ,2);
            grid.add(ipField , 1 ,2);

            //端口号
            Label portLabel = new Label("端口号 ");
            TextField portTextField = new TextField();
            portTextField.setPromptText("例如:1521");
            grid.add(portLabel , 0 ,3);
            grid.add(portTextField , 1 ,3);

            //服务名
            Label dbLabel = new Label("DB/ODBC名称 ");
            dbLabel.setPrefWidth(140);
            TextField dbTextField = new TextField();
            dbTextField.setPromptText("例如:orcl");
            grid.add(dbLabel , 0 ,4);
            grid.add(dbTextField , 1 ,4);

            //用户名
            Label userLabel = new Label("用户名");
            TextField userTextField = new TextField();
            userTextField.setPromptText("用户名");
            grid.add(userLabel , 0 ,5);
            grid.add(userTextField , 1 ,5);

            //密码
            Label pwdLabel = new Label("用户密码 ");
            TextField pwdTextField = new TextField();
            pwdTextField.setPromptText("用户密码");
            grid.add(pwdLabel , 0 ,6);
            grid.add(pwdTextField , 1 ,6);

            Button testConn = new Button("测试连接");
            testConn.setStyle(
                    "-fx-background-color: #f1f1f1; " + // 蓝色背景
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
                    FunctionProcessor.connDatabase(ip,port,db,user,pwd,dbType);
                    ret.setConnName(name);
                    ret.setDbType(dbType);
                    ret.setIp(ip);
                    ret.setPort(port);
                    ret.setServername(db);
                    ret.setUsername(user);
                    ret.setPassword(pwd);

                    //将这个数据库连接信息保存到 xml
                    saveConnDataToxXml(ret);
                } catch (SQLException e) {
                    e.fillInStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("测试连接失败:"+e.getMessage());
                    alert.showAndWait();
                }
            });

            grid.add(testConn , 1 ,9);
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



    }
}
