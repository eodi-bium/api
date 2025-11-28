# [1단계] 빌드 단계
FROM eclipse-temurin:17-jdk-alpine AS builder
# 1. 작업 폴더 설정
WORKDIR /project
# 2. 소스 코드 전체 복사
COPY . .

# 3. Gradle 빌드 (전체 프로젝트 빌드)
RUN chmod +x gradlew
RUN ./gradlew clean build -x test

# [2단계] 실행 단계
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# JAR 파일 위치 변경
COPY --from=builder /project/modules/app/build/libs/*.jar app.jar
# 실행
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]