# Minidooray Team 8 Front

---

## 1. Account API (계정 관리)

### 1.1 회원가입
- **Endpoint**: `POST /accounts/signup`
- **Request (SignupRequest)**:
  ```json
  {
    "id": "user123",
    "email": "user@example.com",
    "password": "password123"
  }
  ```
- **Response (SignupResponse)**:
  ```json
  {
    "id": "user123",
    "status": "ACTIVE"
  }
  ```

### 1.2 로그인
- **Endpoint**: `POST /accounts/login`
- **Request (LoginRequest)**:
  ```json
  {
    "userId": "user123",
    "password": "password123"
  }
  ```
- **Response (LoginResponse)**:
  ```json
  {
    "userId": "user123"
  }
  ```
  *(성공 시 Redis 세션에 `USER_ID`가 저장됩니다.)*

### 1.3 로그아웃
- **Endpoint**: `POST /logout`
- **Description**: 현재 세션을 무효화(invalidate)합니다.

### 1.4 사용자 상태 변경
- **Endpoint**: `PUT /accounts/users/{user-id}/status`
- **Request (UserStatusUpdateRequest)**:
  ```json
  {
    "status": "DORMANT"
  }
  ```

### 1.5 사용자 정보 조회
- **Endpoint**: `GET /accounts/users/{user-id}`
- **Response (UserDto)**:
  ```json
  {
    "userId": "user123",
    "email": "user@example.com",
    "status": "ACTIVE"
  }
  ```

---

## 2. Project API (프로젝트 관리)

### 2.1 프로젝트 목록 조회
- **Endpoint**: `GET /projects`
- **Response**: `List<ProjectDto>`
  ```json
  [
    {
      "projectId": 1,
      "name": "Project A",
      "status": "ACTIVE"
    }
  ]
  ```

### 2.2 프로젝트 상세 조회
- **Endpoint**: `GET /projects/{project-id}`
- **Response (ProjectDetailDto)**:
  ```json
  {
    "projectId": 1,
    "name": "Project A",
    "status": "ACTIVE",
    "adminId": "admin123",
    "members": [ { "userId": "user123" } ],
    "tasks": [
      {
        "taskId": 1,
        "milestoneId": 1,
        "title": "Task 1",
        "content": "Content...",
        "writerId": "user123",
        "createdAt": "2023-10-27T10:00:00",
        "tags": [ { "tagId": 1, "name": "Bug" } ]
      }
    ],
    "milestones": [
      {
        "milestoneId": 1,
        "name": "Sprint 1",
        "startDate": "2023-10-01",
        "endDate": "2023-10-15"
      }
    ]
  }
  ```

### 2.3 프로젝트 생성
- **Endpoint**: `POST /projects`
- **Request (ProjectCreateRequest)**:
  ```json
  {
    "name": "New Project"
  }
  ```
- **Response (ProjectDto)**: 생성된 프로젝트 정보 반환

### 2.4 프로젝트 수정
- **Endpoint**: `PUT /projects/{project-id}/edit`
- **Request (ProjectUpdateRequest)**:
  ```json
  {
    "name": "Updated Name",
    "status": "DORMANT"
  }
  ```

### 2.5 프로젝트 종료
- **Endpoint**: `PUT /projects/{project-id}/close`
- **Description**: 프로젝트 상태를 `TERMINATED`로 강제 업데이트합니다.

### 2.6 프로젝트 멤버 추가
- **Endpoint**: `POST /projects/{project-id}/members`
- **Request (ProjectMemberRequest)**:
  ```json
  {
    "userId": "newuser123"
  }
  ```

### 2.7 프로젝트 멤버 삭제 (탈퇴/강퇴)
- **Endpoint**: `DELETE /projects/{project-id}/members/{user-id}`
- **Description**: 프로젝트에서 특정 멤버를 삭제하거나 본인이 탈퇴합니다.

---

## 3. Task API (업무 관리)

### 3.1 업무 상세 조회
- **Endpoint**: `GET /projects/{project-id}/tasks/{task-id}`
- **Response (TaskDetailDto)**:
  ```json
  {
    "taskId": 1,
    "title": "Task 1",
    "content": "Detailed Content",
    "writerId": "user123",
    "createdAt": "2023-10-27T10:00:00",
    "milestone": {
      "milestoneId": 1,
      "name": "Sprint 1",
      "startDate": "2023-10-01",
      "endDate": "2023-10-15"
    },
    "tags": [
      {
        "tagId": 1,
        "name": "Backend"
      }
    ],
    "comments": [
      {
        "commentId": 1,
        "writerId": "user123",
        "content": "First Comment",
        "createdAt": "2023-10-27T11:00:00"
      }
    ]
  }
  ```

### 3.2 업무 생성
- **Endpoint**: `POST /projects/{project-id}/tasks`
- **Request Body (TaskCreateRequest)**:
  ```json
  {
    "projectId": 1,
    "title": "New Task",
    "content": "Task Content",
    "writerId": "user123"
  }
  ```
- **Optional Request Parameters**:
  - `milestoneId` (Long): 기존 마일스톤 ID 할당
  - `newMilestoneName` (String): 새 마일스톤 생성 및 할당
  - `newMilestoneStartDate` (LocalDate): 새 마일스톤 시작일
  - `newMilestoneEndDate` (LocalDate): 새 마일스톤 종료일
  - `tagIds` (List<Long>): 기존 태그 ID 목록 할당
  - `newTagName` (String): 새 태그 생성 및 추가 할당
- **Response (TaskDto)**: 생성된 업무 정보 반환
- **Description**: 새로운 업무를 생성합니다. 파라미터를 통해 마일스톤이나 태그를 즉석에서 생성하여 할당할 수 있습니다.

### 3.3 업무 수정
- **Endpoint**: `PUT /projects/{project-id}/tasks/{task-id}`
- **Request (TaskUpdateRequest)**:
  ```json
  {
    "title": "Updated Title",
    "content": "Updated Content"
  }
  ```

### 3.4 업무 삭제
- **Endpoint**: `DELETE /projects/{project-id}/tasks/{task-id}`

### 3.5 업무 마일스톤 설정
- **Endpoint**: `POST /projects/{project-id}/tasks/{task-id}/milestones`
- **Optional Request Parameters**:
  - `milestoneId` (Long): 기존 마일스톤 ID
  - `newMilestoneName` (String): 새 마일스톤 이름 (입력 시 신규 생성 및 할당)
  - `newMilestoneStartDate` (LocalDate): 새 마일스톤 시작일
  - `newMilestoneEndDate` (LocalDate): 새 마일스톤 종료일

### 3.6 업무 태그 설정
- **Endpoint**: `POST /projects/{project-id}/tasks/{task-id}/tags`
- **Optional Request Parameters**:
  - `tagIds` (List<Long>): 기존 태그 ID 목록
  - `newTagName` (String): 새 태그 이름 (입력 시 신규 생성 및 추가 할당)

### 3.7 업무 목록 조회 및 필터링 (AJAX)
- **Endpoint**: `GET /projects/{project-id}/tasks`
- **Request Parameters**:
  - `projectId` (Path Variable, Long): 프로젝트 식별자 (필수)
  - `tagId` (Query Parameter, Long): 필터링할 태그 식별자 (선택)
- **Response (List<TaskDto>)**:
  ```json
  [
    {
      "taskId": 1,
      "milestoneId": 10,
      "title": "업무 제목",
      "content": "업무 상세 내용",
      "writerId": "user123",
      "createdAt": "2023-10-27T10:00:00",
      "tags": [
        {
          "tagId": 5,
          "name": "Backend"
        }
      ]
    }
  ]
  ```
- **Description**: 프로젝트에 속한 업무 목록을 JSON으로 반환합니다. `tagId`가 제공되면 해당 태그가 포함된 업무만 필터링하여 반환하고, 파라미터가 없으면 프로젝트의 전체 업무 목록을 반환합니다.

---

## 4. Milestone API (마일스톤 관리)

### 4.1 마일스톤 상세 조회
- **Endpoint**: `GET /projects/{project-id}/milestones/{milestone-id}`
- **Response (MilestoneDetailDto)**:
  ```json
  {
    "milestoneId": 1,
    "name": "Sprint 1",
    "startDate": "2023-10-01",
    "endDate": "2023-10-15",
    "tasks": [
      {
        "taskId": 1,
        "title": "Task Title",
        "content": "Task Content",
        "writerId": "user123",
        "createdAt": "2023-10-27T10:00:00"
      }
    ]
  }
  ```

### 4.2 마일스톤 생성
- **Endpoint**: `POST /projects/{project-id}/milestones`
- **Request (MilestoneCreateRequest)**:
  ```json
  {
    "name": "Sprint 1",
    "startDate": "2023-10-01",
    "endDate": "2023-10-15"
  }
  ```
- **Response (MilestoneDto)**: 생성된 마일스톤 정보 반환

### 4.3 마일스톤 수정
- **Endpoint**: `PUT /projects/{project-id}/milestones/{milestone-id}`
- **Request (MilestoneCreateRequest)**:
  ```json
  {
    "name": "Updated Sprint Name",
    "startDate": "2023-10-02",
    "endDate": "2023-10-16"
  }
  ```

### 4.4 마일스톤 삭제
- **Endpoint**: `DELETE /projects/{project-id}/milestones/{milestone-id}`
- **Description**: 해당 마일스톤을 삭제합니다.

---

## 5. Tag API (태그 관리)

### 5.1 태그 목록 조회
- **Endpoint**: `GET /projects/{project-id}/tags`
- **Response**: `List<TagDto>`
  ```json
  [
    {
      "tagId": 1,
      "name": "Backend"
    },
    {
      "tagId": 2,
      "name": "UI/UX"
    }
  ]
  ```

### 5.2 태그 생성
- **Endpoint**: `POST /projects/{project-id}/tags`
- **Request (TagCreateRequest)**:
  ```json
  {
    "name": "New Tag"
  }
  ```
- **Response (TagDto)**: 생성된 태그 정보 반환

### 5.3 태그 수정
- **Endpoint**: `PUT /projects/{project-id}/tags/{tag-id}`
- **Request (TagCreateRequest)**:
  ```json
  {
    "name": "Updated Tag Name"
  }
  ```

### 5.4 태그 삭제
- **Endpoint**: `DELETE /projects/{project-id}/tags/{tag-id}`
- **Description**: 해당 태그를 삭제합니다.

---

## 6. Comment API (댓글 관리)

### 6.1 댓글 생성
- **Endpoint**: `POST /projects/{project-id}/tasks/{task-id}/comments`
- **Request (CommentCreateRequest)**:
  ```json
  {
    "content": "Comment Content"
  }
  ```

### 6.2 댓글 수정
- **Endpoint**: `PUT /projects/{project-id}/tasks/{task-id}/comments/{comment-id}/edit`
- **Request (CommentCreateRequest)**:
  ```json
  {
    "content": "Updated Comment Content"
  }
  ```

### 6.3 댓글 삭제
- **Endpoint**: `DELETE /projects/{project-id}/tasks/{task-id}/comments/{comment-id}`

---

## 7. Front-End Specification (프론트엔드 명세)

Minidooray 프론트엔드는 사용자 중심의 직관적인 인터페이스와 효율적인 워크플로우를 제공하기 위해 현대적인 웹 기술로 구축되었습니다.

### 7.1 기술 스택 (Tech Stack)
- **언어 및 프레임워크**: Java 21, Spring Boot 3.4.1, Spring MVC
- **보안**: Spring Security (Custom Authentication Provider, Redis Session 연동)
- **템플릿 엔진**: Thymeleaf (Server-side Rendering)
- **UI 라이브러리**: Bootstrap 5.3 (Responsive Design, Modals, Offcanvas)
- **폰트**: Plus Jakarta Sans (Modern Typography)
- **데이터 통신**: RestTemplate (Backend API 연동)

### 7.2 주요 화면 및 기능 (Key UI Features)

#### 1. 랜딩 및 인증 (Landing & Auth)
- **Main (`main.html`)**: 서비스의 핵심 기능을 소개하는 현대적인 그라데이션 히어로 섹션과 애니메이션 효과.
- **Login/Signup (`login.html`, `signup.html`)**:
    - 입체감 있는 카드 UI와 깔끔한 폼 디자인.
    - **강력한 유효성 검사**: 아이디와 비밀번호에 대해 영문/숫자 혼합 8자 이상의 규칙을 HTML5와 백엔드 DTO 양쪽에서 엄격히 검증합니다.
    - **실시간 피드백**: 입력창 하단 도움말과 상단 에러 알림 배너를 통해 사용자에게 명확한 가이드를 제공합니다.

#### 2. 대시보드 (Dashboard)
- **내 프로젝트 (`index.html`)**:
    - 참여 중인 모든 프로젝트를 카드 뷰로 시각화.
    - **상태 필터링**: ALL, ACTIVE, DORMANT, TERMINATED 상태별 프로젝트 필터링 기능.
    - **프로젝트 탈퇴**: 관리자가 아닌 일반 멤버는 카드 메뉴를 통해 자발적으로 프로젝트에서 탈퇴할 수 있습니다.

#### 3. 프로젝트 상세 및 워크스페이스 (Project Detail)
- **마일스톤 사이드바 (Milestone Tracker)**:
    - **GitHub 스타일 UI**: 좌측 고정 사이드바에서 각 마일스톤의 진행 상황을 시간 기반(Time-based) 프로그레스 바로 시각화합니다.
    - 현재 날짜를 기준으로 시작일과 종료일 사이의 경과 시간을 계산하여 자동으로 바의 너비와 색상(진행/완료/지연)을 업데이트합니다.
- **오프캔버스 메뉴 (Project Menu)**:
    - 멤버 관리, 태그 관리 등 부가적인 설정 도구들을 우측에서 슬라이드되는 오프캔버스 메뉴로 분리하여 메인 워크스페이스를 깔끔하게 유지합니다.
- **통합 멤버 관리**: 관리자는 하나의 모달에서 멤버 목록 조회, 신규 초대, 기존 멤버 강퇴(Remove)를 통합적으로 수행할 수 있습니다.
- **태그 필터링**: 사이드바의 태그 배지를 클릭하여 워크스페이스의 업무 목록을 동적으로 필터링합니다.

#### 4. 업무 및 댓글 관리 (Task & Comments)
- **업무 생성/수정**:
    - **원스톱 생성 로직**: 업무 생성과 동시에 **새로운 마일스톤**이나 **새로운 태그**를 즉석에서 만들어 할당할 수 있는 확장된 폼을 제공합니다.
- **댓글 시스템**: 업무 상세 페이지에서 댓글 작성, 수정, 삭제가 가능하며, 본인이 작성한 댓글에 대해서만 편집 권한이 부여됩니다.

### 7.3 고도화된 에러 처리 (Robust Error UX)
- **인-페이지 에러 알림 (In-Page Alert)**:
    - 에러 발생 시 딱딱한 전체 화면 에러 페이지로 이동하는 대신, 사용자가 머물던 원래 페이지로 리다이렉트합니다.
    - Spring의 `FlashMap`을 사용하여 화면 상단에 일시적으로 빨간색 알림 배너(Bootstrap Alert)를 띄워, 작업 흐름을 방해하지 않으면서 문제 발생 원인을 안내합니다.
- **무한 루프 방지**: `Referer` 헤더 분석을 통해 권한 부족 등으로 인한 비정상적인 무한 리디렉션을 자동으로 감지하고 안전하게 대시보드로 복귀시킵니다.
