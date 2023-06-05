## 강아지 인기투표 토이 프로젝트
server URL: [here](https://vote-your-dog.ngrok.app)

API 명세서 URL: [here](https://vote-your-dog.ngrok.app/swagger-ui/index.html#/dogs/dogList)

### 목표
- kafka를 통한 비동기 이벤트 처리를 경험해본다.
- Redis를 이용하여 Cache의 장점을 경험해본다.
- CQRS 패턴을 적용하여 변경과 조회 책임의 분리를 경험해본다.

### BE 아키텍쳐
![스크린샷 2023-05-28 오전 2 31 51](https://github.com/mardi2020/vote_your_dog/assets/58351498/07291d39-ee1d-4a3c-980b-7fc34b531148)

### 사용 기술
- language: Java17
- framework: SpringBoot3
- database: MySQL 8.0, Redis, MongoDB
- messaging: Apache Kafka

### 회고
- 마무리 회고: [here](https://mardi-2020.notion.site/Numble-dd3a1a9df6fe4994945a491a377e1519)
- 기술 적용기: [here](https://mardi-2020.notion.site/134bdd5f8c6c4c21bd73444fa964cde1?v=937bae13201c44eda04f04a165235f4e)