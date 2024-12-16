package com.zht.testjavafx2;

import com.zht.testjavafx2.ui.UICreator;
import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        //构建窗口
        UICreator uiCreator = new UICreator();
        uiCreator.ctrateUI(stage);
    }


    public static void main(String[] args) {
        launch();
    }
}