package jinookk.ourlms.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jinookk.ourlms.dtos.CommentRequestDto;
import jinookk.ourlms.dtos.InquiryDeleteDto;
import jinookk.ourlms.dtos.InquiryDto;
import jinookk.ourlms.dtos.InquiryRequestDto;
import jinookk.ourlms.dtos.InquiryUpdateDto;
import jinookk.ourlms.models.vos.AccountId;
import jinookk.ourlms.models.vos.Content;
import jinookk.ourlms.models.vos.HashTag;
import jinookk.ourlms.models.vos.LectureId;
import jinookk.ourlms.models.vos.LectureTime;
import jinookk.ourlms.models.vos.Name;
import jinookk.ourlms.models.vos.Status;
import jinookk.ourlms.models.vos.Title;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
public class Inquiry {
    @Id @GeneratedValue
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "lecture_id"))
    private LectureId lectureId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "account_id"))
    private AccountId accountId;

    // 누가 쓴것인지 -> 유저 아이디로 들고 있는다.
    // 포스트에 작성자가 익명으로 할당되고, 댓글이랑 어떻게 연결하면
    // 글쓴이 인지 id로 구분을 하자.

    // TODO : 강의시간을 기입할 것
    // TODO : 익명 구현하기 => 질문 게시판 CRUD를 마무리 하고 마지막에 구현하기
    // 익명으로 게시한 글을 계속 익명으로 사용하고 싶다. 댓글도 익명으로 사용하고 싶다.
    // 댓글에서 게시글과 똑같은 익명 닉네임을 사용하고 싶다면 어떻게 해야할까?

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "status"))
    private Status status;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<HashTag> hashTags;

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

    public Inquiry() {
    }

    public Inquiry(Long id, LectureId lectureId, AccountId accountId, Status status, List<HashTag> hashTags, Title title, Content content,
                   LectureTime lectureTime, Name publisher, Boolean anonymous, LocalDateTime publishTime) {
        this.id = id;
        this.lectureId = lectureId;
        this.accountId = accountId;
        this.status = status;
        this.hashTags = hashTags;
        this.title = title;
        this.content = content;
        this.lectureTime = lectureTime;
        this.publisher = publisher;
        this.anonymous = anonymous;

        this.publishTime = publishTime;
    }

    public static Inquiry fake(String content) {
        return fake(new Content(content));
    }

    private static Inquiry fake(Content content) {
        return new Inquiry(1L, new LectureId(1L), new AccountId(1L), new Status(Status.CREATED), List.of(new HashTag("hashTag")),
                new Title("title"), content, new LectureTime(1L, 24L), new Name("tester"), false, LocalDateTime.now());
    }

    public static Inquiry fake(Name publisher) {
        return new Inquiry(1L, new LectureId(1L), new AccountId(1L), new Status(Status.CREATED), List.of(new HashTag("hashTag")),
                new Title("title"), new Content("hi"), new LectureTime(1L, 24L), publisher, false, LocalDateTime.now());
    }

    public static Inquiry of(InquiryRequestDto inquiryRequestDto, AccountId accountId, Name name) {
        LectureId lectureId = inquiryRequestDto.getLectureId();
        Status status = new Status(Status.CREATED);
        Title title = new Title(inquiryRequestDto.getTitle());
        List<HashTag> hashTags = inquiryRequestDto.getHashTags().stream()
                .map(HashTag::new)
                .toList();
        Content content = new Content(inquiryRequestDto.getContent());
        Boolean anonymous = inquiryRequestDto.getAnonymous();
        Name publisher = new Name(name.value(), anonymous);
        LocalDateTime publishTime = null;
        LectureTime lectureTime = new LectureTime(inquiryRequestDto.getMinute(), inquiryRequestDto.getSecond());

        return new Inquiry(null, lectureId, accountId, status, hashTags, title, content,
                lectureTime, publisher, anonymous, publishTime);
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
        return new InquiryDto(id, hashTags, title, content, publisher, publishTime, lectureTime);
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

    public boolean isPublisherId(AccountId accountId) {
        return Objects.equals(this.accountId, accountId);
    }

    public Comment createComment(List<Comment> comments, CommentRequestDto commentRequestDto, Account account) {
        if (isPublisherId(new AccountId(account.id()))) {
            return Comment.of(commentRequestDto, publisher, accountId);
        }

        Optional<Comment> previousComment = previousComment(comments, new AccountId(account.id()));

        System.out.println(previousComment.isPresent());

        if (previousComment.isPresent()) {
            Name author = previousComment.get().author();
            return Comment.of(commentRequestDto, author, new AccountId(account.id()));
        }

        Name author = new Name(account.name().value(), anonymous);
        return Comment.of(commentRequestDto, author, new AccountId(account.id()));
    }

    public Optional<Comment> previousComment(List<Comment> comments, AccountId accountId) {
        return comments.stream()
                .filter(comment -> comment.isMyComment(accountId))
                .findFirst();
    }
}