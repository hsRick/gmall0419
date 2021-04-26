package com.lxj.gmall0419.gmallusermanage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.lxj.gmall0419.gmallusermanage.mapper")
public class GmallUsermanageApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallUsermanageApplication.class, args);
    }

}
