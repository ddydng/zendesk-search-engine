FROM openjdk:11
ADD target target
CMD ["java", "-cp", "target/dependency/*:target/classes", "cli.Console"]