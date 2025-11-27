# [1단계] 빌드 단계: 여기서 Gradle을 실행해서 JAR 파일을 만듭니다.
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /build

# 프로젝트 파일 전체를 Docker 안으로 복사
COPY . .

# Gradle 실행 권한 부여 및 빌드 (테스트 생략)
RUN chmod +x gradlew
RUN ./gradlew clean build -x test

# --------------------------------------------------------

# [2단계] 실행 단계: 위에서 만든 JAR 파일만 쏙 가져와서 실행합니다.
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# 1단계(builder)에서 만든 JAR 파일을 복사해옵니다.
# 경로를 찾을 필요 없이 Docker가 알아서 가져옵니다.
COPY --from=builder /build/build/libs/*.jar app.jar

# 실행
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]