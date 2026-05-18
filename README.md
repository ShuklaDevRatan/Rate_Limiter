# Distributed Sliding Window Rate Limiter

A distributed rate limiter built using Spring Boot and Redis implementing the Sliding Window Algorithm for accurate request limiting.

## Overview

This project demonstrates how modern backend systems protect APIs from abuse, excessive traffic, and brute-force attacks using Redis-based distributed rate limiting.

The application uses:

* Spring Boot
* Redis
* Sliding Window Algorithm
* Interceptors
* Redis Sorted Sets (ZSET)

The rate limiter works across multiple application instances because Redis acts as a centralized shared state store.

---

## Features

* Sliding Window Rate Limiting
* Distributed request tracking using Redis
* Per-IP request limiting
* Middleware-based interception using Spring Interceptors
* Configurable request limits and duration
* Structured JSON error responses
* Automatic expiration and cleanup of old requests
* Redis Sorted Set based implementation

---

## Tech Stack

* Java
* Spring Boot
* Redis
* Docker
* Maven

---

## Rate Limiting Flow

1. Client sends request
2. Interceptor captures request before controller execution
3. Redis removes expired timestamps
4. Current active requests are counted
5. If limit exceeded:

   * Request is blocked
   * HTTP 429 response returned
6. Otherwise:

   * Current timestamp added to Redis
   * Request allowed

---

## Sliding Window Algorithm

Instead of fixed time blocks, the Sliding Window algorithm continuously checks requests from the last N seconds.

Example:

Limit:
5 requests per minute

The system checks:
Current Time - 60 seconds → Current Time

This prevents Fixed Window boundary issues where users can bypass limits near window resets.

---

## Redis Data Structure

Redis Sorted Sets (ZSET) are used.

* Score → Timestamp
* Value → Unique Request Identifier

Example:

127.0.0.1
[
1710001000,
1710001010,
1710001020
]

This allows:

* Fast insertion
* Efficient cleanup
* Range-based querying
* Accurate counting

---

## API Example

### Successful Request

HTTP 200 OK

```json
{
  "message": "Request Allowed"
}
```

### Rate Limited Response

HTTP 429 Too Many Requests

```json
{
  "status": 429,
  "error": "Too Many Requests",
  "message": "Rate limit exceeded",
  "timestamp": 1710001000
}
```

---

## Configuration

application.properties

```properties
rate.limit=5
rate.duration=1
```

* rate.limit → Maximum requests allowed
* rate.duration → Time window in minutes

---

## Architecture Diagram

                +------------------+
                |      Client      |
                |  Browser/Postman |
                +---------+--------+
                          |
                          v
                +------------------+
                | Spring Boot App  |
                +------------------+
                          |
                          v
          +--------------------------------+
          | RateLimiterInterceptor         |
          |  - Extract Client IP           |
          |  - Apply Sliding Window Logic  |
          +--------------------------------+
                          |
                          v
                +------------------+
                | RateLimiterService|
                +------------------+
                          |
                          v
                +------------------+
                |      Redis       |
                |  Sorted Set(ZSET)|
                +------------------+
                          |
          +---------------+---------------+
          |                               |
          v                               v
 +-------------------+         +----------------------+
 | Request Allowed   |         | Rate Limit Exceeded |
 | Forward to API    |         | Return HTTP 429     |
 +-------------------+         +----------------------+
          |                               |
          v                               v
 +-------------------+         +----------------------+
 | Controller        |         | JSON Error Response  |
 | Business Logic    |         | Structured API Error |
 +-------------------+         +----------------------+
---

## Learning Outcomes

This project helped in understanding:

* Distributed Systems Fundamentals
* Sliding Window Algorithm
* Redis Sorted Sets
* Middleware Architecture
* API Protection Strategies
* Atomic Operations
* Scalability Concepts
* Backend System Design

---

## Future Improvements

* Redis Lua Scripts for atomic execution
* Token Bucket Algorithm
* User/API-key based limiting
* Monitoring Dashboard
* Distributed tracing
* Kubernetes deployment

---

## Run Locally

### Start Redis

```bash
docker run --name redis-rate-limiter -p 6379:6379 redis
```

### Start Spring Boot Application

```bash
mvn spring-boot:run
```

---

## Author

Dev Ratan Shukla
