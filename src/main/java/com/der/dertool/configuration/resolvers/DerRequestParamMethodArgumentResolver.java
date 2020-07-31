package com.der.dertool.configuration.resolvers;

import com.alibaba.fastjson.JSONObject;
import com.der.dertool.annotations.DerRequestParam;
import com.der.dertool.configuration.servlet.BodyReadHttpServletRequestWrapper;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.support.MultipartResolutionDelegate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @program: der-tool
 * @description: ${description}
 * @author: long
 * @create: 2020-07-30 17:33
 */
public class DerRequestParamMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver {

    @Override
    protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
        DerRequestParam derRequestParam = parameter.getParameterAnnotation(DerRequestParam.class);
        return derRequestParam == null? new DerNameValueInfo() : new DerNameValueInfo(derRequestParam);
    }

    @Override
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
        HttpServletRequest servletRequest = request.getNativeRequest(HttpServletRequest.class);

        if (servletRequest != null) {
            Object mpArg = MultipartResolutionDelegate.resolveMultipartArgument(name, parameter, servletRequest);
            if (mpArg != MultipartResolutionDelegate.UNRESOLVABLE) {
                return mpArg;
            }
        }

        Object arg = null;
        MultipartRequest multipartRequest = request.getNativeRequest(MultipartRequest.class);
        if (multipartRequest != null) {
            List<MultipartFile> files = multipartRequest.getFiles(name);
            if (!files.isEmpty()) {
                arg = (files.size() == 1 ? files.get(0) : files);
            }
        }
        if (arg == null) {
            BodyReadHttpServletRequestWrapper requestWrapper = request.getNativeRequest(BodyReadHttpServletRequestWrapper.class);
            if (null != requestWrapper) {
                arg = getValueFromRequest(requestWrapper, parameter.getParameter().getType(), name);
            }
        }
        return arg;
    }

    private Object getValueFromRequest(BodyReadHttpServletRequestWrapper request, Class clazz, String name) {
        try {
            JSONObject jsonObject = request.getReaderParams();
            return jsonObject.getObject(name, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(DerRequestParam.class);
    }

    protected static class DerNameValueInfo extends NamedValueInfo {

        public DerNameValueInfo() {
            super("", false, "");
        }

        public DerNameValueInfo(DerRequestParam derRequestParam) {
            super(derRequestParam.value(), derRequestParam.required(), derRequestParam.defaultValue());
        }
    }
}
