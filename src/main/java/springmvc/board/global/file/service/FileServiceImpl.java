package springmvc.board.global.file.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import springmvc.board.global.file.exception.FileException;
import springmvc.board.global.file.exception.FileExceptionType;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{
    @Value("${file.dir}") //application.yml 파일에 있는 file.dir을 가져옴
    private String fileDir;
    
    //TODO Test Code 작성
    @Override
    public String save(MultipartFile multipartFile) {
        String filePath = fileDir + UUID.randomUUID();

        try {
            multipartFile.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new FileException(FileExceptionType.FILE_CAN_NOT_SAVE);
        }

        return filePath;
    }

    @Override
    public void delete(String filePath) {
        File file = new File(filePath);

        //존재 유무 확인
        if(!file.exists()) return;

        if(!file.delete()) throw new FileException(FileExceptionType.FILE_CAN_NOT_DELETE);
    }
}
