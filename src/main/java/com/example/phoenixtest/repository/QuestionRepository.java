package com.example.phoenixtest.repository;

import com.example.phoenixtest.entity.QuestionEntity;
import org.springframework.data.repository.CrudRepository;

public interface QuestionRepository extends CrudRepository<QuestionEntity, String> {
//    @Modifying
//    @Query("delete from QuestionEntity q where q.tags.tag in :tags")
//    void deleteAllByTags(List<String> tags);
}

