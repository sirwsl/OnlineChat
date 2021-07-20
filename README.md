## onlineChat在线聊天系统

### 零、主要功能

**1）登录注册、手机登录**

**2）登录在线广播**

**3）个人信息维护修改、账号绑定**

**4）实时在线好友私聊**

**5）实时共同群聊**

**6）添加、删除好友**

**7）创建群聊、添加群聊、删除群聊**

**8）登录、人数等相关统计**



### 一、地址相关

#### **1、项目演示地址：[https://chat.wslhome.top](https://chat.wslhome.top/)**

#### 2、GitHub地址：

#### 前端:https://github.com/sirwsl/OnlineChat-web

#### 后端：https://github.com/sirwsl/OnlineChat

#### 3、码云（Gitee地址）：

#### 前端:https://gitee.com/sirwsl/OnlineChat-web

#### 后端：https://gitee.com/sirwsl/OnlineChat



### 二、相关技术

**1、该项目整体技术与框架包括：Ant Design Pro、 SpringBoot、WebSocket、MyBatis Plus、flyway 但不仅限于以上技术框架**

**2、项目整体采用前后端分离模式进行开发，前端主要采用TypeScript+Less文件进行编写，组件采用Ant Design组件进行实现**

**3、后端主要为SpringBoot进行实现，但也涉及到Redis、短信等相关内容**

**4、项目基本数据存储采用MYSQL形式，聊天记录采用本地localStorage的形式进行存储，如果需要可在此基础上加入mongoDB进行聊天数据持久化**



### 三、项目运行与部署


### **项目运行**

#### 1、后端

### 运行之前请移除所有子模块pom文件中的以下部分，否则会报错
```xml
<build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <classifier>exec</classifier>
                </configuration>
            </plugin>
        </plugins>
    </build>
```


**1）拉取项目**：https://gitee.com/sirwsl/OnlineChat.git or https://github.com/sirwsl/OnlineChat.git

**2）安装依赖** ：

```cmake
mvn install
```

**3)修改chat-web 模块下的resource中配置文件**

  a) 选择激活环境

```
  profiles:
    active: pro,all
```

  b) 修改config配置文件中对应参数

**4）数据库中创建对应数据库**

**PS：只需要创建数据库，在该系统中采用FlyWay进行数据库版本管理，启动项目后会自动运行flyway文件进行初始化数据库**

**5）找到chat-web模块下Application.java中main函数运行**



#### 2、前端

**1）拉取项目**：https://gitee.com/sirwsl/OnlineChat-web.git or https://github.com/sirwsl/OnlineChat-web.git

 **2）安装所需包**

```cmd
npm install
```

**3)修改config中代理相关配置 proxy.ts文件**

```ts
'/api/': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        pathRewrite: {'^/api': ''},
      },
```

**4）修改src目录下layouts/BasicLayout.tsx文件与pages/Chat/index.tsx中的webSocket链接进行修改**

```tsx
let listClient = new WebSocket('ws://localhost:8080/xxx/'+userId)
```

**5)运行项目**

```cmd
npm start
```



 ### 项目构建

### 构建jar包之前请再所有子模块pom文件中添加以下部分，否则会报错
```xml
<build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <classifier>exec</classifier>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

1、后端项目构建参照chat-web模块下dockerfile文件

dockerfile:

```dockerfile
FROM java:8
MAINTAINER wangshilei <sirwsl@163.com>
VOLUME /tmp
COPY target/chat-web-1.0-exec.jar onlinechat.jar
RUN bash -c "touch /onlinechat.jar"
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo "Asia/Shanghai" >> /etc/timezone
EXPOSE 8001
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","onlinechat.jar"]
```



2、前端项目运行 npm run build后将disc目录复制到nginx的html目录下

nginx中添加代理、nginx配置文件如下：

```nginx
server {
    listen 8001;
    include       mime.types;
    default_type  application/octet-stream;
    sendfile        on;
    gzip on;
    gzip_min_length 1k;
    gzip_comp_level 9;
    gzip_types text/plain application/javascript application/x-javascript text/css application/xml text/javascript application/x-httpd-php image/jpeg image/gif image/png;
    gzip_vary on;
    gzip_disable "MSIE [1-6]\.";

    root /usr/share/nginx/html/disc/;
    location / {
          try_files $uri $uri/ /index.html;
    }

   location /api {
        proxy_pass http://localhost:8001/;
        proxy_set_header Host $host:$server_port;
    }

        location ~ .*\.(images|img|javascript|js|css|flash|media|static|eot|otf|ttf|woff|woff2|map)$ {
           root    /usr/share/nginx/html/disc/;
           autoindex on;
           access_log  off;
           expires     7d;
       }

}

server{
  listen 80;
  return 301 https://xxx$request_uri;
}
```



### 四、作者相关

**项目作者：sirwsl**

**wx：sirwsl （可以一起交流技术、如果是帮忙看问题就别加了，烦的伤）**

**email：sirwsl@163.com （有问题可以进行反馈或者去github提bug）**

**一个初出茅庐的码农，该项目为antDesign与webSocket的实验品，喜欢就给个Star吧**

**个人博客：[https://www.wslhome.top](https://www.wslhome.top/)**

**CSDN:[sirwsl:https://blog.csdn.net/qq_40432886](https://blog.csdn.net/qq_40432886)**

**商城：[https://kill.wslhome.top](https://kill.wslhome.top/)**

**商城后端管理：[https://admin.wslhome.top](https://admin.wslhome.top/)**

**码云地址：https://gitee.com/sirwsl**

**GitHub地址：https://github.com/sirwsl**

