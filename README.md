# 📓 JournalApp

A production-grade **Spring Boot** backend for a journaling application — users log daily entries, get weekly AI-style sentiment summaries mailed to them, and admins can manage the platform. Built with security, event-driven architecture, and cloud deployment in mind.
Live link:- journalapp-production-1986.up.railway.app

---

## ✨ Features

- **JWT-based authentication** — stateless sessions, secured with Spring Security
- **Google OAuth2 login** — sign in with Google alongside username/password auth
- **Journal CRUD** — create, read, update, delete personal journal entries
- **Sentiment tracking** — entries are tagged `HAPPY`, `SAD`, `ANGRY`, or `ANXIOUS`
- **Weekly sentiment digest** — a scheduled job (every Sunday 9 AM) analyzes each user's week and emails them a summary via a Kafka-driven pipeline
- **Redis caching** — caches frequently accessed data (e.g. user lookups) via `AppCache`
- **Kafka event streaming** — powered by **Confluent Cloud**, decouples sentiment computation from the scheduler
- **Weather integration** — fetches live weather data via an external Weather API
- **Email service** — SMTP-based notifications (Gmail)
- **Admin panel APIs** — view all users, create new admin accounts
- **OpenAPI/Swagger documentation** — fully documented, grouped endpoints with bearer-token auth support

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.5.16 |
| Security | Spring Security + JWT (`jjwt` 0.12.5) + Google OAuth2 |
| Database | MongoDB Atlas |
| Cache | Redis |
| Messaging | Apache Kafka (Confluent Cloud, SASL_SSL) |
| Email | Spring Mail (SMTP/Gmail) |
| Docs | SpringDoc OpenAPI 2.8.13 (Swagger UI) |
| Build | Maven |
| Deployment | Railway |

---

## 📂 Project Structure

```
src/main/java/com/nilesh/JournalingApp/
├── Cache/              # AppCache - Redis-backed caching
├── Config/             # Security, Redis, Swagger configuration
├── Controller/         # REST controllers
├── Entity/             # MongoDB documents (User, JournalEntry, Config)
├── Filter/             # JwtFilter - request-level JWT validation
├── Repository/         # Spring Data Mongo repositories
├── Schedular/          # UserSchedular - weekly sentiment cron job
├── Service/            # Business logic (Journal, Email, Redis, Google Auth, Weather, Sentiment)
├── Utils/              # JWTUtil - token generation/validation
├── api/response/       # External API response DTOs
├── enums/              # Sentiment enum
└── model/              # SentimentData (Kafka payload)
```

---

## 🔌 API Endpoints

### Public — `/public`
| Method | Endpoint | Description |
|---|---|---|
| GET | `/public` | Health/welcome check |
| POST | `/public/signup` | Register a new user |
| POST | `/public/login` | Log in and receive JWT |

### Google Auth — `/auth/google`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/google/login` | Authenticate via Google OAuth2 |

### Journal — `/Journal` *(authenticated)*
| Method | Endpoint | Description |
|---|---|---|
| GET | `/Journal` | Get all entries for the logged-in user |
| POST | `/Journal` | Create a new entry |
| GET | `/Journal/id/{myId}` | Get a specific entry |
| PUT | `/Journal/id/{myId}` | Update an entry |
| DELETE | `/Journal/id/{myId}` | Delete an entry |

### User — `/User` *(authenticated)*
| Method | Endpoint | Description |
|---|---|---|
| GET | `/User` | Get logged-in user's profile |
| PUT | `/User` | Update profile |
| DELETE | `/User` | Delete account |

### Admin — `/admin` *(admin only)*
| Method | Endpoint | Description |
|---|---|---|
| GET | `/admin/all-users` | List all users |
| POST | `/admin/create-new-admin` | Promote/create an admin user |

---

## ⚙️ Setup & Configuration

### Prerequisites
- Java 17
- Maven (or use the bundled `mvnw`)
- MongoDB Atlas cluster
- Redis instance
- Confluent Cloud Kafka cluster
- Gmail account (for SMTP) with an app password
- Google OAuth2 client credentials
- A weather API key

### Configuration

Copy `src/main/resources/application_Example.yml` to `application.yml` (or use `application-dev.yml` / `application-prod.yml` profiles) and fill in your own values:

```yaml
spring:
  mail:
    username: yourmail@gmail.com
    password: XXXX XXXX XXXX XXXX     # Gmail app password

  data:
    mongodb:
      uri: mongodb://your-mongodb-uri
      database: your-database-name

    redis:
      host: your-redis-host
      port: XXXX
      password: XXXXXXXXXXXXXXXXXXXXXXXXXXX

  kafka:
    bootstrap-servers: your-confluent-bootstrap-server:9092
    properties:
      sasl:
        jaas:
          config: >-
            org.apache.kafka.common.security.plain.PlainLoginModule required
            username='YOUR_KAFKA_API_KEY'
            password='YOUR_KAFKA_API_SECRET';

  security:
    oauth2:
      client:
        registration:
          google:
            client-id: YOUR_GOOGLE_CLIENT_ID
            client-secret: YOUR_GOOGLE_CLIENT_SECRET

weather:
  api:
    key: YOUR_WEATHER_API_KEY

SECRET_KEY: YOUR_SECRET_KEY_OF_JWTUtil
```

### Run locally

```bash
git clone https://github.com/nilesh24rit/JournalApp.git
cd JournalApp
./mvnw spring-boot:run
```

The app starts on `http://localhost:8080` by default. Swagger UI is available at `/swagger-ui/index.html`.

### Build a JAR

```bash
./mvnw clean package
java -jar target/JournalingApp-0.0.1-SNAPSHOT.jar
```

---

## 🔄 How the Weekly Sentiment Flow Works

1. `UserSchedular` runs every Sunday at 9 AM (`0 0 9 * * SUN`)
2. It pulls each user's entries from the last week and computes a dominant `Sentiment`
3. The result is published to the `weekly-sentiments` Kafka topic (consumer group `weekly-sentiment-group`)
4. `SentimentConsumerService` consumes the event and triggers `EmailService` to send the user a personalized digest

---

## 📌 Notes

- Sessions are **stateless** — every request is authenticated via a `Bearer <JWT>` token, validated in `JwtFilter`.
- MongoDB queries for admin analytics live in `UserRepositoryImpl`.
- Deployed on **Railway**; production profile expects `0.0.0.0/0` MongoDB IP whitelisting for connectivity.

---

## 👤 Author

**Nilesh Kumar Singh**
B.Tech CSE, M. S. Ramaiah Institute of Technology
GitHub: [@nilesh24rit](https://github.com/nilesh24rit)
