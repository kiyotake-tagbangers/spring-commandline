# Spring-commandline

```
export JAVA_HOME=`/usr/libexec/java_home -v 11`

./mvnw clean package

java -jar target/spring-commandline-0.0.1-SNAPSHOT.jar --app.name=spring-commandline --option=optionA

./mvnw spring-boot:run -Dspring-boot.run.arguments=--app.name=spring-commandline,--option=optionA
```

outputs

```
# OptionArgs: 2
OptionArgs:
app.name=[spring-commandline]
option=[optionA]
```
