CREATE TABLE `t_loggers` (
                             `id` varchar (64) NOT NULL COMMENT '日志id',
                             `detail` varchar(255) NOT NULL COMMENT '操作内容',
                             `man_id` varchar (64) NOT NULL COMMENT '操作人id',
                             `type` int(11) NOT NULL DEFAULT '0' COMMENT '操作类型',
                             `grade` int(11) DEFAULT NULL COMMENT '等级(0-正常 1-良好 2-严重 3-极其严重)',
                             `ip` varchar(20) DEFAULT NULL COMMENT 'ip',
                             `creat_time` datetime NOT NULL COMMENT '创建时间(操作时间)',
                             `update_time` datetime NOT NULL COMMENT '更新时间',
                             `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=217 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;