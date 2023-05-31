FROM amazoncorretto:17

ARG JAR_FILE
COPY lib/libWeWorkFinanceSdk.so /usr/lib/libWeWorkFinanceSdk.so
COPY target/${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8000