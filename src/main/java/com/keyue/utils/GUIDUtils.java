package com.keyue.utils;

import java.util.UUID;

public class GUIDUtils {
	
	public static String generateGUID(){
        return UUID.randomUUID().toString().replace("-","");
    }

}
