package com.zht.testjavafx2.ui;

import com.zht.testjavafx2.func.OpenApiFuncProcessor;
import com.zht.testjavafx2.vo.AppItemVO;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("all")
public class OpenApiTestScenceCreator {
    private static String nccVersion = "2105";
    //存放  应用名 --> 应用信息VO的Map
    private static Map<String, AppItemVO> nccAppMap = null;

    //当前选择的应用
    private static String selectedAppName = null;

    //锁定按钮的状态
    private static Boolean lockStatus = false;

    //当前调用 ip
    private static String selectedIp = null;
    //当前的调用路径
    private static String selectedUrl = null;

    private static Button commitBtn = null;

    private static TextArea resText = null;

    private static ComboBox ipComBox = null;

    private static ComboBox urlComBoxr = null;

    private static Button addIpBtn = null;
    private static Button delIpBtn = null;
    private static Button addUrlBtn = null;
    private static Button delUrlBtn = null;

    public static void createScence(Stage stage, BorderPane borderPane) {

        // 使用 VBox，间距为 20
        VBox vbox = new VBox(15);
        // 居中对齐
        vbox.setAlignment(Pos.TOP_CENTER);


        //标题行
        HBox titleBox = new HBox();
        Label titleLabel = new Label(" OPENAPI 测 试 工 具 ");
        titleLabel.setStyle(" -fx-pref-width: 300px;-fx-padding: 5;-fx-font-size: 20px; -fx-font-weight: bold; -fx-font-family: 'Tahoma';");
        titleLabel.setTranslateX(250);
        titleLabel.setTranslateY(10);
        titleBox.getChildren().add(titleLabel);


        //第一行 : 选择版本 ， 选择应用 ， 修改当前应用 ，  新增应用
        HBox firstBox = new HBox(5);
        firstBox.setTranslateY(10);
        Label choseVersion = new Label(" NCC 版本 ");
        choseVersion.setTranslateX(20);
        choseVersion.setStyle(" -fx-pref-width: 100px;-fx-padding: 2;-fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'Tahoma';");
        // 选择版本的下拉列表
        // 创建 ComboBox 并设置提示文本
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setTranslateX(5);
        // 设置ComboBox的大小
        comboBox.setPrefWidth(100);  // 设置首选宽度为200
        comboBox.setPrefHeight(26);   // 设置首选高度为40
        comboBox.setPromptText(nccVersion);

        // 创建数据源
        ObservableList<String> options = FXCollections.observableArrayList(
                "NCC2105"
        );

        // 设置 ComboBox 的项目
        comboBox.setItems(options);

        // 可选：设置默认选中的项
        comboBox.setValue("NCC2105");

        // 添加事件监听器
        comboBox.setOnAction(event -> {
            String selectedItem = comboBox.getValue();
            nccVersion = selectedItem;
            System.out.println("Selected NCCVersion: " + selectedItem);
        });

        // 选择应用 /项目 /工程
        Label choseApp = new Label(" 选择应用 ");
        choseApp.setTranslateX(20);
        choseApp.setStyle(" -fx-pref-width: 100px;-fx-padding: 2;-fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'Tahoma';");
        // 创建 ComboBox 并设置提示文本
        ComboBox<String> choseAppBox = new ComboBox<>();
        // 设置ComboBox的大小
        choseAppBox.setPrefWidth(120);  // 设置首选宽度为200
        choseAppBox.setPrefHeight(27);   // 设置首选高度为40
        choseAppBox.setPromptText("请选择应用");

        // 创建数据源
        ObservableList<String> choseAppOptions = FXCollections.observableArrayList();
        //获取注册过的所有的 NCCApp  封装进一个Map中，随取随用
        try {
            Map<String, AppItemVO> map = OpenApiFuncProcessor.accquireAllApp();
            nccAppMap = map;
            Set<String> set = map.keySet();
            choseAppOptions.addAll(set);
            if (choseAppOptions.size() > 0) {
                selectedAppName = nccAppMap.get(choseAppOptions.get(0)).getAppName();
                List<String> urlList = nccAppMap.get(choseAppOptions.get(0)).getUrlList();
                if (urlList != null && urlList.size() > 0) {
                    selectedUrl = urlList.get(0);
                }
                // --------------------
                List<String> ipList = nccAppMap.get(choseAppOptions.get(0)).getIpList();
                if (ipList != null && ipList.size() > 0) {
                    selectedIp = ipList.get(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 设置 ComboBox 的项目
        choseAppBox.setItems(choseAppOptions);

        // 可选：设置默认选中的项
        choseAppBox.setValue(choseAppOptions.size() == 0 ? null : choseAppOptions.get(0));

        // 添加事件监听器
        choseAppBox.setOnAction(event -> {
            String selectedItem = choseAppBox.getValue();
            if (selectedItem == null) {
                return;
            }
            selectedAppName = selectedItem;
            AppItemVO appItemVO = nccAppMap.get(selectedAppName);
            List<String> ipList = appItemVO.getIpList();
            List<String> urlList = appItemVO.getUrlList();
            ObservableList<String> ipOptions = FXCollections.observableArrayList(
                    ipList);
            ObservableList<String> urlOptions = FXCollections.observableArrayList(
                    urlList);
            ipComBox.setItems(ipOptions);
            if (ipOptions.size() > 0) {
                ipComBox.setValue(ipOptions.get(0));
                selectedIp = ipOptions.get(0);
            }
            // 设置 ComboBox 的项目
            urlComBoxr.setItems(urlOptions);
            // 可选：设置默认选中的项
            if (urlOptions != null && urlOptions.size() > 0) {
                urlComBoxr.setValue(urlOptions.get(0));
            } else {
                urlComBoxr.setValue(null);
            }

            System.out.println("Selected selectedAppName: " + selectedItem);
        });

        //新增APP按钮
        Button addButton = new Button("新增应用");
        addButton.setStyle(
                "-fx-background-color: #f1f1f1; " + // 蓝色背景
                        "-fx-text-fill: #242424; " + // 白色文本颜色
                        "-fx-font-size: 14px; " + // 字体大小
                        "-fx-padding: 4 12 4 12; " + // 内边距（上 右 下 左）
                        "-fx-border-radius: 5; " + // 圆角半径
                        "-fx-background-radius: 5; " + // 背景圆角半径
                        "-fx-border-width: 1; " + // 边框宽度
                        "-fx-border-color: black; " + // 无边框颜色
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 0); " + // 轻微阴影效果

                        // 悬停时的样式变化
                        "-fx-cursor: hand;" // 鼠标悬停时变为手型指针
        );

        addButton.setOnAction(e -> {
            try {
                //新增APP应用
                AppItemVO appItemVO = OpenApiFuncProcessor.addAppButton();
                Boolean flag = checkAppitemVO(appItemVO);
                //拿到界面上传来的ietmVO，将其转换为Xml,持久化到配置文件中
                if (flag) {
                    OpenApiFuncProcessor.makeVO2XML(appItemVO);
                    Map<String, AppItemVO> map = OpenApiFuncProcessor.accquireAllApp();
                    nccAppMap = map;
                    Set<String> set = map.keySet();
                    choseAppOptions.clear();
                    choseAppOptions.addAll(set);
                    choseAppBox.setValue(choseAppOptions.size() == 0 ? null : choseAppOptions.get(0));
                    addIpBtn.setDisable(false);
                    addUrlBtn.setDisable(false);
                    delUrlBtn.setDisable(false);
                    delIpBtn.setDisable(false);
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        //修改APP按钮
        Button editButton = new Button("修改当前应用");
        editButton.setStyle(
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

        editButton.setOnAction(e -> {
            try {
                //修改APP应用
                AppItemVO appItemVO = OpenApiFuncProcessor.editAppButton(nccAppMap.get(selectedAppName));
                Boolean flag = checkAppitemVO(appItemVO);
                //拿到界面上传来的ietmVO，将其转换为Xml,持久化到配置文件中
                if (flag) {
                    List<String> list = new ArrayList<>();
                    list.add(appItemVO.getSelectedIp());
                    appItemVO.setIpList(list);
                    OpenApiFuncProcessor.makeVO2XMLbyEdit(appItemVO);
                    Map<String, AppItemVO> map = OpenApiFuncProcessor.accquireAllApp();
                    nccAppMap = map;
                    Set<String> set = map.keySet();
                    choseAppOptions.clear();
                    choseAppOptions.addAll(set);
                    choseAppBox.setValue(choseAppOptions.size() == 0 ? null : choseAppOptions.get(0));
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        //删除APP按钮
        Button deleteButton = new Button("删除当前应用");
        deleteButton.setStyle(
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

        deleteButton.setOnAction(e -> {
            try {
                // 创建一个确认对话框
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("确认对话框");
                alert.setHeaderText(null);
                alert.setContentText("你确定要删除这个应用吗？");

                // 显示对话框并等待用户响应
                ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

                // 根据用户的选择执行相应的操作
                if (result == ButtonType.OK) {
                    // 用户点击了“确定”，执行功能
                    System.out.println("用户选择了确定，现在执行功能...");
                    //删除APP应用
                    OpenApiFuncProcessor.deleteAppButton(selectedAppName);
                    Map<String, AppItemVO> map = OpenApiFuncProcessor.accquireAllApp();
                    nccAppMap = map;
                    Set<String> set = map.keySet();
                    choseAppOptions.clear();
                    choseAppOptions.addAll(set);
                    if (choseAppOptions.size() > 0) {
                        selectedAppName = nccAppMap.get(choseAppOptions.get(0)).getAppName();
                        selectedUrl = nccAppMap.get(choseAppOptions.get(0)).getUrlList().get(0);
                        choseAppBox.setValue(choseAppOptions.size() == 0 ? null : choseAppOptions.get(0));
                    } else {
                        ipComBox.setItems(null);
                        urlComBoxr.setItems(null);
                        selectedIp = null;
                        selectedUrl = null;
                        addIpBtn.setDisable(true);
                        addUrlBtn.setDisable(true);
                        delIpBtn.setDisable(true);
                        delUrlBtn.setDisable(true);
                    }
                } else {
                    // 用户点击了“取消”或其他按钮，不执行功能
                    System.out.println("用户选择了取消，不会执行功能。");
                }

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        //调用接口要ip
        //调用接口地址
        HBox ipBox = new HBox(5);
        ipBox.setTranslateX(5);
        ipBox.setTranslateY(5);

        Label ipLabel = new Label(" 调用ip端口 ");
        ipLabel.setTranslateX(15);
        ipLabel.setStyle(" -fx-pref-width: 100px;-fx-padding: 2;-fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'Tahoma';");
        // 调用接口地址 的下拉列表
        // 创建 ComboBox 并设置提示文本
        ComboBox<String> ipComboBox = new ComboBox<>();
        ipComBox = ipComboBox;
        // 设置ComboBox的大小
        ipComboBox.setPrefWidth(300);  // 设置首选宽度为200
        ipComboBox.setPrefHeight(24);   // 设置首选高度为40
        ipComboBox.setPromptText("请选择一个应用");

        // 创建数据源
        AppItemVO appItemvo = nccAppMap.get(selectedAppName);
        ObservableList<String> ipOptions = null;
        if (appItemvo != null) {
            ipOptions = FXCollections.observableArrayList(
                    appItemvo.getIpList()
            );
        }

        // 设置 ComboBox 的项目
        ipComboBox.setItems(ipOptions);

        // 可选：设置默认选中的项
        if (ipOptions != null && ipOptions.size() > 0) {
            ipComboBox.setValue(ipOptions.get(0));
            selectedIp = ipOptions == null ? null : ipOptions.get(0);
        } else {
            if (appItemvo != null) {
                ipComboBox.setPromptText("请新增ip:port");
            }
            ipComboBox.setValue(null);
        }
        // 添加事件监听器
        ipComboBox.setOnAction(event -> {
            String selectedItem = ipComboBox.getValue();
            selectedIp = selectedItem;
            System.out.println("Selected selectedip: " + selectedItem);
        });

        //调用接口地址
        HBox urlBox = new HBox(0);
        urlBox.setTranslateX(20);
        urlBox.setTranslateY(5);

        Label urlLabel = new Label(" 调用路径 ");
        urlLabel.setStyle(" -fx-pref-width: 85px;-fx-padding: 2;-fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'Tahoma';");
        // 调用接口地址 的下拉列表
        // 创建 ComboBox 并设置提示文本
        ComboBox<String> urlComboBox = new ComboBox<>();
        urlComboBox.setTranslateX(5);
        urlComBoxr = urlComboBox;
        // 设置ComboBox的大小
        urlComboBox.setPrefWidth(400);  // 设置首选宽度为200
        urlComboBox.setPrefHeight(24);   // 设置首选高度为40
        urlComboBox.setPromptText("请选择一个应用");

        // 创建数据源
        AppItemVO appItemVO1 = nccAppMap.get(selectedAppName);
        ObservableList<String> urlOptions = null;
        if (appItemVO1 != null) {
            urlOptions = FXCollections.observableArrayList(
                    appItemVO1.getUrlList()
            );
        }

        // 设置 ComboBox 的项目
        urlComboBox.setItems(urlOptions);

        // 可选：设置默认选中的项
        if (urlOptions != null && urlOptions.size() > 0) {
            urlComboBox.setValue(urlOptions.get(0));
        } else {
            if (appItemVO1 != null) {
                urlComboBox.setPromptText("请新增调用地址");
            }
        }

        // 添加事件监听器
        urlComboBox.setOnAction(event -> {
            String selectedItem = urlComboBox.getValue();
            selectedUrl = selectedItem;
            System.out.println("Selected selectedUrl: " + selectedItem);
        });

        //新增 ip  删除ip   新增url 删除url
        Button addipButton = new Button("addIP");
        addIpBtn = addipButton;
        if (choseAppOptions == null || choseAppOptions.size() == 0) {
            addipButton.setDisable(true);
        }
        addipButton.setTranslateX(20);
        addipButton.setStyle(
                "-fx-background-color: #f1f1f1; " + // 蓝色背景
                        "-fx-text-fill: #242424; " + // 白色文本颜色
                        "-fx-font-size: 14px; " + // 字体大小
                        "-fx-padding: 2 10 2 10; " + // 内边距（上 右 下 左）
                        "-fx-border-radius: 5; " + // 圆角半径
                        "-fx-background-radius: 5; " + // 背景圆角半径
                        "-fx-border-width: 1; " + // 边框宽度
                        "-fx-border-color: black; " + // 无边框颜色
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 0); " + // 轻微阴影效果

                        // 悬停时的样式变化
                        "-fx-cursor: hand;" // 鼠标悬停时变为手型指针
        );

        addipButton.setOnAction(e -> {
            // 创建对话框
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("新增ip:port");
            dialog.setHeaderText("请输入 ip:port 地址：");

            // 设置对话框的结果转换器，将 OK 按钮与输入框的值关联
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    return ((TextField) dialog.getDialogPane().lookup(".text-field")).getText();
                }
                return null;
            });

            // 创建对话框的内容（GridPane 布局）
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            // 添加标签和文本框到 GridPane
            TextField ipTextField = new TextField();
            ipTextField.setPromptText("ip :port");
            grid.add(new Label("ip :port"), 0, 0);
            grid.add(ipTextField, 1, 0);

            // 将 GridPane 添加为对话框的内容
            dialog.getDialogPane().setContent(grid);

            // 添加 OK 和 Cancel 按钮
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            // 显示对话框并等待用户交互
            dialog.showAndWait().ifPresent(ipAddress -> {
                // 用户点击了“确定”，并且提供了有效的输入
                try {
                    OpenApiFuncProcessor.handleipAdd(selectedAppName, ipAddress);
                    Map<String, AppItemVO> map = OpenApiFuncProcessor.accquireAllApp();
                    nccAppMap = map;
                    Set<String> set = map.keySet();
                    choseAppOptions.clear();
                    choseAppOptions.addAll(set);
                    choseAppBox.setValue(choseAppOptions.size() == 0 ? null : choseAppOptions.get(0));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        });
        Button delipButton = new Button("delIP");
        delIpBtn = delipButton;
        if (choseAppOptions == null || choseAppOptions.size() == 0) {
            delipButton.setDisable(true);
        }
        delipButton.setTranslateX(20);
        delipButton.setStyle(
                "-fx-background-color: #f1f1f1; " + // 蓝色背景
                        "-fx-text-fill: #242424; " + // 白色文本颜色
                        "-fx-font-size: 14px; " + // 字体大小
                        "-fx-padding: 2 10 2 10;" + // 内边距（上 右 下 左）
                        "-fx-border-radius: 5; " + // 圆角半径
                        "-fx-background-radius: 5; " + // 背景圆角半径
                        "-fx-border-width: 1; " + // 边框宽度
                        "-fx-border-color: black; " + // 无边框颜色
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 0); " + // 轻微阴影效果

                        // 悬停时的样式变化
                        "-fx-cursor: hand;" // 鼠标悬停时变为手型指针
        );

        delipButton.setOnAction(e -> {
            // 创建一个确认对话框
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("确认对话框");
            alert.setHeaderText(null);
            alert.setContentText("你确定要删除这个ip吗？");

            // 显示对话框并等待用户响应
            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

            // 根据用户的选择执行相应的操作
            if (result == ButtonType.OK) {
                try {
                    OpenApiFuncProcessor.delIPButton(selectedAppName, selectedIp);
                    Map<String, AppItemVO> map = OpenApiFuncProcessor.accquireAllApp();
                    nccAppMap = map;
                    Set<String> set = map.keySet();
                    choseAppOptions.clear();
                    choseAppOptions.addAll(set);
                    if (choseAppOptions.size() > 0) {
                        selectedAppName = nccAppMap.get(choseAppOptions.get(0)).getAppName();
                        List<String> urlList = nccAppMap.get(choseAppOptions.get(0)).getUrlList();
                        if (urlList != null && urlList.size() > 0) {
                            selectedUrl = urlList.get(0);
                        }
                        List<String> ipList = nccAppMap.get(choseAppOptions.get(0)).getIpList();
                        if (ipList != null && ipList.size() > 0) {
                            selectedIp = ipList.get(0);
                        }
                        choseAppBox.setValue(choseAppOptions.size() == 0 ? null : choseAppOptions.get(0));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        Button addurlButton = new Button("addURL");
        addUrlBtn = addurlButton;
        if (choseAppOptions == null || choseAppOptions.size() == 0) {
            addurlButton.setDisable(true);
        }
        addurlButton.setTranslateX(20);
        addurlButton.setStyle(
                "-fx-background-color: #f1f1f1; " + // 蓝色背景
                        "-fx-text-fill: #242424; " + // 白色文本颜色
                        "-fx-font-size: 14px; " + // 字体大小
                        "-fx-padding: 2 10 2 10; " + // 内边距（上 右 下 左）
                        "-fx-border-radius: 5; " + // 圆角半径
                        "-fx-background-radius: 5; " + // 背景圆角半径
                        "-fx-border-width: 1; " + // 边框宽度
                        "-fx-border-color: black; " + // 无边框颜色
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 0); " + // 轻微阴影效果

                        // 悬停时的样式变化
                        "-fx-cursor: hand;" // 鼠标悬停时变为手型指针
        );

        addurlButton.setOnAction(e -> {

            // 创建对话框
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("新增调用路径");
            dialog.setHeaderText("请输入 调用路径：");

            // 设置对话框的结果转换器，将 OK 按钮与输入框的值关联
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    return ((TextField) dialog.getDialogPane().lookup(".text-field")).getText();
                }
                return null;
            });

            // 创建对话框的内容（GridPane 布局）
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            // 添加标签和文本框到 GridPane
            TextField urlTextField = new TextField();
            urlTextField.setPromptText("调用路径");
            grid.add(new Label("调用路径"), 0, 0);
            grid.add(urlTextField, 1, 0);

            // 将 GridPane 添加为对话框的内容
            dialog.getDialogPane().setContent(grid);

            // 添加 OK 和 Cancel 按钮
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            // 显示对话框并等待用户交互
            dialog.showAndWait().ifPresent(newUrl -> {
                // 用户点击了“确定”，并且提供了有效的输入
                try {
                    OpenApiFuncProcessor.handleurlAdd(selectedAppName, newUrl);
                    Map<String, AppItemVO> map = OpenApiFuncProcessor.accquireAllApp();
                    nccAppMap = map;
                    Set<String> set = map.keySet();
                    choseAppOptions.clear();
                    choseAppOptions.addAll(set);
                    choseAppBox.setValue(choseAppOptions.size() == 0 ? null : choseAppOptions.get(0));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        });
        Button delurlButton = new Button("delURL");
        delUrlBtn = delurlButton;
        if (choseAppOptions == null || choseAppOptions.size() == 0) {
            delurlButton.setDisable(true);
        }
        delurlButton.setTranslateX(20);
        delurlButton.setStyle(
                "-fx-background-color: #f1f1f1; " + // 蓝色背景
                        "-fx-text-fill: #242424; " + // 白色文本颜色
                        "-fx-font-size: 14px; " + // 字体大小
                        "-fx-padding: 2 10 2 10; " + // 内边距（上 右 下 左）
                        "-fx-border-radius: 5; " + // 圆角半径
                        "-fx-background-radius: 5; " + // 背景圆角半径
                        "-fx-border-width: 1; " + // 边框宽度
                        "-fx-border-color: black; " + // 无边框颜色
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 0); " + // 轻微阴影效果

                        // 悬停时的样式变化
                        "-fx-cursor: hand;" // 鼠标悬停时变为手型指针
        );

        delurlButton.setOnAction(e -> {
            // 创建一个确认对话框
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("确认对话框");
            alert.setHeaderText(null);
            alert.setContentText("你确定要删除这个路径吗？");

            // 显示对话框并等待用户响应
            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

            // 根据用户的选择执行相应的操作
            if (result == ButtonType.OK) {
                try {
                    OpenApiFuncProcessor.delUrlButton(selectedAppName, selectedUrl);
                    Map<String, AppItemVO> map = OpenApiFuncProcessor.accquireAllApp();
                    nccAppMap = map;
                    Set<String> set = map.keySet();
                    choseAppOptions.clear();
                    choseAppOptions.addAll(set);
                    if (choseAppOptions.size() > 0) {
                        selectedAppName = nccAppMap.get(choseAppOptions.get(0)).getAppName();
                        List<String> urlList = nccAppMap.get(choseAppOptions.get(0)).getUrlList();
                        if (urlList != null && urlList.size() > 0) {
                            selectedUrl = urlList.get(0);
                        }
                        List<String> ipList = nccAppMap.get(choseAppOptions.get(0)).getIpList();
                        if (ipList != null && ipList.size() > 0) {
                            selectedIp = ipList.get(0);
                        } else {
                            selectedIp = null;
                        }
                        choseAppBox.setValue(choseAppOptions.size() == 0 ? null : choseAppOptions.get(0));
                        addurlButton.setDisable(false);
                        addipButton.setDisable(false);
                        delurlButton.setDisable(false);
                        delipButton.setDisable(false);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        //锁定 应用和调用路径按钮
        Button lockButton = new Button("锁定");
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
            try {
                //锁定APP应用 和调用路径
                // FunctionProcessor.lockAppFunc();
                if (!lockStatus) {
                    //锁定状态
                    lockStatus = true;
                    addButton.setDisable(true);
                    editButton.setDisable(true);
                    deleteButton.setDisable(true);
                    lockButton.setText("取消锁定");
                    addipButton.setDisable(true);
                    addurlButton.setDisable(true);
                    delurlButton.setDisable(true);
                    delipButton.setDisable(true);
                    comboBox.setDisable(true);
                    choseAppBox.setDisable(true);
                    ipComboBox.setDisable(true);
                    urlComboBox.setDisable(true);
                    commitBtn.setDisable(false);
                } else {
                    lockStatus = false;
                    addipButton.setDisable(false);
                    addurlButton.setDisable(false);
                    delurlButton.setDisable(false);
                    delipButton.setDisable(false);
                    addButton.setDisable(false);
                    editButton.setDisable(false);
                    deleteButton.setDisable(false);
                    lockButton.setText("锁定");
                    comboBox.setDisable(false);
                    choseAppBox.setDisable(false);
                    ipComboBox.setDisable(false);
                    urlComboBox.setDisable(false);
                    commitBtn.setDisable(true);
                }
                String str= "chosApp:" + selectedAppName  + "selected ip: " + selectedIp  + "selected url " + selectedUrl ;
                System.out.println(str);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        HBox jsonBox = new HBox(5);
        jsonBox.setTranslateX(40);
        jsonBox.setTranslateY(5);
        // 创建一个TextArea，设置行数和列数
        TextArea jsonTextArea = new TextArea();
        jsonTextArea.setPromptText("请输入 JSON 报文");
        jsonTextArea.setPrefHeight(250);
        jsonTextArea.setWrapText(true); // 允许文本换行

        VBox vBox = new VBox(25);

        //格式化JSON
        Button getTokenButton = new Button("获取token");
        getTokenButton.setTranslateX(20);
        getTokenButton.setStyle(
                "-fx-background-color: #eae9e9; " + // 蓝色背景
                        "-fx-text-fill: #535353; " + // 白色文本颜色
                        "-fx-font-size: 14px; " + // 字体大小
                        "-fx-padding: 4 15 4 15; " + // 内边距（上 右 下 左）
                        "-fx-border-radius: 5; " + // 圆角半径
                        "-fx-background-radius: 5; " + // 背景圆角半径
                        "-fx-border-width: 1; " + // 边框宽度
                        "-fx-border-color: #519f00; " + // 无边框颜色
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 0); " + // 轻微阴影效果

                        // 悬停时的样式变化
                        "-fx-cursor: hand;" // 鼠标悬停时变为手型指针
        );

        getTokenButton.setOnAction(e -> {
            try {

                // 禁用按钮以防止重复点击
                getTokenButton.setDisable(true);
                getTokenButton.setText("勿频繁获取");
                // 创建一个 PauseTransition 来设置冷却时间（例如 5 秒）
                PauseTransition cooldown = new PauseTransition(Duration.seconds(5));
                cooldown.setOnFinished(l -> {

                    // 冷却时间结束后重新启用按钮
                    getTokenButton.setDisable(false);
                    getTokenButton.setText("获取token");
                    System.out.println("按钮现在可以再次点击了！");

                });

                // 启动冷却计时
                cooldown.play();
                //获取token
                AppItemVO appItemVO = nccAppMap.get(selectedAppName);
                appItemVO.setSelectedIp(selectedIp);
                String token = OpenApiFuncProcessor.getTokenForOpenAPI(appItemVO);
                if (StringUtils.isNotBlank(token)) {
                    resText.setText("token:" + token);
                } else {
                    resText.setText("获取token失败！");
                }
            } catch (Exception ex) {
                resText.setText(ex.getMessage());
                throw new RuntimeException(ex);
            }
        });

        //格式化JSON
        Button formatButton = new Button("格式化");
        formatButton.setTranslateX(20);
        formatButton.setStyle(
                "-fx-background-color: #eae9e9; " + // 蓝色背景
                        "-fx-text-fill: #535353; " + // 白色文本颜色
                        "-fx-font-size: 14px; " + // 字体大小
                        "-fx-padding: 4 15 4 15; " + // 内边距（上 右 下 左）
                        "-fx-border-radius: 5; " + // 圆角半径
                        "-fx-background-radius: 5; " + // 背景圆角半径
                        "-fx-border-width: 1; " + // 边框宽度
                        "-fx-border-color: #fcb8b8; " + // 无边框颜色
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 0); " + // 轻微阴影效果

                        // 悬停时的样式变化
                        "-fx-cursor: hand;" // 鼠标悬停时变为手型指针
        );

        formatButton.setOnAction(e -> {
            try {
                //格式化json
                OpenApiFuncProcessor.formatAppFunc(jsonTextArea);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        // 发送请求
        Button commitButton = new Button("发送");
        commitBtn = commitButton;
        commitButton.setTranslateX(20);
        commitButton.setDisable(true);
        commitButton.setStyle(
                "-fx-background-color: #eae9e9; " + // 蓝色背景
                        "-fx-text-fill: #535353; " + // 白色文本颜色
                        "-fx-font-size: 14px; " + // 字体大小
                        "-fx-padding: 4 15 4 15; " + // 内边距（上 右 下 左）
                        "-fx-border-radius: 5; " + // 圆角半径
                        "-fx-background-radius: 5; " + // 背景圆角半径
                        "-fx-border-width: 1; " + // 边框宽度
                        "-fx-border-color: #b6e3ff; " + // 无边框颜色
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 0); " + // 轻微阴影效果

                        // 悬停时的样式变化
                        "-fx-cursor: hand;" // 鼠标悬停时变为手型指针
        );

        commitButton.setOnAction(e -> {
            try {
                //格式化json
                resText.setText(null);
                AppItemVO appItemVO = nccAppMap.get(selectedAppName);
                appItemVO.setSelectedIp(selectedIp);
                String res = OpenApiFuncProcessor.commitAppFunc(nccAppMap.get(selectedAppName), jsonTextArea.getText(), selectedIp, selectedUrl);
                resText.setText(res);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });


        //响应报文 文本框
        HBox resBox = new HBox(5);
        resBox.setTranslateX(40);
        resBox.setTranslateY(2);
        // 创建一个TextArea，设置行数和列数
        TextArea resTextArea = new TextArea();
        resText = resTextArea;
        resTextArea.setEditable(false);
        resTextArea.setPromptText("响应报文");
        resTextArea.setPrefHeight(120);
        resTextArea.setWrapText(true); // 允许文本换行


        resBox.getChildren().addAll(resTextArea);
        vBox.getChildren().addAll(getTokenButton, formatButton, commitButton);
        jsonBox.getChildren().addAll(jsonTextArea, vBox);
        ipBox.getChildren().addAll(ipLabel, ipComboBox, addipButton, delipButton, addurlButton, delurlButton);
        urlBox.getChildren().addAll(urlLabel, urlComboBox, lockButton);
        firstBox.getChildren().addAll(choseVersion, comboBox, choseApp, choseAppBox, addButton, editButton, deleteButton);
        vbox.getChildren().addAll(titleBox, firstBox, ipBox, urlBox, jsonBox, resBox);
        borderPane.setCenter(vbox);
    }

    private static Boolean checkAppitemVO(AppItemVO appItemVO) {
        //当某些字段为空时，不会保存应用
        Boolean ret = false;
        if (StringUtils.isNotBlank(appItemVO.getAppName()) &&
                StringUtils.isNotBlank(appItemVO.getClientId()) &&
                StringUtils.isNotBlank(appItemVO.getUserName()) &&
                StringUtils.isNotBlank(appItemVO.getPwd()) &&
                StringUtils.isNotBlank(appItemVO.getClientSecret()) &&
                StringUtils.isNotBlank(appItemVO.getPubKey())
        ) {
            ret = true;
        }
        return ret;
    }


}
