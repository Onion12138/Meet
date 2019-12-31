package com.ecnu.controller;

import com.ecnu.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Slf4j
public class IntegrationTestForGymController {

    private final static String REQUEST_MAPPING = "/gym";

    private final static String SUCCESS_MSG = "成功";

    private final static int OK = 200;

    private String tokenForUser;

    private String tokenForAdmin;

    private User testUser;

    private User admin;


}
