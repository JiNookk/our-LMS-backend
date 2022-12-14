package jinookk.ourlms.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jinookk.ourlms.dtos.CommentRequestDto;
import jinookk.ourlms.dtos.InquiryDeleteDto;
import jinookk.ourlms.dtos.InquiryDto;
import jinookk.ourlms.dtos.InquiryRequestDto;
import jinookk.ourlms.dtos.InquiryUpdateDto;
import jinookk.ourlms.models.vos.Content;
import jinookk.ourlms.models.vos.HashTag;
import jinookk.ourlms.models.vos.LectureTime;
import jinookk.ourlms.models.vos.Like;
import jinookk.ourlms.models.vos.Name;
import jinookk.ourlms.models.vos.Title;
import jinookk.ourlms.models.vos.ids.AccountId;
import jinookk.ourlms.models.vos.ids.CourseId;
import jinookk.ourlms.models.vos.ids.LectureId;
import jinookk.ourlms.models.vos.status.InquiryStatus;
import jinookk.ourlms.models.vos.status.Status;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
public class Inquiry {
    @Id @GeneratedValue
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "course_id"))
    private CourseId courseId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "lecture_id"))
    private LectureId lectureId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "account_id"))
    private AccountId accountId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "status"))
    private InquiryStatus status;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<HashTag> hashTags = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    private List<Like> likes = new ArrayList<>();

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "title"))
    private Title title;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "content"))
    private Content content;

    @Embedded
    private LectureTime lectureTime;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "publisher"))
    private Name publisher;

    private Boolean anonymous;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime publishTime;

    private LocalDateTime repliedAt;

    // accountId??? ???????????? inquiryId??? ?????? ?????????.
    @Formula("(SELECT COUNT(*) FROM inquiry_likes l WHERE l.inquiry_id = id)")
    private int countOfLikes;

    public Inquiry() {
    }

    public Inquiry(Long id, CourseId courseId, LectureId lectureId, AccountId accountId, InquiryStatus status,
                   List<HashTag> hashTags, List<Like> likes, Title title, Content content, LectureTime lectureTime,
                   Name publisher, Boolean anonymous, LocalDateTime publishTime, LocalDateTime repliedAt) {
        this.id = id;
        this.courseId = courseId;
        this.lectureId = lectureId;
        this.accountId = accountId;
        this.status = status;
        this.hashTags = hashTags;
        this.likes = likes;
        this.title = title;
        this.content = content;
        this.lectureTime = lectureTime;
        this.publisher = publisher;
        this.anonymous = anonymous;
        this.publishTime = publishTime;
        this.repliedAt = repliedAt;
    }

    public static Inquiry fake(String content) {
        return fake(new Content(content));
    }

    private static Inquiry fake(Content content) {
        return new Inquiry(1L, new CourseId(1L), new LectureId(1L), new AccountId(1L), new InquiryStatus(Status.CREATED),
                List.of(new HashTag("hashTag")), List.of(new Like()), new Title("title"), content,
                new LectureTime(1, 24), new Name("tester"), false, LocalDateTime.now(), LocalDateTime.now());
    }

    public static Inquiry fake(Name publisher) {
        return new Inquiry(1L, new CourseId(1L), new LectureId(1L), new AccountId(1L), new InquiryStatus(Status.CREATED),
                List.of(new HashTag("hashTag")), List.of(new Like()), new Title("title"), new Content("hi"),
                new LectureTime(1, 24), publisher, false, LocalDateTime.now(), LocalDateTime.now());
    }

    public static Inquiry of(InquiryRequestDto inquiryRequestDto, AccountId accountId, Name name) {
        Long id = null;
        LectureId lectureId = inquiryRequestDto.getLectureId();
        InquiryStatus status = new InquiryStatus(Status.CREATED);
        Title title = new Title(inquiryRequestDto.getTitle());
        List<HashTag> hashTags = new ArrayList<>();
        inquiryRequestDto.getHashTags().forEach(hashTag -> hashTags.add(new HashTag(hashTag)));
        List<Like> likes = new ArrayList<>();
        Content content = new Content(inquiryRequestDto.getContent());
        Boolean anonymous = inquiryRequestDto.getAnonymous();
        Name publisher = new Name(name.value(), anonymous);
        LocalDateTime publishTime = null;
        LectureTime lectureTime = new LectureTime(inquiryRequestDto.getMinute(), inquiryRequestDto.getSecond());
        LocalDateTime repliedAt = null;
        CourseId courseId = new CourseId(inquiryRequestDto.getCourseId());

        return new Inquiry(id, courseId, lectureId, accountId, status, hashTags, likes, title, content,
                lectureTime, publisher, anonymous, publishTime, repliedAt);
    }

    public Long id() {
        return id;
    }

    public LectureId lectureId() {
        return lectureId;
    }

    public AccountId accountId() {
        return accountId;
    }

    public Boolean anonymous() {
        return anonymous;
    }

    public List<HashTag> hashTags() {
        return List.copyOf(hashTags);
    }

    public Content content() {
        return content;
    }

    public int countOfLikes() {
        return countOfLikes;
    }

    public LocalDateTime repliedAt() {
        return repliedAt;
    }

    public InquiryStatus status() {
        return status;
    }

    public Name publisher() {
        return publisher;
    }

    public LectureTime lectureTime() {
        return lectureTime;
    }

    public LocalDateTime publishTime() {
        return publishTime;
    }

    public InquiryDto toInquiryDto() {
        return new InquiryDto(
                id, lectureId, courseId, hashTags, status, title, likes, content, publisher, publishTime, lectureTime);
    }

    public InquiryDeleteDto toInquiryDeleteDto() {
        return new InquiryDeleteDto(id);
    }

    public void delete() {
        status.delete();
        lectureId = null;
        accountId = null;
        hashTags = null;
        title = null;
        content = null;
        publisher = null;
        publishTime = null;
    }

    public void update(InquiryUpdateDto inquiryUpdateDto) {
        title.update(inquiryUpdateDto.getTitle());
        content.update(inquiryUpdateDto.getContent());
        hashTags = inquiryUpdateDto.getHashTags().stream()
                .map(HashTag::new)
                .toList();
    }

    public Comment createComment(List<Comment> comments, CommentRequestDto commentRequestDto, Account account) {
        if (isPublisherId(new AccountId(account.id()))) {
            return Comment.of(commentRequestDto, publisher, accountId);
        }

        Optional<Comment> previousComment = previousComment(comments, new AccountId(account.id()));

        if (previousComment.isPresent()) {
            Name author = previousComment.get().author();
            return Comment.of(commentRequestDto, author, new AccountId(account.id()));
        }

        Name author = new Name(account.name().value(), anonymous);
        return Comment.of(commentRequestDto, author, new AccountId(account.id()));
    }

    public boolean isPublisherId(AccountId accountId) {
        return Objects.equals(this.accountId, accountId);
    }

    public Optional<Comment> previousComment(List<Comment> comments, AccountId accountId) {
        return comments.stream()
                .filter(comment -> comment.isMyComment(accountId))
                .findFirst();
    }

    public boolean filter(String condition) {
        return this.status.filter(condition);
    }
}
