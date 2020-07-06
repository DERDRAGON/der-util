package com.der.dertool.web;

import com.der.dertool.constants.DerResponse;
import com.der.dertool.vo.IpVo;
import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * api接口文档演示controller
 */
@RestController
@RequestMapping("docs")
public class JApiDocsController {

    /**
     * ip列表
     * @param ipVo 查询VO
     * @return IP列表
     */
    @RequestMapping(value = "list", method = {RequestMethod.GET,  RequestMethod.POST}  )
    public DerResponse<List<IpVo>> getIpList(IpVo ipVo) {
        List<IpVo> list = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            IpVo ip = new IpVo();
            ip.setId(i);
            ip.setIp("这是一个IP" + ip);
            list.add(ip);
        }
        return DerResponse.openSuccess(list);
    }

}
