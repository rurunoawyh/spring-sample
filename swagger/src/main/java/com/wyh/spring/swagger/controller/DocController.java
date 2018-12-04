package com.wyh.spring.swagger.controller;

import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wb-wyh270612
 * @date 2018/12/3  下午3:03
 */
@RestController
@Api(value = "Api", description = "接口文档")
public class DocController {
    @RequestMapping("/create")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "doc", value = "接受参数的含义", required = false, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求已完成"),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 401, message = "未授权客户机访问数据"),
            @ApiResponse(code = 403, message = "服务器资源不可用，或无权访问"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源，或文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")
    })
    @ApiOperation(value = "markdown文档生成", notes = "支持GET方式", response = String.class)
    public Object create(String doc) {
        return doc;
    }
}
