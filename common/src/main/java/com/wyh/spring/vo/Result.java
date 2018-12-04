package com.wyh.spring.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wb-wyh270612
 * @date 2018/12/4  下午12:17
 */
@Getter
@Setter
public class Result<T> implements Serializable {
    private T content;
    private Boolean success;
    private String message;

    public Result(T content, Boolean success, String message) {
        this.content = content;
        this.success = success;
        this.message = message;
    }

    public static <T> Result<T> succeed(T data) {
        return new Result(data, true, "");
    }

    public static <T> Result<T> succeed(String message, T data) {
        return new Result(data, true, message);
    }

    public static <T> Result<T> fail(T data) {
        return new Result(data, false, "");
    }

    public static <T> Result<T> fail(String message, T data) {
        return new Result(data, false, message);
    }
}
