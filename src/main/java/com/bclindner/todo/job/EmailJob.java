package com.bclindner.todo.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bclindner.todo.service.EmailService;

/**
 * Scheduled job for sending an e-mail for unfinished tasks.
 */
@Component
@ConditionalOnProperty(name="emailjob.enabled",havingValue="true")
@ConditionalOnBean(EmailService.class)
public class EmailJob {
    
    private Logger logger = LoggerFactory.getLogger(EmailJob.class);
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private Environment env;
    
    
    public EmailJob(@Value("${emailjob.cron}") String cron) {
        logger.info("Cronjob enabled: " + cron);
    }

    /**
     * Send an email at 8AM daily.
     */
    @Scheduled(cron = "${emailjob.cron}")
    public void sendEmail() {
        logger.info("Running EmailJob...");
        // this is sort of inelegant... SB probably has a better way to do this
        String toEmail = env.getProperty("emailjob.to");
        emailService.sendIncompleteTodoItems(toEmail);
        logger.info("EmailJob finished running.");
    }
    
}
