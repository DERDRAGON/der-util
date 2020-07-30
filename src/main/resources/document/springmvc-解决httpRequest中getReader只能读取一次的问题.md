# springmvc-解决httpRequest中getReader只能读取一次的问题

## 重写HttpServletRequest
com.der.dertool.configuration.servlet.BodyReadHttpServletRequestWrapper

## 新添加过滤器
com.der.dertool.configuration.filter.BodyReaderFilter

## 新建参数resolver