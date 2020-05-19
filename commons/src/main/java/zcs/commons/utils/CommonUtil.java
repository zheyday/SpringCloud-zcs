package zcs.commons.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;
import zcs.commons.config.LocalDateAdapter;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class CommonUtil {

    public String success() {
        return toJson(ResponseState.OK);
    }

    private static LocalDateAdapter localDateAdapter = new LocalDateAdapter();
    private static GsonBuilder gsonBuilder = new GsonBuilder();

    public <T> String success(T map) {
        ResultData resultData = new ResultData(ResponseState.OK, map);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, localDateAdapter).create();
        return gson.toJson(resultData);
    }

    public String toJson(ResponseState state) {
        ResultData resultData = new ResultData(state);
        Gson gson = new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
        return gson.toJson(resultData);
    }


    public <T> String toJson(ResponseState state, T map) {
        ResultData resultData = new ResultData(state, map);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, localDateAdapter).create();
        return gson.toJson(resultData);
    }

//    public String toJson(ResponseState state, Object map) {
//        ResultData resultData = new ResultData(state, map);
//        Gson gson = new GsonBuilder()
//                .setPrettyPrinting()
//                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter()).create();
//        return gson.toJson(resultData);
//    }

    /**
     * 只有一对数据 在这里面把它们转成map
     *
     * @param state
     * @param key
     * @param value
     * @return
     */
    public String toJson(ResponseState state, String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return toJson(state, map);
    }

}
