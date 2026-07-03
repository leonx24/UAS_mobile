# WorkSync REST API Specifications

All endpoints communicate via JSON format. Bearer Token (JWT) must be passed in the `Authorization` header for authenticated endpoints.

- **Base URL**: `https://api.worksync.com/api`
- **Default Headers**:
  ```http
  Content-Type: application/json
  Accept: application/json
  ```

---

## 1. Authentication Endpoints

### POST `/auth/login`
Authenticate a user and retrieve a JWT token.
- **Auth required**: No
- **Request Body**:
  ```json
  {
    "email": "user@worksync.com",
    "password": "password123"
  }
  ```
- **Response (200 OK)**:
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "name": "Leon",
      "email": "leon@worksync.com",
      "role": "project_manager",
      "photo_url": "https://api.worksync.com/storage/photos/leon.png"
    }
  }
  ```

### POST `/auth/logout`
Revoke the current user's token.
- **Auth required**: Yes
- **Response (200 OK)**:
  ```json
  {
    "message": "Successfully logged out"
  }
  ```

### GET `/auth/me`
Retrieve profile details of the currently authenticated user.
- **Auth required**: Yes
- **Response (200 OK)**:
  ```json
  {
    "id": 1,
    "name": "Leon",
    "email": "leon@worksync.com",
    "role": "project_manager",
    "photo_url": "https://api.worksync.com/storage/photos/leon.png",
    "created_at": "2026-07-02T12:00:00Z"
  }
  ```

---

## 2. Dashboard Endpoints

### GET `/dashboard/stats`
Get summary statistics depending on user role.
- **Auth required**: Yes
- **Response (200 OK - Project Manager)**:
  ```json
  {
    "active_projects_count": 12,
    "tasks_summary": {
      "todo": 15,
      "in_progress": 20,
      "review": 5,
      "revision": 3,
      "done": 43
    },
    "overdue_tasks_count": 2
  }
  ```

---

## 3. Project Endpoints

### GET `/projects`
Get list of projects.
- PM/Admin sees all projects. Employees see only projects they are member of.
- **Auth required**: Yes
- **Response (200 OK)**:
  ```json
  [
    {
      "id": 1,
      "name": "Website Company Profile",
      "client_name": "PT Maju Bersama",
      "progress_percent": 80,
      "members_count": 5,
      "start_date": "2026-06-01",
      "end_date": "2026-07-30",
      "status": "active"
    }
  ]
  ```

### POST `/projects`
Create a new project.
- **Auth required**: Yes (Project Manager or Admin only)
- **Request Body**:
  ```json
  {
    "name": "E-Commerce Mobile App",
    "description": "Building mobile app for sales",
    "client_id": 3,
    "start_date": "2026-07-15",
    "end_date": "2026-11-30"
  }
  ```
- **Response (201 Created)**:
  ```json
  {
    "id": 2,
    "name": "E-Commerce Mobile App",
    "description": "Building mobile app for sales",
    "client_id": 3,
    "pm_id": 1,
    "start_date": "2026-07-15",
    "end_date": "2026-11-30",
    "status": "active"
  }
  ```

---

## 4. Task Endpoints

### GET `/tasks`
Get tasks list.
- **Query Parameters**: `project_id` (optional), `status` (optional), `assigned_to` (optional)
- **Auth required**: Yes
- **Response (200 OK)**:
  ```json
  [
    {
      "id": 101,
      "project_name": "Website Company Profile",
      "title": "Login Screen UI",
      "priority": "high",
      "status": "todo",
      "deadline": "2026-07-10",
      "assignee": {
        "id": 3,
        "name": "Andi"
      }
    }
  ]
  ```

### POST `/tasks`
Assign a new task to an employee.
- **Auth required**: Yes (Project Manager or Admin only)
- **Request Body**:
  ```json
  {
    "project_id": 1,
    "assigned_to": 3,
    "title": "Login Screen UI",
    "description": "Design and code the login screen layout using XML.",
    "priority": "high",
    "deadline": "2026-07-10"
  }
  ```
- **Response (201 Created)**:
  ```json
  {
    "id": 101,
    "project_id": 1,
    "assigned_to": 3,
    "created_by": 1,
    "title": "Login Screen UI",
    "description": "Design and code the login screen layout using XML.",
    "priority": "high",
    "status": "todo",
    "deadline": "2026-07-10"
  }
  ```

### PUT `/tasks/{id}/status`
Update status of a task.
- **Auth required**: Yes (Employee assignee or PM)
- **Request Body**:
  ```json
  {
    "status": "in_progress"
  }
  ```
- **Response (200 OK)**:
  ```json
  {
    "id": 101,
    "status": "in_progress",
    "updated_at": "2026-07-02T15:00:00Z"
  }
  ```

### POST `/tasks/{id}/submit`
Submit task deliverables for PM review.
- **Auth required**: Yes (Employee assignee only)
- **Request Body (Multipart Form-Data)**:
  - `file`: Binary file upload (optional)
  - `note`: Text commentary string (optional)
- **Response (200 OK)**:
  ```json
  {
    "submission_id": 50,
    "task_id": 101,
    "file_url": "https://api.worksync.com/storage/submissions/file_abc123.zip",
    "note": "Done coding. Please check the github repo.",
    "submitted_at": "2026-07-02T15:10:00Z"
  }
  ```

---

## 5. Review Endpoints

### POST `/tasks/{id}/review`
Review a submitted task (Approve/Reject).
- **Auth required**: Yes (Project Manager or Admin only)
- **Request Body**:
  ```json
  {
    "status": "approved", // or "rejected"
    "feedback": "Great work! Ready for deployment."
  }
  ```
- **Response (200 OK)**:
  ```json
  {
    "review_id": 10,
    "task_id": 101,
    "status": "done", // Task status changes to "done" if approved, or "revision" if rejected
    "feedback": "Great work! Ready for deployment.",
    "reviewed_at": "2026-07-02T15:30:00Z"
  }
  ```

---

## 6. Common Errors (Standard Format)

```json
{
  "error": "Error Message Details",
  "status_code": 401
}
```
- **401 Unauthorized**: Missing token or token expired.
- **403 Forbidden**: Accessing role-restricted endpoints (e.g. employee trying to create project).
- **422 Unprocessable Entity**: Validation failure (e.g., missing parameter, wrong format).
