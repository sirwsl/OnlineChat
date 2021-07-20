alter table t_friends  CHANGE my_care  `friend_ids` VARCHAR(2000);
alter table t_friends  CHANGE care_me  `room_ids` VARCHAR(2000);