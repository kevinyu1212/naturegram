# AI Identification Specification

## 목표

AI는 관찰자의 판단을 대체하지 않고 식별을 보조한다.

## Input

- image
- optional video/audio
- location
- date/time
- user-provided taxon guess

## Pipeline

```text
Upload
 ↓
Image Quality
 ↓
Taxon Classifier
 ↓
Candidate Ranking
 ↓
Geographic/Temporal Context
 ↓
Explanation
 ↓
User Confirmation
 ↓
Community Identification
```

## Output

```json
{
  "candidates": [
    {
      "taxonId": "uuid",
      "label": "왕잠자리",
      "scientificName": "Anax parthenope",
      "modelScore": 0.91
    }
  ],
  "needsMoreEvidence": true,
  "requestedEvidence": [
    "wing_dorsal",
    "abdomen_tip"
  ]
}
```

## UX 원칙

- 모델 점수는 과학적 확률/확정으로 표현하지 않는다.
- 상위 후보를 함께 보여준다.
- 유사종 비교를 제공한다.
- 식별이 어려우면 `Unknown / Needs Review`를 정상적인 결과로 인정한다.
- 추가 촬영을 요청할 수 있다.

## 모델 운영

모델 버전:
- model_id
- model_version
- taxonomy_version
- inference_time
- input_hash

를 저장하여 결과를 재현할 수 있게 한다.
