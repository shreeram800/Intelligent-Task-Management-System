# 🧠 Intelligent Task Management System

A **Microservices-based Task Management Platform** designed for scalable team collaboration. Built with Spring Boot, Kafka, WebSockets, and secured using JWT. Includes real-time chat, email & push notifications, user roles, and a responsive React frontend.

---

## 🏗️ Architecture Overview

This system follows the **Microservices Architecture** and includes the following services:

### 🧩 Core Services

| Service        | Description |
|----------------|-------------|
| **User Service** | Manages user registration, authentication, roles, and profile updates. |
| **Task Service** | Handles creation, assignment, and tracking of tasks with deadlines and priorities. |
| **Notification Service** | Sends email and push notifications (via Kafka events). |

### 🔁 Supporting Services

| Component       | Role |
|------------------|------|
| **Service Registry (Eureka)** | Registers and discovers all microservices. |
| **Kafka** | Enables asynchronous communication for notifications and event-driven updates. |
| **JWT Auth** | Secures all APIs using bearer tokens and role-based access control. |

---

## 💻 Tech Stack

| Layer        | Technology |
|-------------|------------|
| **Backend** | Java, Spring Boot, Spring Security, Spring Cloud, WebSocket, Kafka, Eureka |
| **Frontend** | React.js, Tailwind CSS |
| **Authentication** | JWT (JSON Web Tokens) |
| **Messaging** | Apache Kafka |
| **Notifications** | Email (SMTP) and Web Push |
| **Real-Time Chat** | WebSocket |
| **Service Discovery** | Netflix Eureka |

---

## 🔐 Authentication & Security

All APIs are **protected with JWT Bearer tokens**, supporting:

- User login and role-based authorization
- Secure endpoints per role (`/admin`, `/manager`, `/employee`, etc.)
- Refresh token capability (optional)

---

## 🔔 Notification System

Notifications are triggered:

- 📧 Via Email (SMTP)
- 📱 As in-app push badges
- 📦 Delivered asynchronously using Kafka

Types of notifications:
- Task assignment & status change
- Upcoming deadlines
- Chat messages or mentions

---

## 💬 Real-Time Chat (WebSocket)

- Users in the same team can chat individually in real time
- Messages persist and are delivered via WebSocket
- Typing indicators and delivery status supported (optional features)

---

## 📁 Project Structure

```bash
.
├── user-service/
├── task-service/
├── notification-service/
├── service-registry/  # Eureka Server
├── frontend/          # React + Tailwind
