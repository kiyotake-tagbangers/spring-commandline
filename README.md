# Spring-commandline

## run
 
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

## build image

```
pack build spring-commandline -p $(\ls target/*jar) --builder cloudfoundry/cnb:bionic
```

or

```
./mvnw spring-boot:build-image
```

```
docker image ls | grep spring-commandline
```

## run as a container

```
docker run --rm \
--name spring-commandline \
spring-commandline:$(./mvnw org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=project.version -q -DforceStdout) \
--command=--app.name=spring-commandline,--option=optionA
```