package com.example.phoenixtest.service;

import com.example.phoenixtest.model.SortOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class QuestionService {
    private final StackOverflowFeignClient feignClient;

    @EventListener(ApplicationReadyEvent.class)
    public void loadLatestQuestions() {
        feignClient.getQuestions(1, 20, "creation", SortOrder.DESCENDING);
    }

}
