# Test Strategy

## Unit
- domain service
- validation
- quality calculation
- geoprivacy transformation

## Integration
- controller + service + repository
- PostgreSQL/PostGIS
- object storage adapter

## Contract
- OpenAPI schema
- frontend/backend API contract

## E2E

핵심 시나리오:
1. 회원가입
2. 로그인
3. 관찰 생성
4. 이미지 업로드
5. 지도 조회
6. 식별 의견
7. 삭제
8. 신고

## Security Test

- auth bypass
- IDOR
- upload abuse
- XSS
- SQL injection
- rate limit
- permission escalation

## Performance

초기 기준:
- 일반 GET p95 < 500ms 목표
- 지도 viewport query < 1s 목표
- 이미지 업로드/AI inference는 별도 비동기 SLA

## Definition of Done

- 기능 구현
- 테스트
- 예외 처리
- 권한 확인
- 로그
- 문서
- code review
- CI 통과
