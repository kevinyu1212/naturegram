# GIS Specification

## 좌표계

- 저장: WGS84 EPSG:4326
- PostGIS geography/geometry를 용도에 맞게 사용
- 지도 표시용 projection은 클라이언트/타일 시스템에 맞춤

## Spatial Queries

- bbox observation search
- radius search
- polygon intersection
- administrative region aggregation
- grid aggregation

## Map Layers

1. Observation points
2. Clusters
3. Species distribution
4. Project boundary
5. Data gap
6. Habitat
7. Conservation sensitivity

## Sensitive Location

원본 좌표와 공개 좌표를 분리한다.

```text
raw_geom
public_geom
geoprivacy
sensitivity_level
```

보호종은 공개 좌표를 격자/범위로 흐림 처리할 수 있다.

## Data Gap

지역 × 기간 × 분류군을 기준으로 관찰량을 계산한다.

예:
- 1km grid
- 최근 30일
- Lepidoptera

결과를 `coverage`로 시각화한다.
