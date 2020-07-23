package com.der.dertool.web;

import com.der.dertool.annotations.JwtTokenPass;
import com.der.dertool.constants.DerResponse;
import com.der.dertool.util.JwtUtils;
import com.der.dertool.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: der-tool
 * @description: ${description}
 * @author: long
 * @create: 2020-07-22 15:06
 */
@RestController
@RequestMapping("user")
public class JwtUserController {

    @JwtTokenPass
    @RequestMapping("login")
    public DerResponse login(UserVo userVo, HttpServletResponse response) {
        if (null == userVo || StringUtils.isAnyBlank(userVo.getPassword(), userVo.getUsername())) {
            return DerResponse.openFail();
        }
        if (!"K0790016".equals(userVo.getUsername()) || !"Admin@123".equals(userVo.getPassword())) {
            return DerResponse.openFail();
        }
        String sign = JwtUtils.sign(userVo.getUsername(), userVo.getPassword());
        if (null == sign) {
            return DerResponse.openFail();
        }
        Cookie cookie = new Cookie("access_token", sign);
        cookie.setMaxAge(JwtUtils.EXPIRE_TIME.intValue());
        response.addCookie(cookie);
        return DerResponse.openSuccess();
    }

}
