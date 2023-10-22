package toyproject.studyscheduler.api.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.function.FunctionType;

@Getter
@NoArgsConstructor
public class SaveRequiredFunctionDto {

    private String title;
    private String description;
    private int expectedTime;
    private FunctionType functionType;

    @Builder
    private SaveRequiredFunctionDto(String title, String description, int expectedTime, FunctionType functionType) {
        this.title = title;
        this.description = description;
        this.expectedTime = expectedTime;
        this.functionType = functionType;
    }
}
