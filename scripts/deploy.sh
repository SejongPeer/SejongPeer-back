#!/bin/bash

cd /home/ec2-user/app

DOCKER_APP_NAME=sejongpeer

EXIST_BLUE=$(sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml ps | grep Up)
EXIST_GREEN=$(sudo docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml ps | grep Up)

echo "배포 시작일자 : $(date '+%Y-%m-%d %H:%M:%S')" >> /home/ec2-user/deploy.log

if [ -z "$EXIST_BLUE" ]; then
  echo "blue 배포 시작 : $(date '+%Y-%m-%d %H:%M:%S')" >> /home/ec2-user/deploy.log

  sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml up -d --build

  sleep 30

  BLUE_HEALTH=$(sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml ps | grep Up)

  if [ -z "$BLUE_HEALTH" ]; then
    echo "blue 배포 실패" >> /home/ec2-user/deploy.log
  else
    echo "green 중단 시작 : $(date '+%Y-%m-%d %H:%M:%S')" >> /home/ec2-user/deploy.log
    sudo docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml down

    sudo docker image prune -af

    echo "green 중단 완료 : $(date '+%Y-%m-%d %H:%M:%S')" >> /home/ec2-user/deploy.log
  fi

else
  echo "green 배포 시작 : $(date '+%Y-%m-%d %H:%M:%S')" >> /home/ec2-user/deploy.log

  sudo docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml up -d --build

  sleep 30

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
