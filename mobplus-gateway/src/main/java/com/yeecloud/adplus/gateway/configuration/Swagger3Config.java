//package com.yeecloud.adplus.gateway.configuration;
//
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//
///**
// * @author: Leonard
// * @create: 2021/8/31
// */
//@Configuration
//public class Swagger3Config {
//
//    @Value("${swagger.swaggerEnabled}")
//    Boolean swaggerEnabled;
//
//    @Bean
//    public Docket createRestApi() {
//        return new Docket(DocumentationType.OAS_30)
//                .apiInfo(apiInfo())
//                .enable(swaggerEnabled)
//                .select()
//                // 扫描的路径使用@Api的controller
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//                // 指定路径处理,PathSelectors.any()代表所有的路径
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("gateway 接口文档")
//                .description("")
//                .contact(new Contact("吴子仲", "", "Leonard@mobplus.net"))
//                .version("1.0")
//                .build();
//    }
//}
