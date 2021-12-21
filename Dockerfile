FROM maven:latest
MAINTAINER developer@TheVer4.ru
RUN git clone https://github.com/TheVer4/spring-web-paint.git
WORKDIR spring-web-paint
ENV JDBC_URL='jdbc:mysql://localhost:3306/springwebpaint' DB_USER='springwebpaint' DB_PASSWD='YtN0Ve6Q0xqqufl6'
RUN mvn package
CMD ["java", "-jar", "target/$(ls target | grep .jar$)"]