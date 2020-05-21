FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD target/oauth-server-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]