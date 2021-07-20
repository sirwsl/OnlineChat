package com.wsl.web.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wsl.common.Result;
import com.wsl.common.VerifyComponent;
import com.wsl.common.component.oss.OssComponent;
import com.wsl.common.component.sms.SmsComponent;
import com.wsl.model.constant.BaseEnum;
import com.wsl.model.constant.RedisEnum;
import com.wsl.model.entity.User;
import com.wsl.service.service.UserService;
import com.wsl.util.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 公共Api
 */
@Slf4j
@RestController
@RequestMapping("/open")
public class OpenApi {

    @Resource
    private VerifyComponent verifyComponent;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private OssComponent ossComponent;

    @Resource
    private SmsComponent smsComponent;

    @Resource
    private UserService userService;

    /**
     * 获取图像验证码
     * @param response：
     * @param request：
     */
    @GetMapping("/getImg/v1")
    public void verifyImg(HttpServletResponse response, HttpServletRequest request){
        try {
            verifyComponent.getKaptCha(response,request);
        }catch (Exception e){
            log.error("获取图形验证码失败");
        }
    }

    /**
     * 获取验证码
     * @param phone：手机号
     * @return 结果
     */
    @GetMapping("/getCode/v1")
    public Result<String> getPhoneCode(String phone){
        if (StringUtils.isBlank(phone)|| !RegexUtils.checkMobile(phone)){
            Result.error("error","手机号不正确，获取失败");
        }
        List<User> list = userService.list(new QueryWrapper<User>().eq(User.PHONE,phone));
        if (CollectionUtils.isEmpty(list)){
            return Result.error("您还未进行注册");
        }
        String phoneCode = String.valueOf((int)(Math.random()*9+1)*1000);
        stringRedisTemplate.opsForValue().set(RedisEnum.PHONE_CODE+phone,phoneCode,5, TimeUnit.MINUTES);
        String[] arr = {phoneCode,"5"};
        smsComponent.send(2224,arr,phone);
        return Result.success("获取成功");
    }

    @PostMapping("/uploadImg/v1")
    public Result<String> uploadFile(@RequestParam("multipartFile") MultipartFile multipartFile){
        try {
            String s = ossComponent.uploadFile(BaseEnum.OSS_ADDRESS, multipartFile);
            if (StringUtils.isNotBlank(s)){
                return Result.success(s);
            }
        }catch (Exception e){
            log.error("文件上传失败");
        }
        return Result.error("上传失败");
    }

}
