package com.ecnu.service;

/**
 * @date 2019/12/11 -9:04 下午
 */
public interface MailService {
    void sendMail(String to, String subject,String content);
}
