FROM alibabadragonwell/dragonwell:21-ubuntu
LABEL author="netbuffer"
WORKDIR /
COPY target/spring-boot4-demo.jar /
ENV SERVER_PORT=8080
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /spring-boot4-demo.jar"]
