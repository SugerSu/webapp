package com.haoxuan.demo.controller;

import com.haoxuan.demo.Auth.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@RunWith(SpringRunner.class)

class UserControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(wac)
                .build(); //初始化MockMvc对象

        CustomUserDetailsService user  = new CustomUserDetailsService();

    }

    @Test
    void hello() throws Exception {



        mvc.perform(MockMvcRequestBuilders.get("/user/hello")
                .contentType(MediaType.ALL))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());


    }

    @Test
    void createUser() throws Exception {
        String user="{\"first_name\":\"Lucy\",\"last_name\":\"Li\",\"username\":\"neo@neu.edu\",\"password\":\"neo12345678\"}";

        mvc.perform(MockMvcRequestBuilders.post("/user/create")
                .content(user)//传json参数
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .characterEncoding("utf-8")

        )
                //.andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void queryUser() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/user/getInfo")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void update() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/updateInfo")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print());
    }
}