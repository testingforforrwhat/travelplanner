# -------- 第一阶段：构建 jar --------  

FROM eclipse-temurin:17-jdk-jammy AS builder  

WORKDIR /build  

# 拷贝 Gradle Wrapper 和工程文件（优化缓存）  
COPY gradlew .  
COPY gradle gradle  
COPY build.gradle .  
COPY settings.gradle .  
COPY src src  

# 构建 jar 包  
RUN ./gradlew clean build -x test  

# -------- 第二阶段：极简运行环境 --------  

FROM eclipse-temurin:17-jre-jammy  

WORKDIR /app  

# 只复制最终 jar 文件到运行环境（生产镜像更小更安全）  
COPY --from=builder /build/build/libs/*.jar app.jar  

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]  