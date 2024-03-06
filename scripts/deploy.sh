#!/bin/bash

# 작업 디렉토리를 /home/ec2-user/app으로 변경
cd /home/ec2-user/app

# 환경변수 DOCKER_APP_NAME을 spring으로 설정
DOCKER_APP_NAME=sejongpeer

# 배포 시작한 날짜와 시간을 기록
echo "배포 시작일자 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> /home/ec2-user/deploy.log

# 현재 실행중인 컨테이너가 있는지 확인
EXIST_BLUE=$(sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml ps | grep Up)
EXIST_GREEN=$(sudo docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml ps | grep Up)

# 기존에 실행중인 도커 컨테이너를 중지하고 삭제
if [ -n "$EXIST_BLUE" ]; then
  sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml down
fi

if [ -n "$EXIST_GREEN" ]; then
  sudo docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml down
fi

# 사용하지 않는 이미지 삭제
sudo docker image prune -af

# 블루 배포 시작
echo "블루 배포 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> /home/ec2-user/deploy.log
sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml up -d --build

echo "배포 종료  : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> /home/ec2-user/deploy.log
echo "===================== 배포 완료 =====================" >> /home/ec2-user/deploy.log
