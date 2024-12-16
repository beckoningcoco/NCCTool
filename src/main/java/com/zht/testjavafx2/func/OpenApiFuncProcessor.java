package com.zht.testjavafx2.func;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.zht.testjavafx2.openapi.FileUtil;
import com.zht.testjavafx2.openapi.OpenapiUtil;
import com.zht.testjavafx2.openapi.XmlConverter;
import com.zht.testjavafx2.vo.AppItemVO;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


/**
 * @author zht
 */
public class OpenApiFuncProcessor {
    private static final String targetFilePath = "D:/Program Files/ncc/appregister.xml";

    public static Map<String, AppItemVO> accquireAllApp() throws Exception {
        Map<String, AppItemVO> ret = new HashMap<>(40);
        FileUtil.copyResourceToFileIfNotExists(targetFilePath);
        InputStream resourceAsStream = FileUtil.getInputStreamForFile(targetFilePath);
        Reader reader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);
        InputSource is = new InputSource(reader);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(is);
        doc.getDocumentElement().normalize();

        System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
        //xml 里至少有一个Apps标签
        // 获取所有 <app> 元素
        NodeList appNodes = doc.getElementsByTagName("app");
        if (appNodes.getLength() == 0) {
            return ret;
        }
        for (int i = 0; i < appNodes.getLength(); i++) {
            Node appNode = appNodes.item(i);
            if (appNode.getNodeType() == Node.ELEMENT_NODE) {
                Element appElement = (Element) appNode;
                AppItemVO appItemVO = new AppItemVO();
                String name = new String(getElementValue(appElement, "appName").getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                appItemVO.setAppName(name);
                appItemVO.setClientId(getElementValue(appElement, "clientId"));
                appItemVO.setClientSecret(getElementValue(appElement, "clientSecret"));
                appItemVO.setPubKey(getElementValue(appElement, "pubKey"));
                appItemVO.setSecretLevel(getElementValue(appElement, "secretLevel"));
                appItemVO.setBusiCenter(getElementValue(appElement, "busiCenter"));
                appItemVO.setGrantType(getElementValue(appElement, "grantType"));
                appItemVO.setSender(getElementValue(appElement, "sender"));
                appItemVO.setPkGroup(getElementValue(appElement, "pkGroup"));
                appItemVO.setDatasource(getElementValue(appElement, "datasource"));
                appItemVO.setGroupCode(getElementValue(appElement, "groupCode"));
                appItemVO.setUserName(getElementValue(appElement, "userName"));
                appItemVO.setPwd(getElementValue(appElement, "pwd"));

                NodeList ipNodeList = appElement.getElementsByTagName("ip");
                List<String> ipList = new ArrayList<>();
                for (int j = 0; j < ipNodeList.getLength(); j++) {
                    Node item = ipNodeList.item(j);
                    ipList.add(item.getTextContent());
                }
                appItemVO.setIpList(ipList);

                NodeList urlNodeList = appElement.getElementsByTagName("url");
                List<String> urlList = new ArrayList<>();
                for (int j = 0; j < urlNodeList.getLength(); j++) {
                    Node item = urlNodeList.item(j);
                    urlList.add(item.getTextContent());
                }
                appItemVO.setUrlList(urlList);

                ret.put(appItemVO.getAppName(), appItemVO);
            }
        }
        return ret;
    }

    public static AppItemVO addAppButton() {
        AppItemVO appItemVO = new AppItemVO();
        // 创建一个自定义的Dialog
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("新增应用");

        // 设置对话框的按钮
        ButtonType loginButtonType = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        Map<String, TextField> tempMap = new HashMap<>(100);
        GridPane grid = createGrid(tempMap, null);
        TextField appName = tempMap.get("appName");

        // 将GridPane设置为对话框的内容
        dialog.getDialogPane().setContent(grid);

        // 请求焦点
        Platform.runLater(appName::requestFocus);

        // 转换对话框的结果
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                setAppItemValues(appItemVO, tempMap);
            }
            return null;
        });

        // 显示对话框并等待结果
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(mes -> {

        });

        return appItemVO;
    }

    private static GridPane createGrid(Map<String, TextField> tempMap, AppItemVO appItemVO) {

        // 创建对话框的内容区域
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        if (appItemVO == null) {
            TextField appName = new TextField();
            appName.setPromptText("appName");
            tempMap.put("appName", appName);

            TextField clientId = new TextField();
            clientId.setPromptText("clientId");
            tempMap.put("clientId", clientId);

            TextField clientSecret = new TextField();
            clientSecret.setPromptText("clientSecret");
            tempMap.put("clientSecret", clientSecret);

            TextField pubKey = new TextField();
            pubKey.setPromptText("pubKey");
            tempMap.put("pubKey", pubKey);

            TextField secretLevel = new TextField();
            secretLevel.setPromptText("secretLevel");
            tempMap.put("secretLevel", secretLevel);

            TextField busiCenter = new TextField();
            busiCenter.setPromptText("busiCenter");
            tempMap.put("busiCenter", busiCenter);

            TextField grantType = new TextField();
            grantType.setPromptText("grantType");
            tempMap.put("grantType", grantType);

            TextField sender = new TextField();
            sender.setPromptText("sender");
            tempMap.put("sender", sender);

            TextField datasource = new TextField();
            datasource.setPromptText("数据源");
            tempMap.put("datasource", datasource);

            TextField userName = new TextField();
            userName.setPromptText("用户名");
            tempMap.put("userName", userName);

            TextField pwd = new TextField();
            pwd.setPromptText("密码");
            tempMap.put("pwd", pwd);

            grid.add(new Label("appName:"), 0, 0);
            grid.add(appName, 1, 0);
            grid.add(new Label("clientId:"), 0, 1);
            grid.add(clientId, 1, 1);
            grid.add(new Label("clientSecret:"), 0, 2);
            grid.add(clientSecret, 1, 2);
            grid.add(new Label("pubKey:"), 0, 3);
            grid.add(pubKey, 1, 3);
            grid.add(new Label("secretLevel:"), 0, 4);
            grid.add(secretLevel, 1, 4);
            grid.add(new Label("busiCenter:"), 0, 5);
            grid.add(busiCenter, 1, 5);
            grid.add(new Label("grantType:"), 0, 6);
            grid.add(grantType, 1, 6);
            grid.add(new Label("sender:"), 0, 7);
            grid.add(sender, 1, 7);
            grid.add(new Label("datasource:"), 0, 8);
            grid.add(datasource, 1, 8);
            grid.add(new Label("userName:"), 0, 9);
            grid.add(userName, 1, 9);
            grid.add(new Label("pwd:"), 0, 10);
            grid.add(pwd, 1, 10);
        } else {
            //修改界面
            TextField appName = new TextField();
            appName.setEditable(false);
            appName.setText(appItemVO.getAppName());
            tempMap.put("appName", appName);

            TextField clientId = new TextField();
            clientId.setText(appItemVO.getClientId());
            tempMap.put("clientId", clientId);

            TextField clientSecret = new TextField();
            clientSecret.setText(appItemVO.getClientSecret());
            tempMap.put("clientSecret", clientSecret);

            TextField pubKey = new TextField();
            pubKey.setText(appItemVO.getPubKey());
            tempMap.put("pubKey", pubKey);

            TextField secretLevel = new TextField();
            secretLevel.setText(appItemVO.getSecretLevel());
            tempMap.put("secretLevel", secretLevel);

            TextField busiCenter = new TextField();
            busiCenter.setText(appItemVO.getBusiCenter());
            tempMap.put("busiCenter", busiCenter);

            TextField grantType = new TextField();
            grantType.setText(appItemVO.getGrantType());
            tempMap.put("grantType", grantType);

            TextField sender = new TextField();
            sender.setText(appItemVO.getSender());
            tempMap.put("sender", sender);

            TextField datasource = new TextField();
            datasource.setText(appItemVO.getDatasource());
            tempMap.put("datasource", datasource);

            TextField userName = new TextField();
            userName.setText(appItemVO.getUserName());
            tempMap.put("userName", userName);

            TextField pwd = new TextField();
            pwd.setText(appItemVO.getPwd());
            tempMap.put("pwd", pwd);


            grid.add(new Label("appName:"), 0, 0);
            grid.add(appName, 1, 0);
            grid.add(new Label("clientId:"), 0, 1);
            grid.add(clientId, 1, 1);
            grid.add(new Label("clientSecret:"), 0, 2);
            grid.add(clientSecret, 1, 2);
            grid.add(new Label("pubKey:"), 0, 3);
            grid.add(pubKey, 1, 3);
            grid.add(new Label("secretLevel:"), 0, 4);
            grid.add(secretLevel, 1, 4);
            grid.add(new Label("busiCenter:"), 0, 5);
            grid.add(busiCenter, 1, 5);
            grid.add(new Label("grantType:"), 0, 6);
            grid.add(grantType, 1, 6);
            grid.add(new Label("sender:"), 0, 7);
            grid.add(sender, 1, 7);
            grid.add(new Label("datasource:"), 0, 8);
            grid.add(datasource, 1, 8);
            grid.add(new Label("userName:"), 0, 9);
            grid.add(userName, 1, 9);
            grid.add(new Label("pwd:"), 0, 10);
            grid.add(pwd, 1, 10);

        }
        return grid;
    }

    //测试获取OPENAPI的token
    public static String getTokenForOpenAPI(AppItemVO appItemVO) {
        String ret = null;
        try {
            OpenapiUtil.init(appItemVO);
            ret = OpenapiUtil.getToken(appItemVO.getSelectedIp());
        } catch (Exception e) {
            return null;
        }
        return ret;
    }

    private static void setAppItemValues(AppItemVO appItemVO, Map<String, TextField> textFieldMap) {
        // 获取所有TextField的文本
        appItemVO.setAppName(textFieldMap.get("appName").getText());
        appItemVO.setClientId(textFieldMap.get("clientId").getText());
        appItemVO.setClientSecret(textFieldMap.get("clientSecret").getText());
        appItemVO.setSecretLevel(textFieldMap.get("secretLevel").getText());
        appItemVO.setPubKey(textFieldMap.get("pubKey").getText());
        appItemVO.setBusiCenter(textFieldMap.get("busiCenter").getText());
        appItemVO.setGrantType(textFieldMap.get("grantType").getText());
        appItemVO.setSender(textFieldMap.get("sender").getText());
        appItemVO.setDatasource(textFieldMap.get("datasource").getText());
        appItemVO.setUserName(textFieldMap.get("userName").getText());
        appItemVO.setPwd(textFieldMap.get("pwd").getText());
        TextField selectedIp = textFieldMap.get("selectedIp");
        appItemVO.setSelectedIp(selectedIp == null ? null : selectedIp.getText());
    }


    private static String getElementValue(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                return node.getTextContent();
            }
        }
        return "";
    }

    //格式化json报文
    public static void formatAppFunc(TextArea jsonTextArea) {
        String text = jsonTextArea.getText();
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser parser = new JsonParser();
            JsonElement parse = parser.parse(text);
            if (parse != null) {
                String ret = gson.toJson(parse);
                if (StringUtils.isNotBlank(ret) && !"null".equals(ret)) {
                    jsonTextArea.setText(ret);
                }
            }
        } catch (Exception e) {
            // 创建并显示一个Alert对话框
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("格式化");
            alert.setHeaderText(null); // 可选：设置头部文本
            alert.setContentText("报文格式异常！");

            // 显示对话框并等待用户关闭它
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    //发送报文，调用api
    public static String commitAppFunc(AppItemVO appItemVO, String json, String ip, String url) {

        String ret = null;
        try {
            OpenapiUtil.init(appItemVO);
            String token = OpenapiUtil.getToken(appItemVO.getSelectedIp());
            ret = OpenapiUtil.testApi(token, ip, "/nccloud/api/" + url, json);
        } catch (Exception e) {
            ret = e.getMessage();
            return ret;
        }
        return ret;

    }

    //将AppitemVO转换为XML
    public static void makeVO2XML(AppItemVO appItemVO) {
        try {
            String xmlStr = XmlConverter.convertToXml(appItemVO);
            xmlStr = XmlConverter.formatXml(xmlStr);
            FileUtil.copyResourceToFileIfNotExists(targetFilePath);
            InputStream resourceAsStream = FileUtil.getInputStreamForFile(targetFilePath);
            Reader reader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);
            InputSource is = new InputSource(reader);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            // 将新的 XML 字符串解析为 DocumentFragment
            Document newDoc = dBuilder.parse(new ByteArrayInputStream(xmlStr.getBytes()));
            Element newElement = (Element) newDoc.getDocumentElement().cloneNode(true);
// 获取根元素并插入新元素
            Element root = doc.getDocumentElement();
            NodeList apps = root.getElementsByTagName("apps");
            apps.item(0).appendChild(doc.importNode(newElement, true));

            // 使用 Transformer 将修改后的文档写回文件
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            File file = new File(targetFilePath);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String loadResourceAsString(String resourcePath) {
        try {
            InputStream resourceAsStream = OpenApiFuncProcessor.class.getResourceAsStream(resourcePath);
            if (resourceAsStream == null) {
                throw new IllegalArgumentException("Resource not found: " + resourcePath);
            }
            // 将输入流转换为字符串
            return new String(resourceAsStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //点击删除按钮，删除当前应用
    public static void deleteAppButton(String selectedAppName) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        InputStream resourceAsStream = FileUtil.getInputStreamForFile(targetFilePath);
        Reader reader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);
        InputSource is = new InputSource(reader);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(is);

        // 获取根元素
        Element root = doc.getDocumentElement();

        // 获取所有 <apps> 元素（假设只有一个 <apps> 元素）
        NodeList appsNodes = root.getElementsByTagName("apps");
        if (appsNodes.getLength() == 0) {
            System.out.println("<apps> 元素未找到！");
            return;
        }
        Element appsElement = (Element) appsNodes.item(0); // 获取第一个 <apps> 元素

        // 遍历所有 <app> 元素
        NodeList appNodes = appsElement.getElementsByTagName("app");
        for (int i = 0; i < appNodes.getLength(); i++) {
            Node appNode = appNodes.item(i);
            if (appNode.getNodeType() == Node.ELEMENT_NODE) {
                Element appElement = (Element) appNode;

                // 检查 <appName> 子元素的内容
                NodeList appNameNodes = appElement.getElementsByTagName("appName");
                if (appNameNodes.getLength() > 0) {
                    Element appNameElement = (Element) appNameNodes.item(0);
                    String appNameContent = appNameElement.getTextContent();

                    // 如果 <appName> 内容匹配，则删除整个 <app> 元素
                    if (appNameContent.equals(selectedAppName)) {
                        appsElement.removeChild(appNode);
                        System.out.println("已删除 appName 为 " + selectedAppName + " 的 <app> 元素");
                    }
                }
            }
        }
        // 使用 Transformer 将修改后的文档写回文件
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        File file = new File(targetFilePath);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

    //点击修改应用按钮，进行修改
    public static AppItemVO editAppButton(AppItemVO appItemVO) {

        // 创建一个自定义的Dialog
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("修改应用");

        // 设置对话框的按钮
        ButtonType loginButtonType = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        Map<String, TextField> tempMap = new HashMap<>(100);
        GridPane grid = createGrid(tempMap, appItemVO);
        TextField appName = tempMap.get("appName");

        // 将GridPane设置为对话框的内容
        dialog.getDialogPane().setContent(grid);

        // 请求焦点
        Platform.runLater(appName::requestFocus);

        // 转换对话框的结果
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                setAppItemValues(appItemVO, tempMap);
            }
            return null;
        });

        // 显示对话框并等待结果
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(mes -> {
            // 创建并显示一个Alert对话框
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("获取Token");
            alert.setHeaderText(null); // 可选：设置头部文本
            alert.setContentText(mes);

            // 显示对话框并等待用户关闭它
            alert.showAndWait();
            System.out.println("获取到token信息: " + mes);
        });
        return appItemVO;
    }

    //修改xml文件
    public static void makeVO2XMLbyEdit(AppItemVO appItemVO) {
        try {
            String appName = appItemVO.getAppName();
            String xmlStr = XmlConverter.convertToXml(appItemVO);
            xmlStr = XmlConverter.formatXml(xmlStr);
            FileUtil.copyResourceToFileIfNotExists(targetFilePath);
            InputStream resourceAsStream = FileUtil.getInputStreamForFile(targetFilePath);
            Reader reader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);
            InputSource is = new InputSource(reader);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            // 获取根元素中的 <apps> 元素
            Element root = doc.getDocumentElement();
            NodeList appsNodes = root.getElementsByTagName("apps");
            if (appsNodes.getLength() == 0) {
                System.out.println("<apps> 元素未找到！");
                return;
            }
            Element appsElement = (Element) appsNodes.item(0);

            // 遍历所有 <app> 元素
            NodeList appNodes = appsElement.getElementsByTagName("app");
            for (int i = 0; i < appNodes.getLength(); i++) {
                Node appNode = appNodes.item(i);
                if (appNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element appElement = (Element) appNode;
                    // 检查 <appName> 子元素的内容
                    NodeList appNameNodes = appElement.getElementsByTagName("appName");
                    if (appNameNodes.getLength() > 0) {
                        Element appNameElement = (Element) appNameNodes.item(0);
                        String appNameContent = appNameElement.getTextContent();
                        // 如果 <appName> 内容匹配，则更新整个 <app> 元素
                        if (appNameContent.equals(appName)) {
                            // 更新 <appName>
                            // 更新其他内容（假设有一个名为 <clientId> 的子元素）
                            NodeList otherNodes = appElement.getElementsByTagName("clientId");
                            if (otherNodes.getLength() > 0) {
                                Element otherElement = (Element) otherNodes.item(0);
                                otherElement.setTextContent(appItemVO.getClientId());
                            }
                            otherNodes = appElement.getElementsByTagName("clientSecret");
                            if (otherNodes.getLength() > 0) {
                                Element otherElement = (Element) otherNodes.item(0);
                                otherElement.setTextContent(appItemVO.getClientSecret());
                            }
                            otherNodes = appElement.getElementsByTagName("pubKey");
                            if (otherNodes.getLength() > 0) {
                                Element otherElement = (Element) otherNodes.item(0);
                                otherElement.setTextContent(appItemVO.getPubKey());
                            }
                            otherNodes = appElement.getElementsByTagName("secretLevel");
                            if (otherNodes.getLength() > 0) {
                                Element otherElement = (Element) otherNodes.item(0);
                                otherElement.setTextContent(appItemVO.getSecretLevel());
                            }
                            otherNodes = appElement.getElementsByTagName("busiCenter");
                            if (otherNodes.getLength() > 0) {
                                Element otherElement = (Element) otherNodes.item(0);
                                otherElement.setTextContent(appItemVO.getBusiCenter());
                            }
                            otherNodes = appElement.getElementsByTagName("grantType");
                            if (otherNodes.getLength() > 0) {
                                Element otherElement = (Element) otherNodes.item(0);
                                otherElement.setTextContent(appItemVO.getGrantType());
                            }
                            otherNodes = appElement.getElementsByTagName("sender");
                            if (otherNodes.getLength() > 0) {
                                Element otherElement = (Element) otherNodes.item(0);
                                otherElement.setTextContent(appItemVO.getSender());
                            }
                            otherNodes = appElement.getElementsByTagName("datasource");
                            if (otherNodes.getLength() > 0) {
                                Element otherElement = (Element) otherNodes.item(0);
                                otherElement.setTextContent(appItemVO.getDatasource());
                            }
                            otherNodes = appElement.getElementsByTagName("userName");
                            if (otherNodes.getLength() > 0) {
                                Element otherElement = (Element) otherNodes.item(0);
                                otherElement.setTextContent(appItemVO.getUserName());
                            }
                            otherNodes = appElement.getElementsByTagName("pwd");
                            if (otherNodes.getLength() > 0) {
                                Element otherElement = (Element) otherNodes.item(0);
                                otherElement.setTextContent(appItemVO.getPwd());
                            }

                            System.out.println("已更新 appName 为 " + appName + " 的 <app> 元素");
                            break; // 假设每个 appName 是唯一的，所以找到一个就可以停止了
                        }
                    }
                }
            }
            // 使用 Transformer 将修改后的文档写回文件
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            File file = new File(targetFilePath);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除某个应用下的某个路径
    public static void delIPButton(String selectedAppName, String selectedIp) throws ParserConfigurationException, IOException, SAXException, TransformerException {

        InputStream resourceAsStream = FileUtil.getInputStreamForFile(targetFilePath);
        Reader reader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);
        InputSource is = new InputSource(reader);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(is);

        // 获取根元素
        Element root = doc.getDocumentElement();

        // 获取所有 <apps> 元素（假设只有一个 <apps> 元素）
        NodeList appsNodes = root.getElementsByTagName("apps");
        if (appsNodes.getLength() == 0) {
            System.out.println("<apps> 元素未找到！");
            return;
        }
        Element appsElement = (Element) appsNodes.item(0); // 获取第一个 <apps> 元素

        // 遍历所有 <app> 元素
        NodeList appNodes = appsElement.getElementsByTagName("app");
        for (int i = 0; i < appNodes.getLength(); i++) {
            Node appNode = appNodes.item(i);
            if (appNode.getNodeType() == Node.ELEMENT_NODE) {
                Element appElement = (Element) appNode;

                // 检查 <appName> 子元素的内容
                NodeList appNameNodes = appElement.getElementsByTagName("appName");
                if (appNameNodes.getLength() > 0) {
                    Element appNameElement = (Element) appNameNodes.item(0);
                    String appNameContent = appNameElement.getTextContent();

                    // 如果 <appName> 内容匹配，则删除整个 <app> 元素
                    if (appNameContent.equals(selectedAppName)) {
                        NodeList ips = appElement.getElementsByTagName("ips");
                        if (ips.getLength() == 0) {
                            System.out.println("<ips> 元素未找到！");
                            return;
                        }
                        Element ipsElement = (Element) ips.item(0); // 获取第一个 <ips> 元素
                        NodeList ipNodes = appsElement.getElementsByTagName("ip");
                        for (int j = 0; j < ipNodes.getLength(); j++) {
                            Node ipNode = ipNodes.item(j);
                            if (ipNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element ipElement = (Element) ipNode;
                                String textContent = ipElement.getTextContent();
                                // 如果 <ip> 内容匹配，则删除当前 <ip> 元素
                                if (textContent.equals(selectedIp)) {
                                    ipsElement.removeChild(ipNode);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        // 使用 Transformer 将修改后的文档写回文件
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        File file = new File(targetFilePath);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

    //删除当前应用下的url
    public static void delUrlButton(String selectedAppName, String selectedUrl) throws TransformerException, ParserConfigurationException, IOException, SAXException {
        InputStream resourceAsStream = FileUtil.getInputStreamForFile(targetFilePath);
        Reader reader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);
        InputSource is = new InputSource(reader);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(is);

        // 获取根元素
        Element root = doc.getDocumentElement();

        // 获取所有 <apps> 元素（假设只有一个 <apps> 元素）
        NodeList appsNodes = root.getElementsByTagName("apps");
        if (appsNodes.getLength() == 0) {
            System.out.println("<apps> 元素未找到！");
            return;
        }
        Element appsElement = (Element) appsNodes.item(0); // 获取第一个 <apps> 元素

        // 遍历所有 <app> 元素
        NodeList appNodes = appsElement.getElementsByTagName("app");
        for (int i = 0; i < appNodes.getLength(); i++) {
            Node appNode = appNodes.item(i);
            if (appNode.getNodeType() == Node.ELEMENT_NODE) {
                Element appElement = (Element) appNode;

                // 检查 <appName> 子元素的内容
                NodeList appNameNodes = appElement.getElementsByTagName("appName");
                if (appNameNodes.getLength() > 0) {
                    Element appNameElement = (Element) appNameNodes.item(0);
                    String appNameContent = appNameElement.getTextContent();

                    // 如果 <appName> 内容匹配，则删除整个 <app> 元素
                    if (appNameContent.equals(selectedAppName)) {
                        NodeList urls = appElement.getElementsByTagName("urls");
                        if (urls.getLength() == 0) {
                            System.out.println("<urls> 元素未找到！");
                            return;
                        }
                        Element urlsElement = (Element) urls.item(0); // 获取第一个 <urls> 元素
                        NodeList urlNodes = appsElement.getElementsByTagName("url");
                        for (int j = 0; j < urlNodes.getLength(); j++) {
                            Node urlNode = urlNodes.item(i);
                            if (urlNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element urlElement = (Element) urlNode;
                                String textContent = urlElement.getTextContent();
                                // 如果 <ip> 内容匹配，则删除当前 <ip> 元素
                                if (textContent.equals(selectedUrl)) {
                                    urlsElement.removeChild(urlNode);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        // 使用 Transformer 将修改后的文档写回文件
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        File file = new File(targetFilePath);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

    //新增一个 ip:port
    public static void handleipAdd(String appName, String ipAddress) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        InputStream resourceAsStream = FileUtil.getInputStreamForFile(targetFilePath);
        Reader reader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);
        InputSource is = new InputSource(reader);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(is);

        // 获取根元素
        Element root = doc.getDocumentElement();
        NodeList appsNodes = root.getElementsByTagName("apps");
        if (appsNodes.getLength() == 0) {
            System.out.println("<apps> 元素未找到！");
            return;
        }
        Element appsElement = (Element) appsNodes.item(0);

        // 遍历所有 <app> 元素
        NodeList appNodes = appsElement.getElementsByTagName("app");
        for (int i = 0; i < appNodes.getLength(); i++) {
            Node appNode = appNodes.item(i);
            if (appNode.getNodeType() == Node.ELEMENT_NODE) {
                Element appElement = (Element) appNode;

                // 检查 <appName> 子元素的内容
                NodeList appNameNodes = appElement.getElementsByTagName("appName");
                if (appNameNodes.getLength() > 0) {
                    Element appNameElement = (Element) appNameNodes.item(0);
                    String appNameContent = appNameElement.getTextContent();

                    // 如果 <appName> 内容匹配，则处理该 <app> 元素
                    if (appNameContent.equals(appName)) {
                        // 查找或创建 <ips> 子元素
                        NodeList ipsNodes = appElement.getElementsByTagName("ips");
                        Element ipsElement;
                        if (ipsNodes.getLength() > 0) {
                            ipsElement = (Element) ipsNodes.item(0);
                        } else {
                            ipsElement = doc.createElement("ips");
                            appElement.appendChild(ipsElement);
                        }

                        // 创建并配置新的 <ip> 元素
                        Element newIpElement = doc.createElement("ip");
                        newIpElement.setTextContent(ipAddress);
                        ipsElement.appendChild(newIpElement);

                        System.out.println("已成功添加 ip 为 " + ipAddress + " 到 appName 为 " + appName + " 的 <app> 元素");
                        // 使用 Transformer 将修改后的文档写回文件
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        DOMSource source = new DOMSource(doc);
                        File file = new File(targetFilePath);
                        StreamResult result = new StreamResult(file);
                        transformer.transform(source, result);
                        return; // 假设每个 appName 是唯一的，所以找到一个就可以停止了
                    }
                }
            }
        }
    }

    //新增调用路径
    public static void handleurlAdd(String appName, String newUrl) throws TransformerException, ParserConfigurationException, IOException, SAXException {
        InputStream resourceAsStream = FileUtil.getInputStreamForFile(targetFilePath);
        Reader reader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);
        InputSource is = new InputSource(reader);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(is);

        // 获取根元素
        Element root = doc.getDocumentElement();
        NodeList appsNodes = root.getElementsByTagName("apps");
        if (appsNodes.getLength() == 0) {
            System.out.println("<apps> 元素未找到！");
            return;
        }
        Element appsElement = (Element) appsNodes.item(0);

        // 遍历所有 <app> 元素
        NodeList appNodes = appsElement.getElementsByTagName("app");
        for (int i = 0; i < appNodes.getLength(); i++) {
            Node appNode = appNodes.item(i);
            if (appNode.getNodeType() == Node.ELEMENT_NODE) {
                Element appElement = (Element) appNode;

                // 检查 <appName> 子元素的内容
                NodeList appNameNodes = appElement.getElementsByTagName("appName");
                if (appNameNodes.getLength() > 0) {
                    Element appNameElement = (Element) appNameNodes.item(0);
                    String appNameContent = appNameElement.getTextContent();

                    // 如果 <appName> 内容匹配，则处理该 <app> 元素
                    if (appNameContent.equals(appName)) {
                        // 查找或创建 <ips> 子元素
                        NodeList urlsNodes = appElement.getElementsByTagName("urls");
                        Element urlsElement;
                        if (urlsNodes.getLength() > 0) {
                            urlsElement = (Element) urlsNodes.item(0);
                        } else {
                            urlsElement = doc.createElement("urls");
                            appElement.appendChild(urlsElement);
                        }

                        // 创建并配置新的 <ip> 元素
                        Element newUrlElement = doc.createElement("url");
                        newUrlElement.setTextContent(newUrl);
                        urlsElement.appendChild(newUrlElement);

                        System.out.println("已成功添加 调用路径 为 " + newUrl + " 到 appName 为 " + appName + " 的 <app> 元素");
                        // 使用 Transformer 将修改后的文档写回文件
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        DOMSource source = new DOMSource(doc);
                        File file = new File(targetFilePath);
                        StreamResult result = new StreamResult(file);
                        transformer.transform(source, result);
                        return; // 假设每个 appName 是唯一的，所以找到一个就可以停止了
                    }
                }
            }
        }
    }
}
