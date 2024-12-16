package com.zht.testjavafx2.openapi;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zht.testjavafx2.vo.AppItemVO;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public class OpenapiUtil {
    // app_secret
    private static String client_secret = null;
    // 公钥
    private static String pubKey = null;
    // app_id
    private static String client_id = null;
    // ncc用户名
    private static String username = null;
    private static String usercode = null;
    // ncc用户名密码
    private static String pwd = null;
    // ncc账套
    private static String busi_center = null;
    private static String dsname = null;
    // 获取token方式：client_credentials、password
    private static String grant_type = null;
    // 服务器ip：port
//	private static String baseUrl = null;
    // 返回值压缩加密级别
    private static String secret_level = null;
    // openapi请求路径
    private static String apiUrl = null;

    // 访问api获取到的access_token
    public static String token = null;

    //选中的调用url
    public static String selectIp = null;

    //初始化 open调用的基础信息
    public static void init(AppItemVO appItemVO) {

        client_secret = appItemVO.getClientSecret();
        client_id = appItemVO.getClientId();
        pubKey = appItemVO.getPubKey();
        username = appItemVO.getUserName();
        usercode = appItemVO.getUserName();
        pwd = appItemVO.getPwd();
        busi_center = appItemVO.getBusiCenter();
        dsname = appItemVO.getDatasource();
        grant_type = appItemVO.getGrantType();
        secret_level = appItemVO.getSecretLevel();
        selectIp = appItemVO.getSelectedIp();
    }


    public static String getToken(String selectIp) throws Exception {
        String token = null;
        if ("password".equals(grant_type)) {
            // 密码模式
            token = getTokenByPWD(selectIp);
        } else if ("client_credentials".equals(grant_type)) {
            // 客户端模式
            token = getTokenByClient(selectIp);
        }
        return token;
    }


    /**
     * 客户端模式获取token
     *
     * @return
     * @throws Exception
     */
    private static String getTokenByClient(String baseUrl) throws Exception {
        Map<String, String> paramMap = new HashMap<String, String>();
        // 密码模式认证
        paramMap.put("grant_type", "client_credentials");
        // 第三方应用id
        paramMap.put("client_id", client_id);
        // 第三方应用secret 公钥加密
        String secret_entryption = Encryption.pubEncrypt(pubKey, client_secret);
        System.out.println("secret_entryption：：" + secret_entryption);
        paramMap.put("client_secret", URLEncoder.encode(secret_entryption, "utf-8"));
        // 账套编码
        paramMap.put("biz_center", busi_center);
        // // TODO 传递数据源和ncc登录用户
//		paramMap.put("dsname", dsname);
        paramMap.put("usercode", usercode);

        // 签名
        String sign = SHA256Util.getSHA256(client_id + client_secret + pubKey, pubKey);
        paramMap.put("signature", sign);
        System.out.println("##gettoken sign::" + sign);

        String url = "http://"+baseUrl + "/nccloud/opm/accesstoken";
        String mediaType = "application/x-www-form-urlencoded";
        String token = HttpClient.doPost(url, paramMap, mediaType, null, "");
        return token;
    }

    /**
     * 密码模式获取token
     *
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unused")
    private static String getTokenByPWD(String baseUrl) throws Exception {
        Map<String, String> paramMap = new HashMap<String, String>();
        // 密码模式认证
        paramMap.put("grant_type", "password");
        // 第三方应用id
        paramMap.put("client_id", client_id);
        // 第三方应用secret 公钥加密
        paramMap.put("client_secret", URLEncoder.encode(Encryption.pubEncrypt(pubKey, client_secret), "utf-8"));
//		paramMap.put("client_secret", client_secret);
        // ncc用户名
        paramMap.put("username", username);
        // 密码 公钥加密
        paramMap.put("password", URLEncoder.encode(Encryption.pubEncrypt(pubKey, pwd), "utf-8"));
//		paramMap.put("password", pwd);
        // 账套编码
        paramMap.put("biz_center", busi_center);
        // 签名
        String sign = SHA256Util.getSHA256(client_id + client_secret + username + pwd + pubKey, pubKey);
        System.out.println("sign::" + sign);
        System.out.println("paramMap::" + paramMap.toString());
        paramMap.put("signature", sign);

        String url = baseUrl + "/nccloud/opm/accesstoken";
        String mediaType = "application/x-www-form-urlencoded";
        String token = HttpClient.doPost(url, paramMap, mediaType, null, "");
        return token;
    }

    public static String testApi(String token, String ip, String apiUrl, String json) throws Exception {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ResultMessageUtil.class, new ResultMessageUtilAdapter())
                .create();
        // token转对象，获取api访问所用token和secret
        ResultMessageUtil returnData = gson.fromJson(token, ResultMessageUtil.class);
        Map<String, Object> data = (Map<String, Object>) returnData.getData();
        String access_token = (String) data.get("access_token");
        String security_key = (String) data.get("security_key");
        String refresh_token = (String) data.get("refresh_token");
        long expire_in = new Double((double) data.get("expires_in")).longValue();
        long ts = new Double((double) data.get("ts")).longValue();
        if (ts + expire_in < System.currentTimeMillis()) {
            token = OpenapiUtil.getToken(ip);
            returnData = new Gson().fromJson(token, ResultMessageUtil.class);
            data = (Map<String, Object>) returnData.getData();
            access_token = (String) data.get("access_token");
            security_key = (String) data.get("security_key");
            refresh_token = (String) data.get("refresh_token");
        }
        System.out.println("【ACCESS_TOKEN】:" + access_token);

        // 请求路径
        String url ="http://" + ip + apiUrl;
        // header 参数
        Map<String, String> headermap = new HashMap<>();
        headermap.put("access_token", access_token);
        headermap.put("client_id", client_id);
        json = new String(json.getBytes("utf-8"), "utf-8");
        StringBuffer sb = new StringBuffer();
        sb.append(client_id);
        if (StringUtils.isNotBlank(json)) {
            sb.append(json);
        }
        sb.append(pubKey);
        String sign = SHA256Util.getSHA256(sb.toString(), pubKey);
        headermap.put("signature", sign);

        String mediaType = "application/json;charset=utf-8";

        // 表体数据json
        // 根据安全级别选择加密或压缩请求表体参数
        String res = OpenapiUtil.dealRequestBody(json, security_key, secret_level);

        // 返回值
        String result = HttpClient.doPost(url, null, mediaType, headermap, res);
        String result2 = dealResponseBody(result, security_key, secret_level);

        return result;

    }

    // 根据安全级别设置，表体是否加密或压缩
    private static String dealRequestBody(String source, String security_key, String level) throws Exception {
        String result = null;
        if (StringUtils.isEmpty(level) || SecretConst.LEVEL0.equals(level)) {
            result = source;
        } else if (SecretConst.LEVEL1.equals(level)) {
            result = Encryption.symEncrypt(security_key, source);
        } else if (SecretConst.LEVEL2.equals(level)) {
            result = CompressUtil.gzipCompress(source);
        } else if (SecretConst.LEVEL3.equals(level)) {
            result = Encryption.symEncrypt(security_key, CompressUtil.gzipCompress(source));
        } else if (SecretConst.LEVEL4.equals(level)) {
            result = CompressUtil.gzipCompress(Encryption.symEncrypt(security_key, source));
        } else {
            throw new Exception("无效的安全等级");
        }

        return result;
    }

    /**
     * 返回值进行过加密和压缩，对返回值进行解压和解密
     *
     * @param source
     * @param security_key
     * @param level
     * @return
     * @throws Exception
     */
    private static String dealResponseBody(String source, String security_key, String level) throws Exception {
        String result = null;

        if (StringUtils.isEmpty(level) || SecretConst.LEVEL0.equals(level)) {
            result = source;
        } else if (SecretConst.LEVEL1.equals(level)) {
            result = Decryption.symDecrypt(security_key, source);
        } else if (SecretConst.LEVEL2.equals(level)) {
            result = CompressUtil.gzipDecompress(source);
        } else if (SecretConst.LEVEL3.equals(level)) {
            result = CompressUtil.gzipDecompress(Decryption.symDecrypt(security_key, source));
        } else if (SecretConst.LEVEL4.equals(level)) {
            result = Decryption.symDecrypt(security_key, CompressUtil.gzipDecompress(source));
        } else {
            throw new Exception("无效的安全等级");
        }

        return result;
    }


    class SecretConst {
        /**
         * LEVEL0 不压缩、不加密
         */
        public static final String LEVEL0 = "L0";
        /**
         * LEVEL1 只加密、不压缩
         */
        public static final String LEVEL1 = "L1";
        /**
         * LEVEL2 只压缩、不加密
         */
        public static final String LEVEL2 = "L2";
        /**
         * LEVEL3 先压缩、后加密
         */
        public static final String LEVEL3 = "L3";
        /**
         * LEVEL4 先加密、后压缩
         */
        public static final String LEVEL4 = "L4";
    }
}
