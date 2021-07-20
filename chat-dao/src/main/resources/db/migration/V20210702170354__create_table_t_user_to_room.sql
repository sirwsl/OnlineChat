CREATE TABLE `t_user_to_room` (
                                  `id` varchar(64) NOT NULL,
                                  `user_id` varchar(64) NOT NULL COMMENT '用户id',
                                  `root_id` varchar(64) NOT NULL COMMENT '房间id',
                                  `create_time` datetime DEFAULT NULL,
                                  `update_time` datetime DEFAULT NULL,
                                  `del_flag` bit(1) DEFAULT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;