package com.zht.testjavafx2.openapi;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

@SuppressWarnings("all")
public class HttpClient {

    public static String doPost(String baseUrl, Map<String, String> paramMap, String mediaType,
                                Map<String, String> headers, String json) throws Exception {

        HttpURLConnection urlConnection = null;
        InputStream in = null;
        OutputStream out = null;
        BufferedReader bufferedReader = null;
        String result = null;
        try {
            StringBuffer sb = new StringBuffer();
            sb.append(baseUrl);
            if (paramMap != null) {
                sb.append("?");
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    sb.append(key + "=" + value).append("&");
                }
                baseUrl = sb.toString().substring(0, sb.toString().length() - 1);
            }

            //濡傛灉IP涓篽ttps,鍒欒烦杩囪瘉涔�
            if(baseUrl.contains("https:")){
                trustAllHttpsCertificates();
                HttpsURLConnection.setDefaultHostnameVerifier(HttpClient.DO_NOT_VERIFY);
            }

            URL urlObj = new URL(baseUrl);
            urlConnection = (HttpURLConnection) urlObj.openConnection();
            urlConnection.setConnectTimeout(50000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.addRequestProperty("content-type", mediaType);
            if (headers != null) {
                for (String key : headers.keySet()) {
                    urlConnection.addRequestProperty(key, headers.get(key));
                }
            }
            out = urlConnection.getOutputStream();
            out.write(json.getBytes("utf-8"));
            out.flush();
            int resCode = urlConnection.getResponseCode();
            System.out.println("状态锟诫：锟斤拷" + resCode);
//			if (resCode == HttpURLConnection.HTTP_OK || resCode == HttpURLConnection.HTTP_CREATED || resCode == HttpURLConnection.HTTP_ACCEPTED) {
            in = urlConnection.getInputStream();
//			} else {
//				in = urlConnection.getErrorStream();
//			}
            bufferedReader = new BufferedReader(new InputStreamReader(in, "utf-8"));
            StringBuffer temp = new StringBuffer();
            String line = bufferedReader.readLine();
            while (line != null) {
                temp.append(line).append("\r\n");
                line = bufferedReader.readLine();
            }
            String ecod = urlConnection.getContentEncoding();
            if (ecod == null) {
                ecod = Charset.forName("utf-8").name();
            }
            result = new String(temp.toString().getBytes("utf-8"), ecod);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        } finally {
            if (null != bufferedReader) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            urlConnection.disconnect();
        }
        return result;
    }

    // 蹇界暐 HTTPS 璇锋眰鐨� SSL 璇佷功锛屽繀椤诲湪 openConnection 涔嬪墠璋冪敤
    private static void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
                .getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection
                .setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    static class miTM implements javax.net.ssl.TrustManager,
            javax.net.ssl.X509TrustManager {
        @Override
        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        @Override
        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }
    }

    /**
     *
     */
    public static final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

}
