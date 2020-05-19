FROM java:8
VOLUME /tmp
ADD target/oauth-server-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
EXPOSE 9120
ENTRYPOINT ["java","-jar","/app.jar"]