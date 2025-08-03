# Architecture Overview

## Purpose
This document explains the core components and data flow of the Real-Time Social Media Trend Analyzer (Phase 1).

## Components

- **SocialPostController**: Exposes REST endpoints (`/api/fetch`, `/api/posts`, `/api/search`) to clients.
- **SocialPostService**: Orchestrates fetching from Twitter, deduplication, and persistence.
- **TwitterApiClient**: Calls Twitter API v2, maps tweets to domain model.
- **SocialPostRepository**: Data access layer, handles saving and querying social posts.
- **Database**: Stores persisted `SocialPost` entities.
- **GlobalExceptionHandler**: Centralized error formatting for API consumers.

## Data Flow
1. Client triggers fetch with a keyword.
2. Controller forwards to Service.
3. Service invokes TwitterApiClient.
4. Client fetches tweets, maps to `SocialPost`.
5. Service deduplicates and persists via Repository.
6. Client can retrieve posts or search through endpoints.
7. Errors are caught and normalized by the exception handler.

## Diagrams
- Component diagram: `component-diagram.svg`
- Sequence diagram: `sequence-diagram.svg`
