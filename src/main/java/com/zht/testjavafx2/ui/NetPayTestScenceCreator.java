package com.zht.testjavafx2.ui;

import com.zht.testjavafx2.func.FunctionProcessor;
import com.zht.testjavafx2.func.SQLScriptFuncProcessor;
import com.zht.testjavafx2.func.NetPayTestFuncProcessor;
import com.zht.testjavafx2.vo.DBConnVO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;


@SuppressWarnings("all")
public class NetPayTestScenceCreator {

    private static DBConnVO nowDbConnVO = null;
    private static TextField dbStatus = null;
    private static Boolean connStatus = false;

    private static TextArea requrl = null ;

    private  static  TextField ipfiled = null ;

    public static void createScence(Stage stage, BorderPane netPayPane) {

        // 使用 VBox，间距为 20
        VBox vbox = new VBox(15);
        // 居中对齐
        vbox.setAlignment(Pos.TOP_CENTER);

        //标题行
        HBox titleBox = new HBox();
        Label titleLabel = new Label(" N C C 模拟网上支付 工具 ");
        titleLabel.setStyle(" -fx-pref-width: 300px;-fx-padding: 5;-fx-font-size: 20px; -fx-font-weight: bold; -fx-font-family: 'Tahoma';");
        titleLabel.setTranslateX(250);
        titleBox.getChildren().add(titleLabel);

        //数据库连接行
        HBox databaseBox = new HBox(10);
        databaseBox.setTranslateX(5);
        Label databaseLabel = new Label(" 连接数据库 ");
        databaseLabel.setStyle(" -fx-pref-width: 100px;-fx-padding: 5;-fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'Tahoma';");
        TextField databaseField = new TextField();
        // 添加文本变化监听器
        databaseField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //文本
                System.out.println("数据库连接变更为:" + newValue);
            }
        });
        databaseField.setTranslateY(3);
        databaseField.setPrefHeight(26);
        databaseField.setPromptText("选择数据源");
        databaseField.setEditable(false);
        //新建连接
        Button addDBButton = new Button("新建连接");
        addDBButton.setStyle(
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
        addDBButton.setOnAction(e -> {

            //创建  新增界面
            DBConnVO dbConnVO = SQLScriptFuncProcessor.ctrateGrid();
            if (StringUtils.isNotBlank(dbConnVO.getConnName())) {
                //设置文本框选中当前数据源
                databaseField.setText(dbConnVO.getConnName());
                //测试连接是否通过
                try {
                    FunctionProcessor.connDatabase(dbConnVO.getIp(), dbConnVO.getPort(), dbConnVO.getServername(), dbConnVO.getUsername(), dbConnVO.getPassword(), dbConnVO.getDbType());
                    nowDbConnVO = dbConnVO ;
                    dbStatus.setText("成功");
                } catch (Exception ex) {
                    dbStatus.setText("失败");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("信息提示");
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                    ex.fillInStackTrace();
                }
            }
        });


        //加载历史连接
        Button loadDBButton = new Button("加载历史连接");
        loadDBButton.setStyle(
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
        loadDBButton.setOnAction(e -> {
            try {
                DBConnVO dbConnVO = SQLScriptFuncProcessor.ctrateLoadGrid();
                if(dbConnVO==null){
                    return;
                }
                //不为空
                databaseField.setText(dbConnVO.getConnName());
                //测试数据库连接
                FunctionProcessor.connDatabase(dbConnVO.getIp(), dbConnVO.getPort(), dbConnVO.getServername(), dbConnVO.getUsername(), dbConnVO.getPassword(), dbConnVO.getDbType());
                nowDbConnVO = dbConnVO;
                dbStatus.setText("成功");
            } catch (Exception ex) {
                dbStatus.setText("失败");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("信息提示");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
                ex.fillInStackTrace();
            }finally {
                FunctionProcessor.closeConn();
            }
        });

        //连接状态
        Label dbstatusLabel = new Label(" 连接状态 ");
        dbstatusLabel.setStyle(" -fx-pref-width: 100px;-fx-padding: 5;-fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'Tahoma';");
        TextField dbstatusField = new TextField();
        dbStatus = dbstatusField;
        dbstatusField.setTranslateY(3);
        dbstatusField.setTranslateX(-20);
        dbstatusField.setPrefHeight(26);
        dbstatusField.setPrefWidth(50);
        dbstatusField.setPromptText("失败");
        dbstatusField.setEditable(false);

        dbstatusField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //文本
                if ("成功".equals(newValue)) {
                    connStatus = true;
                } else {
                    connStatus = false;
                }
                System.out.println("数据库连接状态:" + connStatus);
            }
        });


        // 创建一个水平分割线
        Separator separator = new Separator();

        HBox ipBox = new HBox(10);
        ipBox.setTranslateX(5);
        Label ipLabel = new Label(" ip:port ");
        ipLabel.setStyle(" -fx-pref-width: 120px;-fx-padding: 5;-fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'Tahoma';");
        TextField ipField = new TextField();
        ipfiled = ipField ;

        ipField.setTranslateY(1);
        ipField.setPrefHeight(20);
        ipField.setPrefWidth(180);
        ipField.setPromptText("开发/测试环境ip");

        // 根据结算单号 进行模拟下载支付成功和支付失败
        HBox netTestBox = new HBox(10);
        netTestBox.setTranslateX(5);
        Label netTestLabel = new Label(" 输入结算单号 ");
        netTestLabel.setStyle(" -fx-pref-width: 120px;-fx-padding: 5;-fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'Tahoma';");
        TextField netTestField = new TextField();

        netTestField.setTranslateY(1);
        netTestField.setPrefHeight(28);
        netTestField.setPrefWidth(220);
        netTestField.setPromptText("结算单号");
        // 支付成功
        Button netTestButton = new Button("支付成功");
        netTestButton.setStyle(
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
        netTestButton.setOnAction(e -> {
            try{
                String url = NetPayTestFuncProcessor.postSuccessSend(netTestField.getText(), nowDbConnVO,ipfiled.getText());
                requrl.setText(url);
            }catch (Exception ex){
                ex.fillInStackTrace();
            }finally {
                FunctionProcessor.closeConn();
            }
        });

        // 支付失败
        Button netTestButton2 = new Button("支付失败");
        netTestButton2.setStyle(
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
        netTestButton2.setOnAction(e -> {
            try{
                String url = NetPayTestFuncProcessor.postFailSend(netTestField.getText(), nowDbConnVO,ipfiled.getText());
                requrl.setText(url);
            }catch (Exception ex){
                ex.fillInStackTrace();
            }finally {
                FunctionProcessor.closeConn();
            }
        });
        HBox requrlBox = new HBox(10);
        // 创建一个 JTextArea 用于多行文本输入
        TextArea textArea = new TextArea();
        requrl = textArea ;
        textArea.setTranslateX(130);
        textArea.setPromptText("调用url");
        textArea.setPrefHeight(200);
        textArea.setWrapText(true); // 允许文本换行


        requrlBox.getChildren().addAll(textArea);
        ipBox.getChildren().addAll(ipLabel,ipField);
        netTestBox.getChildren().addAll(netTestLabel,netTestField,netTestButton,netTestButton2);
        databaseBox.getChildren().addAll(databaseLabel, databaseField, addDBButton, loadDBButton, dbstatusLabel, dbstatusField);
        vbox.getChildren().addAll(titleBox,databaseBox,separator,ipBox,netTestBox,requrlBox);
        netPayPane.setCenter(vbox);
    }
}
