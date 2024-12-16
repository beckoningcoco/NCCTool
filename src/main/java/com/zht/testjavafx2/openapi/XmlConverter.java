package com.zht.testjavafx2.openapi;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.zht.testjavafx2.vo.AppItemVO;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlConverter{

    public static String convertToXml(AppItemVO appItemVO) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            return xmlMapper.writeValueAsString(appItemVO);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 或者抛出异常、返回空字符串等
        }
    }


    /**
     * 格式化 XML 字符串。
     *
     * @param xmlStr 未格式化的 XML 字符串
     * @return 格式化后的 XML 字符串
     * @throws TransformerException 如果转换过程中发生错误
     */
    public static String formatXml(String xmlStr) throws TransformerException {
        if (xmlStr == null || xmlStr.trim().isEmpty()) {
            return xmlStr;
        }

        // 创建 TransformerFactory 和 Transformer
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute("indent-number", 4); // 设置缩进级别
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        // 使用 StringWriter 来捕获输出
        StringWriter stringWriter = new StringWriter();
        StreamResult result = new StreamResult(stringWriter);

        // 解析并转换 XML 字符串
        Source source = new StreamSource(new StringReader(xmlStr));
        transformer.transform(source, result);

        return stringWriter.toString().trim();
    }
}
