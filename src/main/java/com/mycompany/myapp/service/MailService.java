package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Customer;
import com.mycompany.myapp.domain.Order;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.exception.NotFoundException;
import com.mycompany.myapp.repository.HospitalRepository;
import com.mycompany.myapp.repository.OrderRepository;
import com.mycompany.myapp.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import tech.jhipster.config.JHipsterProperties;

/**
 * Service for sending emails.
 * <p>
 * We use the {@link Async} annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";

    private final JHipsterProperties jHipsterProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    private final UserRepository userRepository;

    private final OrderRepository orderRepository;
    private final HospitalRepository hospitalRepository;

    public MailService(
        JHipsterProperties jHipsterProperties,
        JavaMailSender javaMailSender,
        MessageSource messageSource,
        SpringTemplateEngine templateEngine,
        UserRepository userRepository,
        OrderRepository orderRepository,
        HospitalRepository hospitalRepository
    ) {
        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.hospitalRepository = hospitalRepository;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug(
            "Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart,
            isHtml,
            to,
            subject,
            content
        );

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (MailException | MessagingException e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }

    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        if (user.getEmail() == null) {
            log.debug("Email doesn't exist for user '{}'", user.getLogin());
            return;
        }
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendEmailOrder(User user, String templateName, String titleKey, Order order) {
        if (user.getEmail() == null) {
            log.debug("Email doesn't exist for user '{}'", user.getLogin());
            return;
        }
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable("order", order);
        context.setVariable("customer", order.getCustomer());
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    public void sendMailApproveOrder(Order order) {
        Customer customer = order.getCustomer();
        User user = userRepository.findById(customer.getUserBooking()).orElseThrow(() -> new NotFoundException("User not found"));
        sendEmailOrder(user, "mail/approveOrder", "email.order.title", order);
    }

    public void sendMailRejectOrder(Order order) {
        Customer customer = order.getCustomer();
        User user = userRepository.findById(customer.getUserBooking()).orElseThrow(() -> new NotFoundException("User not found"));
        sendEmailOrder(user, "mail/rejectOrder", "email.order.title", order);
    }

    public void sendMailComplete(Order order) {
        Customer customer = order.getCustomer();
        User user = userRepository.findById(customer.getUserBooking()).orElseThrow(() -> new NotFoundException("User not found"));
        sendEmailOrder(user, "mail/completeOrder", "email.order.title", order);
    }

    public void sendMailCancelOrder(Order order) {
        User user = null;
        if (order.getDoctor() != null) {
            user = userRepository.findById(order.getDoctor().getUserId()).orElseThrow(() -> new NotFoundException("User not found"));
        } else {
            user =
                userRepository
                    .findById(order.getPack().getHospital().getUserId())
                    .orElseThrow(() -> new NotFoundException("User not found"));
        }
        sendEmailOrder(user, "mail/cancelOrder", "email.order.cancel.title", order);
    }

    public void sendMailHaveNewOrder(Order order) {
        User user = null;
        if (order.getDoctor() != null) {
            user = userRepository.findById(order.getDoctor().getUserId()).orElseThrow(() -> new NotFoundException("User not found"));
        } else {
            user =
                userRepository
                    .findById(order.getPack().getHospital().getUserId())
                    .orElseThrow(() -> new NotFoundException("User not found"));
        }
        sendEmailOrder(user, "mail/newOrder", "email.order.new.title", order);
    }

    public void sendMailChangeOrder(Order order) {
        User user = null;
        if (order.getDoctor() != null) {
            user = userRepository.findById(order.getDoctor().getUserId()).orElseThrow(() -> new NotFoundException("User not found"));
        } else {
            user =
                userRepository
                    .findById(order.getPack().getHospital().getUserId())
                    .orElseThrow(() -> new NotFoundException("User not found"));
        }
        sendEmailOrder(user, "mail/changeOrder", "email.order.change.title", order);
    }

    @Async
    public void sendActivationEmail(User user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title");
    }

    @Async
    public void sendCreationEmail(User user) {
        log.debug("Sending creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/creationEmail", "email.activation.title");
    }

    @Async
    public void sendPasswordResetMail(User user) {
        log.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title");
    }
}
