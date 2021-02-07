package com.haoxuan.demo.controller;

import com.haoxuan.demo.Entity.Response;
import com.haoxuan.demo.Entity.Userinfo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserInfoController {

    @RequestMapping(value = "createUser",produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public Response createUser(@RequestBody Userinfo info){


        Response res= new Response();
        res.setCode(200);
        res.setDesc("info");

        return res;

    }
    @RequestMapping(value = "updateUser",produces = {"application/json;charset=UTF-8"})
    public Response updateUserinfo(){

        Response res= new Response();
        res.setCode(200);
        res.setDesc("success");

        return res;

    }

    @RequestMapping(value = "getUser",produces = {"application/json;charset=UTF-8"})
    public Response getUserinfo(){

        Response res= new Response();
        res.setCode(200);
        res.setDesc("success");

        return res;

    }
}
