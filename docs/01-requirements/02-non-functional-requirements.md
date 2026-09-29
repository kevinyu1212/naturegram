# Non-Functional Requirements

## 성능
- 일반 API p95 응답시간 목표: 500ms 이하
- 이미지 업로드 API와 분석 작업은 비동기 처리 가능해야 함
- 지도 검색은 pagination/viewport 기반으로 제한
- 대량 데이터 조회는 cursor pagination 고려

## 가용성
- MVP 목표: 월 99.5% 이상
- 장애 발생 시 사용자 데이터 손실 방지

## 보안
- 비밀번호 해시 저장
- TLS 강제
- 비밀값 환경변수/Secret Manager 관리
- 권한 서버 검증
- 업로드 파일 MIME/크기/확장자 검증
- rate limit
- 감사 로그

## 개인정보
- 위치 공개 수준 제어
- 계정 삭제 및 데이터 삭제 정책
- 개인정보 최소수집
- 정확 위치는 기본적으로 사용자 통제
- 보호종/희귀종은 자동 위치 보호 정책 적용 가능

## 접근성
- WCAG 2.2 AA를 목표
- 키보드 탐색
- 대체텍스트
- 색상만으로 정보 구분 금지
- 지도 외 정보 제공

## 유지보수성
- API 계약을 OpenAPI로 관리
- 도메인 단위 코드 구조
- 자동 테스트
- lint/format
- CI에서 build/test 수행

## 확장성
- Observation ID는 전역적으로 유일
- Taxon ID는 외부 분류체계 연동 가능
- object storage 분리
- AI 작업은 비동기 queue로 분리 가능
