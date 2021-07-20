CREATE TABLE `t_user` (
                          `id` varchar(255) NOT NULL,
                          `name` varchar(255) NOT NULL COMMENT '用户名',
                          `passward` varchar(255) NOT NULL COMMENT '密码',
                          `head_img` varchar(255) NOT NULL COMMENT '头像',
                          `nick_name` varchar(255) NOT NULL COMMENT '昵称',
                          `create_time` datetime DEFAULT NULL,
                          `update_time` datetime DEFAULT NULL,
                          `del_flag` bit(1) DEFAULT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;