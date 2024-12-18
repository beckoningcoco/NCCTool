package com.zht.testjavafx2.vo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@SuppressWarnings("all")
@JacksonXmlRootElement(localName = "conn")
public class DBConnVO {

    //连接名称
    private String connName ;

    //数据库类型
    private String dbType ;

    //服务名
    private String servername ;
    //ip
    private String ip ;
    //端口
    private String port ;

    //用户
    private String username ;

    private String password ;

    private String def1 ;
    private String def2 ;
    private String def3 ;
    private String def4 ;
    private String def5 ;


    @JacksonXmlProperty(localName = "connName")
    public String getConnName() {
        return connName;
    }

    public void setConnName(String connName) {
        this.connName = connName;
    }

    @JacksonXmlProperty(localName = "dbType")
    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    @JacksonXmlProperty(localName = "servername")
    public String getServername() {
        return servername;
    }

    public void setServername(String servername) {
        this.servername = servername;
    }
    @JacksonXmlProperty(localName = "ip")
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    @JacksonXmlProperty(localName = "port")
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
    @JacksonXmlProperty(localName = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JacksonXmlProperty(localName = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDef1() {
        return def1;
    }

    public void setDef1(String def1) {
        this.def1 = def1;
    }

    public String getDef2() {
        return def2;
    }

    public void setDef2(String def2) {
        this.def2 = def2;
    }

    public String getDef3() {
        return def3;
    }

    public void setDef3(String def3) {
        this.def3 = def3;
    }

    public String getDef4() {
        return def4;
    }

    public void setDef4(String def4) {
        this.def4 = def4;
    }

    public String getDef5() {
        return def5;
    }

    public void setDef5(String def5) {
        this.def5 = def5;
    }

    @Override
    public String toString() {
        return "DBConnVO{" +
                "connName='" + connName + '\'' +
                ", dbType='" + dbType + '\'' +
                ", servername='" + servername + '\'' +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
