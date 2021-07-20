package com.wsl.common.component.request;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.wsl.common.component.jwt.JwtComponent;
import com.wsl.common.component.jwt.JwtEnum;
import com.wsl.model.pojo.bos.UserBO;
import com.wsl.model.constant.RedisEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;

/**
 * 线上获取当前登录用户信息
 * @author wsl
 */
@Slf4j
@Component
@Profile({"pro"})
public class ProdAbstractCurrentRequestComponent extends AbstractCurrentRequestComponent {


    @Resource
    private RedisTemplate<String, UserBO> redisTemplate;

    @Resource
    private JwtComponent jwtComponent;
    /**
     * 获取当前登陆用户信息
     * @return 当前登录用户信息
     */
    @Override
    public UserBO getCurrentUser() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String token = request.getHeader(JwtEnum.AUTH_HEADER_KEY);
        if (StringUtils.isBlank(token) ) {
            if (request.getCookies()!=null && CollectionUtils.isNotEmpty(Arrays.asList(request.getCookies()))) {
                for (Cookie cookie : request.getCookies()) {
                    if (JwtEnum.TOKEN.equals(cookie.getName())){
                        token = cookie.getValue();
                    }
                }
            }
            return null;
        }
        try {
            return jwtComponent.getTokenInfo(token.substring(JwtEnum.TOKEN_PREFIX.length()));
        }catch (Exception e){
            log.info("获取用户信息失败"+e.getMessage());
        }

        return null;
    }
}
