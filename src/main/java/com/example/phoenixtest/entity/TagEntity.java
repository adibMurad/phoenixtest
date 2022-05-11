package com.example.phoenixtest.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "tags")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TagEntity {
    @Id
    @GeneratedValue
    private Integer id;
    @Column
    @EqualsAndHashCode.Include
    private String tag;
    @ManyToMany(mappedBy = "tags")
    private List<QuestionEntity> questions;

    public String toString() {
        return "TagEntity(id="
                + this.getId()
                + ", tag="
                + this.getTag()
                + ", questions="
                + this.getQuestions().stream().map(QuestionEntity::getId).collect(Collectors.toList()) + ")";
    }
}
