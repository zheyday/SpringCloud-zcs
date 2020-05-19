FROM java:8
VOLUME /tmp
ADD target/zuul-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 9110
ENTRYPOINT ["java","-jar","/app.jar"]