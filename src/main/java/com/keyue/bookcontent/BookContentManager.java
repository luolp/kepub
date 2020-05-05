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
                    sb.insert(i,"<a class='marker' data-v='戚蓼生序:吾闻绛树两歌，一声在喉，一声在鼻；黄华二牍，左腕能楷，右腕能草。神乎技也，吾未之见也。今则两歌而不分乎喉鼻，二牍而无区乎左右，一声也而两歌，一手也而二牍，此万万不能有之事，不可得之奇，而竟得之《石头记》一书。嘻！异矣。夫敷华掞藻、立意遣词无一落前人窠臼，此固有目共赏，姑不具论；第观其蕴于心而抒于手也，注彼而写此，目送而手挥，似谲而正，似则而淫，如春秋之有微词、史家之多曲笔。试一一读而绎之：写闺房则极其雍肃也，而艳冶已满纸矣；状阀阅则极其丰整也，而式微已盈睫矣；写宝玉之淫而痴也，而多情善悟，不减历下琅琊；写黛玉之妒而尖也，而笃爱深怜，不啻桑娥石女。他如摹绘玉钗金屋，刻画芗泽罗襦，靡靡焉几令读者心荡神怡矣，而欲求其一字一句之粗鄙猥亵，不可得也。盖声止一声，手只一手，而淫佚贞静，悲戚欢愉，不啻双管之齐下也。噫！异矣。其殆稗官野史中之盲左、腐迁乎？然吾谓作者有两意，读者当具一心。譬之绘事，石有三面，佳处不过一峰；路看两蹊，幽处不逾一树。必得是意，以读是书，乃能得作者微旨。如捉水月，只挹清辉；如雨天花，但闻香气，庶得此书弦外音乎？乃或者以未窥全豹为恨，不知盛衰本是回环，万缘无非幻泡，作者慧眼婆心，正不必再作转语，而千万领悟，便具无数慈航矣。彼沾沾焉刻楮叶以求之者，其与开卷而寤者几希！'>注</a>");
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
