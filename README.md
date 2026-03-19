# Conversation Practice Village - Backend

대화 연습 마을 프로젝트의 백엔드 API 서버입니다. Spring Boot (Java 21, Gradle) 기반으로 구축되었고, 인증(JWT, Refresh Token), WebSocket(STOMP), Redis(Session)와 Google Gemini AI 연동을 포함한 실서비스 운영을 목표로 개발되었습니다.

## 📋 목차
- [기술 스택](#기술-스택)
- [주요 기능](#주요-기능)
- [프로젝트 구조](#프로젝트-구조)
- [시작하기](#시작하기)
- [환경 설정](#환경-설정)
- [API 엔드포인트](#api-엔드포인트)
- [데이터베이스 스키마](#데이터베이스-스키마)
- [문제 해결](#문제-해결)

## 🛠 기술 스택

- Framework: Spring Boot (Java 21)
- Build Tool: Gradle (프로젝트 내 Gradle Wrapper 사용)
- Database: MySQL 8+
- ORM: Spring Data JPA (Hibernate)
- Cache/Session: Redis (Spring Session)
- Security: Spring Security, JWT (Access/Refresh token)
- Realtime: Spring WebSocket (STOMP) + SockJS
- AI Integration: Google Gemini (Generative Language API)
- 기타: Lombok, Validation, Swagger(OpenAPI)

## ✨ 주요 기능

### 1. 회원 관리 (Member)
- 회원 가입 및 로그인
- 회원 정보 조회 및 수정
- OAuth2 인증 준비 (Google, Kakao 등)

### 2. 페르소나 시스템 (Persona)
- 사용자 정의 대화 상대 생성
- 페르소나별 특성 설정
  - 이름, 나이, 직업
  - MBTI 성격 유형
  - 관계 타입 (친구, 연인, 선배 등)
  - 말투 스타일
  - 성격 키워드
- 친밀도 및 신뢰도 점수 관리

### 3. 대화 관리 (Conversation & Message)
- 페르소나와의 대화방 생성
- 실시간 메시지 송수신
- Google Gemini AI 기반 자동 응답
- 대화 히스토리 저장 및 조회

### 4. 팔로우 시스템 (Follow)
- 다른 사용자 팔로우/언팔로우
- 팔로워/팔로잉 목록 조회

## 📁 프로젝트 구조

```
src/main/java/com/example/conversationpracticevillageback/
├── domain/
│   ├── AI/
│   │   └── AiService.java              # Google Gemini API 연동
│   ├── controller/
│   │   ├── MemberController.java       # 회원 API
│   │   ├── PersonaController.java      # 페르소나 API
│   │   ├── ConversationController.java # 대화방 API
│   │   ├── MessageController.java      # 메시지 API
│   │   └── FollowController.java       # 팔로우 API
│   ├── service/
│   │   ├── MemberService.java
│   │   ├── PersonaService.java
│   │   ├── ConversationService.java
│   │   ├── MessageService.java
│   │   └── FollowService.java
│   ├── entity/
│   │   ├── Member.java
│   │   ├── Persona.java
│   │   ├── Conversation.java
│   │   ├── Message.java
│   │   ├── Follow.java
│   │   └── SenderType.java
│   ├── repository/
│   │   └── (JPA Repositories)
│   └── dto/
│       └── (Data Transfer Objects)
├── global/
│   └── SecurityConfig.java             # Spring Security & CORS 설정
└── ConversationPracticeVillageBackApplication.java
```

## 🚀 시작하기

### 사전 요구사항

- Java 21 이상
- MySQL 8+
- Redis (세션 또는 캐시 용도)
- Gradle Wrapper (프로젝트 포함)

### 빠른 실행 (개발)

1) 프로젝트 루트로 이동
```bash
cd conversation-practice-village-back
```

2) 환경 변수(예시)
 - 로컬 개발용: `.env` 또는 셸에서 export
```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/conversation_practice
export SPRING_DATASOURCE_USERNAME=root
export SPRING_DATASOURCE_PASSWORD=your_password
export JWT_SECRET=your_jwt_secret
export REFRESH_TOKEN_SECRET=your_refresh_token_secret
export GEMINI_API_KEY=your_gemini_key
export REDIS_URL=redis://localhost:6379
```

3) 빌드 및 실행
```bash
# Unix / macOS
./gradlew clean build
./gradlew bootRun

# 또는 jar 실행
./gradlew bootJar
java -jar build/libs/conversation-practice-village-back-0.0.1-SNAPSHOT.jar
```

서버가 실행되면 기본 포트는 `8080`입니다.

### 프로덕션 배포(요약)
- EC2에 JAR를 배포하거나 Docker 이미지를 만들어 컨테이너로 배포할 수 있습니다.
- Docker를 쓰려면 프로젝트 루트에 `Dockerfile`이 필요합니다(현재 프로젝트에 Dockerfile이 포함되어 있을 수 있음). 배포 시 포트, 환경변수, 로그 및 시스템d/컨테이너 오케스트레이션을 설정하세요.

예: Docker 이미지 빌드/실행
```bash
# 프로젝트 루트에서
docker build -t cpv-back:latest .
docker run -d --name cpv-back -p 8080:8080 \
  -e SPRING_DATASOURCE_URL='jdbc:mysql://db:3306/conversation_practice' \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=pass \
  -e GEMINI_API_KEY=your_key \
  cpv-back:latest
```

## ⚙️ 환경 설정

### application.properties (참고)

환경변수로 치환하여 사용합니다. 예시 키:

```properties
# Database
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}

# JPA
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update

# AI (예시)
google.gemini.api-key=${GEMINI_API_KEY}
# endpoint는 SDK/버전에 따라 다르므로 로그(404 메시지)를 참고하여 맞춰주세요.
```

### CORS 설정

현재 다음 Origin에서의 요청을 허용합니다:
- `http://localhost:3000` (로컬 개발)
- `http://13.125.244.156.nip.io:3000` (배포 환경)

추가 Origin이 필요한 경우 `SecurityConfig.java`를 수정하세요.

## 📡 API 엔드포인트

### 회원 (Member)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/api/members` | 회원 가입 |
| GET    | `/api/members/{id}` | 회원 정보 조회 |
| PUT    | `/api/members/{id}` | 회원 정보 수정 |
| DELETE | `/api/members/{id}` | 회원 탈퇴 |

### 페르소나 (Persona)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/api/personas` | 페르소나 생성 |
| GET    | `/api/personas/{id}` | 페르소나 조회 |
| GET    | `/api/personas/member/{memberId}` | 특정 회원의 모든 페르소나 조회 |
| PUT    | `/api/personas/{id}` | 페르소나 수정 |
| DELETE | `/api/personas/{id}` | 페르소나 삭제 |

### 대화 (Conversation)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/api/conversations` | 새 대화방 생성 |
| GET    | `/api/conversations/{id}` | 대화방 조회 |
| GET    | `/api/conversations/persona/{personaId}` | 페르소나의 모든 대화 조회 |

### 메시지 (Message)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/api/messages/{conversationId}` | 메시지 전송 (AI 자동 응답 포함) |
| GET    | `/api/messages/{conversationId}` | 대화방의 모든 메시지 조회 |

**메시지 전송 예시:**
```json
// Request
POST /api/messages/1
{
  "message": "안녕, 오늘 날씨 어때?"
}

// Response
{
  "userMessage": {
    "id": 1,
    "conversationId": 1,
    "senderType": "USER",
    "content": "안녕, 오늘 날씨 어때?",
    "createdAt": "2026-03-02T10:30:00"
  },
  "aiMessage": {
    "id": 2,
    "conversationId": 1,
    "senderType": "AI",
    "content": "안녕! 오늘 날씨 정말 좋더라~ 햇살도 따뜻하고 산책하기 딱이야!",
    "createdAt": "2026-03-02T10:30:02"
  }
}
```

### 팔로우 (Follow)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/api/follows` | 팔로우 |
| DELETE | `/api/follows/{followerId}/{followingId}` | 언팔로우 |
| GET    | `/api/follows/followers/{memberId}` | 팔로워 목록 |
| GET    | `/api/follows/following/{memberId}` | 팔로잉 목록 |

## 🗄️ 데이터베이스 스키마

### ERD 주요 테이블

```
Member (회원)
├── id (PK)
├── email (UNIQUE)
├── nickname
├── password
└── created_at

Persona (페르소나)
├── id (PK)
├── member_id (FK → Member)
├── name
├── age
├── job
├── mbti
├── relationship_type
├── personality_keywords
├── speech_style
├── intimacy_score
├── trust_score
└── created_at

Conversation (대화방)
├── id (PK)
├── persona_id (FK → Persona)
└── created_at

Message (메시지)
├── id (PK)
## 🔧 운영/개발 중 자주 발생하는 문제와 체크리스트

1) 401 Unauthorized (토큰 문제)
 - 브라우저에서 요청 시 쿠키(Access/Refresh)를 제대로 보내는지 확인하세요. 프론트에서 fetch 시 credentials: 'include'가 필요합니다.
 - 서버 로그에서 "토큰이 없거나 형식이 틀립니다." 메시지가 보이면 Authorization 헤더 또는 쿠키가 누락된 것입니다.

2) 404 / Gemini 모델 관련 오류
 - Google API 버전(v1/v1beta)에 따라 모델 사용 가능 여부가 달라집니다. 로그의 404 메시지를 보고 endpoint 형식을 확인하세요.
 - 일시적으로 모델 호출이 실패하면 예외를 던져 트랜잭션을 rollback 하도록 처리하세요. (현재 AiService에서 예외 로깅/재던짐 여부 확인 권장)

3) 데이터가 DB에 빈값으로 들어가는 문제
 - AI 호출 실패 시 RuntimeException으로 예외를 던져 @Transactional 메서드가 rollback되도록 하세요. 단순 로깅 후 무시하면 DB에 불완전한 레코드가 남을 수 있습니다.

4) CORS / WebSocket 연결 문제
 - `SecurityConfig`의 allowedOrigins와 WebSocket AllowedOrigins가 일치해야 합니다.
 - 배포 도메인(퍼블릭 IP / nip.io 등)을 정확히 설정하세요.

### 디버깅 팁
- 로그 레벨을 DEBUG로 올리고 예외 스택 트레이스를 함께 출력하도록 `AiService`의 catch 문을 수정하세요.
- 로컬에서 curl로 API를 직접 호출해 응답 확인 (토큰/헤더 포함).

## Architecture (간단 요약)

컴포넌트:
- Frontend (Next.js) — HTTP / WebSocket 연결, 클라이언트 쿠키에 Refresh Token 저장(권장: HttpOnly cookie), Access Token은 메모리/쿠키로 사용
- Backend (Spring Boot) — REST API, JWT 인증(Access/Refresh), WebSocket(STOMP), AI 연동(AiService), DB(MySQL), Cache/Session(Redis)
- External — Google Gemini API

주요 시퀀스(요약):
1. 사용자 로그인 → Backend에서 인증 후 Access/Refresh 토큰 발급(Refresh는 HttpOnly 쿠키로 설정)
2. 클라이언트는 Access Token을 Authorization 헤더로 전송하거나, Cookie 기반 인증을 사용할 때는 credentials: 'include'로 요청
3. 대화 생성/메시지 전송 시 Backend는 Conversation/Message를 저장하고 AiService를 비동기 또는 동기 호출하여 AI 응답 생성
4. AI 호출 실패 시 트랜잭션을 적절히 rollback하도록 처리하고, 실패 메시지를 사용자에게 전달

네트워크 포트 요약:
- Frontend 개발: 3000
- Backend 개발: 8080
- WebSocket: /ws (8080에서 서빙)
- DB: MySQL 3306
- Redis: 6379

---

더 구체적인 배포/디버깅 스텝이나 `AiService` 코드 수정(예외 로깅/재던짐) 등 원하시면 해당 파일을 열어 직접 수정해 드리겠습니다.
**증상:** `Cannot create PoolableConnectionFactory`

**해결 방법:**
```bash
# MySQL 서비스 확인
mysql.server status

# MySQL 시작
mysql.server start

# 데이터베이스 및 권한 확인
mysql -u root -p
SHOW DATABASES;
SHOW GRANTS FOR 'cpv_user'@'localhost';
```

### 4. Port 8080 이미 사용 중

**해결 방법:**
```bash
# 8080 포트 사용 프로세스 확인
lsof -i :8080

# 프로세스 종료
kill -9 <PID>

# 또는 다른 포트 사용 (application.properties)
server.port=8081
```

### 5. 빌드 실패

**해결 방법:**
```bash
# Gradle 캐시 정리
./gradlew clean

# 의존성 다시 다운로드
./gradlew build --refresh-dependencies

# Gradle Wrapper 업데이트
./gradlew wrapper --gradle-version=8.5
```

## 📝 개발 팁

### 로깅 레벨 설정
```properties
# application.properties
logging.level.com.example.conversationpracticevillageback=DEBUG
logging.level.org.springframework.web=DEBUG
```

### H2 In-Memory DB로 테스트
빠른 로컬 테스트를 위해 H2 사용:
```properties
# application-test.properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop
```

### API 테스트
```bash
# 회원 가입
curl -X POST http://localhost:8080/api/members \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","nickname":"테스터","password":"pass1234"}'

# 페르소나 생성
curl -X POST http://localhost:8080/api/personas \
  -H "Content-Type: application/json" \
  -d '{
    "memberId": 1,
    "name": "지민",
    "age": 25,
    "job": "디자이너",
    "mbti": "ENFP",
    "relationshipType": "친구",
    "speechStyle": "밝고 활발한 친구 말투"
  }'

# 메시지 전송
curl -X POST http://localhost:8080/api/messages/1 \
  -H "Content-Type: application/json" \
  -d '{"message":"안녕?"}'
```

## 📚 참고 자료

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Google Gemini API](https://ai.google.dev/docs)
- [Spring Security](https://spring.io/projects/spring-security)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)

## 📧 문의

프로젝트 관련 문의사항이 있으시면 이슈를 등록해주세요.

---

**Last Updated:** 2026-03-02

