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
다섯째, PeriodCalculator로 직접 생성하게 될 경우 여러 비슷한 유틸 클래스들이 매번 생성 또는 di가 되어야 한다.
그렇기 때문에 StudyUtil이라는 관리 클래스를 의존성 주입을 시켜 다른 유틸 클래스들을 필요에 따라 생성할 수 있도록 명시적으로 바꿨다.
여섯째, PeriodCalculator를 의존성 주입해서 사용할 경우 해당 객체를 사용할 때 마다 필드 값을 초기화해줘야 한다. 하지만 초기화 하지
않고 기간을 계산하는 메서드를 호출할 경우 오류가 생기기 때문에 StudyUtil에서 생성을 강제하는 것으로 정했다.
```

+ LocalDate.MAX, MIN, EPOCH
```
종료되지 않은 Study의 realEndDate필드 값을 LocaDate.MAX 또는 MIN으로 초기화하려 했다. 하지만 값이 계속 이상하게 출력되어
MariaDB공식 문서를 찾아보니 DATE타입은 4Bytes라 overflow가 일어난 것이다. 
또 다른 상수 값을 만들까 했지만 LocalDate.EPOCH라는 유닉스 시간을 상수를 가지고 있어 이를 활용했다.
```

+ @Transactional 과 Lazy loading, cascade
```java
@DisplayName("")
    @Test
    void test() {
        // given
        LocalDate startDate1 = LocalDate.of(2023, 7, 1);
        LocalDate endDate1 = LocalDate.of(2023, 8, 3);

        Member member = createMember();
        memberRepository.save(member);

        ToyProject toyProject = createToyProject(startDate1, endDate1, member);
        RequiredFunction function1 = createFunction(CREATE, toyProject);
        RequiredFunction function2 = createFunction(READ, toyProject);
        TechStack stack1 = createTechStack("Java", LANGUAGE, toyProject);
        TechStack stack2 = createTechStack("Spring", FRAMEWORK, toyProject);

        toyProjectRepository.save(toyProject);

        // when
        LocalDate startDate = LocalDate.of(2023, 7, 1);
        LocalDate endDate = LocalDate.of(2023, 7, 31);

        ToyProject toyProject1 = toyProjectRepository.findAll().get(0);
        String title = toyProject1.getRequiredFunctions().get(0).getTitle();
        TechCategory techCategory = toyProject1.getTechStacks().get(0).getTechCategory();
        System.out.println("=======================================");
        // then
        System.out.println(title+ " ============================= ");
        System.out.println(techCategory+ " ============================= ");

    }
```
```
문제 상황 : 위와 같은 테스트 코드를 작성했을 때 @Transactional이 없는 경우 지연 로딩과 영속성 전이가 모두 작동을 안했다.
원인 : 원인은 @Transactional이 있는 경우만 영속성 컨텍스트가 살아 있고 이 때의 엔티티들은 영속 상태 이다. 하지만 없거나 끝난 경우라면
엔티티들은 준영속 상태가 된다 .그렇기 때문에 지연 로딩과 영속성 전이 기능을 사용할 수 없는 것이다.
```