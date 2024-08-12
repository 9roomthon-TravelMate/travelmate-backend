package travelmate.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import travelmate.backend.entity.PostImage;

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
            // 지정된 디렉토리 경로가 존재하지 않을 경우에만 디렉토리를 생성
            // 만약 경로가 이미 존재하면 아무 작업도 하지 않고, 예외도 발생시키지 않음
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

    public void deleteFile(PostImage file) {
        Path path = Paths.get(file.getFilePath());

        // 로그를 통해 경로 확인
        System.out.println("Attempting to delete file: " + path.toAbsolutePath());

        try {
            if (Files.exists(path)) {
                Files.delete(path);
                System.out.println("File successfully deleted: " + path.toAbsolutePath());
            } else {
                System.out.println("File not found: " + path.toAbsolutePath());
            }
        } catch (IOException ex) {
            throw new RuntimeException("Could not delete file ", ex);
        }
    }

}
