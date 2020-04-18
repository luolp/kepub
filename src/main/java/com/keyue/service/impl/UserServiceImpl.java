package com.keyue.service.impl;

import com.keyue.common.constant.SysConst;
import com.keyue.common.constant.UserStatusConst;
import com.keyue.common.util.RandomUtil;
import com.keyue.common.util.RegexUtil;
import com.keyue.component.MailSend;
import com.keyue.component.RedisManager;
import com.keyue.dao.UserMapper;
import com.keyue.dao.model.User;
import com.keyue.entity.MailEntity;
import com.keyue.entity.ResultModel;
import com.keyue.service.IUserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    MailSend mailSend;
    @Autowired
    RedisManager redisManager;
    @Autowired
    UserMapper userMapper;

    @Override
    public ResultModel<String> login(HttpSession session, String username, String password) {
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return ResultModel.failed("账号或密码错误");
        }
        // username可能是邮箱也可能是有阅号
        boolean isEmail = username.contains("@");
        User record = new User();
        if(isEmail){
            record.setEmail(username);
        }else{
            record.setKeno(username);
        }
        record.setStatus(UserStatusConst.ACTIVE);
        User existUser = userMapper.findByRecord(record);
        if(existUser == null
                || !DigestUtils.md5Hex(password + existUser.getSalted()).equals(existUser.getPassword())){
            return ResultModel.failed("账号或密码错误");
        }
        session.setAttribute(SysConst.USER_ID, existUser.getId());
        return ResultModel.success("登录成功");
    }

    @Override
    public ResultModel<String> register(String email, String password, String nickname) {
        if(!RegexUtil.isEmail(email)){
            ResultModel.failed("请输入正确的邮箱");
        }
        if(!RegexUtil.isPassword(password)){
            ResultModel.failed("请输入6~18位字母、数字或特殊字符组成的密码");
        }
        if(StringUtils.isEmpty(nickname.trim())){
            ResultModel.failed("昵称不能为空");
        }
        if(!RegexUtil.isNickname(nickname)){
            ResultModel.failed("请输入1~20位的昵称");
        }
        // 邮箱是否已经注册
        User record = new User();
        record.setEmail(email);
        User existUser = userMapper.findByRecord(record);
        if(existUser != null && existUser.getStatus() == UserStatusConst.ACTIVE){
            ResultModel.failed("该邮箱已注册");
        }
        // 保存数据
        String salted = RandomUtil.lengthString(32);
        User user = new User();
        user.setKeno("keid_" + RandomUtil.getKeid());
        user.setEmail(email);
        user.setPassword(DigestUtils.md5Hex(DigestUtils.sha1Hex(password) + salted));
        user.setSalted(salted);
        user.setNickname(nickname);
        user.setAvatar(null);
        user.setStatus(UserStatusConst.UNACTIVE);

        int row = 0;
        if(existUser == null){
            user.setId(existUser.getId());
            user.setUpdateTime(new Date());
            row = userMapper.updateByPrimaryKeySelective(user);
        }else{
            row = userMapper.insertSelective(user);
        }
        if(row == 0){
            ResultModel.failed("注册失败");
        }
        // 发送激活邮件
        String code = RandomUtil.lengthString(32);
        String link = "http://m.liulangcat.com/user/activate.php?code=" + code;
        String mailContent = String.format("<p>您好，欢迎注册可阅!</p>" +
                "<p>请点击下面的链接完成注册：</p>" +
                "<a href='%s'>%s</a>" +
                "<p>如果以上链接无法点击，请将上面的地址复制到你的浏览器的地址栏</p>", link, link);
        MailEntity mailEntity = new MailEntity();
        mailEntity.setTo(email);
        mailEntity.setSubject("可阅账号激活");
        mailEntity.setText(mailContent);
        mailEntity = mailSend.sendMail(mailEntity);
        if(!"ok".equals(mailEntity.getStatus())){
            ResultModel.failed("注册失败，激活邮件发送失败");
        }
        // 保存激活码
        redisManager.set("activation_code_" + code, user.getId());

        return ResultModel.success("注册成功");
    }

    public static void main(String[] args) {
        String code = RandomUtil.lengthString(32);
        String link = "http://m.liulangcat.com/user/activate.php?code=" + code;
        String mailContent = String.format("<p>您好，欢迎注册可阅!</p>" +
                "<p>请点击下面的链接完成注册：</p>" +
                "<a href='%s'>%s</a>" +
                "<p>如果以上链接无法点击，请将上面的地址复制到你的浏览器的地址栏</p>", link, link);
        System.out.println(mailContent);

    }
}
