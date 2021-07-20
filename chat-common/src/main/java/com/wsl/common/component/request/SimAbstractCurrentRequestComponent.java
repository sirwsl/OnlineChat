package com.wsl.common.component.request;

import com.wsl.model.pojo.bos.UserBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 本地获取当前登录用户信息
 * @author wsl
 */
@Slf4j
@Component
@Profile({"dev"})
public class SimAbstractCurrentRequestComponent extends AbstractCurrentRequestComponent {

    @Override
    public UserBO getCurrentUser() {
        UserBO userBO = new UserBO();
        return userBO.setId("1").setFlag(UserBO.ADMIN).setName("sirwsl");
    }
}
