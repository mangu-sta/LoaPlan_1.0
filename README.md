# 🎮 LoaPlan

<div align="center">

### 로스트아크 유저를 위한 캐릭터 일정 관리 + 정보 통합 웹 서비스

여러 캐릭터의 일정을 한눈에 관리하고,  
게임 정보 조회와 커뮤니티 기능을 함께 사용할 수 있도록 구성한 개인 프로젝트입니다.

</div>

---

## 프로젝트 정보

| 항목 | 내용 |
|---|---|
| 프로젝트명 | LoaPlan |
| 프로젝트 유형 | 개인 프로젝트 |
| 한 줄 설명 | 로스트아크 유저를 위한 캐릭터 일정 관리 + 정보 통합 웹 서비스 |
| 목적 | 로스트아크 여러 캐릭터의 일정을 원하는 커스텀으로 관리하기 위해 제작 |
| 배포 여부 | X |

---

## 목차

- [프로젝트 소개](#-프로젝트-소개)
- [주요 기능](#-주요-기능)
- [시스템 아키텍처](#-시스템-아키텍처)
- [기술 스택](#-기술-스택)
- [프로젝트 구조](#-프로젝트-구조)
- [API](#-api)
- [개발자](#-개발자)

---

## 📝 프로젝트 소개

LoaPlan은 로스트아크 플레이 시 여러 캐릭터의 일정을 한눈에 관리할 수 있도록 만든 서비스입니다.

기존에는 캐릭터 일정, 이벤트, 레이드, 커뮤니티 확인이 여러 곳에 흩어져 있어 불편함이 있었고, 이를 줄이기 위해 일정 관리, 정보 조회, 커뮤니티 기능을 하나의 서비스로 통합했습니다.

여러 캐릭터를 운영하는 플레이어가 필요한 정보를 한 곳에서 확인하고, 원하는 방식으로 일정을 관리할 수 있도록 구성한 프로젝트입니다.

---

## ✨ 주요 기능

### 1. 캐릭터 스케줄러
- 캐릭터 등록, 삭제, 새로고침
- 캐릭터 순서 변경
- 캐릭터별 일정 관리

### 2. 일정 관리
- 일일 / 주간 / 레이드 / 이벤트 / 커스텀 일정 추가
- 진행도 체크
- 일정 삭제

### 3. 홈 대시보드
- 오늘의 모험섬 조회
- 필드보스 타이머
- 카오스게이트 타이머

### 4. 커뮤니티 게시판
- 게시글 작성
- 이미지 업로드
- 댓글 작성 및 조회

### 5. 캐릭터 검색
- 전투정보 조회
- 인벤 사고글 여부 확인

### 6. 사고글 검색
- 인벤 크롤링 기반 검색
- 무한 스크롤 결과 조회

### 7. 레이드 정보
- 관문 정보 조회
- 보상 정보 조회
- 난이도별 정보 확인

### 8. 회원 기능
- 회원가입
- 이메일 인증
- 로그인
- Google OAuth 로그인
- 세션 확인 및 로그아웃

### 9. 부가 기능
- 경매 계산기
- 프로필 이미지 조회
- 다크모드

---

## 🏗 시스템 아키텍처

서비스는 React 기반 프론트엔드와 Spring Boot 백엔드로 구성되며,  
MariaDB와 외부 API를 연동하는 구조로 설계했습니다.

![시스템 아키텍처](./AT.png)

---

## 🛠 기술 스택

### Frontend

<p>
  <img src="https://img.shields.io/badge/React-19-61DAFB?style=for-the-badge&logo=react&logoColor=black" alt="React 19" />
  <img src="https://img.shields.io/badge/React_Router_DOM-7-CA4245?style=for-the-badge&logo=reactrouter&logoColor=white" alt="React Router DOM 7" />
  <img src="https://img.shields.io/badge/Vite-7-646CFF?style=for-the-badge&logo=vite&logoColor=white" alt="Vite 7" />
  <img src="https://img.shields.io/badge/CSS-Stylesheet-1572B6?style=for-the-badge&logo=css3&logoColor=white" alt="CSS" />
  <img src="https://img.shields.io/badge/Fetch_API-HTTP_Client-000000?style=for-the-badge" alt="Fetch API" />
</p>

### Backend

<p>
  <img src="https://img.shields.io/badge/Java-17-007396?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 17" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot 3.5" />
  <img src="https://img.shields.io/badge/Spring_Web-Web-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Web" />
  <img src="https://img.shields.io/badge/WebFlux-Reactive-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="WebFlux" />
  <img src="https://img.shields.io/badge/Spring_Security-Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white" alt="Spring Security" />
  <img src="https://img.shields.io/badge/Spring_Data_JPA-JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Data JPA" />
  <img src="https://img.shields.io/badge/QueryDSL-Query-0769AD?style=for-the-badge" alt="QueryDSL" />
  <img src="https://img.shields.io/badge/OAuth2_Client-Login-4285F4?style=for-the-badge&logo=google&logoColor=white" alt="OAuth2 Client" />
  <img src="https://img.shields.io/badge/Validation-Bean_Validation-FF6F00?style=for-the-badge" alt="Validation" />
  <img src="https://img.shields.io/badge/Mail-SMTP-0A66C2?style=for-the-badge" alt="Mail" />
  <img src="https://img.shields.io/badge/Jsoup-HTML_Parser-4E9A06?style=for-the-badge" alt="Jsoup" />
  <img src="https://img.shields.io/badge/Thymeleaf-Template-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white" alt="Thymeleaf" />
  <img src="https://img.shields.io/badge/Lombok-Boilerplate_Reduction-BC4521?style=for-the-badge" alt="Lombok" />
</p>

### Database / Infra

<p>
  <img src="https://img.shields.io/badge/MariaDB-Database-003545?style=for-the-badge&logo=mariadb&logoColor=white" alt="MariaDB" />
  <img src="https://img.shields.io/badge/H2-Database-0F4C81?style=for-the-badge" alt="H2" />
  <img src="https://img.shields.io/badge/Local_File_Upload-Storage-555555?style=for-the-badge" alt="Local File Upload" />
</p>

### External

<p>
  <img src="https://img.shields.io/badge/Lost_Ark_Open_API-External_API-1E88E5?style=for-the-badge" alt="Lost Ark Open API" />
  <img src="https://img.shields.io/badge/Google_OAuth-Authentication-4285F4?style=for-the-badge&logo=google&logoColor=white" alt="Google OAuth" />
  <img src="https://img.shields.io/badge/Inven_Crawling-Search-8E24AA?style=for-the-badge" alt="Inven Crawling" />
</p>

---

## 📂 프로젝트 구조

```text
LoaPlan_1.0
├─ frontend
│  └─ src
│     ├─ pages        # 화면 단위 페이지
│     ├─ components   # 공통 UI 컴포넌트 및 모달
│     ├─ utils        # API 호스트 설정 등 유틸리티
│     ├─ styles       # 페이지/컴포넌트 스타일
│     └─ assets       # 이미지 및 아이콘 리소스
│
└─ LoaPlan
   └─ src
      └─ main
         ├─ java/com/example/loaplan
         │  ├─ domain
         │  │  ├─ board           # 게시판, 댓글, 첨부파일
         │  │  ├─ character       # 캐릭터 등록 및 조회
         │  │  ├─ inven           # 인벤 검색 크롤링
         │  │  ├─ island          # 모험섬 정보
         │  │  ├─ raidInfo        # 레이드 목록/보상 정보
         │  │  ├─ raidDetail      # 레이드 상세 정보
         │  │  ├─ raidDetailItem  # 레이드 상세 구성 요소
         │  │  ├─ task            # 일정 마스터 데이터
         │  │  ├─ taskProgress    # 캐릭터별 일정 진행도
         │  │  └─ user            # 사용자 도메인
         │  │
         │  └─ global
         │     ├─ config              # 보안, 웹, 메일 설정
         │     ├─ login               # 로그인, 이메일 인증, OAuth 처리
         │     ├─ springSecurity      # 사용자 인증/인가 처리
         │     ├─ api                 # 외부 API 연동
         │     └─ redirectController  # 프론트엔드 리다이렉트
         │
         └─ resources
            └─ application.properties # 환경 설정
```

---

## 🔌 API

### 인증/세션
- `POST /api/user/login`
- `POST /api/user/join`
- `GET /api/session`
- `POST /api/session/logout`
- `GET /api/session/debug`
- `GET /oauth2/authorization/google`

### 이메일 인증
- `POST /api/user/email/send`
- `POST /api/user/email/verify`

### 회원/프로필
- `GET /api/user/check-email`
- `GET /api/user/check-nickname`
- `GET /api/user/profile`
- `GET /api/user/me`

### 캐릭터
- `GET /api/characters`
- `POST /api/characters`
- `DELETE /api/characters/{characterId}`
- `POST /api/characters/refresh`
- `POST /api/characters/reorder`
- `GET /api/armory/search`

### 일정/스케줄
- `GET /api/task/list`
- `GET /api/schedule/list`
- `POST /api/schedule/add/official`
- `POST /api/schedule/add/custom`
- `POST /api/schedule/update`
- `DELETE /api/schedule/delete`

### 게시판/댓글
- `GET /api/board/list`
- `POST /api/board/create`
- `GET /api/board/detail`
- `GET /api/board/images`
- `GET /api/comment/list`
- `POST /api/comment`

### 게임/콘텐츠 정보
- `GET /api/islands/today`
- `GET /api/raid/list`
- `GET /api/raid/detail`

### 크롤링/검색
- `GET /api/inven/search`

---

## 👨‍💻 개발자

| 이름 | 역할 |
|---|---|
| 망구 | Full Stack / 개인 프로젝트 |
