package com.lllebin.response;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: Result
 * Package: com.lllebin.common
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */
@Data
public class CommonResponse<T> {
    //编码：1成功，0和其它数字为失败
    private Integer code;
    //错误信息
    private String msg;
    // 数据
    private T data;
    // 动态数据
    private Map map = new HashMap();

    public static <T> CommonResponse<T> success(T object) {
        CommonResponse<T> r = new CommonResponse<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> CommonResponse<T> error(String msg) {
        CommonResponse r = new CommonResponse();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public CommonResponse<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}
