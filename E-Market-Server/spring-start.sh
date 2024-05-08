#!/bin/bash

SPRING_PID=$(pgrep -f effortless-market-0.0.1)
SPRING_PATH="./effortless-market-0.0.1-SNAPSHOT.jar"
SPRING_PROFILE="prod"

if [ -z "$SPRING_PID" ]; then
  echo "애플리케이션을 시작합니다."
  nohup java -jar -Dspring.profiles.active=$SPRING_PROFILE $SPRING_PATH > /dev/null 2>&1 &
  echo "애플리케이션 시작 완료."
else
  echo "실행중인 서비스가 있습니다."
  echo "서비스 종료 후 다시 시작 하시겠습니까? (y,n)"

  read ANSWER

  if [ "$ANSWER" = "y" ]; then
    echo "서비스를 종료 합니다."
    kill $SPRING_PID

    while kill -0 $SPRING_PID 2>/dev/null; do
        echo "서비스 종료를 기다리는 중입니다...."
        sleep 1
    done

    echo "애플리케이션을 다시 시작합니다."
    nohup java -jar -Dspring.profiles.active=$SPRING_PROFILE $SPRING_PATH > /dev/null 2>&1 &
    echo "애플리케이션 시작 완료."
  else
    echo "서비스 재시작을 취소했습니다."
  fi
fi
