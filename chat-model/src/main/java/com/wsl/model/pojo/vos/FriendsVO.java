package com.wsl.model.pojo.vos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
public class FriendsVO {

    private List<Friend> myFriends;

    private List<Friend> room;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class Friend{
        private String id;

        /**
         * 账号
         */
        private String name;

        private String nickName;

        /**
         * 头像
         */
        private String avatar;

        @Override
        public boolean equals(Object obj) {
            if(this == obj){
                return true;//地址相等
            }
            if(obj == null){
                return false;
            }

            if(obj instanceof Friend){
                Friend other = (Friend) obj;
                //需要比较的字段相等，则这两个对象相等
                if(this.id.equals(other.id)){
                    return true;
                }
            }
            return false;
        }

        @Override
        public int hashCode() {
            int result = 16;
            result = 31 * result + (id == null ? 0 : id.hashCode());
            return result;
        }
    }
}
