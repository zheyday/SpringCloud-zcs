FROM java:8
VOLUME /tmp
ADD target/consumer_feign-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8765
ENTRYPOINT ["java","-jar","/app.jar"]