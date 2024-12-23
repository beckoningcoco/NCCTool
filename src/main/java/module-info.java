module com.zht.testjavafx2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.swing;
    requires javafx.web;
    requires java.sql;
    requires java.sql.rowset; // 添加这一行以包含 rowset 模块
    requires org.apache.commons.lang3;
    requires com.google.gson;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.dataformat.xml;
    requires DmJdbcDriver18;
    requires org.apache.poi.ooxml;
    requires org.apache.xmlbeans;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml.schemas;
    requires org.apache.poi.xwpf.converter.core;
    requires org.apache.poi.xwpf.converter.xhtml;
    requires org.apache.commons.codec;
    requires java.base;

    // JavaFX modules

    // 导出 vo 包给所有模块
    exports com.zht.testjavafx2.vo;

    // 打开 vo 包给 jackson.databind 模块
    opens com.zht.testjavafx2.vo to com.fasterxml.jackson.databind;

    opens com.zht.testjavafx2 to javafx.fxml;
    exports com.zht.testjavafx2;
    exports com.zht.testjavafx2.ui;
    opens com.zht.testjavafx2.ui to javafx.fxml;
    exports com.zht.testjavafx2.func;
    opens com.zht.testjavafx2.func to javafx.fxml;
}