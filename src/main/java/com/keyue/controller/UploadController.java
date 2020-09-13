package com.keyue.controller;

import com.keyue.entity.ResultModel;
import com.keyue.utils.DateUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/Upload")
public class UploadController {
    @Value("${avatar.file.upload.path}")
    private String avatarPath;
    @Value("${avatar.file.url}")
    private String avatarUrl;

    /**
     * 上传头像
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadAvatar")
    @ResponseBody
    public ResultModel<Map<String, String>> uploadApp(@RequestParam("file") MultipartFile file){
        return this.uploadFile(file, avatarPath, avatarUrl, false, false);
    }

    /**
     * 上传文件
     * @param file
     * @param path 保存路径
     * @param url 访问路径
     * @return
     */
    private ResultModel<Map<String, String>> uploadFile(MultipartFile file, String path, String url,
                                                        boolean isNeedSubPath, boolean isNeedThumb) {
        ResultModel<Map<String, String>> rm = new ResultModel<>();

        try {
            String originalFilename = file.getOriginalFilename();

            // 子目录
            String subPath = "";
            if(isNeedSubPath){
                subPath = DateUtil.dateToStr(new Date(), "yyyyMM") + "/";
            }

            // 文件名 (yyyyMMddHHmmss + 六位随机数)
            Random random = new Random();
            StringBuilder builder = new StringBuilder();
            builder.append(DateUtil.dateToStr(new Date(), "yyyyMMddHHmmss"));
            builder.append(random.nextInt(899999) + 100000);
            builder.append(originalFilename.substring(originalFilename.indexOf(".")));
            String filename = builder.toString();

            String filePath = path + subPath + filename;

            File saveFile = new File(filePath);
            if (!saveFile.getParentFile().exists()) {
                saveFile.getParentFile().mkdirs();
            }

            // 通过CommonsMultipartFile的方法直接写文件（注意这个时候）
            file.transferTo(saveFile);

            if(isNeedThumb){
                // 保存一张缩略图
                String thumbFilePath = path + subPath + "small-" + filename;
                Thumbnails.of(filePath).scale(0.2f).toFile(thumbFilePath);//按比例缩小
            }

            Map<String, String> data = new HashMap<>();
            data.put("fullUrl", url  + subPath  + filename); // 前端显示用
            data.put("partUrl", subPath + filename); // 保存到数据库中用
            rm.setData(data);

            rm.setMsg("上传成功");

        } catch (Exception e) {
            e.printStackTrace();
            rm.setCode(1);
            rm.setMsg("上传失败");
        }
        return rm;
    }
}
