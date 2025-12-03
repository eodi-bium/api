# [1단계] 빌드 단계
# Amazon Corretto 21 기본 이미지 (Amazon Linux 기반)
FROM amazoncorretto:21 AS builder

# 1. 작업 폴더 설정
WORKDIR /project

# 2. 소스 코드 전체 복사
COPY . .

# 3. Gradle 빌드
# gradlew 실행 권한 부여
RUN chmod +x gradlew
RUN ./gradlew clean build -x test

# [2단계] 실행 단계
# 실행 환경도 동일한 Amazon Corretto 21 기본 이미지 사용
FROM amazoncorretto:21
WORKDIR /app

# [필수 패키지 설치 및 설정]
# Amazon Linux(RHEL 계열)는 yum을 사용합니다.
# ca-certificates: 인증서 패키지 (최신화)
# tzdata: 시간대 데이터
# findutils: 기본적인 파일 검색 도구 등 (필요시)
RUN yum -y update && \
    yum -y install ca-certificates tzdata findutils && \
    yum clean all && \
    update-ca-trust

# 한국 시간대 설정 (KST)
ENV TZ=Asia/Seoul
# /etc/localtime을 서울 시간대로 링크 (RHEL/CentOS 계열 표준 설정 방식)
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# JAR 파일 위치 변경
COPY --from=builder /project/modules/app/build/libs/*.jar app.jar

# 실행
ENTRYPOINT ["java", "-Dspring.profiles.active=prod","-Djavax.net.debug=ssl:handshake", "-jar", "app.jar"]