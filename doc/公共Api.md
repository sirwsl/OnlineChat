### 获取图像验证码
地址：/open/getImg/v1

方式：GET

请求参数： 无

返回值：
.jpg

### 文件上传
地址：/open/uploadImg/v1

方式：POST

请求参数：

|  参数   | 类型  |  是否必须 | 说明  |
|  ----  | ----  |---- | ---- |
| multipartFile  | file |是||

返回值：
```json
{
  "code": 0,
  "msg": "SUCCESS",
  "userMsg": "操作成功",
  "data": "http://static.wslhome.top/onlineChat/7f7201a1-7b3c-426c-a6d5-ccffdbc8fe26.jpg"
}
```
### 获取短信code
地址：/open/getCode/v1

方式：GET

请求参数： 

|  参数   | 类型  |  是否必须 | 说明  |
|  ----  | ----  |---- | ---- |
| phone  | String |是||


返回值：
```json
{
  "code": 0,
  "msg": "SUCCESS",
  "userMsg": "操作成功",
  "data": "发送成功"
}
```

