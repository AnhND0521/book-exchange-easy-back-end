package itss.group22.bookexchangeeasy.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface CloudinaryService {
    Map uploadFile(MultipartFile multipartFile) throws IOException;
}
