package com.zht.testjavafx2.ui;

import com.zht.testjavafx2.func.FunctionProcessor;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

@SuppressWarnings("all")
public class UICreator {

    private static Stage primaryStage;
    private static Scene exportNodescence, openapiScence ,SQLScriptScence,testNetPayScence;
    private static MenuBar menuBar ;

    public void ctrateUI(Stage stage){
        primaryStage = stage ;
        // 创建菜单
        menuBar = new MenuBar();
        Menu menu = new Menu("工具");
        MenuItem switchToScene1 = new MenuItem("导出新建节点脚本");
        MenuItem switchToScene2 = new MenuItem("测试OPENAPI");
        MenuItem switchToScene3 = new MenuItem("导出脚本工具");
        MenuItem switchToScene4 = new MenuItem("模拟网上支付工具");

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

        switchToScene3.setOnAction(e -> {
            //创建  导出脚本工具的界面
            BorderPane sqlscriptPane = new BorderPane();
            SQLScriptScenceCreator.createScence(stage,sqlscriptPane);
            sqlscriptPane.setTop(menuBar);
            SQLScriptScence = new Scene(sqlscriptPane, 800, 600);
            primaryStage.setScene(SQLScriptScence);
            System.out.println("切换菜单 SQLScriptScence ");
        });

        switchToScene4.setOnAction(e -> {
            //创建  模拟网上支付 工具的 界面
            BorderPane netPayPane = new BorderPane();
            NetPayTestScenceCreator.createScence(stage,netPayPane);
            netPayPane.setTop(menuBar);
            testNetPayScence = new Scene(netPayPane, 800, 600);
            primaryStage.setScene(testNetPayScence);
            System.out.println("切换菜单 testNetPayScence ");
        });

        otherItem1.setOnAction(e -> {
            System.out.println("可扩展按钮1 otherItem1 ");
        });
        otherItem2.setOnAction(e -> {
            System.out.println("可扩展按钮2 otherItem1 ");
        });

        menu.getItems().addAll(switchToScene1, switchToScene2,switchToScene3,switchToScene4);
        otherMenu.getItems().addAll(otherItem1, otherItem2);

        //说明文档
        Menu useMenu = new Menu("说明文档");
        MenuItem useItem1 = new MenuItem("查看说明文档");
        useMenu.getItems().add(useItem1);
        useItem1.setOnAction(event -> {
            Scene scene = FunctionProcessor.processUseDoc();
            primaryStage.setScene(scene);
        });
        menuBar.getMenus().addAll(menu,otherMenu,useMenu);

        //创建  模拟网上支付 工具的界面
        BorderPane netPayPane = new BorderPane();
        NetPayTestScenceCreator.createScence(stage,netPayPane);
        netPayPane.setTop(menuBar);
        testNetPayScence = new Scene(netPayPane, 800, 600);

        // 设置初始场景
        primaryStage.setTitle("NCC开发工具");
        primaryStage.setScene(testNetPayScence);

        primaryStage.show();

    }

}
