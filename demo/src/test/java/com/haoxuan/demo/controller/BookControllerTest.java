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
class BookControllerTest {
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
    void getBooks() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/book/all")
                .contentType(MediaType.ALL))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void createBook() throws Exception {
        String bookinfo="{\"id\": \"d6193106-a192-46db-aae9-f151004ee453\",\"title\": \"Computer Networks\",\"author\": \"Andrew S. Tanenbaum\",\"isbn\": \"978-0132126953\",\"published_date\": \"May, 2020\" }";
        mvc.perform(MockMvcRequestBuilders.post("/book/add")
                .content(bookinfo)//传json参数
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .characterEncoding("utf-8")
        )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void queryBookById() throws Exception {
        String booid="{\"id\":\"d6193106-a192-46db-aae9-f151004ee453\"}";
        mvc.perform(MockMvcRequestBuilders.post("/book/queryBookById")
                .content(booid)//传json参数
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .characterEncoding("utf-8")
        )
                //.andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void deleteBookById() throws Exception {

        String booid="{\"id\":\"d6193106-a192-46db-aae9-f151004ee453\"}";
        mvc.perform(MockMvcRequestBuilders.post("/book/deleteBookById")
                .content(booid)//传json参数
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .characterEncoding("utf-8")
        )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print());
    }
}