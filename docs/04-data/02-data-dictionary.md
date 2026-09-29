# Data Dictionary

## Observation

| Field | Type | Required | Description |
|---|---|---:|---|
| id | UUID | yes | 관찰 고유 ID |
| observer_id | UUID | yes | 관찰자 |
| taxon_id | UUID | no | 현재 선택된 분류군 |
| description | text | no | 관찰 설명 |
| observed_at | timestamptz | yes | 실제 관찰 시각 |
| visibility | enum | yes | public/limited/private |
| geoprivacy | enum | yes | open/obscured/private |
| license_id | UUID | yes | 데이터/미디어 라이선스 |

## Location

- WGS84 EPSG:4326
- `geom`은 PostGIS geography(Point,4326)
- `positional_accuracy_m`로 GPS 정확도 기록
- 공개 위치는 원본과 별도 생성

## Taxon

외부 분류체계와 연계할 수 있도록:
- source
- source_taxon_id
- scientific_name
- rank
- parent_id

를 유지한다.

## Identification

AI 결과는 Identification 테이블에 직접 넣지 않는다.
- AI: prediction
- 사람: identification
- 전문가: expert_review

로 구분한다.
