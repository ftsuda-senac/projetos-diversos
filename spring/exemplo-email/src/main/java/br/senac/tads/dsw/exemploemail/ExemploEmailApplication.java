package br.senac.tads.dsw.exemploemail;

import java.io.IOException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExemploEmailApplication implements CommandLineRunner {

	@Autowired
	private EmailSender emailSender;

	public static void main(String[] args) {
		SpringApplication.run(ExemploEmailApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Sending Email...");

		try {
			//emailSender.sendEmail();
			//emailSender.sendEmailWithAttachment();
			emailSender.sendEmailWithInlineAttachment();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Done");

	}

}
