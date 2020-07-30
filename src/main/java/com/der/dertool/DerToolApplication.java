package com.der.dertool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan(basePackages = "com.der.dertool.configuration")
@SpringBootApplication
public class DerToolApplication {

    public static void main(String[] args) {
        SpringApplication.run(DerToolApplication.class, args);
    }

}
