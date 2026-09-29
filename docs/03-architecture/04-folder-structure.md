# Repository Folder Structure

```text
naturegram/
├── apps/
│   ├── web/                 # React
│   ├── api/                 # Spring Boot
│   └── ai/                  # FastAPI
├── infra/
│   ├── docker/
│   ├── nginx/
│   └── terraform/           # later
├── db/
│   ├── migrations/
│   └── seeds/
├── docs/
├── .github/
│   ├── workflows/
│   └── ISSUE_TEMPLATE/
├── docker-compose.yml
├── .env.example
└── README.md
```

MVP에서는 `apps/api`를 하나의 modular monolith로 유지한다.
