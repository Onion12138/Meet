package com.ecnu;

import com.alibaba.fastjson.JSON;
import com.ecnu.dto.OrderRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * @author onion
 * @date 2019/12/21 -10:49 上午
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MeetHereApplication.class)
public class MeetHereApplicationTests {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;
    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
//    private RestTemplate restTemplate = new RestTemplate();
    private String[] gymIdList = {"1576735101615918637","1576735204701168411","1576735258162278886","1576735298305583694","1576735397351353004",
    "1576735430522746891","1576735459315443381","1576735499269682613","1576735552118835509","1576735660554433911","1576735768316552263","1576735803662110387"
            ,"1576735922138154448","1576736038219173033","1576736117075412419","1576736117075412420","1576736117075412421"};
    private String[] userId = {"969023010@qq.com","969023011@qq.com","969023012@qq.com","969023013@qq.com","969023014@qq.com","969023015@qq.com","969023016@qq.com",
    "969023017@qq.com","969023018@qq.com","969023019@qq.com","744673725@qq.com","10175101226@stu.ecnu.edu.cn"};
    @Test
    public void generateOrder() throws Exception {
        int gymLength = gymIdList.length;
        int userLength = userId.length;
        Random random = new Random();
        for (int i = 1; i <= 31; i++) {
            for (int j = 16; j <= 36; j+= 4) {
                int gymIndex = random.nextInt(gymLength);
                int userIndex = random.nextInt(userLength);
                OrderRequest request = new OrderRequest();
                request.setGymId(gymIdList[gymIndex]);
                request.setUserEmail(userId[userIndex]);
                request.setDate("2019/12/" + (i < 10 ? "0" + i : i));
                request.setStartTime(j);
                request.setEndTime(j + random.nextInt(4) + 1);
                String content = JSON.toJSONString(request);
                MvcResult mvcResult = mockMvc.perform(post("/order/testOnly").contentType(MediaType.APPLICATION_JSON).content(content)).andReturn();
//                MultiValueMap<String, OrderRequest> body = new LinkedMultiValueMap<>();
//                body.add("request",request);
//                HttpEntity <MultiValueMap> requestEntity = new HttpEntity<>(body);
//                restTemplate.postForEntity("http://localhost:8080/order/testOnly", requestEntity,String.class);
            }
        }
    }
}
