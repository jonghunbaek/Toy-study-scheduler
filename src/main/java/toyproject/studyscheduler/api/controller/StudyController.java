package toyproject.studyscheduler.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import toyproject.studyscheduler.api.service.StudyService;

@RequiredArgsConstructor
@Controller
public class StudyController {

    private final StudyService studyService;
}
