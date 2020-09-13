package com.keyue.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.keyue.common.constant.SysConst;
import com.keyue.common.constant.UserStatusConst;
import com.keyue.common.enums.DiarySecrecyType;
import com.keyue.common.util.RandomUtil;
import com.keyue.common.util.RegexUtil;
import com.keyue.component.MailSend;
import com.keyue.component.RedisManager;
import com.keyue.dao.UserDiaryMapper;
import com.keyue.dao.UserMapper;
import com.keyue.dao.UserReadHistoryMapper;
import com.keyue.dao.UserVisitTimeLogMapper;
import com.keyue.dao.model.*;
import com.keyue.entity.MailEntity;
import com.keyue.entity.ResultModel;
import com.keyue.entity.VisitLogData;
import com.keyue.entity.VisitLogRect;
import com.keyue.entity.res.UserRes;
import com.keyue.service.IUserService;
import com.keyue.utils.DateUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    // 显示的总格子数 = 20 * 7
    public final static int SHOW_RECT_NUM = 140;
    // 至少显示两列14个未来格子
    public final static int FUTURE_RECT_NUM = 14;

    @Autowired
    MailSend mailSend;
    @Autowired
    RedisManager redisManager;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserVisitTimeLogMapper userVisitTimeLogMapper;
    @Autowired
    UserDiaryMapper userDiaryMapper;
    @Autowired
    UserReadHistoryMapper userReadHistoryMapper;

    @Value("${avatar.file.url}")
    private String avatarFileUrl;

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
    public ResultModel<String> saveVisitTimeLog(UserVisitTimeLog userVisitTimeLog) {
        int row = userVisitTimeLogMapper.insertSelective(userVisitTimeLog);
        if (row == 0) {
            return ResultModel.failed("保存失败");
        }
        return ResultModel.success("保存成功");
    }

    @Override
    public ResultModel<String> saveDiary(UserDiary userDiary) {
        userDiary.setSecrecyType(DiarySecrecyType.ONESELF.getId());
        userDiary.setUpdateTime(new Date());

        UserDiary record = new UserDiary();
        record.setUserId(userDiary.getUserId());
        record.setDiaryDate(userDiary.getDiaryDate());
        UserDiary existUserDiary = userDiaryMapper.selectByRecord(record);

        int row;
        if(existUserDiary == null){
            row = userDiaryMapper.insertSelective(userDiary);
        }else{
            userDiary.setId(existUserDiary.getId());
            row = userDiaryMapper.updateByPrimaryKeySelective(userDiary);
        }

        if (row == 0) {
            return ResultModel.failed("保存失败");
        }
        return ResultModel.success("保存成功");
    }

    @Override
    public UserDiary findUserDiary(int userId, String date) {
        UserDiary record = new UserDiary();
        record.setUserId(userId);
        record.setDiaryDate(date);
        UserDiary userDiary = userDiaryMapper.findByRecord(record);
        userDiary = userDiary == null ? new UserDiary() : userDiary;
        return userDiary;
    }

    @Override
    public void saveReadHistory(int userId, String bookNo, Integer chaptherNo) {
        // 如果存在（userId、bookNo）,替换掉(逻辑删除)
        UserReadHistory record = new UserReadHistory();
        record.setUserId(userId);
        record.setBookNo(bookNo);
        userReadHistoryMapper.deleteByRecord(record);

        UserReadHistory userReadHistory = new UserReadHistory();
        userReadHistory.setUserId(userId);
        userReadHistory.setBookNo(bookNo);
        userReadHistory.setChapterNo(String.valueOf(chaptherNo));
        userReadHistory.setLastReadTime(new Date());
        userReadHistoryMapper.insertSelective(userReadHistory);
    }

    @Override
    public ResultModel<List<UserReadHistory>> queryReadHistoryByPage(UserReadHistory userReadHistory, Integer page, Integer limit) {
        ResultModel<List<UserReadHistory>> rm = new ResultModel<>();
        Page<UserReadHistory> tPage = PageHelper.startPage(page, limit);
        List<UserReadHistory> tList = userReadHistoryMapper.selectList(userReadHistory);
        rm.setData(tList);
        return rm;
    }

    @Override
    public ResultModel<String> delReadHistory(Integer id) {
        ResultModel<String> rm = new ResultModel<>();

        UserReadHistory userReadHistory = new UserReadHistory();
        userReadHistory.setId(id);
        userReadHistory.setIsDel("1");
        int row = userReadHistoryMapper.updateByPrimaryKeySelective(userReadHistory);

        if(row>0){
            rm.setMsg("删除成功");
        }else{
            rm.setCode(1);
            rm.setMsg("删除失败");
        }

        return rm;
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
        // 用户基本信息
        UserRes userRes = null;
        User user = null;
        if(userId != 0){
            user = userMapper.selectByPrimaryKey(userId);
        }
        if(user != null){
            userRes = new UserRes();
            BeanUtils.copyProperties(user, userRes);
            userRes.setAvatar(avatarFileUrl + userRes.getAvatar());
        }
        return userRes;
    }

    /**
     * 阅读时长网格（网格种类：rank0-未阅读，rank1-一级，rank2-二级，rank3-三级，rank4-四级，empty-空格）
     * @param userId 用户ID
     * @param regTime 注册时间
     */
    @Override
    public List<VisitLogRect> queryVisitLog(int userId, Date regTime) {
        // 为补全格子，在注册日期前面加preEmptyNum个空格
        Date curTime = new Date();
        int regWeek = DateUtil.getWeekOfDate(regTime);
        int curWeek = DateUtil.getWeekOfDate(curTime);
        int preEmptyNum = regWeek - 1;
        int nextEmptyNum = 7 - curWeek;

        List<VisitLogRect> rects = new ArrayList<>();
        // 空的格子（前面补）
        int preRectIndex = 0;
        while(preRectIndex < preEmptyNum)
        {
            VisitLogRect rect = new VisitLogRect();
            rect.setRectType("empty");
            rects.add(rect);
            preRectIndex++;
        }
        // 注册日期到当前日期的格子
        List<VisitLogData> logDataList = userVisitTimeLogMapper.queryVisitLog(userId);
        Map<String, VisitLogData> dataMap =
                logDataList.stream().collect(Collectors.toMap(VisitLogData::getRecordDate,
                Function.identity(), (o1, o2) -> o1));
        List<String> dateList = DateUtil.getDateList(regTime, curTime);
        for (String date : dateList) {
            VisitLogData logData = dataMap.get(date);
            VisitLogRect rect = new VisitLogRect();
            rect.setRecordDate(date);
            if(logData != null) {
                int dayVisitDuration = logData.getTotalDuration() == null ? 0 : logData.getTotalDuration();
                rect.setTotalDuration(dayVisitDuration);
                String rectType = "rank0";
                if(dayVisitDuration >= 3 * 60 * 60) {
                    rectType = "rank4";
                } else if(dayVisitDuration >= 2 * 60 * 60) {
                    rectType = "rank3";
                } else if(dayVisitDuration >= 60 * 60) {
                    rectType = "rank2";
                } else if(dayVisitDuration > 0) {
                    rectType = "rank1";
                }
                rect.setRectType(rectType);
            } else {
                rect.setTotalDuration(0);
                rect.setRectType("rank0");
            }

            rects.add(rect);
        }

        // 空的格子（后面未到）
        int hasRectNum = DateUtil.daysDiff(regTime, curTime) + 1 + preRectIndex;
        int futureNum = 0;
        if(hasRectNum > SHOW_RECT_NUM - FUTURE_RECT_NUM) {
            futureNum = FUTURE_RECT_NUM + nextEmptyNum;
        } else {
            futureNum = SHOW_RECT_NUM - hasRectNum;
        }
        int futureRectIndex = 0;
        while(futureRectIndex < futureNum)
        {
            VisitLogRect rect = new VisitLogRect();
            rect.setRectType("empty");
            rects.add(rect);
            futureRectIndex++;
        }
        return rects;
    }

    @Override
    public ResultModel<String> updateUser(User user) {
        if(user.getNickname() != null && !RegexUtil.isNickname(user.getNickname())){
            return ResultModel.failed("请输入1~20位的昵称");
        }
        if(user.getKeno() != null){
            User existUser = userMapper.selectByPrimaryKey(user.getId());
            if(!existUser.getKeno().startsWith("keid_")){
                return ResultModel.failed("可阅号不可修改");
            }
            if(!RegexUtil.isKeno(user.getKeno())){
                return ResultModel.failed("可阅号必须为6~18位的字母或数字");
            }
            if(isExistKeno(user.getKeno())) {
                return ResultModel.failed(user.getKeno() + "已被使用");
            }
        }
        int ret = userMapper.updateByPrimaryKeySelective(user);
        if(ret>0){
            return ResultModel.success("修改成功");
        }
        return ResultModel.failed("修改失败");
    }

    private boolean isExistKeno(String keno) {
        User record = new User();
        record.setKeno(keno);
        User user = userMapper.findByRecord(record);
        if(user != null) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
//        String code = RandomUtil.lengthString(32);
//        String link = "http://m.liulangcat.com/user/activate.php?code=" + code;
//        String mailContent = String.format("<p>您好，欢迎注册可阅!</p>" +
//                "<p>请点击下面的链接完成注册：</p>" +
//                "<a href='%s'>%s</a>" +
//                "<p>如果以上链接无法点击，请将上面的地址复制到你的浏览器的地址栏</p>", link, link);
//        System.out.println(mailContent);

        Date beginDate = DateUtil.strToDate("2019-08-31 21:01:09", DateUtil.FRONT_DATE_FORMAT_STRING);
        Date endDate = new Date();
        System.out.println(DateUtil.daysDiff(beginDate, endDate));
        System.out.println(DateUtil.getDateList(beginDate, endDate));

    }
}
