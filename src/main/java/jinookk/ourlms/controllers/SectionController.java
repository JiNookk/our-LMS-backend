package jinookk.ourlms.controllers;

import jinookk.ourlms.dtos.SectionDto;
import jinookk.ourlms.dtos.SectionRequestDto;
import jinookk.ourlms.dtos.SectionUpdateRequestDto;
import jinookk.ourlms.dtos.SectionsDto;
import jinookk.ourlms.models.vos.ids.AccountId;
import jinookk.ourlms.models.vos.ids.CourseId;
import jinookk.ourlms.services.SectionService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/sections")
    public SectionDto create(
            @Validated @RequestBody SectionRequestDto sectionRequestDto
    ) {
        return sectionService.create(sectionRequestDto);
    }

    @GetMapping("/courses/{courseId}/sections")
    public SectionsDto list(
            @RequestAttribute("accountId") Long accountId,
            @PathVariable Long courseId
    ) {
        return sectionService.listWithProgress(new CourseId(courseId), new AccountId(accountId));
    }

    @PatchMapping("/sections/{sectionId}")
    public SectionDto update(
            @Validated @RequestBody SectionUpdateRequestDto sectionUpdateRequestDto,
            @PathVariable Long sectionId
    ) {
        return sectionService.update(sectionId, sectionUpdateRequestDto);
    }

    @DeleteMapping("/sections/{sectionId}")
    public SectionDto delete(
            @PathVariable Long sectionId
    ) {
        return sectionService.delete(sectionId);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String missingProperties() {
        return "Property is Missing";
    }
}
