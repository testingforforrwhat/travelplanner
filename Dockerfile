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