- [Swagger配置全局Response Model](#swagger配置全局response-model)
  - [注解方式配置](#注解方式配置)
  - [全局配置](#全局配置)

# Swagger配置全局Response Model

## 注解方式配置
使用注解方式，可以单独为一个API配置Response Model

**缺点**：每个Controller上都会有一大堆的、重复的@ApiResponses注解，以至于把正常的业务代码淹没。

## 全局配置
通过Swagger的全局配置，可以自定义默认的Response Model。
1. 首先，在任何一个Controller上，添加至少一个@ApiResponses注解，标明response的类。
```
@ApiResponses({@ApiResponse(code = 500, message = "服务器内部错误", response = ApiError.class)})
```
2. 然后，在Swagger配置类的Docket上加入globalResponseMessage
```
@Bean
public Docket userApi() {
    List<ResponseMessage> responseMessageList = new ArrayList<>();
    responseMessageList.add(new ResponseMessageBuilder().code(404).message("找不到资源").responseModel(new ModelRef("ApiError")).build());
    responseMessageList.add(new ResponseMessageBuilder().code(409).message("业务逻辑异常").responseModel(new ModelRef("ApiError")).build());
    responseMessageList.add(new ResponseMessageBuilder().code(422).message("参数校验异常").responseModel(new ModelRef("ApiError")).build());
    responseMessageList.add(new ResponseMessageBuilder().code(500).message("服务器内部错误").responseModel(new ModelRef("ApiError")).build());
    responseMessageList.add(new ResponseMessageBuilder().code(503).message("Hystrix异常").responseModel(new ModelRef("ApiError")).build());

    return new Docket(DocumentationType.SWAGGER_2)

            .globalResponseMessage(RequestMethod.GET, responseMessageList)
            .globalResponseMessage(RequestMethod.POST, responseMessageList)
            .globalResponseMessage(RequestMethod.PUT, responseMessageList)
            .globalResponseMessage(RequestMethod.DELETE, responseMessageList)

            .build()
            .apiInfo(apiInfo());

}
```
ps:
*请注意第一条不能省略，new ModelRef("ApiError")，会查询之前定义@ApiResponse的response中指定的class，该class若定义了@ApiModel，不要添加value，否则会影响model*