package com.wsl.web.api;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wsl.model.entity.User;
import com.wsl.model.pojo.params.LoginParam;
import com.wsl.model.pojo.params.UserParam;
import com.wsl.common.Result;
import com.wsl.common.VerifyComponent;
import com.wsl.model.pojo.vos.UserVO;
import com.wsl.service.service.LoginService;
import com.wsl.service.service.UserService;
import com.wsl.util.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Objects;


/**
 * @author WangShilei
 * @date 2020/11/23-14:22
 **/
@Slf4j
@RestController
@RequestMapping("/open")
public class LoginApi {

    @Resource
    private LoginService loginService;

    @Resource
    private VerifyComponent verifyComponent;

    @Resource
    private UserService userService;

    /**
     * 用户登录接口
     *
     * @param response  :
     * @param request   :
     * @param loginParam :
     * @return Result<java.lang.String>
     * @author wangShilei
     * @date 2020/11/24 11:48
     */
    @PostMapping("/login/v1")
    public Result<UserVO> userLogin(HttpServletResponse response, HttpServletRequest request, @Valid @RequestBody LoginParam loginParam ) throws UnsupportedEncodingException {

        if (loginParam.getType().equals("account")){
          //账号密码登录
            if(StringUtils.isBlank(loginParam.getName())){
                Result.error("error","账号不能为空");
            }
            if(StringUtils.isBlank(loginParam.getPassword())){
                Result.error("error","密码不能为空");
            }
            if(StringUtils.isBlank(loginParam.getCode())){
                Result.error("error","验证码不能为空");
            }
            if (!verifyComponent.imgVerifyCode(loginParam.getCode(), request)) {
                return Result.error("error", "验证码不正确");
            }
        }else {
            //手机登录
            if(StringUtils.isBlank(loginParam.getPhone())){
                Result.error("error","密码不能为空");
            }
            if(RegexUtils.checkMobile(loginParam.getPhone())){
                Result.error("error","手机号格式不正确");
            }
            if(StringUtils.isBlank(loginParam.getPhoneCode())){
                Result.error("error","验证码不能为空");
            }
        }

        UserVO login = loginService.login(response, loginParam);
        if (Objects.nonNull(login)){
            return Result.success(login);
        }

        return Result.error();
    }

    /**
     * 退出系统
     * @author wangShilei
     * @date 2020/12/3 14:08
     * @param response :
     * @param request :
     * @return Result<java.lang.Boolean>
     */
    @GetMapping("/exit/v1")
    public Result<Boolean> exitLogin(HttpServletResponse response, HttpServletRequest request){
        return Result.success(loginService.exit(response,request));
    }


    @PostMapping("/register/v1")
    public Result<String> addUser(@Valid @RequestBody UserParam userParams, HttpServletRequest request){
        if (Objects.nonNull(userService.getOne(new QueryWrapper<User>().eq(User.NAME,userParams.getName())))){
            return Result.error("error","用户名已存在，请重新输入");
        }
        if (StringUtils.isBlank(userParams.getCode())){
            return Result.error("error", "验证码不能为空");
        }
        if (!verifyComponent.imgVerifyCode(userParams.getCode(), request)) {
            return Result.error("error", "验证码不正确");
        }
        if (userService.addUser(userParams)){
            return Result.success("恭喜您注册成功！喜欢就给个star");
        }
        return Result.error("error","注册失败，请您稍后再试");
    }
}
