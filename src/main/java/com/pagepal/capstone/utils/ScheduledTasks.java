package com.pagepal.capstone.utils;

import com.pagepal.capstone.services.WebhookService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    private final WebhookService webhookService;

    private final DateUtils dateUtils;

    public void scheduleTaskWithFixedRate() {
    }

//    @Scheduled(fixedDelay = 600000)
//    public void scheduleTaskWithFixedDelay() {
//        Map<String, String> content = new HashMap<>();
//        content.put("Content", "Schedule task executed");
//        content.put("Task executed at", dateUtils.getCurrentVietnamDate().toString());
//        content.put("Number of booking updated", "Có đâu mà update");
//        content.put("Important message", "Trôn trôn Việt Nam");
//        webhookService.sendWebhookWithDataSchedule("Task with fixed delay", content, Boolean.FALSE);
//    }

    public void scheduleTaskWithInitialDelay() {
    }

    public void scheduleTaskWithCronExpression() {
    }
}