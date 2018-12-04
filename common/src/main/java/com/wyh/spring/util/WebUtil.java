package com.wyh.spring.util;

import com.alibaba.fastjson.util.IOUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.util.StringUtils.commaDelimitedListToSet;

/**
 * web应用程序的工具类
 *
 * @author 杨小院
 */
@Slf4j
public class WebUtil extends WebUtils {

    public static final String X_REQUESTED_WIDTH = "X-Requested-With";
    public static final String X_REAL_IP = "X-Real-IP";
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    public static final String XML_HTTP_REQUEST = "XMLHttpRequest";
    public static final String UNKNOWN = "unKnown";
    public static final Cache<Object, Object> PATTERN_MATCH_CACHE = CacheBuilder.newBuilder().maximumSize(10000)
        .expireAfterAccess(24, TimeUnit.HOURS).build();

    /**
     * 获取http请求中的request对象
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes == null ? null : attributes.getRequest();
    }

    /**
     * 获取http请求中的response对象
     *
     * @return
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes == null ? null : attributes.getResponse();
    }

    /**
     * 判断是否是Ajax请求
     *
     * @return
     */
    public static boolean isAjaxRequest() {
        HttpServletRequest request = getRequest();
        return request != null && XML_HTTP_REQUEST.equals(request.getHeader(X_REQUESTED_WIDTH));
    }

    /**
     * 将对象的json字符串写到response中
     *
     * @param obj
     */
    public static void writeJsonResponse(Object obj) {
        HttpServletResponse response = getResponse();
        HttpServletRequest request = getRequest();
        try {
            if (response != null && request != null) {
                String results = JsonUtil.getJsonString(obj);
                response.setContentType("text/html;charset=utf-8");

                String callbackFunName = request.getParameter("callback");
                if (StringUtils.isBlank(callbackFunName)) {
                    //没有callback直接输出json格式结果
                    response.getWriter().println(results);
                } else {
                    //输出jsonp格式的响应
                    if (!IOUtils.isValidJsonpQueryParam(callbackFunName)) {
                        log.error("无效的jsonp回调方法{}，将使用默认回调方法callback", callbackFunName);
                        callbackFunName = "callback";
                    }
                    response.getWriter().println(callbackFunName + "( " + results + " )");
                }

                response.flushBuffer();
            }
        } catch (Exception e) {
            log.error("exception when writeJsonResponse", e);
        }
    }

    /**
     * 获取当前请求的真实ip
     *
     * @return
     */
    public static String getIpAddr() {
        HttpServletRequest request = getRequest();
        return (request == null) ? "" : getIpAddr(request);
    }

    /**
     * 获取request请求的真实ip
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader(X_FORWARDED_FOR);
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(X_REAL_IP);
        }

        if (StringUtils.isNotEmpty(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            return ip.split(",")[0];
        } else {
            return request.getRemoteAddr();
        }
    }

    /**
     * 判断传入的text是否满足patterns，对指定的文本进行模糊匹配
     *
     * @param text     要进行模糊匹配的文本
     * @param patterns java正则表达式组，不区分大小写,多个表达式用逗号分隔
     * @return
     */
    public static boolean isMatched(final String text, String... patterns) {
        int cacheKey = Objects.hash(text, StringUtils.join(patterns, ":"));
        try {
            //使用cache，避免每次都解析
            return (boolean)PATTERN_MATCH_CACHE.get(cacheKey, () -> {
                if (text != null && patterns != null) {
                    List<String> patternList = Stream.of(patterns).flatMap(
                        pattern -> commaDelimitedListToSet(pattern).stream()).filter(StringUtils::isNotBlank).collect(
                        Collectors.toList());
                    return patternList.stream().anyMatch(pattern -> Pattern.compile(pattern, Pattern.DOTALL)
                        .matcher(text.trim()).matches());
                } else {
                    return false;
                }
            });
        } catch (ExecutionException e) {
            log.error("ExecutionException when handling cacheKey", e);
            return false;
        }

    }

}
