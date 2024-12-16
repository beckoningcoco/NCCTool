package com.zht.testjavafx2.vo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.List;

@JacksonXmlRootElement(localName = "app")
public class AppItemVO {
    private String appName ;
    //应用必须有一个选中的ip
    private String selectedIp ;
    private String clientId ;
    private String clientSecret ;

    private String pubKey;
    private String secretLevel ;
    private String busiCenter ;
    private String grantType ;
    private String sender ;
    private String pkGroup ;
    private String datasource ;
    private String pwd ;
    private String groupCode ;
    private String userName ;

    private List<String> ipList;

    private List<String> urlList;

    public String getSelectedIp() {
        return selectedIp;
    }

    public void setSelectedIp(String selectedIp) {
        this.selectedIp = selectedIp;
    }
    @JacksonXmlProperty(localName = "pubKey")
    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    @JacksonXmlProperty(localName = "appName")
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @JacksonXmlProperty(localName = "clientId")
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @JacksonXmlProperty(localName = "clientSecret")
    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    @JacksonXmlProperty(localName = "secretLevel")
    public String getSecretLevel() {
        return secretLevel;
    }


    public void setSecretLevel(String secretLevel) {
        this.secretLevel = secretLevel;
    }

    @JacksonXmlProperty(localName = "busiCenter")
    public String getBusiCenter() {
        return busiCenter;
    }


    public void setBusiCenter(String busiCenter) {
        this.busiCenter = busiCenter;
    }

    @JacksonXmlProperty(localName = "grantType")
    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    @JacksonXmlProperty(localName = "sender")
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getPkGroup() {
        return pkGroup;
    }

    public void setPkGroup(String pkGroup) {
        this.pkGroup = pkGroup;
    }

    @JacksonXmlProperty(localName = "datasource")
    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    @JacksonXmlProperty(localName = "pwd")
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    @JacksonXmlProperty(localName = "userName")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JacksonXmlElementWrapper(localName = "ips", useWrapping = true)
    @JacksonXmlProperty(localName = "ip")
    public List<String> getIpList() {
        return ipList;
    }

    public void setIpList(List<String> ipList) {
        this.ipList = ipList;
    }

    @JacksonXmlElementWrapper(localName = "urls", useWrapping = true)
    @JacksonXmlProperty(localName = "url")
    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    @Override
    public String toString() {
        return "AppItemVO{" +
                "appName='" + appName + '\'' +
                ", selectedIp='" + selectedIp + '\'' +
                ", clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", pubKey='" + pubKey + '\'' +
                ", secretLevel='" + secretLevel + '\'' +
                ", busiCenter='" + busiCenter + '\'' +
                ", grantType='" + grantType + '\'' +
                ", sender='" + sender + '\'' +
                ", pkGroup='" + pkGroup + '\'' +
                ", datasource='" + datasource + '\'' +
                ", pwd='" + pwd + '\'' +
                ", groupCode='" + groupCode + '\'' +
                ", userName='" + userName + '\'' +
                ", ipList=" + ipList +
                ", urlList=" + urlList +
                '}';
    }
}
