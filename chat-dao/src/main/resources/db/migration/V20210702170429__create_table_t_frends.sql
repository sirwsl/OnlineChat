CREATE TABLE `t_friends` (
                            `id` varchar(64) NOT NULL,
                            `user_id` varchar(64) DEFAULT NULL,
                            `my_care` varchar(2000) DEFAULT NULL COMMENT '我关注的',
                            `care_me` varchar(2000) DEFAULT NULL COMMENT '关注我的',
                            `frend` varchar(2000) DEFAULT NULL COMMENT '好友',
                            `create_time` datetime DEFAULT NULL,
                            `update_time` datetime DEFAULT NULL,
                            `del_flag` bit(1) DEFAULT NULL,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;