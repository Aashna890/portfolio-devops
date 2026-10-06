# Investment Portfolio Dashboard

A full-stack web application for tracking investment portfolios, built with Spring Boot + Thymeleaf + H2.

## Tech Stack
- Java 21, Spring Boot 3.x
- Thymeleaf (server-side templates)
- H2 in-memory database
- Maven build tool
- Bootstrap 5 (dark theme UI)

## Features
- Add/edit/delete stocks, mutual funds, ETFs
- Dashboard with P&L, current value, overall return
- Search/filter assets
- Price performance chart per asset
- Configurable alerts (loss %, target price)

## Running Locally
```bash
./mvnw spring-boot:run
```
App runs at http://localhost:8085

## DevOps Pipeline
- CI/CD: Jenkins
- Containerization: Docker
- Configuration Management: Ansible
- Testing: Selenium WebDriver

## Branch Strategy
- `main` — production-ready code
- `develop` — integration branch
- `feature/*` — individual features
