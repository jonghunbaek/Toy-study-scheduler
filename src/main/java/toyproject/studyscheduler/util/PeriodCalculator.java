package toyproject.studyscheduler.util;

import lombok.*;
import toyproject.studyscheduler.domain.function.RequiredFunction;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static java.time.DayOfWeek.*;

// TODO : 강의, 독서까지 작성해놓고 보니 중복되는 코드가 많다. 다형성을 활용해 이 클래스를 추상화하고 중복을 제거해보자.
// 근데 토이프로젝트까지 해보니 굳이 할 필요가 있을까 싶다. 토이프로젝트의 기간을 계산하는 것은 강의를 계산하는 것과 같기 때문이다.
// 근데 또 리팩토링을 해서 중복코드를 메서드 하나로 합치니 봐줄만 한데??
// 근데 또 리팩토링 완료해놓고 보니 이럴바엔 그냥 Study를 상속받는 자식 클래스들을 그냥 매개변수로 넘겨주는게 깔끔할거 같은데?
// 근데 생각을 해보자. PeriodCalculator 클래스를 활용하는 것은 1. 처음에 예상기간 구할 때, 2. 하루 공부량 입력하면 남은 예상일 구할 때 두 경우
// 이 때 첫번째 예상기간은 entity에 저장되기 전이기 때문에 매개변수로 dto의 값을 매개변수로 받아야 함. 두번째는 entity로 구할 수 있음 그렇기 때문에 매개변수로
// 엔티티를 받지 말고 원시값을 받아서 해결하는 것이 나을 것 같다.
/**
 *  예상되는 학습 기간을 계산하는 클래스. StudyUtil에 의해서만 호출된다.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PeriodCalculator {

    private int planQuantityInWeekday;
    private int planQuantityInWeekend;
    private LocalDate startDate;

    @Builder(access = AccessLevel.PROTECTED)
    private PeriodCalculator(int planQuantityInWeekday, int planQuantityInWeekend, LocalDate startDate) {
        this.planQuantityInWeekday = planQuantityInWeekday;
        this.planQuantityInWeekend = planQuantityInWeekend;
        this.startDate = startDate;
    }

    public int calculatePeriodBy(int totalRunTime) {
        return calculatePeriod(totalRunTime);
    }

    public int calculatePeriodBy(List<Integer> expectedTimes) {
        int totalExpectedTime = expectedTimes.stream()
            .mapToInt(i -> i)
            .sum();

        return calculatePeriod(totalExpectedTime);
    }

    public int calculatePeriodBy(int totalPage, int readPagePerMin) {
        planQuantityInWeekday *= readPagePerMin;
        planQuantityInWeekend *= readPagePerMin;

        return calculatePeriod(totalPage);
    }

    private int calculatePeriod(int totalQuantity) {
        DayOfWeek dayOfWeek = startDate.getDayOfWeek();
        int period = 0;
        int studyDay = 0;

        while (true) {
            studyDay = dayOfWeek.getValue();
            period++;

            totalQuantity = calculateRemaining(totalQuantity, studyDay);

            if (totalQuantity <= 0) {
                break;
            }

            dayOfWeek = dayOfWeek.plus(1);
        }

        return period;
    }

    private int calculateRemaining(int totalQuantity, int studyDay) {
        if (studyDay <= FRIDAY.getValue()) {
            return totalQuantity - planQuantityInWeekday;
        } else {
            return totalQuantity - planQuantityInWeekend;
        }
    }
}
