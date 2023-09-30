# study-scheduler
학습 계획을 관리하는 애플리케이션
### 0. 계획
#### 0.1 작업 중
+ service layer test 작성 중 - 싱글테이블 전략으로 상속관계 매핑 시 자식 테이블의 구분을 Java에서 어떻게 하는지? 테이블 컬럼에 dtype이 있는데 이를 활용할 방법?
+ 해당 문제 해결 되면 캘린더, 크롤링 준비
+ 총 학습 예상 시간 계산해주는 기능 추가 - Study

#### 0.2 작업 예정
+ SSR준비 - 로그인 전/후 메인 페이지 작성, Study등록, 수정, 조회(캘린더) 페이지 작성

### 1. 개요
+ 인프런 강의 학습을 진행하며 수강 계획을 세우기 위해 엑셀을 사용했었음
+ 엑셀을 잘 사용하지도 못하는데다 엑셀을 쓸 바엔 그냥 하나 만들자는 생각이 들었다.
+ 계획에 따른 수강률, 수강시간, 계획시간 등을 캘린더에 표시하려한다.
+ 하지만 욕심이 생겨 인프런 강의 학습 계획뿐만 아니라 모든 개인학습 계획을 작업할 수 있는 애플리케이션을 만들고 싶어 졌다.
+ 그리고 이러한 계획과 수행 과정을 그래프 , 간트차트를 통해 표현하려한다.

---

### 2. 요구사항
+ 온라인 강의, 독서, 사이드 프로젝트의 계획, 예상 진행율, 수행시간, 수행율 등을 달력에 표현할 것
+ 캘린더는 월 단위로 표시하고, 특정 날짜를 클릭하면 해당 날짜에 포함되는 학습내용에 대한 상세정보가 보여야 한다.
+ 상세 정보에선 새로운 학습을 추가, 삭제, 수정할 수 있다.
+ 강의, 독서, 프로젝트에 대한 정보를 저장, 수정, 조회할 수 있어야 한다.
+ 사용자는 해당 데이터를 로그인해야 확인할 수 있다.
+ 사용자의 정보를 저장, 수정, 조회할 수 있어야 한다.
+ 프로젝트는 TDD를 기반으로 진행한다.

---

### 3. 세부 요구사항
##### 3-1. Study
+ 강의, 독서, 프로젝트는 interface Study를 구현한다.
+ Study는 학습률(진행률), 학습 계획률 조회할 수 있어야 한다.
##### 3-1-1. Lecture implements Study
+ Lecture는 Study를 구현해야 한다.
+ Lecture는 아이디, 이름, 강의시간, 실제 학습시간, 계획 학습시간(평일, 주말)을 필드로 가진다.
##### 3-1-2. Reading implements Study
+ Reading은 Study를 구현해야 한다.
+ Reading은 아이디, 이름, 예상 총 학습시간, 실제 학습시간, 계획 학습시간(평일, 주말)을 필드로 가진다.
##### 3-1-3. ToyProject implements Study
+ ToyProject는 Study를 구현해야한다.
+ ToyProject는 아이디, 이름, 실제 작업시간, 계획 작업시간(평일, 주말), 구현 예정 기능들(List)을 필드로 가진다.
+ ScheduledFunction과 일대다 관계를 가진다.

##### 3-2. User
+ 로그인한 사용자만 서비스를 이용할 수 있어야 한다.
+ 구글, 네이버를 통한 소셜 로그인이 가능하다.
+ User는 아이디, 이름, 이메일, 비밀번호, 학습 중인 것들(강의,독서,프로젝트), 계정상태(휴면, 활성화, 블랙), 프로필 이미지를 필드로 가진다.
+ User는 Study와 의존관계를 가진다 - 일대다.

##### 3-3. RequiredFunction
+ 구현 예정인 기능을 나타내는 클래스
+ 아이디, 이름, 설명, 총 작업 예상 시간, 실제 작업 시간, 계획 작업 시간(평일, 주말)을 필드로 가진다.
+ ToyProject와 다대일 의존관게를 가진다.

---

### 10. 작업 중 고민, 문제 상황 및 해결책
+ 도메인 설계 중 Study에 학습시간 필드 추가
```
문제 상황 : 기존엔 Study에 예정된 총 학습시간(totalExpectedTime)과 현재까지 완료한 학습 시간(completeTime)을 같이 두었다.
월별 달력에 일별 학습율, 학습 시간을 표시하려 했는데 Study객체 하나에 모든 일별 데이터를 담기엔 객체의 책임이 너무 커진다고 생각.
가능 하다고 해도 completeTime을 수정하면 이전 날짜의 학습율, 시간이 모두 같아 질거라 예상됐다.

해결 방안 : StudyTime엔티티를 만들어 일별 학습시간을 저장하려 한다. Study의 id를 FK로 가지고 일별 학습시간을 입력 시 새로운 객체를 생성해
데이터를 저장하고자 한다.
```

+ 도메인 설계 중 Study와 자식 클래스들이 상속관계
```
고민 상황 : 일반적으로 상속관계를 매핑할 때는 3가지 전략이 있다. 그 중 성능 이슈가 적은 싱글 테이블 전략을 선택했다. 다만 추후 필드가 추가 되거나 변경 사항이 생길 경우 테이블의 
크기가 너무 커질 수 있다. 다른 방법은 BaseEntity처럼 Java에서는 상속관계이지만 @MappedSuperClass를 통해 필드만 상속받고 테이블은 각각의 자식클래스 별로 따로 생성하는 것이다. 
이때 어떤 트레이드 오프가 생길 지 고민이다.

해결 방안 : 
```

+ StudyRepository test 작성 중
```
루트 오류  
Caused by: java.lang.IllegalStateException
: Failed to replace DataSource with an embedded database for tests. 
If you want an embedded database please put a supported one on the classpath or tune the replace attribute of @AutoConfigureTestDatabase.

해결 : 
@DataJpaTest의 경우 내장된 DB를 쓰는데 build.gradle에 h2 DB의 의존성이 없었기 때문이다.
```

+ StudyRepository 메소드의 반환타입
```java
//고민 상황 : 부모 클래스인 Study로 반환을 받으면 자식 클래스들의 고유 필드를 사용할 수 없다.
//해결 방안1 : 자식 클래스마다 repository를 만들어줘서 해결
//해결 방안2 : single table 전략에서 DiscriminatorColumn을 조회해서 타입을 구분해 줄 수 있다.
@Column(name="study_type", insertable = false, updatable = false)
private String studyType;
```

+ StudyUtil - PeriodCalculator 
```
처음엔 StudyPeriodCalculatorUtil이라는 클래스로 생성
첫 번째로 calculateTotalExpectedPeriod() 메서드 호출 시 인자로 4개의 변수를 주는 것이 너무 많아 3개의 공통 사용될 수 있는 변수를 초기화 시 받을 수 있도록
필드에 추가
두 번째, 빈으로 등록시 공통으로 사용될 필드 3개에 값을 대입하기가 어려워짐. 또한 생성시에만 값을 전해줘서 불변의 객체를 만들기를 원했음
세번째, 이러한 이유로 StudyUtil - PeriodCalculator로 분리를 하고 StudyUtil에선 PeriodCalculator의 생성만을 담당하게 설계했음
네번째, StudyUtil을 분리한 이유는 또한 StudyTime의 학습 시간, 학습률을 계산하는 메서드들을 클래스로 만들어 사용하기 위함
```