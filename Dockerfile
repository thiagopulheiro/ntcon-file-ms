FROM openjdk:8-jdk-alpine
RUN apk add --no-cache bash coreutils
VOLUME /tmp
ADD wait-for-it.sh wait-for-it.sh
EXPOSE 8080
