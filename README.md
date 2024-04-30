# study-scheduler
### 학습(강의, 도서) 계획을 관리하는 애플리케이션

### 1. 요구사항
- 사용자는 완료된, 시작할 학습을 등록할 수 있다.
  - [x] 학습의 종류는 강의, 도서 두 가지다.
  - [x] 완료된 학습은 시작일, 종료일을 같이 입력 받아 등록한다.
  - [x] 시작할 학습은 예상 종료일을 계산할 수 있다. 
- 사용자는 일일 학습을 기록할 수 있다.
  - [x] 일일 학습은 특정 일을 기준으로 생성한다.
  - [X] 일일 학습을 생성한 후엔 남은 학습량과 남은 기간을 보여준다.
  - [ ] 일일 학습은 수정, 삭제될 수 있다.
  - [ ] 특정 학습에 대한 일일 학습을 전체 조회, 기간별 조회할 수 있다.
- 등록된 학습을 캘린더를 통해 보여준다. - 일별 학습
  - [ ] 전체 달력엔 진행중인 학습들이 표시된다.
    - [x] 특정 기간에 진행, 완료된 학습을 조회한다.
  - [ ] 각 학습을 클릭 시
    - [ ] 해당 학습에 대한 일일 학습(학습 시간, 내용)을 추가, 삭제 할 수 있다.
    - [x] 해당 학습에 대한 상세 정보 조회 및 수정할 수 있다.
    - [ ] 일일 학습이 추가되면 해당 학습의 글자 색이 변경 된다.
  - [ ] 특정 일(ex - 21일)을 클릭하면 해당 일에 학습한 일일 학습 리스트를 보여준다.
  - [ ] 입력 받은 학습 시간을 통해 학습률이 100%가 될 경우
    - [ ] 학습 완료에 대한 알림
    - [ ] 학습의 상태를 종료도 바꾸고, 종료일을 업데이트한다.
- 등록된 학습을 완료, 진행 중, 진행 예정으로 구분해 각각 페이징 처리하여 보여준다.
  - [ ] 리스트는 시작일 기준 최신순으로 노출 - 개별 학습
  - [ ] 개별 학습의 제목을 클릭하면 상세 내용이 모달창으로 노출(학습 내용 수정, 삭제)
- 통계 페이지를 통해 최근 한달 간 학습에 대한 성취율을 그래프로 보여준다.


