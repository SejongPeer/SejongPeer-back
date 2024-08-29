#!/bin/bash

# 작업 디렉토리를 /home/ec2-user/app으로 변경
cd /home/ec2-user/app

# 환경변수 DOCKER_APP_NAME을 sejongpeer으로 설정
DOCKER_APP_NAME=sejongpeer

# 현재 실행중인 컨테이너가 있는지 확인
EXIST_BLUE=$(sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml ps | grep Up)
EXIST_GREEN=$(sudo docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml ps | grep Up)

echo "배포 시작일자 : $(date '+%Y-%m-%d %H:%M:%S')" >> /home/ec2-user/deploy.log

# green이 실행 중이면 blue를 배포
if [ -z "$EXIST_BLUE" ]; then
  echo "blue 배포 시작 : $(date '+%Y-%m-%d %H:%M:%S')" >> /home/ec2-user/deploy.log

  # blue 배포 시작
  sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml up -d --build

  sleep 30

  # blue가 정상적으로 실행되었는지 확인
  BLUE_HEALTH=$(sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml ps | grep Up)

  if [ -z "$BLUE_HEALTH" ]; then
    echo "blue 배포 실패" >> /home/ec2-user/deploy.log
  else
    echo "green 중단 시작 : $(date '+%Y-%m-%d %H:%M:%S')" >> /home/ec2-user/deploy.log
    sudo docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml down

    sudo docker image prune -af

    echo "green 중단 완료 : $(date '+%Y-%m-%d %H:%M:%S')" >> /home/ec2-user/deploy.log
  fi

# blue가 실행 중이면 green을 배포
else
  echo "green 배포 시작 : $(date '+%Y-%m-%d %H:%M:%S')" >> /home/ec2-user/deploy.log

  # green 배포 시작
  sudo docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml up -d --build

  sleep 30

  # green이 정상적으로 실행되었는지 확인
  GREEN_HEALTH=$(sudo docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml ps | grep Up)

  if [ -z "$GREEN_HEALTH" ]; then
    echo "green 배포 실패" >> /home/ec2-user/deploy.log
  else
    echo "blue 중단 시작 : $(date '+%Y-%m-%d %H:%M:%S')" >> /home/ec2-user/deploy.log
    sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml down

    sudo docker image prune -af

    echo "blue 중단 완료 : $(date '+%Y-%m-%d %H:%M:%S')" >> /home/ec2-user/deploy.log
  fi
fi

echo "배포 종료 : $(date '+%Y-%m-%d %H:%M:%S')" >> /home/ec2-user/deploy.log
echo "===================== 배포 완료 =====================" >> /home/ec2-user/deploy.log
echo "" >> /home/ec2-user/deploy.log
