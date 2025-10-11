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

![Rezerve System Architecture](https://github.com/AyushVarshney1/Rezerve-Smart-Seat-Booking-and-Dynamic-Pricing-Backend-System/blob/b64041a36c63c4682db7ae4ecdcebe73ec963bc7/Rezerve%20Animated%20System%20Architecture%20Cropped.gif)

---

## 🔄 Booking Event Flow — Orchestration SAGA Pattern

> This flow demonstrates how a booking request travels through the system — showcasing how the **Booking Service** orchestrates calls between **Event**, and **Inventory** services, ensuring consistency through compensating actions and Redis TTL-based rollback.

![Booking Event Flow — Orchestration SAGA Pattern](https://github.com/AyushVarshney1/Rezerve-Smart-Seat-Booking-and-Dynamic-Pricing-Backend-System/blob/75c325118147dbd7733027d25d014deda1378a18/Booking%20Event%20Flow.png)

### Flow Summary

1. **User initiates booking** through API Gateway → Booking Service.  
2. Booking Service calls **Event Service (gRPC)** to verify event existence and pricing.  
3. Calls **Inventory Service (gRPC)** to confirm seat availability.  
4. Creates provisional booking in PostgreSQL and Redis (with TTL = 10 mins).  
5. Returns booking ID to the client.

---

## 💰 Dynamic Pricing Flow — Choreography SAGA Pattern

> The pricing updates are handled in a fully **decentralized, event-driven manner** using Kafka. Services react to events rather than direct orchestration calls, implementing a **Choreography SAGA Pattern**.

![Dynamic Pricing Flow — Choreography SAGA Pattern](https://github.com/AyushVarshney1/Rezerve-Smart-Seat-Booking-and-Dynamic-Pricing-Backend-System/blob/d0f0377eb4f232531d8cdcaef578f3f333159eb7/Dynamic%20Pricing%20Flow.png)


### Flow Summary

1. **Inventory Service** publishes seat updated events to Kafka.  
2. **Pricing Service** consumes these events to calculate new prices dynamically based on seat availability and event type.  
3. Updated price information is sent to **Event Service** via Kafka.  
4. **Event Service** updates event pricing in PostgreSQL and Redis cache.  
5. No central coordinator — each service reacts autonomously to events.

---

## 📈 Observability Stack (Prometheus + Grafana)

> Each service exposes its own `/actuator/prometheus` endpoint to be scraped by **Prometheus**.  
> **Grafana** visualizes latency, throughput, Kafka lag, and database performance metrics for deep insights into the health of the distributed system.

📊 Metrics Monitored:
- Service latency (95th percentile)  
- Kafka consumer lag  
- Redis cache hits/misses  
- Database connection pool usage  
- Request rate & error rate per service  

📉 **_Insert Grafana Dashboard Screenshot here (optional)_**

---

## 🧰 Tech Stack

| Category | Technologies |
|-----------|---------------|
| **Backend Framework** | Spring Boot, Spring JPA, Spring Cloud Gateway |
| **Messaging** | Apache Kafka |
| **Inter-service Communication** | gRPC |
| **Databases** | PostgreSQL, MongoDB |
| **Caching & Locks** | Redis, Redisson |
| **Auth & Security** | Spring Security (JWT) |
| **Monitoring** | Prometheus, Grafana |
| **Deployment** | Docker & Docker Compose |

---

