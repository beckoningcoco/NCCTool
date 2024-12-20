package com.zht.testjavafx2.ui;

import com.zht.testjavafx2.func.FunctionProcessor;
import com.zht.testjavafx2.func.SQLScriptFuncProcessor;
import com.zht.testjavafx2.vo.DBConnVO;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//导出sql脚本工具的界面
@SuppressWarnings("all")
public class SQLScriptScenceCreator {

    private static DBConnVO nowDbConnVO = null;

    private static TextField dbStatus = null;
    private static Boolean connStatus = false;

    public static void createScence(Stage stage, BorderPane borderPane) {

        // 使用 VBox，间距为 20
        VBox vbox = new VBox(15);
        // 居中对齐
        vbox.setAlignment(Pos.TOP_CENTER);

        //标题行
        HBox titleBox = new HBox();
        Label titleLabel = new Label(" N C C 脚本 导 出 工 具 ");
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
                    nowDbConnVO = dbConnVO;
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
                if (dbConnVO == null) {
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
            } finally {
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

        //导出业务插件脚本
        HBox listenerBox = new HBox(5);
        Label listenerLabel = new Label(" 导出业务插件 ");
        listenerLabel.setStyle(" -fx-pref-width: 110px;-fx-padding: 5;-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: 'Tahoma';");
        TextField listenerField = new TextField();
        listenerField.setTranslateY(2);
        listenerField.setPrefHeight(24);
        listenerField.setPrefWidth(200);
        listenerField.setPromptText("插件全类名");

        // 创建一个新的 CheckBox
        CheckBox checkBox = new CheckBox("导出事件源");
        checkBox.setTranslateY(5);
        checkBox.setTranslateX(10);
        // 创建一个 BooleanProperty 来存储 CheckBox 的状态
        BooleanProperty selectedProperty = new SimpleBooleanProperty();

        // 将 CheckBox 的 selected 属性与 BooleanProperty 进行双向绑定
        checkBox.selectedProperty().bindBidirectional(selectedProperty);

        // 当 CheckBox 状态改变时更新控制台输出
        selectedProperty.addListener((observable, oldValue, newValue) -> {
            System.out.println("CheckBox 状态已更改至: " + newValue);
        });

        //导出脚本到桌面
        Button execButton = new Button("导出至桌面");
        execButton.setTranslateX(25);
        execButton.setStyle(
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
        execButton.setOnAction(e -> {
            try {
                // 根据业务插件全类名，导出sql脚本到桌面
                if (StringUtils.isBlank(listenerField.getText())) {
                    return;
                }
                SQLScriptFuncProcessor.exceListenerSql(listenerField.getText(), checkBox.isSelected(), nowDbConnVO);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("信息提示");
                alert.setContentText("导出脚本成功！");
                alert.showAndWait();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("信息提示");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
                ex.fillInStackTrace();
            } finally {
                FunctionProcessor.closeConn();
            }
        });

        // 创建一个水平分割线
        Separator separator2 = new Separator();

        //导出自定义档案
        HBox defdocBox = new HBox(5);
        Label defdocLabel = new Label(" 导出自定义档案 ");
        defdocLabel.setStyle(" -fx-pref-width: 120px;-fx-padding: 5;-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: 'Tahoma';");
        TextField defdocField = new TextField();
        defdocField.setTranslateY(2);
        defdocField.setPrefHeight(24);
        defdocField.setPrefWidth(120);
        defdocField.setPromptText("自定义档案List编码");

        //导出自定义档案脚本到桌面
        Button execdefButton = new Button("导出至桌面");
        execdefButton.setTranslateX(25);
        execdefButton.setStyle(
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
        execdefButton.setOnAction(e -> {

            try {
                if (StringUtils.isBlank(defdocField.getText())) {
                    return;
                }
                SQLScriptFuncProcessor.execDefdocSQL(defdocField.getText(), nowDbConnVO);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("信息提示");
                alert.setContentText("导出脚本成功！");
                alert.showAndWait();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("信息提示");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
                ex.fillInStackTrace();
            } finally {
                FunctionProcessor.closeConn();
            }

        });

        // 创建一个水平分割线
        Separator separator3 = new Separator();

        //导出自定义档案
        HBox pluginBox = new HBox(5);
        Label pluginLabel = new Label(" 导出后台任务 ");
        pluginLabel.setStyle(" -fx-pref-width: 120px;-fx-padding: 5;-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: 'Tahoma';");
        TextField pluginField = new TextField();
        pluginField.setTranslateY(2);
        pluginField.setPrefHeight(24);
        pluginField.setPrefWidth(120);
        pluginField.setPromptText("后台任务全类名");

        //导出自定义档案脚本到桌面
        Button execpluButton = new Button("导出至桌面");
        execpluButton.setTranslateX(25);
        execpluButton.setStyle(
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
        execpluButton.setOnAction(e -> {
            try {
                if (StringUtils.isBlank(pluginField.getText())) {
                    return;
                }
                SQLScriptFuncProcessor.execPluginsSQL(pluginField.getText(), nowDbConnVO);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("信息提示");
                alert.setContentText("导出脚本成功！");
                alert.showAndWait();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("信息提示");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
                ex.fillInStackTrace();
            } finally {
                FunctionProcessor.closeConn();
            }

        });
        // 创建一个水平分割线
        Separator separator4 = new Separator();

        // 生成扩展字段脚本
        HBox extendBox = new HBox(5);
        Label extendLabel = new Label(" 生成扩表字段语句 ");
        extendLabel.setStyle(" -fx-pref-width: 140px;-fx-padding: 5;-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: 'Tahoma';");
        TextField extendField = new TextField();
        extendField.setTranslateY(2);
        extendField.setPrefHeight(24);
        extendField.setPrefWidth(100);
        extendField.setPromptText("表名");

        TextField extendfiledField = new TextField();
        extendfiledField.setTranslateY(2);
        extendfiledField.setPrefHeight(24);
        extendfiledField.setPrefWidth(100);
        extendfiledField.setPromptText("字段名");

        TextField lengthfiledField = new TextField();
        lengthfiledField.setTranslateY(2);
        lengthfiledField.setPrefHeight(24);
        lengthfiledField.setPrefWidth(100);
        lengthfiledField.setPromptText("长度");

        //导出扩展字段脚本
        Button extendButton = new Button("导出至桌面");
        extendButton.setTranslateX(25);
        extendButton.setStyle(
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
        extendButton.setOnAction(e -> {
            try {
                if (StringUtils.isBlank(extendField.getText()) || StringUtils.isBlank(extendfiledField.getText()) || StringUtils.isBlank(lengthfiledField.getText())) {
                    return;
                }
                SQLScriptFuncProcessor.execExtendLengthSQL();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("信息提示");
                alert.setContentText("导出脚本成功！");
                alert.showAndWait();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("信息提示");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
                ex.fillInStackTrace();
            } finally {
                FunctionProcessor.closeConn();
            }

        });

        // 创建一个水平分割线
        Separator separator5 = new Separator();

        // 导出单据转换规则
        HBox ruleBox = new HBox(5);
        Label ruleLabel = new Label(" 导出单据转换规则 ");
        ruleLabel.setStyle(" -fx-pref-width: 140px;-fx-padding: 5;-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: 'Tahoma';");
        TextField ruleSrcBillTypeField = new TextField();
        ruleSrcBillTypeField.setTranslateY(2);
        ruleSrcBillTypeField.setPrefHeight(24);
        ruleSrcBillTypeField.setPrefWidth(90);
        ruleSrcBillTypeField.setPromptText("来源单据类型");

        TextField ruleSrcTradeTypeField = new TextField();
        ruleSrcTradeTypeField.setTranslateY(2);
        ruleSrcTradeTypeField.setPrefHeight(24);
        ruleSrcTradeTypeField.setPrefWidth(90);
        ruleSrcTradeTypeField.setPromptText("来源交易类型");

        TextField ruleTarBillTypeField = new TextField();
        ruleTarBillTypeField.setTranslateY(2);
        ruleTarBillTypeField.setPrefHeight(24);
        ruleTarBillTypeField.setPrefWidth(90);
        ruleTarBillTypeField.setPromptText("目的单据类型");

        TextField ruleTarTradeTypeField = new TextField();
        ruleTarTradeTypeField.setTranslateY(2);
        ruleTarTradeTypeField.setPrefHeight(24);
        ruleTarTradeTypeField.setPrefWidth(90);
        ruleTarTradeTypeField.setPromptText("目的单据类型");


        //导出单据转换规则
        Button ruleButton = new Button("导出至桌面");
        ruleButton.setTranslateX(25);
        ruleButton.setStyle(
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
        ruleButton.setOnAction(e -> {

        });

        // 创建一个水平分割线
        Separator Lastseparator = new Separator();

        ruleBox.getChildren().addAll(ruleLabel, ruleSrcBillTypeField, ruleSrcTradeTypeField, ruleTarBillTypeField, ruleTarTradeTypeField, ruleButton);
        extendBox.getChildren().addAll(extendLabel, extendField, extendfiledField, extendButton);
        pluginBox.getChildren().addAll(pluginLabel, pluginField, execpluButton);
        defdocBox.getChildren().addAll(defdocLabel, defdocField, execdefButton);
        listenerBox.getChildren().addAll(listenerLabel, listenerField, checkBox, execButton);
        databaseBox.getChildren().addAll(databaseLabel, databaseField, addDBButton, loadDBButton, dbstatusLabel, dbstatusField);
        // 将所有组件添加到 VBox 中
        vbox.getChildren().addAll(titleBox, databaseBox, separator, listenerBox, separator2, defdocBox, separator3, pluginBox, separator4, extendBox, separator5, ruleBox, Lastseparator);
        borderPane.setCenter(vbox);
    }


}
