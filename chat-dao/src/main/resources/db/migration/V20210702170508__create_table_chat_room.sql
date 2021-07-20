CREATE TABLE `t_chat_room` (
                               `id` varchar(64) NOT NULL,
                               `name` varchar(255) NOT NULL COMMENT '群名称',
                               `head_img` varchar(255) NOT NULL COMMENT '群头像',
                               `users` varchar(255) NOT NULL COMMENT '群用户',
                               `leader` varchar(255) NOT NULL COMMENT '群主',
                               `detail` varchar(255) NOT NULL COMMENT '简介',
                               `notice` varchar(255) NOT NULL COMMENT '通知',
                               `create_time` datetime DEFAULT NULL,
                               `update_time` datetime DEFAULT NULL,
                               `del_flag` bit(1) DEFAULT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;