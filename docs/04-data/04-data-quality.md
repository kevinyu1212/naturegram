# Data Quality Model

단일 종합점수 하나로 과학적 정확도를 표현하지 않는다.

## Quality Dimensions

1. Media Quality
2. Location Quality
3. Temporal Quality
4. Taxonomic Consensus
5. Metadata Completeness
6. Expert Review Status

예시:

```text
Media:       18/20
Location:    20/20
Temporal:    10/10
Consensus:   14/20
Metadata:     8/10
Expert:       pending
```

## 상태

- Draft
- Needs Review
- Community Supported
- Expert Reviewed
- Disputed
- Withdrawn

`Research Ready` 같은 표현은 데이터 활용 조건을 별도 정책으로 정의한 뒤 사용한다.

## 중요한 원칙

AI confidence, community consensus, expert review, data completeness는 서로 다른 개념이다.
