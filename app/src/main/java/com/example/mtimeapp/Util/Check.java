package com.example.mtimeapp.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Check {
    String code;
    public  Check(String code){
        this.code=code;
    }

    public boolean password(){
        Pattern pattern = Pattern.compile("[\\S]{8,16}$");
        Matcher matcher = pattern.matcher(code);
        return matcher.matches();
    }

}
