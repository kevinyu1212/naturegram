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
| license_id | UUID | deferred | 데이터 라이선스; 카탈로그와 정책 확정 후 필수 여부 및 FK 결정 |

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

## Observation MVP clarifications

- Observation visibility is public / limited / private and controls access to the observation.
- Location geoprivacy is open / obscured / private and belongs to the optional observation location, not the observation visibility setting.
- Exact location geometry is restricted. Public responses use only a separately generated public location or omit coordinates.
- Observation taxon_id is the observer-selected current taxon; human identification history is stored separately.
- AI results are predictions, human/community assertions are identifications, and expert decisions are expert reviews.
- Data and media licenses are separate. Do not infer research-use permission from a license identifier alone.
- Environment fields and data-quality score ranges remain deferred until their definitions and calculation rules are documented.
