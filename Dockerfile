FROM openjdk:11-jdk-slim
ARG CODERINTUITION_DATASOURCE_URL
ARG CODERINTUITION_DATASOURCE_USERNAME
ARG CODERINTUITION_DATASOURCE_PASSWORD
ARG CODERINTUITION_GOOGLE_CLIENT_ID
ARG CODERINTUITION_GOOGLE_CLIENT_SECRET
ARG CODERINTUITION_FACEBOOK_CLIENT_ID
ARG CODERINTUITION_FACEBOOK_CLIENT_SECRET
ARG CODERINTUITION_GITHUB_CLIENT_ID
ARG CODERINTUITION_GITHUB_CLIENT_SECRET
ARG CODERINTUITION_JWT_SECRET
ARG CODERINTUITION_MAILGUN_KEY
ENV CODERINTUITION_DATASOURCE_URL=$CODERINTUITION_DATASOURCE_URL
ENV CODERINTUITION_DATASOURCE_USERNAME=$CODERINTUITION_DATASOURCE_USERNAME
ENV CODERINTUITION_DATASOURCE_PASSWORD=$CODERINTUITION_DATASOURCE_PASSWORD
ENV CODERINTUITION_GOOGLE_CLIENT_ID=$CODERINTUITION_GOOGLE_CLIENT_ID
ENV CODERINTUITION_GOOGLE_CLIENT_SECRET=$CODERINTUITION_GOOGLE_CLIENT_SECRET
ENV CODERINTUITION_FACEBOOK_CLIENT_ID=$CODERINTUITION_FACEBOOK_CLIENT_ID
ENV CODERINTUITION_FACEBOOK_CLIENT_SECRET=$CODERINTUITION_FACEBOOK_CLIENT_SECRET
ENV CODERINTUITION_GITHUB_CLIENT_ID=$CODERINTUITION_GITHUB_CLIENT_ID
ENV CODERINTUITION_GITHUB_CLIENT_SECRET=$CODERINTUITION_GITHUB_CLIENT_SECRET
ENV CODERINTUITION_JWT_SECRET=$CODERINTUITION_JWT_SECRET
ENV CODERINTUITION_MAILGUN_KEY=$CODERINTUITION_MAILGUN_KEY
WORKDIR /home
ADD target/*.jar coderintuition.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "coderintuition.jar" ]
