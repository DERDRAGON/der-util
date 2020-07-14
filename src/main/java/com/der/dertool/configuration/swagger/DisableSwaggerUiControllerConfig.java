package com.der.dertool.configuration.swagger;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: der-tool
 * @description: ${description}
 * @author: long
 * @create: 2020-07-06 14:39
 */
@ConditionalOnProperty(prefix = "swagger",value = {"disabled"}, havingValue = "true", matchIfMissing = true)
@Configuration
@RestController
public class DisableSwaggerUiControllerConfig {

    @RequestMapping(value = "swagger-ui.html", method = RequestMethod.GET)
    public void getSwagger(HttpServletResponse httpResponse) throws IOException {
        httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
    }

}
