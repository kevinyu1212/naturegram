# Taxonomy and Biodiversity Data Standard

## Taxonomy

분류군은 내부 UUID와 외부 taxonomy source ID를 함께 보유한다.

권장 필드:
- scientificName
- scientificNameAuthorship
- taxonRank
- kingdom
- phylum
- class
- order
- family
- genus
- species
- vernacularName
- taxonSource

## 외부 표준

연구 데이터 export는 Darwin Core 호환을 장기 목표로 한다.

관찰 export에서 우선 검토:
- occurrenceID
- eventDate
- recordedBy
- scientificName
- taxonRank
- decimalLatitude
- decimalLongitude
- coordinateUncertaintyInMeters
- occurrenceStatus
- basisOfRecord

정확한 매핑은 실제 데이터 모델과 라이선스 정책 확정 후 별도 버전 관리한다.

## 원칙

내부 데이터 모델을 외부 표준에 무리하게 종속시키지 않고, export adapter에서 매핑한다.
