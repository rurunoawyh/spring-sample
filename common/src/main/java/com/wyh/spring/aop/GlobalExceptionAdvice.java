package com.wyh.spring.aop;

import com.wyh.spring.util.WebUtil;
import com.wyh.spring.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wb-wyh270612
 * @date 2018/12/4  上午11:18
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionAdvice extends AbstractErrorController implements HandlerExceptionResolver {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    public GlobalExceptionAdvice(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @Override
    public String getErrorPath() {
        return null;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        log.error("resolveException", e);
        String errorCode = "500";
        /*if (e instanceof BizException) {
            errorCode = ((BizException)e).getCode();
        }*/
        try {
            return getExceptionModelAndView(httpServletRequest, errorCode, e.getMessage());
        } catch (Exception e1) {
            log.error("exception when resolveException", e1);
            return null;
        }
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e, HttpServletRequest request) throws Exception {
        log.error("handleException", e);

        return getExceptionModelAndView(request, "", e.getMessage());
    }

    /**
     * 异常响应
     *
     * @param request
     * @param errorCode
     * @param errorMsg
     * @return
     * @throws Exception
     */
    private ModelAndView getExceptionModelAndView(HttpServletRequest request, String errorCode, String errorMsg)
            throws Exception {
        ModelAndView mav = new ModelAndView();

        boolean isResponseJson = isJsonResultRequest(request);
        if (isResponseJson) {
            Result jsonResult = Result.fail(errorMsg, errorCode);
            WebUtil.writeJsonResponse(jsonResult);
        } else {
            mav.setViewName("errorPath");
            mav.addObject("errorCode", errorCode);
            mav.addObject("errorMsg", errorMsg);
        }
        return mav;
    }


    /**
     * 判断请求是否返回json格式数据
     *
     * @param request
     * @return
     * @throws Exception
     */
    private boolean isJsonResultRequest(HttpServletRequest request) throws Exception {
        //当前的请求方法返回ResponseBody
        boolean isResponseJson = false;
        HandlerExecutionChain handlerExecutionChain = this.requestMappingHandlerMapping.getHandler(request);
        Object handler = (handlerExecutionChain == null) ? null : handlerExecutionChain.getHandler();
        if (handler != null && handler instanceof HandlerMethod) {
            MethodParameter returnType = ((HandlerMethod) handler).getReturnType();
            isResponseJson = returnType.hasMethodAnnotation(ResponseBody.class) || returnType.getMethod()
                    .getDeclaringClass().isAnnotationPresent(RestController.class);
        }
        return isResponseJson;
    }
}
