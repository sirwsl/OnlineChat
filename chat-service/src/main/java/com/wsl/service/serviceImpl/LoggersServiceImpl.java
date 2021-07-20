package com.wsl.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wsl.service.service.LoggersService;
import com.wsl.dao.mapper.LoggersMapper;
import com.wsl.model.entity.Loggers;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author user
 * @since 2021-07-02
 */
@Service
public class LoggersServiceImpl extends ServiceImpl<LoggersMapper, Loggers> implements LoggersService {

}
