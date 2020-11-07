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

    public static String getContentV2(String filePath, List<MarkedSymbol> markedSymbolList){
        String content = FileHelper.readAll(filePath);

        int cslen = markedSymbolList.size();
        int cspos = cslen - 1;
        content = content.replace("\r\n","\n");
        StringBuilder sb = new StringBuilder(content);

        //计算换行符位置
        List<Integer> posList = new ArrayList<>();
        int npos = -1;
        while ((npos = sb.indexOf("\n",npos + 1)) != -1){
            posList.add(npos);
        }

        //插入标记
        //从后往前计算，避免插入标签后重新计算当前处理位置
        for (int i = sb.length() - 1; i >= 0 && cspos >=0; i--) {
            //插入到结尾处
            if(markedSymbolList.get(cspos).getSymbolPos() >= sb.length()){
                sb.append("<a>...</a>");
                cspos --;
            }
            if (i == markedSymbolList.get(cspos).getSymbolPos()){
                if(markedSymbolList.get(cspos).getType() == 0){
                    sb.insert(i+1,"</a>");
                    sb.insert(i,"<a>");
                } else{
                    // 查询出注释
//                    sb.insert(i,"<a class='marker' data-v='" + markedSymbolList.get(cspos).() + "'>注</a>");
                }
                cspos --;
            }
        }

        //从新计算换行符位置
        List<Integer> newPosList = new ArrayList<>();
        npos = -1;
        while ((npos = sb.indexOf("\n",npos + 1)) != -1){
            newPosList.add(npos);
        }

        assert (posList.size() == newPosList.size());
        //加入html p标签
        String insertStr = "<p>";
        int revisePos = 0;
        sb.insert(0,insertStr);
        revisePos += insertStr.length();
        for (int i = 0; i < posList.size(); i++) {
            insertStr = "</p>\n<p>";
            sb.replace(newPosList.get(i) + revisePos,newPosList.get(i) + 1 + revisePos,insertStr);
            revisePos += (insertStr.length() - 1); //1=="\n".length
        }
        insertStr = "</p>";
        sb.append(insertStr);
        revisePos += insertStr.length();

        return sb.toString();
    }
}
