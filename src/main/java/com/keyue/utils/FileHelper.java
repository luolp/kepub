package com.keyue.utils;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.keyue.utils.Constant.Charset.UTF_8;

public class FileHelper {
    public static List<String> readLinesAndClose(String filePath){
        List<String> result = new ArrayList<>();
        InputStream inputStream;
        BufferedReader reader = null;
        try{
            inputStream = new FileInputStream(filePath);
            reader = new BufferedReader(new InputStreamReader(inputStream, UTF_8));
            String line ;
            while ((line = reader.readLine()) != null) {
                result.add(line.trim());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }finally {
            close(reader);
        }
        return result;
    }

    public static String readAll(String filePath){
        String content = "";
        InputStream is = null;
        try{
            is = new FileInputStream(filePath);   //读文件 临时
            int iAvail = is.available();
            byte[] bytes = new byte[iAvail];
            is.read(bytes);
            content = new String(bytes,UTF_8);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close(is);
        }
        return content;
    }
    public static void close(Closeable closed) {
        if (closed != null) {
            try {
                closed.close();
            } catch (IOException ignore) {
                // can ignore
            }
        }
    }
}
