package com.ecnu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author onion
 * @date 2019/12/10 -10:47 下午
 */
@SpringBootApplication
public class MeetHereApplication {
    public static void main(String[] args) {
        SpringApplication.run(MeetHereApplication.class, args);
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
