package com.haoxuan.demo.controller;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class HomeControllerTest {

    private MockMvc mockMvc;

    @Before
    public void setUp(){
        final HomeController homeController = new HomeController();

        mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
    }

    @Test
    public void HomeTest() throws Exception {
        String request="This is Test String!";

        MvcResult mvcResult=
                mockMvc.perform(
                get("/home/hello")
        ).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
}
