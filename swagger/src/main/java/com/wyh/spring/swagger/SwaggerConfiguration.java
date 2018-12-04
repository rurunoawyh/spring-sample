package com.wyh.spring.swagger;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author wb-wyh270612
 * @date 2018/12/3  下午2:57
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    public static final String VERSION = "1.0.0";

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //标题
                .title("Swagger API")
                //描述
                .description("This is to show api description")
                //描述下的超链接
                .license("百度一下")
                .licenseUrl("http://www.baidu.com")
                .termsOfServiceUrl("")
                //版本号
                .version(VERSION)
                .build();
    }

    @Bean
    public Docket customImplementation() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("RestAPI")
                .genericModelSubstitutes(ResponseEntity.class)
                .useDefaultResponseMessages(true)
                .forCodeGeneration(false)
                .select()
                //换成这种通过注解生成接口也可以
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .build()
                .apiInfo(apiInfo());
    }
}
