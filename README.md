# Wework Finance SDK Agent

企业微信会话存档代理服务

使用 Http Api 方式暴露会话存档接口  

**<font color=#FF000>不含调用鉴权，需自行配置</font>**

### Docker 镜像

https://hub.docker.com/repository/docker/xxutianyi/wework-finance-agent

### 配置项
```dotenv
# .env 文件或环境变量

DB_HOST=
DB_NAME=
DB_USER=
DB_PASSWORD=

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

### 导入接口参数
```http request
POST /api/init
Content-Type: application/json
```
```json5
// Request.body
{
  "corpId": " 企业ID ",
  "financeSecret": "会话存档 secret",
  "financePrivateKey": "-----BEGIN PRIVATE KEY-----******-----END PRIVATE KEY-----"
}

// Response.data:null
```

### 删除接口参数
```http request
DELETE /api/{corpID}/delete
```
```json5
// Request.body:null
// Response.data:null
```

### 获取会话内容
```http request
GET /api/{corpID}/messages
```
```json5
// Request.body:null
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
GET /api/{corpID}/media/{id}
```
```json5
// Request.body:null
// Response.data:腾讯云COS托管文件地址
```

 
 
 
