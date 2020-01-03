# Meethere

> 软件测试期末项目后端及测试代码仓库，写于2019年冬季。

## 项目导航

+ 需求、测试等（报告）文档汇总见[Meethere_Doc](https://github.com/LEODPEN/Meethere_Doc)。

+ [本仓库](https://github.com/Onion12138/Meet)为后端及测试代码仓库。

+ 前端代码仓库见[MeetHere_Front](https://github.com/TimGin117/MeetHereFront)。

## 后端工具与技术选用

+ IDE: `IDEA` 2019.3 、 `Visual Studio Code` 1.41.1

+ 数据库: `Mysql 8.0` 、 `Redis`

+ 数据校验: `JSR303` `Validation`

+ 持久层框架: `通用Mapper`、 `SpringDataJPA`

+ 安全: `SpringSecurity` 、`Jwt`

+ 对象存储: `七牛云`

## 测试工具与技术选用

+ 单元测试: `Junit 5`、 `Mockito`

+ 工具: `Postman`、 `JMeter 5.2.1`

+ 静态代码分析: `FindBugs` 、 `Alibaba编程规约P3C` ( 自动生成的`html`格式报告见Doc仓库 )

## 后端开发代码结构
```
.
├── java
│   └── com
│       └── ecnu
│           ├── MeetHereApplication.java
│           ├── annotation
│           │   ├── AdminOnly.java
│           │   └── LoginRequired.java
│           ├── aop
│           │   ├── MyExceptionHandler.java
│           │   ├── VerifyAdmin.java
│           │   └── VerifyLogin.java
│           ├── config
│           │   └── WebSecurityConfig.java
│           ├── controller
│           │   ├── GymController.java
│           │   ├── NewsController.java
│           │   ├── OrderController.java
│           │   └── UserController.java
│           ├── dao
│           │   ├── CommentMapper.java
│           │   ├── GymDao.java
│           │   ├── GymMapper.java
│           │   ├── NewsDao.java
│           │   ├── NewsMapper.java
│           │   ├── OrderDao.java
│           │   ├── OrderMapper.java
│           │   ├── UserDao.java
│           │   └── UserMapper.java
│           ├── domain
│           │   ├── Gym.java
│           │   ├── News.java
│           │   ├── NewsComment.java
│           │   ├── Order.java
│           │   └── User.java
│           ├── enums
│           │   └── ResultEnum.java
│           ├── exception
│           │   └── MyException.java
│           ├── request
│           │   ├── AvailableTimeRequest.java
│           │   ├── CommentRequest.java
│           │   ├── GymFilterRequest.java
│           │   ├── NewsRequest.java
│           │   ├── OrderCommentRequest.java
│           │   ├── OrderRequest.java
│           │   ├── UserLoginRequest.java
│           │   └── UserRegisterRequest.java
│           ├── service
│           │   ├── GymService.java
│           │   ├── MailService.java
│           │   ├── NewsService.java
│           │   ├── OrderService.java
│           │   ├── UserService.java
│           │   └── impl
│           │       ├── GymServiceImpl.java
│           │       ├── MailServiceImpl.java
│           │       ├── NewsServiceImpl.java
│           │       ├── OrderServiceImpl.java
│           │       └── UserServiceImpl.java
│           ├── utils
│           │   ├── CodeUtil.java
│           │   ├── JwtUtil.java
│           │   ├── KeyUtil.java
│           │   └── ParamUtil.java
│           └── vo
│               └── ResultVO.java
└── resources
    └── application.yml
```

## 测试代码结构
```
.
└── java
    └── com
        └── ecnu
            ├── IntegrationTests
            │   ├── IntegrationTestForGymController.java
            │   ├── IntegrationTestForNewsController.java
            │   ├── IntegrationTestForOrderController.java
            │   └── IntegrationTestForUserController.java
            ├── controller
            │   ├── GymControllerTest.java
            │   ├── NewsControllerTest.java
            │   ├── OrderControllerTest.java
            │   └── UserControllerTest.java
            ├── dao
            │   └── OrderDaoTest.java
            ├── service
            │   ├── GymServiceTest.java
            │   ├── NewsServiceTest.java
            │   ├── OrderServiceTest.java
            │   └── UserServiceTest.java
            └── utils
                ├── CodeUtilTest.java
                └── JwtUtilTest.java

```
## 其他
如有其他问题请于`issues`提出或与此仓库拥有者联系。




