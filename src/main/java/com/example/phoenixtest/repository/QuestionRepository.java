package com.example.phoenixtest.repository;

import com.example.phoenixtest.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<QuestionEntity, Integer> {
//    @Modifying
//    @Query("delete from QuestionEntity q where q.tags.tag in :tags")
//    void deleteAllByTags(List<String> tags);
}

