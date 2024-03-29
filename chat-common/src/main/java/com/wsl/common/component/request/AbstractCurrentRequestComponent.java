package com.wsl.common.component.request;
import com.wsl.model.pojo.bos.UserBO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author wsl
 */
@Component
public abstract class AbstractCurrentRequestComponent {


    /**
     * 获取当前登陆用户信息
     * @return 当前登录用户信息
     */
    public abstract UserBO getCurrentUser();
}
