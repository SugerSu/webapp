package com.haoxuan.demo.Helper;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;


public class Helper {

    //Generate 8 length salt
    private final String tokenSet="1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public String genSalt(){
        StringBuilder sb = new StringBuilder();
        int len=tokenSet.length();
        Random ran= new Random();

        for(int i=0;i<8;i++){
            int idx=ran.nextInt(len);
            sb.append(tokenSet.charAt(idx));
        }

        return sb.toString();
    }


    //Base64 Encoding
    public String BEncrypt(String rawPwd,String salt){
        String encodeStr = Base64.getEncoder().encodeToString((salt+":"+rawPwd).getBytes(StandardCharsets.UTF_8));
        return encodeStr;
    }
    //Base64 Decoding
    public String BDecrypt(String encrtpPwd,String salt){
        String decode =new String(Base64.getDecoder().decode(encrtpPwd),StandardCharsets.UTF_8);
        int index=decode.indexOf(":");
        return decode.substring(index+1);
    }
    public String getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");

        Date date = new Date();
        //System.out.println("现在时间：" + sdf.format(date));
        return sdf.format(date);
    }

    //check password complexity
    //the password should be 8-12 bits
    //should only have number and a-z or A-z
    //no signs like !@#
    public boolean checkPwd(String str){

        if(str.length()<8|| str.length()>12) return false;
        Set<Character> nums = new HashSet<>();
        Set<Character> chars = new HashSet<>();

        for(char ch:str.toCharArray()){
            if(ch>='0' && ch<='9') nums.add(ch);
            else if(ch>='a'&& ch<='z') chars.add(ch);
            else if(ch>='A'&& ch<='Z') chars.add(ch);
            else return false;
        }

        if(nums.size()<=0 || chars.size()<=0) return false;

        return true;
    }
}
