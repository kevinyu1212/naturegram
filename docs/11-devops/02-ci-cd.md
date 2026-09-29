# CI/CD

## Pull Request

1. lint
2. unit test
3. build
4. integration test
5. security checks
6. review

## Main merge

1. build image
2. push registry
3. deploy staging
4. smoke test
5. production approval/deploy

## Rollback

- 이전 image tag 유지
- DB migration backward compatibility 우선
- 배포 버전 기록
- 장애 시 이전 버전으로 rollback

## Migration

DB migration tool을 사용하고 schema 변경은 코드와 함께 버전 관리한다.
