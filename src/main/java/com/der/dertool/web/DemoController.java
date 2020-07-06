package com.der.dertool.web;

import com.der.dertool.domain.IpTable;
import com.der.dertool.repository.IpTableRepotitory;
import com.der.dertool.util.Ipv4Covert;
import com.der.dertool.util.WebIpUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @program: der-tool
 * @description: ${description}
 * @author: long
 * @create: 2020-07-03 22:09
 */
@RestController
@RequestMapping("demo")
public class DemoController {

    @Resource
    private IpTableRepotitory ipTableRepotitory;

    @RequestMapping("test")
    public String test(HttpServletRequest request) {
        String address = WebIpUtil.getIpAddress(request);
        if (StringUtils.isBlank(address)) {
            return "fail";
        }
        IpTable ip = new IpTable();
        ip.setIp(Ipv4Covert.bytesToInt(Ipv4Covert.ipToBytes(address)));
        ipTableRepotitory.save(ip);
        return "success";
    }
}
