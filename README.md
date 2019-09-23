# springcloud-demo

## spring cloud weathermap 使用指南
### 构建应用镜像
1. 登录ServiceStage，选择“持续交付 > 构建”，单击“基于源码构建”。
2. 在新页面填写参数。
   * 填写“Job名称”，建议设置为weathermap。
   * 设置“代码源来源”，选择“Github”，单击“绑定帐户”绑定自己的Github帐号。
   * 命名空间选择自己的Github帐号，仓库选择springcloud-demo，分支选择master。
   * 选择“构建集群”，即用于构建的集群。
3. 单击“下一步”，选择自定义构建模板。
4. 单击“高级配置”并在环境设置页面进行以下设置。
   * 在“编译”栏中，单击“添加插件”，选择“Build Common Cmd”，“构建语言”选择“Java”。
   * 在“编译”栏中，单击“添加插件”，选择“Docker”，分别添加四条构建任务，参数设置如下。

   |      Dockerfile目录              |  镜像名称         | 
   | ---------------------------      | ------------     |
   | ./weather-sample/weather/        |    weather-sc    | 
   | ./weather-sample/forecast/       |    forecast-sc   | 
   | ./weather-sample/fusionweather/  | fusionweather-sc | 
   | ./weather-sample/weathermapweb/  | weathermapweb-sc | 

   * 在“归档”栏中，单击“添加插件”，选择“Publish Build Image”，分别归档6.b中的镜像。
其他参数无需修改。该任务成功后，镜像包会自动归档到镜像仓库，供后续步骤使用。

5. 单击“构建”启动构建任务。
------
### 创建引擎
1. 登录ServiceStage，选择“基础设施 > 微服务引擎(CSE)”，单击“购买微服务引擎”。
2. 消息中间件选择“RabbitMQ”，设置并记下用户名密码。
3. HTTPS关闭，然后单击“立即购买”。
4. 引擎创建好后，点击引擎名字进入引擎基本信息界面，点击“服务类型”。
5. 下载项目根目录下的weather-input.json文件，替换相应的参数。
   * mqhost:设置为引擎详情，“服务类型”下面的"messageQueue"的host
   * mqport:设置为引擎详情，“服务类型”下面的"messageQueue"的port
   * mquser:设置为第2步中的用户名
   * mqpassword:设置为第2步中的密码
   * eureka:设置为引擎详情，“服务类型”下面的"eureka"的访问地址
   
------
### 创建后台应用
1. 登录ServiceStage，选择“应用管理 > 应用列表”，单击“创建应用”。
2. 选择“SpringCloud”，然后单击“创建应用”。
3. 进行如下设置。
   * 框架选择：Spring Cloud。
   * “运行环境”选择“Docker”。
   * 填写“应用名称”，设置为weather-sc。
   * 选择应用“部署集群”。
   * 注意：所有应用需要部署在同一个集群中。
   * “实例数量”修改为1。
   * 其他参数无需设置。
4. 单击“下一步”配置应用。
   * 单击“选择镜像”。
   * 在弹出页面中单击“我的镜像”，选择刚刚发布的镜像,单击“确定”。
   * 设置微服务引擎。
   *     说明：应用创建以后，微服务会注册到设置的微服务引擎。所有应用需要注册到同一个微服务引擎。其他参数无需设置。
   * 单击“高级设置 > 应用配置”，进入“环境变量”，单击“导入”，选择编辑后的weather-input.json文件。也可选择手动导入环境变量。
5. 单击“下一步”，确认规格，单击“创建”，创建应用。
#### 应用参数配置

| 应用名称      | 选择镜像      | 环境变量 | TCP/UDP路由配置                                              |
| ------------- | ------------- | ---------- | ------------------------------------------------------------ |
| weather       | weather-sc  | 是(导入)       | 否                                                           |
| forecast      | forecast-sc     | 是(导入)       | 否                                                           |
| fusionweather | fusionweather-sc | 是(导入)       | 应用创建成功后，在“应用列表”页单击“fusionweather”，进入应用详情页。选择“访问方式”页签。 |
|               |              |         |   1.单击“TCP/UDP路由配置”区域的“添加服务”。                         |
|               |              |         |   2.“服务名称”建议填写fusion。                                 |
|               |              |         |   3.“访问方式”选择“集群内访问”。“协议”选择TCP。                   |
|               |              |         |   4.“容器端口”填写8876。                                      |
|               |              |         |   5.“访问端口”请自行填写，但请保证该端口未被占用，建议与容器端口相同。 |

#### 异常处理
    应用创建以后，如果服务状态长期处于“未就绪”状态时，可以进入服务实例列表，展开实例详细信息，在“事件”页签查看详细信息，显示内存不足。可以参考添加节点（可选）新增节点。
    到创建的5个服务的状态都为“运行中”，服务可以正常访问。
-----
### 创建前台应用
1. 登录ServiceStage，选择“应用管理 > 应用列表”，单击“创建应用”。
2. 选择“Spring Cloud”，然后单击“创建应用”。
3. 进行如下设置。
   * “运行环境”选择“Docker”。
   * 填写“应用名称”，设置为weathermapweb-sc。
   * 选择应用“部署集群”。
   * 注意：所有应用需要部署在同一个集群中。
   * “实例数量”修改为1。
   * 其他参数无需设置。
4. 单击“下一步”配置应用。
   * 单击“选择镜像”。
   * 在弹出页面中单击“我的镜像”，搜索“weathermapweb-sc”。
   * 选中搜索结果中的weathermapweb-sc镜像包，单击“使用此镜像”。
   * 设置微服务引擎。
   * 单击“高级设置 > 应用配置”，进入“环境变量”，单击“添加环境变量”，添加如下环境变量。

   |      类型        |  变量名     |    变量值       | 
   | --------------- | -----------   | ------------  | 
   |    手动添加      | HTTP_PROXY_HOST |   (填写fusionweather的集群内IP）    | 
   |    手动添加      | HTTP_PROXY_PORT |   (填写创建fusionweather的集群内PORT） |
      
5. 单击“下一步”，确认规格，单击“创建”，创建应用。
6 .应用创建成功后，在“应用列表”页单击“weathermapweb”，进入应用详情页。
7. 选择“访问方式”页签。
   * 单击“TCP/UDP路由配置”区域的“添加服务”。
   * “服务名称”建议填写weathermapweb-sc。
   * “访问方式”选择“公网访问”。
   * “访问类型”选择“弹性IP”。
   * “协议”选择TCP。
   * “容器端口”填写3000。
   * “访问端口”选择“自动生成”。
------
### 访问应用
点击刚配置的弹性IP，即可访问应用
