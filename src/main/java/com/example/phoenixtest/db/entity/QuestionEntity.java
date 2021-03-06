package com.example.phoenixtest.db.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "questions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class QuestionEntity {
    @Id
    private Integer id;
    @ManyToMany
    @JoinTable(
            name = "question_tags",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<TagEntity> tags;
    @Column(name = "is_answered")
    private Boolean answered;
    @Column(name = "view_count")
    private Integer viewCount;
    @Column(name = "answer_count")
    private Integer answerCount;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @Column(name = "user_id")
    private Integer userId;
}
