package toyproject.studyscheduler.dailystudy.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import toyproject.studyscheduler.dailystudy.domain.entity.DailyStudy;
import toyproject.studyscheduler.study.application.dto.Period;

import java.util.List;

import static toyproject.studyscheduler.dailystudy.domain.entity.QDailyStudy.*;

@RequiredArgsConstructor
public class DailyStudyQuerydslRepositoryImpl implements DailyStudyQuerydslRepository {

    private final JPAQueryFactory query;

    @Override
    public List<DailyStudy> findDailyStudyByConditions(Long studyId, Period period) {
        return query.selectFrom(dailyStudy)
                .where(dailyStudy.study.id.eq(studyId), studyDateBetween(period))
                .fetch();
    }

    private BooleanExpression studyDateBetween(Period period) {
        if (period == null || (period.getStartDate() == null && period.getEndDate() == null)) {
            return null;
        }

        if (period.getStartDate() != null && period.getEndDate() == null) {
            return dailyStudy.studyDate.goe(period.getStartDate());
        }

        if (period.getStartDate() == null) {
            return dailyStudy.studyDate.loe(period.getEndDate());
        }

        return dailyStudy.studyDate.between(period.getStartDate(), period.getEndDate());
    }
}
