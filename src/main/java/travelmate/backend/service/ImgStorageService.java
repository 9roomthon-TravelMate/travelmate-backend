package travelmate.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImgStorageService {

    // 현재 작업 디렉토리의 upload-dir 폴더를 절대 경로로 설정
    private final Path fileStorageLocation = Paths.get("img-dir").toAbsolutePath().normalize();

    public void fileStorageService() {
        try {
            // upload-dir 폴더가 존재하지 않으면 생성
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        try {
            fileStorageService();
            String originalName = file.getOriginalFilename();
            String saveName = System.currentTimeMillis() + "_" + originalName;
            // 저장할 파일 경로
            Path targetLocation = this.fileStorageLocation.resolve(saveName);
            // 파일 저장
            Files.copy(file.getInputStream(), targetLocation);
            return saveName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename() + ". Please try again!", ex);
        }
    }

}
