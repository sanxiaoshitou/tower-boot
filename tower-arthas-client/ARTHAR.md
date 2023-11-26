##  Arthas 集成

### 一、下载
参考官网:https://arthas.aliyun.com/doc/ 集成。    
下载部署 arthas-tunnel-server.jar    
从 Maven 仓库下载：https://arthas.aliyun.com/download/arthas-tunnel-server/latest_version?mirror=aliyun    
从 Github Releases 页下载： https://github.com/alibaba/arthas/releases

### 二、部署 arthas-tunnel-server.jar    3.7.1版本为例
1、直接启动 或后台启动写法
```
java -jar arthas-tunnel-server-3.7.1-fatjar.jar
```
```
nohup java -jar arthas-tunnel-server-3.7.1-fatjar.jar > server.out &
```
2、管理平台    
默认情况下，arthas tunnel server 的web 端口是8080,arthas agent 连接的端口是7777。   
通过 Spring Boot 的 Endpoint，可以查看到具体的连接信息：http://127.0.0.1:8080/actuator/arthas ，
登陆用户名是arthas,密码在 arthas tunnel server 的日志里可以找到。

### 三、spring 项目引用配置
1、pom依赖
```xml
<dependency>
    <groupId>com.taobao.arthas</groupId>
    <artifactId>arthas-spring-boot-starter</artifactId>
    <version>3.7.1</version>
</dependency>
```
2、yml 依赖
```yml
arthas:
  # telnetPort、httpPort为 -1 ，则不listen telnet端口，为 0 ，则随机telnet端口
  # 如果是防止一个机器上启动多个 arthas端口冲突。可以配置为随机端口，或者配置为 -1，并且通过tunnel server来使用arthas。
  # ~/logs/arthas/arthas.log (用户目录下面)里可以找到具体端口日志
  telnetPort: -1
  httpPort: -1
  # 127.0.0.1只能本地访问，0.0.0.0则可网络访问，但是存在安全问题
  ip: 127.0.0.1
  appName: arthas_test
  # 默认情况下，会生成随机ID，如果 arthas agent配置了 appName，则生成的agentId会带上appName的前缀。
  agent-id: hxl123456
  # tunnel-server地址
  tunnel-server: ws://127.0.0.1:7777/ws
```

### 四、其他集成方式
1、pom依赖
```xml
<dependency>
    <groupId>com.taobao.arthas</groupId>
    <artifactId>arthas-agent-attach</artifactId>
    <version>${arthas.version}</version>
</dependency>

<dependency>
    <groupId>com.taobao.arthas</groupId>
    <artifactId>arthas-packaging</artifactId>
    <version>${arthas.version}</version>
</dependency>
```
2、用法
```java
HashMap<String, String> configMap = new HashMap<String, String>();
configMap.put("arthas.appName", "demo");
configMap.put("arthas.tunnelServer", "ws://127.0.0.1:7777/ws");
ArthasAgent.attach(configMap);
```

### 五、常见命令
dashboard - 当前系统的实时数据面板   
getstatic - 查看类的静态属性   
heapdump - dump java heap, 类似 jmap 命令的 heap dump 功能
jvm - 查看当前 JVM 的信息   
logger - 查看和修改 logger   
mbean - 查看 Mbean 的信息   
memory - 查看 JVM 的内存信息   
ognl - 执行 ognl 表达式   
perfcounter - 查看当前 JVM 的 Perf Counter 信息   
sysenv - 查看 JVM 的环境变量   
sysprop - 查看和修改 JVM 的系统属性   
thread - 查看当前 JVM 的线程堆栈信息   
vmoption - 查看和修改 JVM 里诊断相关的 option   
vmtool - 从 jvm 里查询对象，执行 forceGc   

### 六、arthas工具辅助
参考:
https://blog.csdn.net/m0_47743175/article/details/128750284

