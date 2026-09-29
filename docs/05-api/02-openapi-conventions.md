# API Conventions

## Success

```json
{
  "data": {},
  "meta": {}
}
```

## Error

```json
{
  "error": {
    "code": "OBSERVATION_NOT_FOUND",
    "message": "Observation was not found.",
    "requestId": "..."
  }
}
```

## HTTP

- 200: 조회/수정 성공
- 201: 생성 성공
- 204: 삭제 성공
- 400: 입력 오류
- 401: 인증 필요
- 403: 권한 없음
- 404: 없음
- 409: 충돌
- 422: 도메인 검증 실패
- 429: rate limit
- 500: 서버 오류

API schema는 OpenAPI 파일을 코드와 함께 버전 관리한다.
