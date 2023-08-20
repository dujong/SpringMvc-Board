package springmvc.board.global.file.service;

import org.springframework.web.multipart.MultipartFile;
import springmvc.board.global.file.exception.FileException;

public interface FileService {

    //저장된 파일 경로 반환
    String save(MultipartFile multipartFile) throws FileException;

    void delete(String filePath);
}
