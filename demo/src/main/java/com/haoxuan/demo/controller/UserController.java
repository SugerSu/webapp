package com.haoxuan.demo.controller;

import com.haoxuan.demo.Entity.*;
import com.haoxuan.demo.Helper.Helper;
import com.haoxuan.demo.MySQL.DB;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserController {


    private Helper help = new Helper();

    @RequestMapping("/hello")
    public String hello(){
        return "Welcome to this Web with Auth!";
    }

    /**
     * create new user
     * if email already exist return failed!
     * Password stored in base64 encoding.
     */
    @RequestMapping(value = "/create",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
    public CreateUserResponse createUser(@RequestBody CreateUserRequest user,HttpServletResponse response){
        CreateUserResponse res=new CreateUserResponse();
        try{

            String userName=user.getUsername();
            if(userName==null || userName.length()<=0){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }
            //check the user exited or not
            UserTable info = DB.getInstance().queryUser(userName);
            if(info!=null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }

            //check the password.
            boolean pwdRes=help.checkPwd(user.getPassword());
            if(!pwdRes){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }

            String salt = help.genSalt();
            String encodePwd=help.BEncrypt(user.getPassword(),salt);
            String timeStamp=help.getDate();
            UserTable ut = new UserTable(
                    user.getFirst_name(),
                    user.getLast_name(),
                    user.getUsername(),
                    encodePwd,
                    timeStamp,
                    timeStamp,
                    salt
            );

            //add to database
            boolean status= DB.getInstance().addUser(ut);

            UserTable queryUser = DB.getInstance().queryUser(userName);
            if(status){
                response.setStatus(HttpServletResponse.SC_CREATED);
                res.setId(queryUser.getId());
                res.setFirst_name(queryUser.getFirst_name());
                res.setLast_name(queryUser.getLast_name());
                res.setUsername(queryUser.getUsername());
                res.setAccount_created(queryUser.getAccount_created());
                res.setAccount_updated(queryUser.getAccount_updated());
                return res;

            }else{
                response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Something Wrong");
            response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);

        }
        return null;
    }

    /**
     * Query user info
     */
    @RequestMapping(value = "/getInfo",produces = {"application/json;charset=UTF-8"})
    public GetUserResponse queryUser(HttpServletResponse response){

        try{
            //get user info from login
            String userName = null;
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principal instanceof UserDetails) {
                userName = ((UserDetails)principal).getUsername();
            }else {
                userName = principal.toString();
            }
            //fist check user info
            UserTable info = DB.getInstance().queryUser(userName);

            GetUserResponse userResponse = new GetUserResponse(
                    info.getId(),
                    info.getFirst_name(),
                    info.getLast_name(),
                    info.getUsername(),
                    info.getAccount_created(),
                    info.getAccount_updated()
            );


            return userResponse;

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Something Wrong");
            response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
        }
        return null;
    }

    /**
     * Update user info
     */
    @RequestMapping(value = "updateInfo",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
    public void update(@RequestBody UpdateUserRequest user, HttpServletResponse response){

        try{
            //get user info from login
            String userName = null;
            String enpwd=null;
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principal instanceof UserDetails) {
                userName = ((UserDetails)principal).getUsername();
            }else {
                userName = principal.toString();
            }
            //fist check user info
            if(user.getPassword()!=null){
                //check the password.
                boolean pwdRes=help.checkPwd(user.getPassword());
                if(!pwdRes){
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                //fist check user info
                UserTable info = DB.getInstance().queryUser(userName);
                enpwd=help.BEncrypt(user.getPassword(),info.getSalt());

            }


            System.out.println(userName);
            //update user info
            UserTable newInfo = new UserTable();
            newInfo.setFirst_name(user.getFirst_name());
            newInfo.setLast_name(user.getLast_name());
            newInfo.setUsername(userName);
            newInfo.setPassword(enpwd);
            newInfo.setAccount_updated(help.getDate());

            boolean slqRes=DB.getInstance().updateUser(newInfo);

            if(slqRes){
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }else{
                response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
            }


        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Something Wrong");
            response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
        }

    }
}
