package itss.group22.bookexchangeeasy.service;

import itss.group22.bookexchangeeasy.service.mail.Mail;

public interface MailService {
    void sendMail(String to, Mail mail);
}
