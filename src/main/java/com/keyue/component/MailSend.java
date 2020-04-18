package com.keyue.component;

import com.keyue.entity.MailEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Component
public class MailSend {
    private static final Logger logger = LoggerFactory.getLogger(MailSend.class);

    @Autowired
    private JavaMailSenderImpl javaMailSenderImpl;

    /**
     * 发送邮件
     */
    public MailEntity sendMail(MailEntity mailEntity) {
        try {
            checkMail(mailEntity); //1.检测邮件
            sendMimeMail(mailEntity); //2.发送邮件
            return saveMail(mailEntity); //3.保存邮件
        } catch (Exception e) {
            logger.error("发送邮件失败:", e);//打印错误信息
            mailEntity.setStatus("fail");
            mailEntity.setError(e.getMessage());
            return mailEntity;
        }
    }

    //检测邮件信息类
    private void checkMail(MailEntity mailEntity) {
        if (StringUtils.isEmpty(mailEntity.getTo())) {
            throw new RuntimeException("邮件收信人不能为空");
        }
        if (StringUtils.isEmpty(mailEntity.getSubject())) {
            throw new RuntimeException("邮件主题不能为空");
        }
        if (StringUtils.isEmpty(mailEntity.getText())) {
            throw new RuntimeException("邮件内容不能为空");
        }
    }

    //构建复杂邮件信息类
    private void sendMimeMail(MailEntity mailEntity) {
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(javaMailSenderImpl.createMimeMessage(), true);//true表示支持复杂类型
            mailEntity.setFrom(getMailSendFrom());//邮件发信人从配置项读取
            messageHelper.setFrom(mailEntity.getFrom());//邮件发信人
            messageHelper.setTo(mailEntity.getTo().split(","));//邮件收信人
            messageHelper.setSubject(mailEntity.getSubject());//邮件主题
            messageHelper.setText(mailEntity.getText());//邮件内容
            if (!StringUtils.isEmpty(mailEntity.getCc())) {//抄送
                messageHelper.setCc(mailEntity.getCc().split(","));
            }
            if (!StringUtils.isEmpty(mailEntity.getBcc())) {//密送
                messageHelper.setCc(mailEntity.getBcc().split(","));
            }
            if (mailEntity.getMultipartFiles() != null) {//添加邮件附件
                for (MultipartFile multipartFile : mailEntity.getMultipartFiles()) {
                    messageHelper.addAttachment(multipartFile.getOriginalFilename(), multipartFile);
                }
            }
            if (StringUtils.isEmpty(mailEntity.getSentDate())) {//发送时间
                mailEntity.setSentDate(new Date());
                messageHelper.setSentDate(mailEntity.getSentDate());
            }
            javaMailSenderImpl.send(messageHelper.getMimeMessage());//正式发送邮件
            mailEntity.setStatus("ok");
            logger.info("发送邮件成功：{}->{}", mailEntity.getFrom(), mailEntity.getTo());
        } catch (Exception e) {
            throw new RuntimeException(e);//发送失败
        }
    }

    //保存邮件
    private MailEntity saveMail(MailEntity mailEntity) {
        //将邮件保存到数据库..
        return mailEntity;
    }

    //获取邮件发信人
    public String getMailSendFrom() {
        return javaMailSenderImpl.getJavaMailProperties().getProperty("from");
    }
}
