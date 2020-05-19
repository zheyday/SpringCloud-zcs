FROM java:8
VOLUME /tmp
ADD target/eureka-server-single-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
EXPOSE 8761
ENTRYPOINT ["java","-jar","/app.jar"]