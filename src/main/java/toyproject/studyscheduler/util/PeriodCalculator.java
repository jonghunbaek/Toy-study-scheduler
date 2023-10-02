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
// 근데 또 리팩토링 완료해놓고 보니 이럴바엔 그냥 Study를 상속받는 자식 클래스들을 그냥 매개변수로 넘겨주는게 깔끔할거 같은데? 이건 나중에 시간되면 도전
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

    public int calculatePeriodBy(List<RequiredFunction> functions) {
        int totalExpectedTime = functions.stream()
            .mapToInt(RequiredFunction::getExpectedTime)
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
