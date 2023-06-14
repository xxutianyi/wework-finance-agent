FROM amazoncorretto:17

COPY lib/libWeWorkFinanceSdk.so /usr/lib/libWeWorkFinanceSdk.so

CMD mkdir /usr/local/application
WORKDIR /usr/local/application

ARG JAR_FILE=target/api-wework-finance.jar

COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8900