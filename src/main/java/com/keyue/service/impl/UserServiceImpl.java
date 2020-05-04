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
import com.keyue.entity.res.UserRes;
import com.keyue.service.IUserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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

    private static final String ACTIVATION_CODE_PREFIX = "ACTIVATION_CODE_";
    private static final int ACTIVATION_CODE_EXPIRE = 30 * 60;
    private static final String CAPTCHA_PREFIX = "CAPTCHA_CODE_";
    private static final int CAPTCHA_EXPIRE = 30 * 60;

    @Override
    public ResultModel<String> login(HttpSession session, String username, String password) {
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return ResultModel.failed("账号或密码错误");
        }
        // username可能是邮箱也可能是有阅号
        boolean isEmail = RegexUtil.isEmail(username);
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
            return ResultModel.failed("请输入正确的邮箱");
        }
        if(!RegexUtil.isPassword(password)){
            return ResultModel.failed("请输入6~18位字母、数字或特殊字符组成的密码");
        }
        if(nickname == null || StringUtils.isEmpty(nickname.trim())){
            return ResultModel.failed("昵称不能为空");
        }
        if(!RegexUtil.isNickname(nickname)){
            return ResultModel.failed("请输入1~20位的昵称");
        }
        // 邮箱是否已经注册
        User record = new User();
        record.setEmail(email);
        User existUser = userMapper.findByRecord(record);
        if(existUser != null && existUser.getStatus() == UserStatusConst.ACTIVE){
            return ResultModel.failed("该邮箱已注册");
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
            row = userMapper.insertSelective(user);
        }else{
            user.setId(existUser.getId());
            user.setUpdateTime(new Date());
            row = userMapper.updateByPrimaryKeySelective(user);
        }
        if(row == 0){
            return ResultModel.failed("注册失败");
        }
        // 发送激活邮件
        String code = RandomUtil.lengthString(32);
        String link = "http://localhost:8070/user/activate?code=" + code;
        String mailContent = String.format("<p>您好，欢迎注册可阅!</p>" +
                "<p>请点击下面的链接完成注册：</p>" +
                "<a href='%s'>%s</a>" +
                "<p>如果以上链接无法点击，请将上面的地址复制到浏览器地址栏。</p>" +
                "<p>激活链接30分钟后失效。</p>", link, link);
        MailEntity mailEntity = new MailEntity();
        mailEntity.setTo(email);
        mailEntity.setSubject("可阅账号激活");
        mailEntity.setText(mailContent);
        mailEntity = mailSend.sendMail(mailEntity);
        if(!"ok".equals(mailEntity.getStatus())){
            return ResultModel.failed("注册失败，激活邮件发送失败");
        }
        // 保存激活码（30分钟有效）
        redisManager.set(ACTIVATION_CODE_PREFIX + code, user.getId(), ACTIVATION_CODE_EXPIRE);

        return ResultModel.success("注册成功");
    }

    @Override
    public ResultModel<String> activate(String code) {
        Integer userId = (Integer) redisManager.get(ACTIVATION_CODE_PREFIX + code);
        if(userId == null){
            return ResultModel.failed("激活链接已失效");
        }
        if(userId != null){
            int row = userMapper.activate(userId);
            if(row == 0){
                return ResultModel.failed("激活失败");
            }
        }
        // 激活成功后删除激活码
        redisManager.del(ACTIVATION_CODE_PREFIX + code);
        return ResultModel.success("账号激活成功");
    }

    @Override
    public ResultModel<String> getCaptcha(String username) {
        // username可能是邮箱也可能是有阅号
        boolean isEmail = RegexUtil.isEmail(username);
        User record = new User();
        if(isEmail){
            record.setEmail(username);
        }else{
            record.setKeno(username);
        }
        record.setStatus(UserStatusConst.ACTIVE);
        User existUser = userMapper.findByRecord(record);
        if(existUser == null){
            return ResultModel.failed("用户不存在");
        }
        // 发送验证码到邮件
        String code = RandomUtil.number(6);
        String mailContent = String.format("<p>您好，您正在进行邮箱验证，本次请求的验证码为：</p>" +
                "<p style=\"line-height:30px;font-size:18px;color:#00BFA5;font-weight: bold;\">%s</p>" +
                "<p style=\"color:#979797;\">(为了保障您帐号的安全性，请在30分钟内完成验证。)</p>", code);
        MailEntity mailEntity = new MailEntity();
        mailEntity.setTo(existUser.getEmail());
        mailEntity.setSubject("可阅验证码");
        mailEntity.setText(mailContent);
        mailEntity = mailSend.sendMail(mailEntity);
        if(!"ok".equals(mailEntity.getStatus())){
            return ResultModel.failed("验证码发送失败");
        }
        // 保存验证码（30分钟有效）
        redisManager.set(CAPTCHA_PREFIX + code, existUser.getEmail(), CAPTCHA_EXPIRE);

        return ResultModel.success("已发送至" + existUser.getEmail());
    }

    @Override
    public ResultModel<String> updatePwd(String username, String code, String password) {
        if(!RegexUtil.isPassword(password)){
            return ResultModel.failed("请输入6~18位字母、数字或特殊字符组成的密码");
        }
        // username可能是邮箱也可能是有阅号
        boolean isEmail = RegexUtil.isEmail(username);
        User record = new User();
        if(isEmail){
            record.setEmail(username);
        }else{
            record.setKeno(username);
        }
        record.setStatus(UserStatusConst.ACTIVE);
        User existUser = userMapper.findByRecord(record);
        if(existUser == null){
            return ResultModel.failed("用户不存在");
        }

        String email = (String) redisManager.get(CAPTCHA_PREFIX + code);
        if(email == null || !email.equals(existUser.getEmail())){
            return ResultModel.failed("验证码错误或已失效");
        }

        String salted = RandomUtil.lengthString(32);
        User user = new User();
        user.setId(existUser.getId());
        user.setPassword(DigestUtils.md5Hex(DigestUtils.sha1Hex(password) + salted));
        user.setSalted(salted);
        user.setUpdateTime(new Date());
        int row = userMapper.updateByPrimaryKeySelective(user);
        if(row == 0){
            return ResultModel.failed("密码修改失败");
        }
        // 修改成功后删除验证码
        redisManager.del(CAPTCHA_PREFIX + code);
        return ResultModel.success("密码修改成功");
    }

    @Override
    public UserRes getUser(int userId) {
        UserRes userRes = null;
        User user = null;
        if(userId != 0){
            user = userMapper.selectByPrimaryKey(userId);
        }
        if(user != null){
            userRes = new UserRes();
            BeanUtils.copyProperties(user, userRes);
        }
        return userRes;
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
