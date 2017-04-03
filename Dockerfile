FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
RUN mkdir -p /usr/app/ignite
COPY ignite/ /usr/app/ignite
ADD api-ignite-creditcard-0.0.5.jar app.jar
ENV spring_profiles_active=test
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]