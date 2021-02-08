package com.haoxuan.demo.MySQL;

import com.haoxuan.demo.Entity.UserTable;

import java.sql.*;

public class DB {

    private final String url="jdbc:mysql://localhost:3306/cloud";
    private final String userName="su";
    private final String password="Spring2021!";

    private static DB instance;

    public static synchronized DB getInstance(){
        if(instance == null){
            instance=new DB();
        }
        return instance;
    }
    //add a new user into the userInfo table
    public boolean addUser(UserTable info)  {

        try{
            Connection conn= DriverManager.getConnection(url,userName,password);
            String sql="INSERT INTO user (firstName,lastName,userName,password,salt,createTime,updateTime) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1,info.getFirstName());
            statement.setString(2,info.getLastName());
            statement.setString(3,info.getUserName());
            statement.setString(4,info.getPassword());
            statement.setString(5,info.getSalt());
            statement.setString(6,info.getCreateTime());
            statement.setString(7,info.getUpdateTime());


            statement.execute();
            statement.close();
            conn.close();

        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;

    }

    //query user infomation from the userInfo table by email
    public UserTable queryUser(String email){
        UserTable info=null;
        try{
            Connection conn= DriverManager.getConnection(url,userName,password);
            String sql="SELECT * FROM user WHERE  userName='"+email+"';";

            System.out.println(sql);
            Statement statement =conn.createStatement();
            ResultSet res=statement.executeQuery(sql);

            while(res.next()){

                //skip the first index  of id
                String fname=res.getString(2);
                String lname=res.getString(3);

                String mail=res.getString(4);
                String password=res.getString(5);

                String salt=res.getString(6);
                String createTime=res.getString(7);
                String updateTime=res.getString(8);
                //System.out.println("fname"+fname+" lname"+lname+" mail"+mail+" password"+password+" salt"+salt+" ctime"+createTime+" utime"+updateTime);
                info=new UserTable(
                        fname,
                        lname,
                        mail,
                        password,
                        createTime,
                        updateTime,
                        salt
                );
            }
            System.out.println(info.getUserName()+info.getPassword()+info.getFirstName());
            statement.close();
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally{
        }

        return info;
    }

    //update user information from the userInfo table by email and password
    public boolean updateUser(UserTable info){
        int rows=0;
        try{

            Connection conn= DriverManager.getConnection(url,userName,password);

            String sql="UPDATE user SET firstName ='"+info.getFirstName()
                    +"', lastName='"+info.getLastName()
                    +"', updateTime='"+info.getUpdateTime()
                    +"' WHERE userName = '"+info.getUserName()+"';";

            System.out.println(sql);
            Statement statement =conn.createStatement();
            rows=statement.executeUpdate(sql);

            statement.close();
            conn.close();
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }

    }
}
