package br.senac.tads.dsw.exemploemail;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/*
 * Referencias:
 * https://mkyong.com/spring-boot/spring-boot-how-to-send-email-via-smtp/
 * http://dolszewski.com/spring/sending-html-mail-with-spring-boot-and-thymeleaf/
 * https://memorynotfound.com/spring-mail-sending-email-inline-attachment-example/
 */
@Service
public class EmailSender {

    @Value("${app.email.to}")
    private String to;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendEmail() {
        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setTo(to);
        msg.setSubject("Teste Spring Simple E-mail");
        msg.setText("Teste de e-mail enviado pela aplicação Spring Boot");

        javaMailSender.send(msg);
    }

    public void sendEmailWithAttachment() throws MessagingException, IOException {

        MimeMessage msg = javaMailSender.createMimeMessage();

        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(to);

        helper.setSubject("Teste Spring MIME E-mail");

        // default = text/plain
        // helper.setText("Check attachment for image!");
        // true = text/html
        helper.setText("<h1>Check attachment for image!</h1>", true);
        helper.addAttachment("imagem.jpg", new ClassPathResource("jaspion.jpg"));

        javaMailSender.send(msg);
    }

    public String buildContent(String mensagem) {
        Context context = new Context();
        context.setVariable("mensagem", mensagem);
        context.setVariable("dataHora", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return templateEngine.process("mail/template", context);
    }

    public void sendEmailWithInlineAttachment() throws MessagingException, IOException {

        MimeMessage msg = javaMailSender.createMimeMessage();

        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(msg, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        helper.setTo(to);

        helper.setSubject("Teste Spring MIME E-mail com imagem");

        // default = text/plain
        // helper.setText("Check attachment for image!");
        // true = text/html
        String content = buildContent("Teste de e-mail com imagem");
        System.out.println(content);
        helper.setText(content, true);
        helper.addInline("imagem", new ClassPathResource("jaspion.jpg"), "image/jpg");

        javaMailSender.send(msg);
    }

}
