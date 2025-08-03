# Real-Time Social Media Trend Analyzer

## Project Overview

This project is a full-stack Java backend service that fetches social media posts (Twitter API v2), stores them, and provides endpoints to retrieve and search posts in real time.  
Future phases will add AI-driven trend summarization, marketing suggestions, and risk detection.

---

## Features (Phase 1)

- Fetch tweets for a given keyword via REST API  
- Deduplicate and save tweets to a database  
- Retrieve all saved tweets or search by keyword  
- Global exception handling for robust error responses

---

## Architecture

Quick overview is below. For full details and interactive diagrams, see the [Architecture Overview](docs/architecture/architecture-overview.md).

### Component Diagram  
![Component Diagram](docs/architecture/component-diagram.svg)

### Sequence Diagram  
(Available in the Architecture Overview document)

---

## API Endpoints

| Method | Path           | Description                   | Request Body          | Response                  |
|--------|----------------|-------------------------------|----------------------|---------------------------|
| POST   | /api/fetch     | Fetch & save tweets by keyword | `{ "keyword": "java" }` | Summary of saved tweets    |
| GET    | /api/posts     | Retrieve all saved tweets      | N/A                  | List of tweets             |
| GET    | /api/search?q= | Search tweets by keyword       | N/A                  | Filtered list of tweets    |

---

## Getting Started

### Prerequisites

- Java 17+  
- Maven  
- Twitter API credentials (API Key, Bearer Token)  

### Setup

1. Clone the repo  
2. Configure Twitter API credentials in `application.properties`  
3. Build with Maven:  
   ```bash
   mvn clean install


