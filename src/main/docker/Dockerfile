FROM openjdk:8-jre-alpine

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    APP_SLEEP=0 \
    JAVA_OPTS=""

# Add a warehouse user to run our application so that it doesn't need to run as root
RUN adduser -D -s /bin/sh warehouse
WORKDIR /home/warehouse

ADD entrypoint.sh entrypoint.sh
RUN chmod 755 entrypoint.sh && chown warehouse:warehouse entrypoint.sh
USER warehouse

ENTRYPOINT ["./entrypoint.sh"]

EXPOSE 8080

ADD *.war app.war

