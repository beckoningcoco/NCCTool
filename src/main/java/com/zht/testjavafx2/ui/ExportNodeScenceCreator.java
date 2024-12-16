package com.zht.testjavafx2.ui;

import com.zht.testjavafx2.func.FunctionProcessor;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;

@SuppressWarnings("all")
public class ExportNodeScenceCreator {
    //数据库类型，默认oracle
    private static String databaseType = "ORACLE";
    //数据库 ip
    private static String ip = null;
    // 数据库端口
    private static String port = null;

    //用户名
    private static String username = null;

    //密码
    private static String password = null;

    //DB/ODBC名称
    private static String db_odbc = null;

    //数据库锁定状态
    private static Boolean lockStatus = false;

    //应用编码
    private static String appCode = null;

    //导出脚本的目录
    private static String dirUrl = null;

    public static String getDatabaseType() {
        return databaseType;
    }

    public static String getIp() {
        return ip;
    }

    public static String getPort() {
        return port;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static String getDb_odbc() {
        return db_odbc;
    }

    public static Boolean getLockStatus() {
        return lockStatus;
    }

    public static String getAppCode() {
        return appCode;
    }

    public static String getDirUrl() {
        return dirUrl;
    }


    public static void createScence(Stage stage ,BorderPane borderPane) {

        // 使用 VBox，间距为 20
        VBox vbox = new VBox(15);
        // 居中对齐
        vbox.setAlignment(Pos.TOP_CENTER);

        //标题行
        HBox titleBox = new HBox();
        Label titleLabel = new Label(" N C C 节 点 导 出 工 具 ");
        titleLabel.setStyle(" -fx-pref-width: 300px;-fx-padding: 5;-fx-font-size: 20px; -fx-font-weight: bold; -fx-font-family: 'Tahoma';");
        titleLabel.setTranslateX(250);
        titleBox.getChildren().add(titleLabel);

        //数据库类型
        // 创建 ComboBox 并设置提示文本
        HBox databaseBox = new HBox(50);
        databaseBox.setTranslateX(20);
        Label databaselabel = new Label("数据库类型 ");
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

        // 添加事件监听器
        comboBox.setOnAction(event -> {
            String selectedItem = comboBox.getValue();
            databaseType = selectedItem;
            System.out.println("Selected: " + selectedItem);
        });
        databaseBox.getChildren().addAll(databaselabel, comboBox);

        // 数据库 IP
        HBox ipBox = new HBox(55);
        Label iplabel = new Label("数据库 IP ");
        TextField ipField = new TextField();
        ipField.setPromptText("例如:192.168.3.7");
        //输入框的值改变后可以通过.textProperty().addListener赋值给成员变量
        ipField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // 更新成员变量
                ip = newValue;
            }
        });
        ipBox.setTranslateX(20);
        ipBox.getChildren().addAll(iplabel, ipField);

        // 端口号
        HBox portBox = new HBox(70);
        Label portLabel = new Label("端口号 ");
        portLabel.setTranslateX(1);
        TextField portTextField = new TextField();
        portTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // 更新成员变量
                port = newValue;
            }
        });
        portTextField.setPromptText("例如: 1521");
        portBox.setTranslateX(20);
        portBox.getChildren().addAll(portLabel, portTextField);

        // DB/ODBC 名称
        HBox dbBox = new HBox(26);
        Label dbLabel = new Label("DB/ODBC名称 ");
        TextField dbTextField = new TextField();
        dbTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // 更新成员变量
                db_odbc = newValue;
            }
        });
        dbTextField.setPromptText("例如: DM/ORCL");
        dbBox.setTranslateX(20);
        dbBox.getChildren().addAll(dbLabel, dbTextField);

        // 用户名
        HBox userBox = new HBox(68);
        Label userLabel = new Label("用户名  ");
        userLabel.setTranslateX(1);
        TextField userTextField = new TextField();
        userTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // 更新成员变量
                username = newValue;
            }
        });
        userTextField.setPromptText("输入用户名");
        userBox.setTranslateX(20);
        userBox.getChildren().addAll(userLabel, userTextField);

        // 密码
        HBox pwdBox = new HBox(60);
        Label pwdLabel = new Label("用户密码 ");
        pwdLabel.setTranslateX(1);
        TextField pwdTextField = new TextField();
        pwdTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // 更新成员变量
                password = newValue;
            }
        });
        pwdTextField.setPromptText("输入密码");
        pwdBox.setTranslateX(20);
        pwdBox.getChildren().addAll(pwdLabel, pwdTextField);


        // 添加按钮
        HBox buttonBox = new HBox(20);
        Button connectButton = new Button("测试连接");
        connectButton.setTranslateY(20);
        connectButton.setTranslateX(80);
        connectButton.setStyle(
                "-fx-background-color: #f1f1f1; " + // 蓝色背景
                        "-fx-text-fill: #242424; " + // 白色文本颜色
                        "-fx-font-size: 14px; " + // 字体大小
                        "-fx-padding: 4 15 4 15; " + // 内边距（上 右 下 左）
                        "-fx-border-radius: 5; " + // 圆角半径
                        "-fx-background-radius: 5; " + // 背景圆角半径
                        "-fx-border-width: 1; " + // 边框宽度
                        "-fx-border-color: black; " + // 无边框颜色
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 0); " + // 轻微阴影效果

                        // 悬停时的样式变化
                        "-fx-cursor: hand;" // 鼠标悬停时变为手型指针
        );
        connectButton.setOnAction(e -> {
            connectButton.setDisable(true);
            // 处理用户输入
            System.out.println("连接到 IP: " + ip + " 端口: " + port + "用户名：" + username + "密码：" + password + "DB/ODBC名称：" + db_odbc + "数据库类型：" + databaseType);
            Boolean success = true;
            try {
                FunctionProcessor.connDatabase(ip, port,db_odbc ,username, password, databaseType);
            } catch (SQLException ex) {
                // 创建一个提示框
                success = false;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("测试连接");
                alert.setHeaderText(null);
                alert.setContentText("  \r 连接不通过！");

                // 显示提示框
                alert.showAndWait();
            } finally {
                FunctionProcessor.closeConn();
                connectButton.setDisable(false);
            }
            if (success) {
                // 创建一个提示框
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("测试连接");
                alert.setHeaderText(null);
                alert.setContentText("  \r 连接通过！");

                // 显示提示框
                alert.showAndWait();
            }

        });
        // 导出新节点按钮
        HBox funBox = new HBox(15);
        funBox.setTranslateX(20);
        funBox.setTranslateY(30);
        Label funlabel = new Label("功能节点编码");
        funlabel.setStyle(" -fx-pref-width: 110px;-fx-padding: 5;-fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'Tahoma';");
        //功能编码
        TextField funField = new TextField();
        funField.setPrefHeight(30);
        funField.setPromptText("输入APPCODE/应用编码");
        //输入框的值改变后可以通过.textProperty().addListener赋值给成员变量
        funField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // 更新成员变量
                appCode = newValue;
            }
        });

        funField.setDisable(true);
        Button nodeButton = new Button("导出新节点脚本");
        nodeButton.setDisable(true);
        nodeButton.setStyle(
                "-fx-background-color: #f1f1f1; " + // 蓝色背景
                        "-fx-text-fill: #242424; " + // 白色文本颜色
                        "-fx-font-size: 14px; " + // 字体大小
                        "-fx-padding: 4 15 4 15; " + // 内边距（上 右 下 左）
                        "-fx-border-radius: 5; " + // 圆角半径
                        "-fx-background-radius: 5; " + // 背景圆角半径
                        "-fx-border-width: 1; " + // 边框宽度
                        "-fx-border-color: black; " + // 无边框颜色
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 0); " + // 轻微阴影效果

                        // 悬停时的样式变化
                        "-fx-cursor: hand;" // 鼠标悬停时变为手型指针
        );

        nodeButton.setOnAction(e -> {
            try {
                FunctionProcessor.exportNodeSQL(appCode, FunctionProcessor.connDatabase(ip, port,db_odbc, username, password, databaseType));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            // 创建一个提示框
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("导出节点脚本");
            alert.setHeaderText(null);
            alert.setContentText("  \r 导出成功！");

            // 显示提示框
            alert.showAndWait();

        });

        funBox.getChildren().addAll(funlabel, funField, nodeButton);

        // 导出节点sql目录按钮
        HBox dirBox = new HBox(15);
        dirBox.setTranslateX(20);
        dirBox.setTranslateY(30);
        Label dirlabel = new Label("导出脚本目录");
        dirlabel.setStyle(" -fx-pref-width: 110px;-fx-padding: 5;-fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'Tahoma';");
        //功能编码
        TextField dirField = new TextField();
        dirField.setPrefHeight(30);
        dirField.setPromptText("脚本存储目录");
        //输入框的值改变后可以通过.textProperty().addListener赋值给成员变量
        dirField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // 更新成员变量
                dirUrl = newValue;
            }
        });

        dirField.setEditable(false);
        Button dirButton = new Button("选择目录");
        dirButton.setDisable(true);
        dirButton.setStyle(
                "-fx-background-color: #f1f1f1; " + // 蓝色背景
                        "-fx-text-fill: #242424; " + // 白色文本颜色
                        "-fx-font-size: 14px; " + // 字体大小
                        "-fx-padding: 4 15 4 15; " + // 内边距（上 右 下 左）
                        "-fx-border-radius: 5; " + // 圆角半径
                        "-fx-background-radius: 5; " + // 背景圆角半径
                        "-fx-border-width: 1; " + // 边框宽度
                        "-fx-border-color: black; " + // 无边框颜色
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 0); " + // 轻微阴影效果

                        // 悬停时的样式变化
                        "-fx-cursor: hand;" // 鼠标悬停时变为手型指针
        );
        dirButton.setOnAction(event -> {
            // 创建 DirectoryChooser 实例
            DirectoryChooser directoryChooser = new DirectoryChooser();

            // 设置初始目录（可选）
            directoryChooser.setInitialDirectory(new File("C:\\"));

            // 显示目录选择对话框，并获取用户选择的目录
            File selectedDirectory = directoryChooser.showDialog(stage);

            if (selectedDirectory != null) {
                dirUrl = selectedDirectory.getAbsolutePath();
                dirField.setPromptText(selectedDirectory.getAbsolutePath());
                dirField.setText(selectedDirectory.getAbsolutePath());
                System.out.println("选择了目录: " + selectedDirectory.getAbsolutePath());
            } else {
                System.out.println("没有选择任何目录");
            }
        });
        dirBox.getChildren().addAll(dirlabel, dirField, dirButton);


        // ------------ 锁定按钮

        // 添加按钮
        Button lockButton = new Button("锁定数据库");
        lockButton.setTranslateY(20);
        lockButton.setTranslateX(80);
        lockButton.setStyle(
                "-fx-background-color: #f1f1f1; " + // 蓝色背景
                        "-fx-text-fill: #242424; " + // 白色文本颜色
                        "-fx-font-size: 14px; " + // 字体大小
                        "-fx-padding: 4 15 4 15; " + // 内边距（上 右 下 左）
                        "-fx-border-radius: 5; " + // 圆角半径
                        "-fx-background-radius: 5; " + // 背景圆角半径
                        "-fx-border-width: 1; " + // 边框宽度
                        "-fx-border-color: black; " + // 无边框颜色
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 0); " + // 轻微阴影效果

                        // 悬停时的样式变化
                        "-fx-cursor: hand;" // 鼠标悬停时变为手型指针
        );
        lockButton.setOnAction(e -> {
            // 处理用户输入
            if ("锁定数据库".equals(lockButton.getText())) {
                //锁定数据库前，先测试连接一下数据库，成功则允许锁定，不成功则不允许锁定
                Boolean success = true;
                try {
                    FunctionProcessor.connDatabase(ip, port,db_odbc, username, password, databaseType);
                } catch (Exception ex) {
                    success = false;
                    nodeButton.setDisable(true);
                    funField.setDisable(true);
                    dirButton.setDisable(true);
                    // 创建一个提示框
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("锁定连接");
                    alert.setHeaderText(null);
                    alert.setContentText("  \r 锁定失败: 测试连接不通过！");
                    // 显示提示框
                    alert.showAndWait();
                } finally {
                    FunctionProcessor.closeConn();
                }
                if (success) {
                    System.out.println("锁定数据库连接！");
                    comboBox.setDisable(true);
                    ipField.setDisable(true);
                    portTextField.setDisable(true);
                    dbTextField.setDisable(true);
                    userTextField.setDisable(true);
                    pwdTextField.setDisable(true);
                    lockStatus = true;
                    lockButton.setText("取消锁定");
                    nodeButton.setDisable(false);
                    funField.setDisable(false);
                    dirButton.setDisable(false);
                    // 创建一个提示框
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("锁定连接");
                    alert.setHeaderText(null);
                    alert.setContentText("  \r 锁定成功！");

                    // 显示提示框
                    alert.showAndWait();
                }

            } else {
                System.out.println("取消数据库连接！");
                lockStatus = false;
                comboBox.setDisable(false);
                ipField.setDisable(false);
                portTextField.setDisable(false);
                dbTextField.setDisable(false);
                userTextField.setDisable(false);
                pwdTextField.setDisable(false);
                lockButton.setText("锁定数据库");
                nodeButton.setDisable(true);
                funField.setDisable(true);
                dirButton.setDisable(true);
                // 创建一个提示框
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("取消锁定连接");
                alert.setHeaderText(null);
                alert.setContentText("  \r 取消锁定成功！");

                // 显示提示框
                alert.showAndWait();

            }
        });
        buttonBox.getChildren().addAll(connectButton, lockButton);


        // 将所有组件添加到 VBox 中
        vbox.getChildren().addAll( titleBox, databaseBox, ipBox, portBox, dbBox, userBox, pwdBox, buttonBox, funBox, dirBox);
        borderPane.setCenter(vbox);


    }
}

