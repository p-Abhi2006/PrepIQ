# 🎯 PrepIQ — Placement Skill-Gap & Readiness Platform

PrepIQ is a **placement preparation platform** designed to help students understand their current skills, identify weaknesses, and follow a personalized preparation path for software engineering placements.

The platform evaluates performance across areas such as **DSA, Aptitude, SQL, DBMS, OS, and Computer Networks** and converts the results into actionable preparation insights.

---

## 🚀 Features

* 👤 Student profile management
* 📝 Skill-based assessments
* 📊 Topic-wise performance tracking
* 🎯 Skill-gap analysis
* 📈 Placement readiness score
* 🏢 Company & role-based skill requirements
* 📚 Personalized preparation plans
* 🔄 Progress tracking
* 🤖 AI-assisted explanations and study guidance
* 📊 Preparation dashboard

---

## 🏗️ System Architecture

```text
                    ┌─────────────────────┐
                    │      Student        │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │   React Frontend    │
                    └──────────┬──────────┘
                               │ REST API
                               ▼
                    ┌─────────────────────┐
                    │   Spring Boot API  │
                    └──────────┬──────────┘
                               │
             ┌─────────────────┼─────────────────┐
             ▼                 ▼                 ▼
      ┌─────────────┐   ┌─────────────┐   ┌─────────────┐
      │ Assessment  │   │ Skill Gap   │   │ Preparation │
      │   Engine    │   │   Engine    │   │   Planner   │
      └──────┬──────┘   └──────┬──────┘   └──────┬──────┘
             │                 │                 │
             └─────────────────┼─────────────────┘
                               ▼
                    ┌─────────────────────┐
                    │     PostgreSQL      │
                    └─────────────────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │    AI Integration   │
                    │   Spring AI / API   │
                    └─────────────────────┘
```

---

## 🛠️ Tech Stack

### Backend

* Java
* Spring Boot
* Spring Security
* REST APIs

### Frontend

* React.js
* TypeScript
* Tailwind CSS

### Database

* PostgreSQL

### AI

* Spring AI
* OpenAI API

### Tools

* Git
* GitHub
* Postman
* Docker
* IntelliJ IDEA

### Cloud

* AWS *(planned)*

---

## 📚 Preparation Areas

PrepIQ focuses on important placement preparation skills:

* DSA
* Aptitude
* SQL
* DBMS
* Operating Systems
* Computer Networks
* Core Java
* Problem Solving

---

## 🔄 How It Works

```text
Student
   ↓
Take Assessment
   ↓
Performance Analysis
   ↓
Skill Gap Detection
   ↓
Readiness Score
   ↓
Personalized Preparation Plan
   ↓
Track Progress
```

---

## 📊 Example

A student completes assessments in DSA, SQL and DBMS.

PrepIQ analyses the results:

```text
DSA        → Strong
SQL        → Moderate
DBMS       → Weak
Aptitude   → Strong
```

The system then identifies **DBMS and SQL** as priority areas and generates a preparation plan focused on those topics.

---

## 🗄️ Core Database Entities

The initial system is planned around entities such as:

* User
* Student Profile
* Assessment
* Question
* Submission
* Skill
* Skill Progress
* Target Company
* Preparation Plan

---

## 📂 Project Structure

```text
PrepIQ/
│
├── backend/
│   └── Spring Boot Application
│
├── frontend/
│   └── React Application
│
├── docs/
│   └── Project Documentation
│
├── screenshots/
│   └── Application Screenshots
│
└── README.md
```

---

## 🧪 Testing

Testing will include:

* REST API testing using Postman
* Unit testing
* Integration testing
* Database testing
* Authentication testing
* Input validation
* Error handling

---

## 🔐 Security

The application will implement:

* User authentication
* Password encryption
* Role-based authorization
* API validation
* Secure database access
* Protected REST endpoints

---

## 📈 Development Roadmap

### Phase 1 — Backend Foundation

* Spring Boot setup
* PostgreSQL configuration
* User management
* REST APIs
* Basic authentication

### Phase 2 — Assessment System

* Question management
* Assessment creation
* Answer submission
* Score calculation

### Phase 3 — Skill Analysis

* Topic-wise performance
* Skill-gap calculation
* Readiness score

### Phase 4 — Preparation Planner

* Target company selection
* Skill requirements
* Personalized preparation roadmap
* Progress tracking

### Phase 5 — AI Integration

* AI-powered explanations
* Study recommendations
* Revision assistance
* Personalized guidance

### Phase 6 — Deployment

* Dockerization
* Cloud deployment
* Monitoring
* Performance testing

---

## 🤖 AI-Assisted Development

AI development tools may be used during implementation to accelerate:

* Boilerplate code generation
* Documentation
* Debugging assistance
* Test generation
* Code improvement

The architecture, requirements, implementation decisions, testing, debugging, and final integration are reviewed and understood during development.

---

## 🎯 Project Goals

PrepIQ aims to demonstrate practical knowledge of:

* Java backend development
* Spring Boot
* REST API design
* Database design
* Authentication & authorization
* Software architecture
* Problem-solving
* AI API integration
* Docker
* Cloud deployment

---

## 📌 Current Status

🚧 **In Development**

The project is being developed incrementally, starting with the backend foundation and core assessment functionality.

---

## 🔮 Future Improvements

* Company-specific preparation paths
* Advanced analytics
* Resume-based skill analysis
* Interview preparation
* Coding assessment integration
* AI-powered mock interviews
* Mobile application
* Advanced recommendation engine

---

## 👨‍💻 Developer

### Abhishek P

🎓 B.E. Computer Science & Engineering Student
💻 Aspiring Java Backend Engineer
☁️ Interested in Cloud & AI Integration

GitHub:
https://github.com/p-Abhi2006

---

⭐ **Building skills. Building systems. Preparing for the future.**

---

## Local Development Setup

### Prerequisites

- Java 21
- PostgreSQL
- Git

### 1. Create the database

Create the PostgreSQL database if it does not already exist:

```sql
CREATE DATABASE prepiq_db;
```

### 2. Configure the backend

Configure the PostgreSQL connection URL, username, and password in your local `backend/src/main/resources/application.properties`.

Do not commit database credentials or secrets to GitHub.

### 3. Start the backend

From the `backend` directory, run:

```powershell
.\mvnw.cmd spring-boot:run
```

The backend runs at `http://localhost:8080` when startup succeeds.

### 4. Verify the Questions API

Open another terminal and run:

```powershell
Invoke-RestMethod http://localhost:8080/api/questions
```

The endpoint returns the questions available in the database.

### Database Documentation

- [Database Schema](docs/database-schema.md)
- [Entity Relationship Diagram](docs/er-diagram.md)

- [SQL Schema (DDL)](docs/schema.sql)
