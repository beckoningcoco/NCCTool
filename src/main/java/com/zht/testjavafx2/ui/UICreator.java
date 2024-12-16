package com.zht.testjavafx2.ui;

import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

@SuppressWarnings("all")
public class UICreator {

    private static Stage primaryStage;
    private static Scene exportNodescence, openapiScence;

    private static MenuBar menuBar ;

    public void ctrateUI(Stage stage){
        primaryStage = stage ;
        // 创建菜单
        menuBar = new MenuBar();
        Menu menu = new Menu("工具");
        MenuItem switchToScene1 = new MenuItem("导出新建节点脚本");
        MenuItem switchToScene2 = new MenuItem("测试OPENAPI");

        //扩展菜单项
        Menu otherMenu = new Menu("其他");
        MenuItem otherItem1 = new MenuItem("待扩展按钮1");
        MenuItem otherItem2 = new MenuItem("待扩展按钮2");
        // 设置菜单项的事件处理
        switchToScene1.setOnAction(e -> {
            //创建  导出节点工具的界面
            BorderPane borderPane = new BorderPane();
            ExportNodeScenceCreator.createScence(stage , borderPane);
            borderPane.setTop(menuBar);
            exportNodescence = new Scene(borderPane, 800, 600);
            primaryStage.setScene(exportNodescence);
            System.out.println("切换菜单 exportNodescence ");

        });
        switchToScene2.setOnAction(e -> {
            //创建  openapi工具的界面
            BorderPane openapiPane = new BorderPane();
            OpenApiTestScenceCreator.createScence(stage,openapiPane);
            openapiPane.setTop(menuBar);
            openapiScence = new Scene(openapiPane, 800, 600);
            primaryStage.setScene(openapiScence);
            System.out.println("切换菜单 openapiScence ");
        });

        otherItem1.setOnAction(e -> {
            System.out.println("可扩展按钮1 otherItem1 ");
        });
        otherItem2.setOnAction(e -> {
            System.out.println("可扩展按钮2 otherItem1 ");
        });

        menu.getItems().addAll(switchToScene1, switchToScene2);
        otherMenu.getItems().addAll(otherItem1, otherItem2);
        menuBar.getMenus().addAll(menu,otherMenu);

        //创建  openapi工具的界面
        BorderPane openapiPane = new BorderPane();
        OpenApiTestScenceCreator.createScence(stage,openapiPane);
        openapiPane.setTop(menuBar);
        openapiScence = new Scene(openapiPane, 800, 600);


        //创建  导出节点工具的界面
        /*
        BorderPane borderPane = new BorderPane();
        ExportNodeScenceCreator.createScence(stage , borderPane);
        borderPane.setTop(menuBar);
        exportNodescence = new Scene(borderPane, 800, 600);
        */

        // 设置初始场景
        primaryStage.setTitle("NCC开发工具");
        primaryStage.setScene(openapiScence);

        primaryStage.show();

    }

}
