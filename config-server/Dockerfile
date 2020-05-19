FROM java:8
VOLUME /tmp
ADD target/config-server-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8610
ENTRYPOINT ["java","-jar","/app.jar"]