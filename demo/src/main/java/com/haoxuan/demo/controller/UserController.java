package com.haoxuan.demo.controller;

import com.haoxuan.demo.Entity.*;
import com.haoxuan.demo.Helper.Helper;
import com.haoxuan.demo.MySQL.DB;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private Helper help = new Helper();

    /**
     * create new user
     * if email already exist return failed!
     * Password stored in base64 encoding.
     */
    @RequestMapping(value = "create",produces = {"application/json;charset=UTF-8"})
    public Response createUser(@RequestBody CreateUserRequest user){
        Response res= new Response();
        try{

            String userName=user.getEmail();
            //check the user exited or not
            UserTable info = DB.getInstance().queryUser(userName);
            if(info!=null) {
                res.setCode(400);
                res.setDesc("Bad Request: User Already Exist!");
                return res;
            }

            //check the password.
            boolean pwdRes=help.checkPwd(user.getPassword());
            if(!pwdRes){
                res.setCode(406);
                res.setDesc("Not Acceptable: Password size should be 8-12 and only contains 0-9 and a-z and A-Z!");
                return res;
            }

            String salt = help.genSalt();
            String encodePwd=help.BCrypt(user.getPassword(),salt);
            String timeStamp=help.getDate();
            UserTable ut = new UserTable(
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    encodePwd,
                    timeStamp,
                    timeStamp,
                    salt
            );

            //add to database
            boolean status= DB.getInstance().addUser(ut);

            if(status){
                res.setCode(200);
                res.setDesc("Create User Success!");
            }else{
                res.setCode(406);
                res.setDesc("Create User Failed!");
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Something Wrong");
            res.setCode(500);
            res.setDesc(" Internal Server Error: Update Failed! Sever Error!");
        }
        return res;
    }

    /**
     * Query user info
     */
    @RequestMapping(value = "getInfo",produces = {"application/json;charset=UTF-8"})
    public Response queryUser(@RequestBody GetUserRequest user){
        Response res= new Response();
        try{
            //fist check user info
            String userName=user.getUserName();
            String pwd=user.getPassword();
            UserTable info = DB.getInstance().queryUser(userName);

            if(info==null) {
                res.setCode(400);
                res.setDesc("Bad Request: Failed: User Not Exist!");
                return res;
            }

            if(!info.getPassword().equals(help.BCrypt(pwd,info.getSalt()))){
                res.setCode(406);
                res.setDesc("Not Acceptable: Password is wrong!");
                return res;
            }


            GetUserResponse userResponse = new GetUserResponse(
                    info.getFirstName(),
                    info.getLastName(),
                    info.getUserName()
            );

            res.setCode(200);
            res.setDesc("Query Success! "+userResponse.toString());

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Something Wrong");
            res.setCode(500);
            res.setDesc("Internal Server Error: Update Faled! Sever Error!");
        }
        return res;
    }

    /**
     * Update user info
     */
    @RequestMapping(value = "updateInfo",produces = {"application/json;charset=UTF-8"})
    public Response update(@RequestBody UpdateUserRequest user){
        Response res= new Response();
        try{
            //fist check user info
            String userName=user.getUserName();
            String pwd=user.getPassword();
            UserTable info = DB.getInstance().queryUser(userName);

            if(info==null) {
                res.setCode(400);
                res.setDesc("Bad Request: Failed: User Not Exist!");
                return res;
            }

            if(!info.getPassword().equals(help.BCrypt(pwd,info.getSalt()))){
                res.setCode(406);
                res.setDesc("Not Acceptable: Failed: Password is wrong!");
                return res;
            }


            //update user info
            UserTable newInfo = new UserTable();
            newInfo.setFirstName(user.getFirstName());
            newInfo.setLastName(user.getLastName());
            newInfo.setUserName(user.getUserName());
            newInfo.setUpdateTime(help.getDate());

            boolean slqRes=DB.getInstance().updateUser(newInfo);
            GetUserResponse userResponse = new GetUserResponse(
                    user.getFirstName(),
                    user.getLastName(),
                    user.getUserName()
            );

            if(slqRes){
                res.setCode(200);
                res.setDesc("Update Success! "+userResponse.toString());
            }else{
                res.setCode(406);
                res.setDesc("Update Faled! "+userResponse.toString());
            }


        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Something Wrong");
            res.setCode(500);
            res.setDesc("Internal Server Error: Update Faled! Sever Error!");
        }
        return res;
    }
}
