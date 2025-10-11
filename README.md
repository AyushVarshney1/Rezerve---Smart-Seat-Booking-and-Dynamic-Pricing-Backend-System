# 🎟️ Rezerve Smart Seat Booking & Dynamic Pricing Backend System

A microservices-based backend system designed for smart, dynamic event seat booking with real-time pricing adjustments, distributed communication, and robust observability.

---

## 🚀 Overview

**Rezerve** is a distributed, event-driven microservices system built with **Spring Boot**, enabling users to book seats for various events (Movies, Concerts, Flights, Buses, Trains) while supporting **dynamic pricing**, **fault-tolerant booking orchestration**, and **real-time notifications**.

It integrates **Kafka**, **Redis**, **gRPC**, and **PostgreSQL** with **Prometheus** and **Grafana** for monitoring, providing an end-to-end scalable backend architecture similar to real-world systems like BookMyShow or Ticketmaster.

---

## 🧩 Microservices

| Service | Description |
|----------|--------------|
| **Auth Service** | Handles user registration, login, JWT authentication, role management, and inter-service token validation. Sends user-created events via Kafka. |
| **Event Service** | Manages events (travel-based & venue-based). Uses Redis caching for performance and publishes new events to **Inventory** and **Pricing** services via Kafka. |
| **Inventory Service** | Manages seat availability with concurrency control via **Redisson locks**. Handles seat booking and release and publishes updates to **Pricing Service**. |
| **Booking Service** | Orchestrator service implementing the **Orchestration SAGA Pattern**. Manages booking lifecycle using gRPC calls, Redis TTL for pending bookings, and Kafka for event coordination. |
| **Payment Service** | Handles booking payments. Validates booking via gRPC with **Booking Service** and sends asynchronous success/failure notifications via Kafka to **Notification Service**. |
| **Pricing Service** | Implements the **Choreography SAGA Pattern**. Dynamically adjusts prices based on event type and seat availability from Kafka messages sent by **Inventory Service**. |
| **Notification Service** | Listens to user creation and payment events via Kafka. Sends templated HTML emails using **Java Mail Sender** and **Thymeleaf**, storing sent data in MongoDB. |
| **API Gateway** | Single entry point for all clients. Routes requests to respective services and validates JWT tokens with **Auth Service**. |
| **Prometheus & Grafana** | Collects and visualizes system metrics across all services for observability and monitoring. |

---

## 🧠 Key Features

- ✅ **JWT Authentication** with Role-Based Access Control  
- ⚙️ **gRPC** for fast inter-service communication  
- 📬 **Kafka** for event-driven microservice coordination  
- 🔒 **Distributed Locks** (Redisson) for concurrent seat booking  
- 💸 **Dynamic Pricing Engine** via Kafka events  
- 🧾 **SAGA Pattern Implementation**
  - **Orchestration Saga** for booking transactions  
  - **Choreography Saga** for dynamic pricing updates  
- 🧰 **Redis** for caching & booking timeout management  
- 📊 **Observability Stack** with Prometheus + Grafana  
- 🐳 **Docker Compose** for full local orchestration  

---

## 🏗️ System Architecture

> The following diagram illustrates the high-level architecture of the Rezerve system — showing all microservices, their databases, and communication mechanisms (Kafka, gRPC, Redis, etc.).

📈 **[_Insert your system architecture diagram here_](https://github.com/AyushVarshney1/Rezerve-Smart-Seat-Booking-and-Dynamic-Pricing-Backend-System/blob/98556a82ef2aff556161b2c04d65dc3cbdef6743/Rezerve%20Animated%20System%20Architecture%20Cropped.gif)**
