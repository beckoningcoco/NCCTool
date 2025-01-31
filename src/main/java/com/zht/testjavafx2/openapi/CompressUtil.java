package com.zht.testjavafx2.openapi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.*;

@SuppressWarnings("all")
public class CompressUtil {


    private static int buffSize = 1024;

    /**
     * deflaterCompress 默认压缩
     *
     * @param source 原文
     * @return
     * @throws Exception
     */
    public static String deflaterCompress(String source) throws Exception {
        String value = null;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Deflater compressor = new Deflater();

        try {

            byte[] input = source.getBytes(StandardCharsets.UTF_8);
            // 设置压缩登记
            compressor.setLevel(Deflater.DEFAULT_COMPRESSION);
            compressor.setInput(input);
            compressor.finish();
            final byte[] buf = new byte[buffSize];

            while (!compressor.finished()) {
                int count = compressor.deflate(buf);
                bos.write(buf, 0, count);
            }
            value = Base64Util.encryptBASE64(bos.toByteArray());

        } finally {
            bos.close();
            compressor.end();
        }

        return value;
    }

    /**
     * deflaterDecompress 默认解压
     *
     * @param source 压缩后文本
     * @return
     * @throws @throws     Exception
     */
    public static String deflaterDecompress(String source) throws Exception {
        String value = null;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Inflater decompressor = new Inflater();

        try {
            byte[] input = Base64Util.decryptBASE64(source);

            decompressor.setInput(input);
            final byte[] buf = new byte[buffSize];

            while (!decompressor.finished()) {
                int count = decompressor.inflate(buf);
                bos.write(buf, 0, count);
            }

            value = new String(bos.toByteArray(), StandardCharsets.UTF_8);
        } catch (IOException | DataFormatException e) {
            throw new Exception("解压异常 " + e.getMessage());
        } finally {
            bos.close();
            decompressor.end();
        }
        return value;
    }

    /**
     * gzipCompress 基于gzip压缩
     *
     * @param source 原文
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String gzipCompress(String source) throws Exception {
        String value = null;

        ByteArrayOutputStream out = null;

        try {
            out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            byte[] input = source.getBytes(StandardCharsets.UTF_8);
            gzip.write(input);
            gzip.close();
            value = Base64Util.encryptBASE64(out.toByteArray());
        } catch (IOException e) {
            throw new Exception("压缩异常 " + e.getMessage());
        } finally {
            if (out != null) {
                out.close();
            }
        }

        return value;
    }

    /**
     * gzipDecompress gzip解压
     *
     * @param source 压缩后文本
     * @return
     * @throws Exception
     */
    public static String gzipDecompress(String source) throws Exception {
        String value = null;

        ByteArrayOutputStream out = null;
        ByteArrayInputStream in = null;

        try {
            byte[] input = Base64Util.decryptBASE64(source);
            out = new ByteArrayOutputStream();
            in = new ByteArrayInputStream(input);
            GZIPInputStream ungzip = new GZIPInputStream(in);

            byte[] buffer = new byte[buffSize];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            ungzip.close();
            value = new String(out.toByteArray(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new Exception("压缩异常 " + e.getMessage());
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }

        }
        return value;
    }
}
