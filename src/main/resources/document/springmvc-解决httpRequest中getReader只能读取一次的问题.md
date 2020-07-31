# springmvc-解决httpRequest中getReader只能读取一次的问题(自定义参数解析)

## 重写HttpServletRequest
com.der.dertool.configuration.servlet.BodyReadHttpServletRequestWrapper

## 新添加过滤器
com.der.dertool.configuration.filter.BodyReaderFilter

## 新建参数resolver
com.der.dertool.configuration.resolvers.DerRequestParamMethodArgumentResolver
com.der.dertool.configuration.resolvers.ResolverConfig


