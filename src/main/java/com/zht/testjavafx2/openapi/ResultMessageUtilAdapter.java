package com.zht.testjavafx2.openapi;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class ResultMessageUtilAdapter  extends TypeAdapter<ResultMessageUtil> {

    @Override
    public void write(JsonWriter jsonWriter, ResultMessageUtil resultMessageUtil) throws IOException {

    }

    @Override
    public ResultMessageUtil read(JsonReader in) throws IOException {
        // 实现反序列化逻辑
        ResultMessageUtil result = new ResultMessageUtil();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "success":
                    result.setSuccess(in.nextBoolean());
                    break;
                // 处理其他字段...
                case "data":
                    if (in.peek() != JsonToken.NULL) {
                        // 使用Gson从JsonReader中直接解析为Map
                        Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
                        Map<String, Object> data = new Gson().fromJson(in, mapType);
                        result.setData(data); // 假设 setData 方法接受 Map<String, Object> 类型
                    } else {
                        in.nextNull(); // 处理 null 值的情况
                        result.setData(null);
                    }
                    break;
                case "code":
                    result.setCode(in.nextString());
                    break;
                case "errorMessage":
                    result.setErrorMessage(in.nextString());
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }
        in.endObject();
        return result;
    }
    }

