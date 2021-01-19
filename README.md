# cc-mall
springboot + layui 开发mall电商

## 后端技术
* Spring Boot
* Spring Framework
* Spring Security
* JWT
* Mybatis
* Mybatis-Plus
* Redis
* RocketMQ
* Qiniu
* Swagger
* LogStash
* Hutool
* Lombok

## 结构
cc-mall:

-------mall-admin:管理系统后台

-------mall-app:商城app后台

-------mall-common:基础模块

-------mall-mbg:mbg数据库代码生成
## Mysql
1.安装
```
$ yum install mysql
```
2.启动关闭mysql服务
```
$ service mysql start
$ service mysql stop
```
或者
```
$ systemctl start mysqld.service
$ systemctl stop mysqld.service
```
## Redis
1.安装
```
linux
$ yum install redis

windows
下载地址：https://github.com/MicrosoftArchive/redis/releases
$ redis-server.exe redis.windows.conf

可视化工具 AnotherRedisDesktopManager
下载地址：https://github.com/qishibo/AnotherRedisDesktopManager
```
2.启动/关闭
```
$ redis-server 或者 ($ redis-server /etc/redis.conf)
$ redis-cli shutdown
```
## maven
```
$ wget https://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
$ tar -zxvf apache-maven-3.6.3-bin.tar.gz
$ mv apache-maven-3.6.3-bin.tar.gz maven
$ mv maven /usr/local
```
配置环境
```
$ vim ~/.zshrc 或者 vim ~/.bash_profile
+ export M2_HOME=/usr/local/maven      // 更改为你的路径
+ export PATH=$PATH:$M2_HOME/bin
$ source ~/.zshrc 或者 source ~/.bash_profile   // 执行配置好的环境
$ mvn -v  // 查看
```
## RocketMQ
1. 安装
```
rocketmq
$ wget https://archive.apache.org/dist/rocketmq/4.7.0/rocketmq-all-4.7.0-bin-release.zip
$ unzip rocketmq-all-4.7.0-bin-release.zip
$ mv rocketmq-all-4.7.0-bin-release rocketmq
$ mv rocketmq /usr/local

$ vim /etc/profile
+ export PATH=$PATH:/usr/local/rocketmq/bin
+ export NAMESRV_ADDR=localhost:9876

$ source /etc/profile
```
```
rocketmq 监控可视化平台
$ git clone https://github.com/apache/rocketmq-externals
# 或者本地下载后另外上传服务器
$ unzip rocketmq-externals-master.zip
$ cd /rocketmq-externals-master/rocketmq-console
$ mvn clean package -Dmaven.test.skip=true
$ cd target/
# 启动监控平台
$ java -jar rocketmq-console-ng-2.0.0.jar --server.port=8080 --rocketmq.config.namesrvAddr=ip地址:9876
```
2. 启动/关闭
```
$ mqnamesrv &
$ mqbroker -n localhost:9876 &

$ mqshutdown broker
$ mqshutdown namesrv

# 外网访问
$ vim /usr/local/rocketmq/conf/broker.conf
+ namesrvAddr=www.littleredhat1997.com:9876
+ brokerIP1=www.littleredhat1997.com

# 后台运行
$ nohup mqnamesrv &
$ nohup mqbroker -c /usr/local/rocketmq/conf/broker.conf &
# jps查看进程是否启动
$ jps
```

## Tomcat
1. 安装
```
$ wget http://mirrors.tuna.tsinghua.edu.cn/apache/tomcat/tomcat-9/v9.0.30/bin/apache-tomcat-9.0.30.tar.gz
$ tar -zxvf apache-tomcat-9.0.30.tar.gz
$ mv apache-tomcat-9.0.30 tomcat
$ mv tomcat /usr/local
```

2. 启动/关闭
```
$ cd /usr/local/tomcat
$ ./bin/start.sh
$ ./bin/stop.sh
```

bug list:
* Long类型在浏览器中精度丢失

  参考链接
> * https://github.com/zhangbincheng1997/mall
> * http://www.macrozheng.com/#/README
> * https://github.com/ityouknow/spring-boot-examples
> * https://github.com/apache/rocketmq-spring/wiki
> * https://www.layui.com/doc/
> * https://github.com/apache/rocketmq-spring/wiki
> * https://baomidou.com/guide/

