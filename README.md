# Wework Finance SDK Agent

企业微信会话存档代理服务

使用 Http Api 方式暴露会话存档接口  

**<font color=#FF000>服务中不保留任何敏感信息，可放心使用</font>**

### Docker 镜像

https://hub.docker.com/repository/docker/xxutianyi/api-wework-finance

### 配置项
```dotenv
# 环境变量

TENCENT_CLOUD_ID=
TENCENT_CLOUD_KEY=
TENCENT_CLOUD_REGION=
TENCENT_CLOUD_BUCKET=

```

### 返回格式
```json5
{
    "success": true,
    "errorCode": "2000",
    "errorMessage": "success",
    "showType": 1,
    "traceId": "9cba3ce3-7557-445f-8cc1-4f5a824b1989",
    "host": "127.0.0.1",
    "data": "any data"
}
```

### 获取会话内容（一次返回50条）
```http request
POST /api/messages/{seq}
```
```json5
// Request.body
{
  "corpId": " 企业ID ",
  "financeSecret": "会话存档 secret",
  "financePrivateKey": "-----BEGIN PRIVATE KEY-----******-----END PRIVATE KEY-----"
}
```
```json5
// Response.data
//和官方接口返回解密后内容完全一致
[
  {
    msgid: "xxx",
    action: "send/recall/switch",
    from: "xxx",
    tolist: [
      "woXxx",
      "wmXxx"
    ],
    roomid: "",
    msgtime: "1685063000000",
    msgtype: "text/image/...",
    text: {
      content: "Hello World"
    },
    image: {
      md5sum: "",
      filesize: 102400,
      sdkfileid: "xxx"
    },
    seq: 12000
  }
  //...
]
```

### 获取媒体文件
媒体文件会上传到腾讯云COS对象存储，上传完毕删除本地临时文件  
可自行修改 MessageServiceImpl.uploadToObjectStorage()
```http request
POST /api/media/{id}
```
```json5
// Request.body
{
  "corpId": " 企业ID ",
  "financeSecret": "会话存档 secret",
  "financePrivateKey": "-----BEGIN PRIVATE KEY-----******-----END PRIVATE KEY-----"
}
```
```json5
// Response.data:腾讯云COS托管文件地址
```

 
 
 
