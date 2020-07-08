- [swagger常用注解](#swagger常用注解)
  - [Api](#api)
  - [ApiOperation](#apioperation)

# swagger常用注解

**常用注解**
- Api
- ApiModel
- ApiModelProperty
- ApiOperation
- ApiParam
- ApiResponse
- ApiResponses
- ResponseHeader

## Api
Api 用在类上，说明该类的作用。可以标记一个Controller类做为swagger 文档资源，使用方式：
```
@Api(value = "/user", description = "Operations about user")
```
与Controller注解并列使用。 属性配置：
| 属性名称       | 备注                                                             |
| -------------- | ---------------------------------------------------------------- |
| value          | url的路径值                                                      |
| tags           | 如果设置这个值、value的值会被覆盖(主显示，value会显示其右侧小字) |
| description    | 对api资源的描述                                                  |
| basePath       | 基本路径可以不配置                                               |
| position       | 如果配置多个Api 想改变显示的顺序位置                             |
| produces       | For example, "application/json, application/xml"                 |
| consumes       | For example, "application/json, application/xml"                 |
| protocols      | Possible values: http, https, ws, wss.                           |
| authorizations | 高级特性认证时配置                                               |
| hidden         | 配置为true 将在文档中隐藏                                        |

在SpringMvc中的配置如下：
```
@Controller
@RequestMapping(value = "/api/pet", produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
@Api(value = "/pet", description = "Operations about pets")
public class PetController {
}
```

## ApiOperation
ApiOperation：用在方法上，说明方法的作用，每一个url资源的定义,使用方式：
```
@ApiOperation(
          value = "Find purchase order by ID",
          notes = "For valid response try integer IDs with value <= 5 or > 10. Other values will generated exceptions",
          response = Order,
          tags = {"Pet Store"})
```
与Controller中的方法并列使用。属性配置：
| 属性名称          | 备注                                                                                                           |
| ----------------- | -------------------------------------------------------------------------------------------------------------- |
| value             | url的路径值                                                                                                    |
| tags              | 如果设置这个值、value的值会被覆盖 （不要写这个，会将显示级别调整到controller级别，样式全乱了）                 |
| responseReference | 指定对响应类型的引用，指定的应用可以使本地引用，也可以是远程引用，将按原样使用，并将覆盖任何指定的response()类 |
| position          | 如果配置多个Api 想改变显示的顺序位置                                                                           |
| produces          | For example, "application/json, application/xml"                                                               |
| consumes          | For example, "application/json, application/xml"                                                               |
| protocols         | Possible values: http, https, ws, wss.                                                                         |
| authorizations    | 高级特性认证时配置                                                                                             |
| hidden            | 配置为true 将在文档中隐藏                                                                                      |
| response          | 返回的对象                                                                                                     |
| responseContainer | 声明响应的容器，有效值为List,Set,Map，任何其他值都将被忽略                                                     |
| httpMethod        | "GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS" and "PATCH"                                                  |
| nickname          | 没什么卵用，写了也ui页面也看不到                                                                               |

在SpringMvc中的配置如下：
```
@RequestMapping(value = "/order/{orderId}", method = GET)
  @ApiOperation(
      value = "Find purchase order by ID",
      notes = "For valid response try integer IDs with value <= 5 or > 10. Other values will generated exceptions",
      response = Order.class,
      tags = { "Pet Store" })
   public ResponseEntity<Order> getOrderById(@PathVariable("orderId") String orderId)
      throws NotFoundException {
    Order order = storeData.get(Long.valueOf(orderId));
    if (null != order) {
      return ok(order);
    } else {
      throw new NotFoundException(404, "Order not found");
    }
  }
```