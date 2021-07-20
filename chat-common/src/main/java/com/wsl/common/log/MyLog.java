package com.wsl.common.log;


import java.lang.annotation.*;

/**
 * 自定义注解类
 * Log日志存储
 * @author wsl
 */
@Target(ElementType.METHOD) //注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented //生成文档
public @interface MyLog {

    String detail() default "";

    String value() default  "";

    String grade() default "";
}