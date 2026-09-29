# Information Architecture

```text
/
├── explore
│   ├── observations
│   ├── species
│   ├── map
│   └── places
├── observe
│   └── new
├── projects
│   ├── list
│   └── :slug
├── missions
│   └── :id
├── research
│   ├── dashboard
│   └── datasets
├── community
│   ├── feed
│   └── experts
├── profile
│   └── :username
├── auth
│   ├── login
│   └── signup
└── admin
    ├── reports
    ├── users
    ├── observations
    └── audit-logs
```

## 핵심 도메인 객체

Observation > Identification > Taxon > Location > Media

Post/Comment는 Observation과 독립적인 social domain으로 유지한다.
