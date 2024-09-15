package itss.group22.bookexchangeeasy.service.mail;

public class ActivateAccountMail extends Mail {
    public ActivateAccountMail(String userName, String url) {
        super(
                "templates/mail/activate_account_mail.html",
                "Book Exchange Easy - Kích hoạt tài khoản"
        );
        content = content.replaceAll("\\{userName}", userName);
        content = content.replaceAll("\\{url}", url);
    }
}
