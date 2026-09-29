# Data Governance

## 데이터 계층

1. Raw Observation
2. Processed Observation
3. Identification
4. Quality Assessment
5. Research Dataset
6. Public Aggregate

## 수정

원본 변경을 무제한 덮어쓰지 않고 중요 변경은 audit trail을 남긴다.

## 삭제

삭제된 데이터는 일반 사용자에게 비공개가 되며, 연구 export/캐시/검색 인덱스에서 제거되는 정책을 정의한다.

## Provenance

연구용 데이터셋에는:
- 생성일
- 기준 버전
- 필터
- 포함/제외 조건
- taxonomy version
- export version
- license

를 포함한다.

## 데이터 품질

품질은 자동 계산 + 사용자 피드백 + 전문가 검토의 조합으로 관리한다.
