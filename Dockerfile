
# Dockerfile 集成 ./gradlew clean build 自动构建 JAR，就需要用到 多阶段构建（multi-stage build）。
# 这样你可以不用在本地 ./gradlew clean build  先手动构建 jar，让 Docker 构建流程从拉代码到打包、运行“一步到位”。 适合 CI/CD 流水线或无需提前手动构建的场景。

# -------- 第一阶段：用 JDK 构建 jar --------
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /build

# 优化缓存层，先拷贝基础构建文件
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 拷贝源码
COPY src src

# 构建 jar 包，x test 可选跳过单元测试，加快速度
#  Dockerfile 集成 ./gradlew clean build 自动打包 Spring Boot 项目; 适合 CI/CD，代码更新后即可自动打jar并部署。

#15 [builder 8/8] RUN ./gradlew clean build -x test
#15 0.331 /bin/sh: 1: ./gradlew: Permission denied
#15 ERROR: process "/bin/sh -c ./gradlew clean build -x test" did not complete

# 添加这行，确保 gradlew 可执行
RUN chmod +x gradlew 
RUN ./gradlew clean build -x test

# -------- 第二阶段：用 JRE 极简运行 --------
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# 只拷贝最终的 jar 包进来
COPY --from=builder /build/build/libs/*.jar app.jar

EXPOSE 8081

# JVM 连接 PostgreSQL：只要 build.gradle 已引入 postgresql 驱动，无需单独写
# 启动命令如需外部参数可参考参数化写法
ENTRYPOINT ["java", "-jar", "app.jar"]  