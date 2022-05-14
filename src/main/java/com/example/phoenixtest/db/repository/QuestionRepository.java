package com.example.phoenixtest.db.repository;

import com.example.phoenixtest.db.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<QuestionEntity, Integer> {
//    @Modifying
//    @Query("delete from QuestionEntity q where q.tags.tag in :tags")
//    void deleteAllByTags(List<String> tags);
}

