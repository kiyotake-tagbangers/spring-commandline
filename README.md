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
--app.name=spring-commandline --option=optionA
```

## run as a task in ECS environment

### push ecr

```
aws ecr get-login-password --region ap-northeast-1 | docker login --username AWS --password-stdin YOUR_AWS_ACCOUNT.dkr.ecr.ap-northeast-1.amazonaws.com

docker tag spring-commandline:$(./mvnw org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=project.version -q -DforceStdout) \
YOUR_AWS_ACCOUNT.dkr.ecr.ap-northeast-1.amazonaws.com/spring-commandline:latest

docker push YOUR_AWS_ACCOUNT.dkr.ecr.ap-northeast-1.amazonaws.com/spring-commandline:latest
```

### run

```
# クラスタの一覧
aws ecs list-clusters | jq '.clusterArns[]' -r

# タスク定義の登録
aws ecs register-task-definition --cli-input-json file://./task-def.json

# タスク定義の表示
aws ecs list-task-definitions | grep spring-commandline

# タスクの実行
aws ecs run-task --cluster your-cluster-name \
--task-definition spring-commandline \
--count 1 \
--launch-type FARGATE \
--network-configuration "awsvpcConfiguration={subnets=[subnet-your-subnet],securityGroups=[sg-your-security-group],assignPublicIp=DISABLED}" \
--overrides file://./container-overrides.json
 
# 実行中のタスクの一覧
aws ecs list-tasks --cluster your-cluster-name

# 終了した停止しているタスク一覧
aws ecs list-tasks --cluster your-cluster-name --desired-status STOPPED

TASK_ID="aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"

# ログの確認(grep はタスクID)
aws logs describe-log-streams --log-group-name /spring-commandline | grep $TASK_ID

LOG_STREAM_NAME="ecs/spring-commandline/aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"

# ログをローカルにファイルとして保存
aws logs get-log-events --log-group-name /spring-commandline --log-stream-name $LOG_STREAM_NAME --query 'events[*].message' > /tmp/$(date "+%Y%m%d-%H%M%S").log
```

### register and run as scheduled task

```
# ルールの登録
aws events put-rule --schedule-expression "cron(0/5 * * * ? *)" --name spring-commandline-every5-minites

aws events list-rules

# rule に target を登録 
aws events put-targets --rule "spring-commandline-every5-minites" \
--targets file://target.json

aws events list-targets-by-rule --rule spring-commandline-every5-minites

# 削除する場合
aws events remove-targets --rule "spring-commandline-every5-minites" --ids 1

aws events list-targets-by-rule --rule "spring-commandline-every5-minites"

aws events delete-rule --name "spring-commandline-every5-minites"
```
