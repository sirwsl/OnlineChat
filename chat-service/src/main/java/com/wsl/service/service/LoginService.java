package com.wsl.service.service;

import com.wsl.model.pojo.params.LoginParam;
import com.wsl.model.pojo.vos.UserVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * @author : WangShiLei
 * @date : 2020/11/23 10:58 下午
 **/
public interface LoginService {

    /**
     * 登录验证
     * @author wangShilei
     * @date 2020/11/23 10:59 下午
     * @param loginParam : 登录参数
     * @param response :
     * @return boolean
     * @throws UnsupportedEncodingException:
     */
    UserVO login(HttpServletResponse response, LoginParam loginParam) throws UnsupportedEncodingException;

    /**
     * 推出登录实现
     * @author wangShilei
     * @date 2020/12/3 11:27
     * @param response :
     * @param request :
     * @return boolean:
     */
    boolean exit(HttpServletResponse response, HttpServletRequest request);


}
