package com.der.dertool.configuration.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

/**
 * @program: der-tool
 * @description: ${description}
 * @author: long
 * @create: 2020-07-30 15:24
 */
public class BodyReadHttpServletRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 请求的byte数组 -- 定义为final 保证请求参数不会
     */
    private final byte[] body;

    /**
     * 请求参数
     */
    private final Map<String, String[]> params = Maps.newHashMap();

    /**
     * 请求的byte数组组成的输入流
     */
    private ServletInputStream inputStream;

    /**
     * 请求的输入流读取bufferedReader
     */
    private BufferedReader bufferedReader;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public BodyReadHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        //请求请求交给父类，不重载的方法可以调用父类方法；防止出错
        super(request);
        this.body = IOUtils.toByteArray(request.getInputStream());
        this.params.putAll(request.getParameterMap());
    }

    /**
     * 获取请求reader里的参数
     * @return json
     * @throws IOException IO异常
     */
    public JSONObject getReaderParams() throws IOException {
        BufferedReader reader = getReader();
        StringBuilder sb = new StringBuilder();
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            sb.append(line);
        }
        return StringUtils.isBlank(sb)? new JSONObject() : JSON.parseObject(sb.toString());
    }

    /**
     * 添加所有的参数
     * @param paramters 参数集合
     */
    public void addAllParamters(Map<String, Object> paramters) {
        if (MapUtils.isEmpty(paramters)) {
            return;
        }
        for (Map.Entry<String, Object> entry : paramters.entrySet()) {
            this.addParamter(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 添加一个参数
     * @param key 键
     * @param value 值
     */
    public void addParamter(String key, Object value) {
        if (null == value) {
            return;
        }
        if (value instanceof Integer || value instanceof Long || value instanceof Double || value instanceof Float || value instanceof Boolean
                || value instanceof Character || value instanceof  Byte) {
            this.params.put(key, new String[] {value.toString()});
        } else {
            this.params.put(key, new String[] {JSON.toJSONString(value)});
        }
    }

    @Override
    public String getParameter(String name) {
        String[] values = this.getParameterValues(name);
        return (null == values || values.length == 0)? null : values[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return this.params;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(this.params.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        return this.params.get(name);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (this.bufferedReader == null) {
            this.bufferedReader = new BufferedReader(new InputStreamReader(this.inputStream, this.getCharacterEncoding()));
        }
        return this.bufferedReader;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (this.inputStream == null) {
            this.inputStream = new ReReadServletInputStream(new ByteArrayInputStream(this.body));
        }
        return this.inputStream;
    }

    /**
     * 自定义重写ServletInputStream
     */
    private class ReReadServletInputStream extends ServletInputStream {

        private final ByteArrayInputStream inputStream;

        public ReReadServletInputStream(ByteArrayInputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public boolean isFinished() {
            return inputStream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {

        }

        @Override
        public int read() throws IOException {
            return inputStream.read();
        }
    }

}
