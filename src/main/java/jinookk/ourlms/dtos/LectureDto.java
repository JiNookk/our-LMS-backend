package jinookk.ourlms.dtos;

import jinookk.ourlms.models.vos.Title;
import jinookk.ourlms.models.vos.VideoUrl;
import jinookk.ourlms.models.vos.ids.CourseId;
import jinookk.ourlms.models.vos.ids.SectionId;

public class LectureDto {
    private Long id;
    private Long courseId;
    private Long sectionId;
    private String title;
    private String videoUrl;

    public LectureDto() {
    }

    public LectureDto(Long id, CourseId courseId, SectionId sectionId, Title title, VideoUrl videoUrl) {
        this.id = id;
        this.courseId = courseId.value();
        this.sectionId = sectionId.value();
        this.title = title.value();
        this.videoUrl = videoUrl.value();
    }

    public Long getId() {
        return id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public String getTitle() {
        return title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
