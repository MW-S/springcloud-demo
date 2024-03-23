

# springcloud-demo

## 当前开发（运行）环境：

- win10
- maven3.6.3
- OpenJDK17
- Docker
- IDEA

## 项目安装

1. 使用Git拉取项目

```
git clone https://gitee.com/mkingdragon/springcloud-demo.git
```

​	默认使用Gitee，也可以使用Github

```
git clone https://github.com/MW-S/springcloud-demo.git
```

2. 在项目根目录下运行下列命令

```shell
mvn clean install
```

### 

## 项目运行

### LDAP服务相关

1. 服务启动后，LDAP服务器的数据需要手动导入一下，文件目录在all/openldap/ldap_admin.ldif
2. LDAP管理员
   1. 账号：cn=admin,dc=test,dc=com
   2. 密码：123456

### Mysql服务相关

1. 账号：root
2. 密码：123456

### 系统账号密码

![image-20240324011915688](.\mdPic\account.png)

### 项目启动

0. **若只想运行Mysql以及LDAP服务，则直接到OtherService目录，运行以下指令**

    ```shell
    docker-compose up -d
    ```

1. 在根目录下进行打包，jar包会自动存放至all文件夹

```shell
mvn clean package
```

2. 打包完成后，需要到各个项目的根目录下面的target目录将jar包放置到all目录下，并分别重命名为Config.jar、Eureka.jar、Provider.jar、Uaa.jar

   ![image-20240324004246011](.\mdPic\projectStruct.png)

3. 进入all目录，使用docker-compose一键运行容器（运行完成后，LDAP服务器的数据需要手动导入一下，文件目录在all/openldap/ldap_admin.ldif）

```shell
docker-compose up -d
```

## 运行效果：

注：windows下的参数双引号需要加转义符

#### 获取token(数据库):

##### Linux环境：

```bash
curl.exe -X POST  -H "Content-Type: application/json"    -d '{"username":"editor_1","password":"editor_1"}'    "http://127.0.0.1:7573/jwt/login"
```

##### Windows CMD:

```shell
curl.exe -X POST  -H "Content-Type: application/json"    -d "{\"username\":\"editor_1\",\"password\":\"editor_1\"}"    "http://127.0.0.1:7573/jwt/login"
```

##### 效果图

![image-20240323234312873](.\mdPic\getToken1.png)

#### 获取token(LDAP):

##### Linux环境：

```bash
curl.exe -X POST  -H "Content-Type: application/json"    -d '{"username":"ldap_user_1","password":"ldap_user_1"}'    "http://127.0.0.1:7573/ldap/login"
```

##### Windows CMD:

```shell
curl.exe -X POST  -H "Content-Type: application/json"    -d "{\"username\":\"ldap_user_1\",\"password\":\"ldap_user_1\"}"    "http://127.0.0.1:7573/ldap/login"
```

##### 效果图

![image-20240324002307881](.\mdPic\getToken2.png)

#### 添加产品

##### Linux环境：

```bash
curl.exe -X POST  -H "Content-Type: application/json" -H "token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MTEyMTExNzIsInN1YiI6ImVkaXRvcl8xIn0.S6qYlPI9Rhy9ZQIxfabSRD7hWlYJExF4SjrZcZtazWI"   -d '{"name":"product1"}'    "http://127.0.0.1:7573/product/save"
```

##### Windows CMD:

```shell
curl.exe -X POST  -H "Content-Type: application/json" -H "token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MTEyMTExNzIsInN1YiI6ImVkaXRvcl8xIn0.S6qYlPI9Rhy9ZQIxfabSRD7hWlYJExF4SjrZcZtazWI"   -d "{\"name\":\"product1\"}"   "http://127.0.0.1:7573/product/save"
```

##### ![image-20240324000936171](.\mdPic\add.png)

查询产品列表

##### Linux环境：

```bash
curl.exe -X GET   -H "token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MTEyMTExNzIsInN1YiI6ImVkaXRvcl8xIn0.S6qYlPI9Rhy9ZQIxfabSRD7hWlYJExF4SjrZcZtazWI"    "http://127.0.0.1:7573/product/getList"
```

##### Windows CMD:

```shell
curl.exe -X GET   -H "token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MTEyMTExNzIsInN1YiI6ImVkaXRvcl8xIn0.S6qYlPI9Rhy9ZQIxfabSRD7hWlYJExF4SjrZcZtazWI"    "http://127.0.0.1:7573/product/getList"
```

##### ![image-20240324001701733](.\mdPic\read.png)

#### 修改产品

##### Linux环境：

```bash
curl.exe -X POST  -H "Content-Type: application/json" -H "token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MTEyMTExNzIsInN1YiI6ImVkaXRvcl8xIn0.S6qYlPI9Rhy9ZQIxfabSRD7hWlYJExF4SjrZcZtazWI"   -d '{"id":"2","name":"u_product1"}'    "http://127.0.0.1:7573/product/update"
```

##### Windows CMD:

```shell
curl.exe -X POST  -H "Content-Type: application/json" -H "token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MTEyMTExNzIsInN1YiI6ImVkaXRvcl8xIn0.S6qYlPI9Rhy9ZQIxfabSRD7hWlYJExF4SjrZcZtazWI"   -d "{\"id\":\"2\", \"name\":\"u_product1\"}"   "http://127.0.0.1:7573/product/update"
```

##### ![image-20240324001924701](.\mdPic\update.png)

#### 删除产品

##### Linux环境：

```bash
curl.exe -X POST  -H "Content-Type: application/json" -H "token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MTEyMTExNzIsInN1YiI6ImVkaXRvcl8xIn0.S6qYlPI9Rhy9ZQIxfabSRD7hWlYJExF4SjrZcZtazWI"   -d '{"id":"2"}'    "http://127.0.0.1:7573/product/delById"
```

##### Windows CMD:

```shell
curl.exe -X POST  -H "Content-Type: application/json" -H "token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MTEyMTExNzIsInN1YiI6ImVkaXRvcl8xIn0.S6qYlPI9Rhy9ZQIxfabSRD7hWlYJExF4SjrZcZtazWI"   -d "{\"id\":\"2\"}"   "http://127.0.0.1:7573/product/delById"
```

##### ![image-20240324002042588](.\mdPic\del.png)