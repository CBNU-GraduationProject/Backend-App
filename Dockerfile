
# OpenJDK 17 사용
FROM openjdk:17-jdk-slim

# 작업 디렉터리 설정
WORKDIR /app

# JAR 파일 복사 (빌드된 JAR 파일 경로에 맞게 수정)
COPY demo/build/libs/demo-frontend.jar app.jar

# 포트 80을 노출
EXPOSE 80

# Spring Boot 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
