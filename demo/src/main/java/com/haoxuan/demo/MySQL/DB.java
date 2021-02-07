package com.haoxuan.demo.MySQL;

import com.haoxuan.demo.Entity.Userinfo;
import com.sun.corba.se.impl.orb.PrefixParserAction;

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
    public boolean addUser(Userinfo info)  {
        boolean res=false;
        try{
            Connection conn= DriverManager.getConnection(url,userName,password);
            String sql="INSERT INTO userInfo (email,password,firstName,lastName) VALUES (?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1,info.getEmail());
            statement.setString(2,info.getPassword());
            statement.setString(3,info.getFirstName());
            statement.setString(4,info.getLastName());

            res =statement.execute();
            statement.close();
            conn.close();

        }catch (SQLException e){
            e.printStackTrace();
        }finally{

        }
        return res;
    }
    //query user infomation from the userInfo table by email
    public Userinfo queryUser(String email){
        Userinfo info=null;
        try{
            Connection conn= DriverManager.getConnection(url,userName,password);
            String sql="SELECT * FROM userInfo WHERE  email='"+email+"';";

            System.out.println(sql);
            Statement statement =conn.createStatement();
            ResultSet res=statement.executeQuery(sql);

            while(res.next()){
                String mail=res.getNString("email");
                String fname=res.getNString("firstName");
                String lname=res.getNString("lastName");

                info=new Userinfo(fname,lname,mail,"Hiden");
            }
            statement.close();
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally{
        }

        return info;
    }

    //update user information from the userInfo table by email and password
    public boolean updateUser(Userinfo info){
        int rows=0;
        try{

            Connection conn= DriverManager.getConnection(url,userName,password);
            String sql="UPDATE userInfo SET firstName ='"+info.getFirstName()+"', lastName='"+info.getLastName()
                    +"' WHERE email = '"+info.getEmail()+"' and password="+info.getPassword()+";";

            System.out.println(sql);
            Statement statement =conn.createStatement();
            rows=statement.executeUpdate(sql);

            statement.close();
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally{
        }

        if(rows>0) return true;
        else return false;

    }
}
