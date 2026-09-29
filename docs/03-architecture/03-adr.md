# Architecture Decision Records

## ADR-001 Modular Monolith First

상태: Accepted

초기 백엔드는 Spring Boot modular monolith로 구성한다.

이유:
- 개인/소규모 팀 개발 속도
- 배포 복잡성 감소
- 트랜잭션 단순성
- 도메인 경계가 먼저 필요

## ADR-002 PostgreSQL + PostGIS

상태: Accepted

Observation의 위치와 공간 검색이 핵심이므로 PostgreSQL/PostGIS를 선택한다.

## ADR-003 Observation과 Post 분리

상태: Accepted

Observation은 과학 데이터이고 Post는 social content다. 동일 테이블/도메인으로 합치지 않는다.

## ADR-004 AI Prediction과 Identification 분리

상태: Accepted

AI prediction은 모델의 출력이고 identification은 사람이 제출하는 생물학적 식별 의견이다. 두 데이터를 별도 기록한다.

## ADR-005 Sensitive Location Protection

상태: Accepted

보호종/희귀종/민감 서식지는 정확 좌표를 공개하지 않을 수 있어야 한다.

## ADR-006 Asynchronous AI

상태: Proposed

관찰 저장과 AI 분석을 분리한다. 사용자는 먼저 관찰을 저장하고 AI 결과는 이후 갱신할 수 있다.
