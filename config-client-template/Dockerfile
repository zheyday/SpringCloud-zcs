FROM java:8
VOLUME /tmp
ADD target/config-client-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8620
ENTRYPOINT ["java","-jar","/app.jar"]