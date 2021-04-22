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

    private static final LocalDateAdapter localDateAdapter = new LocalDateAdapter();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, localDateAdapter).create();


    public String success() {
        return toJson(ResponseState.OK);
    }

    public String success(String key, Object value) {
        return toJson(ResponseState.OK, key, value);
    }

    public String toJson(ResponseState state) {
        ResultData resultData = new ResultData(state);
//        Gson gson = new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
        return gson.toJson(resultData);
    }


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

    public <T> String toJson(ResponseState state, T map) {
        ResultData resultData = new ResultData(state, map);
        return gson.toJson(resultData);
    }

}
