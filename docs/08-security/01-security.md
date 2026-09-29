# Security Requirements

## Authentication
- Spring Security
- password hashing
- access/refresh token 정책
- OAuth2 선택적 지원
- session/token revoke 전략

## Authorization

서버에서 다음을 항상 검사한다.
- resource ownership
- role
- project membership
- expert privileges
- admin privileges

## Upload Security

- 허용 MIME 검사
- 파일 크기 제한
- 이미지 디코딩 검사
- 악성 파일 방어
- 원본 파일명 비신뢰
- storage key 난수화
- public URL 직접 노출 최소화

## API Security

- rate limit
- request size limit
- CORS allowlist
- CSRF 전략
- input validation
- SQL injection 방어
- XSS 출력 인코딩
- SSRF 방어

## Secrets

절대 Git에:
- DB password
- JWT secret
- OAuth secret
- S3 key
- AI API key

를 저장하지 않는다.

`.env`는 로컬 전용이며 `.gitignore`에 포함한다.

## Audit

다음 행위는 감사로그:
- 권한 변경
- 관찰 삭제
- 신고 처리
- 민감 위치 접근
- 관리자 조작
- 데이터 export
