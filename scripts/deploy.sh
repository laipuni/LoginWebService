#!/bin/bash

REPOSITORY=/home/ec2-user/app
PROJECT_NAME=LoginWebService

echo "> Copy Build File"

cp $REPOSITORY/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/

echo "> Check Running Application"

CURRENT_PID=$(pgrep -fl LoginWebService | awk '{print $1}')

echo "> Running Application Pid : $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
        echo "> Now Running Application isn't Exist"
else
        echo "> kill -15 $CURRENT_PID"
        kill -15 $CURRENT_PID
        sleep 15
fi

echo "> Deploy New Application"

JAR_NAME=$(ls -tr $REPOSITORY/ | grep jar | tail -n 1)

echo "> JAR NAME: $JAR_NAME"

echo "> add Excution previlage to $JAR_NAME"

chmod +x $JAR_NAME

nohup java -jar $REPOSITORY/$JAR_NAME 2>&1 &

$JAR_NAME > $REPOSITORY/nohup.out 2>&1 &

