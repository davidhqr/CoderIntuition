FROM openjdk:11-jdk-slim
ARG CODERINTUITION_DATASOURCE_URL
ARG CODERINTUITION_DATASOURCE_USERNAME
ARG CODERINTUITION_DATASOURCE_PASSWORD
ARG CODERINTUITION_JWT_SECRET
ENV CODERINTUITION_DATASOURCE_URL=$CODERINTUITION_DATASOURCE_URL
ENV CODERINTUITION_DATASOURCE_USERNAME=$CODERINTUITION_DATASOURCE_USERNAME
ENV CODERINTUITION_DATASOURCE_PASSWORD=$CODERINTUITION_DATASOURCE_PASSWORD
ENV CODERINTUITION_JWT_SECRET=$CODERINTUITION_JWT_SECRET
WORKDIR /home
ADD target/*.jar coderintuition.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "coderintuition.jar" ]
