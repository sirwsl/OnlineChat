package com.wsl.common.log;

import com.wsl.model.pojo.bos.UserBO;
import com.wsl.common.component.request.AbstractCurrentRequestComponent;
import com.wsl.model.entity.Loggers;
import com.wsl.util.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/** Log切面类
 * @author WangShilei
 * @date 2020/11/9-10:15
 **/
@Aspect
@Component
@Slf4j
public class LoggersAspect {


    @Resource
    private AbstractCurrentRequestComponent abstractCurrentRequestComponent;

    /**
     *定义切点 @Pointcut 在注解的位置切入代码
     **/
    @Pointcut("@annotation(com.wsl.common.log.MyLog)")
    public void logPointCut() {
    }

    /**
     *切面 配置通知
     */

    @AfterReturning("logPointCut()")
    public void saveSysLog(JoinPoint joinPoint) throws NoSuchMethodException {
        //保存日志
        Loggers loggers = new Loggers();

        //从切面织入点处通过反射机制获取织入点处的方法

        Signature signature =  joinPoint.getSignature();//方法签名
        Method method = ( (MethodSignature)signature ).getMethod();

        //这个方法才是目标对象上有注解的方法
        Method realMethod = joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(), method.getParameterTypes());


        MyLog myLog = realMethod.getAnnotation(MyLog.class);

        if (myLog != null) {
            //获取请求的类名
            String[] className = joinPoint.getTarget().getClass().getName().split("\\.");
            //保存获取的操作
            //
            loggers.setDetail(myLog.detail())
                    .setGrade(Integer.parseInt(myLog.grade()));
            try {
                UserBO userBO =  abstractCurrentRequestComponent.getCurrentUser();
                if (Objects.nonNull(userBO))
                loggers.setManId(userBO.getId()).setType(userBO.getFlag());
                loggers.setManId("0").setType(0);
            }catch (Exception e){
                loggers.setManId("0").setType(0);
            }
            //获取用户ip地址
            HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
            loggers.setIp(IpUtils.getIP(request));

            //调用service保存SysLog实体类到数据库
            loggers.insert();
        }
    }


}
