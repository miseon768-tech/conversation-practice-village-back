# Conversation Practice Village - Backend

대화 연습 마을 프로젝트의 백엔드 API 서버입니다. Spring Boot 기반으로 구축되었으며, Google Gemini AI를 활용한 페르소나 기반 대화 시스템을 제공합니다.

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

- **Framework**: Spring Boot 3.5.11
- **Language**: Java 21
- **Database**: MySQL 8.0+
- **ORM**: Spring Data JPA (Hibernate)
- **Security**: Spring Security (JWT 인증 준비)
- **AI Integration**: Google Gemini 1.5 Flash API
- **Build Tool**: Gradle 8.x
- **기타**: Lombok, Validation

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
- MySQL 8.0 이상
- Gradle 8.x (프로젝트에 포함된 Gradle Wrapper 사용 가능)
- Google Gemini API Key

### 설치 및 실행

1. **레포지토리 클론**
   ```bash
   cd conversation-practice-village-back
   ```

2. **데이터베이스 설정**
   ```sql
   -- MySQL에 데이터베이스 생성
   CREATE DATABASE conversation_practice CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   
   -- 사용자 생성 및 권한 부여 (선택사항)
   CREATE USER 'cpv_user'@'localhost' IDENTIFIED BY 'your_password';
   GRANT ALL PRIVILEGES ON conversation_practice.* TO 'cpv_user'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. **환경 변수 설정**
   
   `.env` 파일 생성 또는 시스템 환경 변수 설정:
   ```bash
   export GEMINI_API_KEY=your_google_gemini_api_key
   export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/conversation_practice
   export SPRING_DATASOURCE_USERNAME=cpv_user
   export SPRING_DATASOURCE_PASSWORD=your_password
   ```

4. **빌드 및 실행**
   
   **방법 1: Gradle Wrapper 사용 (권장)**
   ```bash
   # Unix/macOS
   ./gradlew clean build
   ./gradlew bootRun
   
   # Windows
   gradlew.bat clean build
   gradlew.bat bootRun
   ```
   
   **방법 2: JAR 파일 실행**
   ```bash
   ./gradlew clean bootJar
   java -jar build/libs/conversation-practice-village-back-0.0.1-SNAPSHOT.jar
   ```

5. **서버 확인**
   
   서버가 정상적으로 시작되면 `http://localhost:8080`에서 접근 가능합니다.

## ⚙️ 환경 설정

### application.properties

```properties
# Application Name
spring.application.name=conversation-practice-village-back

# Database Configuration
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=true

# Google Gemini API
google.gemini.api-key=${GEMINI_API_KEY}
google.gemini.url=https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent
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
├── conversation_id (FK → Conversation)
├── sender_type (USER | AI)
├── content
└── created_at

Follow (팔로우)
├── id (PK)
├── follower_id (FK → Member)
├── following_id (FK → Member)
└── created_at
```

스키마 파일: `src/main/resources/schema.sql`

## 🔧 문제 해결

### 1. CORS 에러 발생

**증상:**
```
Access to fetch at 'http://localhost:8080/api/messages/persona/1' from origin 'http://localhost:3000' 
has been blocked by CORS policy: No 'Access-Control-Allow-Origin' header is present
```

**해결 방법:**

#### ✅ 백엔드 서버가 실행 중인지 확인
```bash
# 8080 포트 사용 확인
lsof -i :8080

# 서버 재시작
./gradlew bootRun
```

#### ✅ SecurityConfig의 CORS 설정 확인
`SecurityConfig.java`에서 프론트엔드 Origin이 허용되어 있는지 확인:
```java
config.setAllowedOrigins(List.of(
    "http://localhost:3000",  // 로컬 개발
    "http://13.125.244.156.nip.io:3000"  // 배포 환경
));
```

#### ✅ Controller의 @CrossOrigin 어노테이션 확인
각 컨트롤러에 `@CrossOrigin` 어노테이션이 있는지 확인

### 2. Gemini API 에러

**증상:** AI 응답이 돌아오지 않거나 500 에러 발생

**해결 방법:**
- Gemini API 키가 올바르게 설정되었는지 확인
  ```bash
  echo $GEMINI_API_KEY
  ```
- API 키 발급: [Google AI Studio](https://makersuite.google.com/app/apikey)
- API 요청 한도 확인 (무료 티어: 분당 60회)

### 3. 데이터베이스 연결 실패

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

