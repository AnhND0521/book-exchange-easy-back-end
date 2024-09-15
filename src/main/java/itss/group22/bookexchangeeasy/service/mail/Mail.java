package itss.group22.bookexchangeeasy.service.mail;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public abstract class Mail {
    private final String templateFilePath;
    protected String subject;
    protected String content;

    protected Mail(String templateFilePath, String subject) {
        this.templateFilePath = templateFilePath;
        this.subject = subject;
        this.content = readTemplate();
        this.content = this.content.replaceAll("\\{subject}", subject);
    }

    protected String readTemplate() {
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(templateFilePath);

            if (inputStream == null) {
                throw new FileNotFoundException(templateFilePath);
            } else {
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
                reader.close();
                return sb.toString();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }
}
