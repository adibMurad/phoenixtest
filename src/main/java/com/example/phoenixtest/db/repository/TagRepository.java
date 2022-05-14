package com.example.phoenixtest.db.repository;

import com.example.phoenixtest.db.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<TagEntity, Integer> {
//    @Modifying
//    @Query("delete from QuestionEntity q where q.tags.tag in :tags")
//    void deleteAllByTags(List<String> tags);

    @Query("from TagEntity where tag in :tags")
    List<TagEntity> findAllByTags(List<String> tags);
}

