FROM openjdk:8-alpine

COPY target/uberjar/testjsonpatch.jar /testjsonpatch/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/testjsonpatch/app.jar"]
