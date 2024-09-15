package itss.group22.bookexchangeeasy.service.mail;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ResetPasswordMail extends Mail {

    public ResetPasswordMail(String userName, LocalDateTime requestTime, String url) {
        super(
                "templates/mail/reset_password_mail.html",
                "Book Exchange Easy - Đặt lại mật khẩu"
        );
        content = content.replaceAll("\\{userName}", userName);
        content = content.replaceAll("\\{requestTime}", requestTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        content = content.replaceAll("\\{requestDate}", requestTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        content = content.replaceAll("\\{url}", url);
    }
}
