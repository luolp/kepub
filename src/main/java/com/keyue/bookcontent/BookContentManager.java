package com.keyue.bookcontent;

import com.keyue.dao.model.MarkedSymbol;
import com.keyue.utils.FileHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BookContentManager {
    public static String getContent(String filePath, Map<Integer, List<MarkedSymbol>> markedSymbolListMap){
        List<String> contentList = FileHelper.readLinesAndClose(filePath);

        //给每个段落加上id 这一步可以预先处理好
        String pattern_pre = "<p>(.*)</p>";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern_pre);
        // 现在创建 matcher 对象
        int count = 1;
        for (int i = 0; i < contentList.size(); i++) {
            Matcher m = r.matcher(contentList.get(i));
            if (m.find()){
                String newparagraph = String.format("<p id=\"%05d\">%s</p>",count++,m.group(1));
                contentList.set(i,newparagraph);
            }
        }



        //data for test
//        List<Integer> markedCharsP1 = new ArrayList<>();
//        markedCharsP1.add(20);
//        markedCharsP1.add(21);
//        markedCharsP1.add(22);
//        markedCharsP1.add(70);
//        markedCharsMap.put(1,markedCharsP1);


        String pattern = "<p id=\"([0-9]*)\">(.*)</p>";    //todo 待改进
        Pattern patternC = Pattern.compile(pattern);

        contentList = contentList.stream().map(paragraph -> {

            Matcher m = patternC.matcher(paragraph);
            if(!m.find()){
                return paragraph;
            }

            String paragraphId = m.group(1);
            String rawContent = m.group(2);

            List<MarkedSymbol> markedSymbolList = markedSymbolListMap.get(Integer.parseInt(paragraphId));
            if(null == markedSymbolList || markedSymbolList.isEmpty()){
                return paragraph;
            }
            //markedChars 正序
            int cslen = markedSymbolList.size();
            int cspos = cslen - 1;

            StringBuilder sb = new StringBuilder(rawContent);
            //从后往前计算，避免插入标签后重新计算当前处理位置
            for (int i = sb.length() - 1; i >= 0 && cspos >=0; i--) {
                if (i == markedSymbolList.get(cspos).getSymbolPos()){
                    cspos --;
                    sb.insert(i,"</a>");
                    sb.insert(i-1,"<a>");
                }
            }

            return String.format("<p id=\"%05d\">%s</p>",Integer.parseInt(paragraphId),sb.toString());
        }).collect(Collectors.toList());

        String result = contentList.stream().collect(Collectors.joining(System.lineSeparator()));

        return result;
    }
}
