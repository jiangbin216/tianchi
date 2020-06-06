package com.rul.tianchi;

import com.rul.tianchi.filter.FilterData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan(basePackages = "com.rul.tianchi")
public class TianchiApplication {

    public static void main(String[] args) {
        String port = System.getProperty("server.port", "8080");
        if (NodePort.isFilter()) {
            FilterData.initCache();
        }
        SpringApplication.run(TianchiApplication.class,
                "--server.port=" + port
        );
    }

}
