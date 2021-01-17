package com.css.cloudkitchen.common.result;

/**
 * RESTful Result Builder
 * @author Joe Ding
 */
public class ResultBuilder {

    public static <T> Result<T> success() {
        Result<T> res = new Result<>();
        res.setMsg("Success");
        res.setStatus(ResultCode.SUCCESS);
        return res;
    }

    public static <T> Result<T> success(String msg) {
        Result<T> res = new Result<>();
        res.setMsg(msg);
        res.setStatus(ResultCode.SUCCESS);
        return res;
    }

    public static <T> Result<T> success(T data, String msg) {
        Result<T> res = new Result<>();
        res.setData(data);
        res.setMsg(msg);
        res.setStatus(ResultCode.SUCCESS);
        return res;
    }

    public static <T> Result<T> error() {
        Result<T> res = new Result<>();
        res.setMsg("Fail");
        res.setStatus(ResultCode.ERROR);
        return res;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> res = new Result<>();
        res.setMsg(msg);
        res.setStatus(ResultCode.ERROR);
        return res;
    }
}