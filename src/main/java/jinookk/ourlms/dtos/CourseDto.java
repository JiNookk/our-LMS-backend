package jinookk.ourlms.dtos;

import jinookk.ourlms.models.vos.Category;
import jinookk.ourlms.models.vos.Content;
import jinookk.ourlms.models.vos.HashTag;
import jinookk.ourlms.models.vos.ImagePath;
import jinookk.ourlms.models.vos.Price;
import jinookk.ourlms.models.vos.status.Status;
import jinookk.ourlms.models.vos.ids.AccountId;
import jinookk.ourlms.models.vos.Name;
import jinookk.ourlms.models.vos.Title;

import java.util.List;

public class CourseDto {
    private Long id;
    private Long instructorId;
    private String category;
    private String title;
    private Integer price;
    private String description;
    private String status;
    private String instructor;
    private String coverImage;
    private List<String> hashTags;

    public CourseDto() {
    }

    public CourseDto(Long id, Category category, Title title, Price price, Content description, Status status,
                     Name instructor, AccountId instructorId, ImagePath coverImage, List<HashTag> hashTags) {
        this.id = id;
        this.category = category.value();
        this.title = title.value();
        this.price = price.value();
        this.description = description.value();
        this.status = status.value();
        this.instructor = instructor.value();
        this.instructorId = instructorId.value();
        this.coverImage = coverImage.value();
        this.hashTags = hashTags.stream().map(HashTag::tagName).toList();
    }

    public Long getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getInstructor() {
        return instructor;
    }

    public List<String> getHashTags() {
        return hashTags;
    }

    public Long getInstructorId(){
        return instructorId;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }
}
