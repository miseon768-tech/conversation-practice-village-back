#!/bin/bash

# 환경변수 설정 스크립트
# 사용법: ./run-dev.sh

echo "🚀 Conversation Practice Village Backend 시작..."

# .env 파일이 있으면 로드
if [ -f .env ]; then
    echo "📝 .env 파일 로드 중..."
    source .env
else
    echo "⚠️  .env 파일이 없습니다. .env.example을 참고하여 .env 파일을 생성해주세요."
    exit 1
fi

# 환경변수 확인
if [ -z "$GEMINI_API_KEY" ]; then
    echo "❌ GEMINI_API_KEY가 설정되지 않았습니다."
    echo "   https://aistudio.google.com/app/apikey 에서 API 키를 발급받아 .env 파일에 추가해주세요."
    exit 1
fi

if [ -z "$DB_PASSWORD" ]; then
    echo "❌ DB_PASSWORD가 설정되지 않았습니다."
    exit 1
fi

echo "✅ 환경변수 로드 완료"
echo "🔧 애플리케이션 빌드 중..."

./gradlew clean build -x test

if [ $? -eq 0 ]; then
    echo "✅ 빌드 완료"
    echo "🚀 서버 시작..."
    java -jar build/libs/conversation-practice-village-back-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
else
    echo "❌ 빌드 실패"
    exit 1
fi

