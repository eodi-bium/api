# 1. Base Image: Java 실행 환경 (JRE) 설정
# 프로젝트의 JDK 버전과 맞춰야 합니다. (여기선 17로 가정)
# eclipse-temurin은 가장 널리 쓰이는 안정적인 OpenJDK 배포판 중 하나입니다.
FROM eclipse-temurin:17-jre-alpine

# 2. 작업 디렉토리 생성 및 설정
# 컨테이너 내부에서 애플리케이션이 위치할 폴더를 지정합니다.
WORKDIR /app

# 3. 빌드된 JAR 파일 위치 변수 정의
# Gradle 빌드 후 생성되는 JAR 파일의 경로입니다.
# 보통 build/libs/프로젝트명-버전-SNAPSHOT.jar 형태로 생성됩니다.
ARG JAR_FILE=build/libs/*.jar

# 4. JAR 파일 복사
# 위에서 정의한 호스트의 JAR 파일을 컨테이너 내부의 /app/app.jar로 복사합니다.
COPY ${JAR_FILE} app.jar

# 5. 실행 포트 노출 (문서화 목적)
# 실제 포트 연결은 docker run -p 옵션이 하지만, 이 이미지가 8080을 쓴다는 것을 명시합니다.
EXPOSE 8080

# 6. 애플리케이션 실행 명령어
# 컨테이너가 시작될 때 실행할 명령입니다.
# 프로파일 설정(-Dspring.profiles.active=prod)을 여기서 넣는 것이 가장 확실합니다.
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]