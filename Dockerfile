# OpenJDK 17 Slim 버전의 이미지를 사용
FROM openjdk:17-jdk-slim-buster

# 빌드된 jar 파일을 컨테이너로 복사
COPY build/libs/*.jar app.jar

# 포트 노출
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
