package toyproject.studyscheduler.study.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class StudyPeriod {

    private LocalDate startDate;
    private LocalDate endDate;

    private StudyPeriod(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // TODO :: 시작일이 항상 종료일 보다 더 빠름을 검증해야함
    // TODO :: MariaDB DATE 타입 최대 값이 9999-12-31이르몰 LocalDate.MAX를 사용 못함. 대체 값 상수로 변경(가능하면 String으로)
    public static StudyPeriod fromStarting(LocalDate startDate) {
        return new StudyPeriod(startDate, LocalDate.of(9999, 12, 31));
    }

    public static StudyPeriod fromTerminated(LocalDate startDate, LocalDate endDate) {
        return new StudyPeriod(startDate, endDate);
    }
}
