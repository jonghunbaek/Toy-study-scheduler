package toyproject.studyscheduler.dailystudy.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.study.web.dto.FindStudyTimeResponseDto;
import toyproject.studyscheduler.dailystudy.entity.StudyTime;
import toyproject.studyscheduler.dailystudy.repository.StudyTimeRepository;

import java.time.LocalDate;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class StudyTimeService {

    private final StudyTimeRepository studyTimeRepository;

    public List<FindStudyTimeResponseDto> findAllBy(LocalDate startDate, LocalDate endDate) {
        List<StudyTime> studyTimes = studyTimeRepository.findAllByPeriod(startDate, endDate);

        return studyTimes.stream()
            .map(FindStudyTimeResponseDto::of)
            .toList();
    }
}
