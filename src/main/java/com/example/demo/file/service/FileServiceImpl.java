package com.example.demo.file.service;


import com.example.demo.file.domain.entity.FileInfo;
import com.example.demo.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class FileServiceImpl implements FileService{

    @Value("${upload.path}")
    private String uploadPath;

    private final FileRepository fileRepository;

    /***
     * 파일 업로드, DB에 파일 정보 저장 영역
     * @param recivedfile
     * @throws IOException
     */
    @Override
    public void fileUpload(MultipartFile recivedfile) throws IOException{
        try {
            // 업로더가 업로드한 file 이름
            String originalFilename = recivedfile.getOriginalFilename();

            // uuid 식별자 붙힌 file 이름
            String saveFileName = createSavedFileName(originalFilename);

            // file이 로컬storage에 저장되는 경로(위치) -> cloud flare로 바꿀 예정
            String savedPath = getFullPath(saveFileName);

            // 파라미터로 java.io.File 객체를 받아 해당 위치에 파일을 저장!!!!!
            recivedfile.transferTo(new File(savedPath));

            // 로컬storage에 저장 된 file의 정보 db에 저장
            recodeFileInfoToDB(originalFilename, saveFileName, savedPath);

        }catch (Exception e){
            throw new IOException("파일 업로드 중 에러가 발생했습니다.");
        }
    }

    /***
     * 로컬storage에 저장 된 file의 정보 db에 저장
     * @param originalFilename
     * @param savedFileName
     * @param savedPath
     */
    private void recodeFileInfoToDB(String originalFilename, String savedFileName, String savedPath){
        FileInfo fileInfo = FileInfo.builder()
                .savedFileName(savedFileName)
                .originalFileName(originalFilename)
                .savedPath(savedPath)
                .build();
        fileRepository.save(fileInfo);
    }

    /***
     * 저장소에 저장된 파일의 경로(위치) -> cloudflare 주소로 변경 예정
     * @param saveFileName
     * @return  파일 저장 경로
     */
    private String getFullPath(String saveFileName) {   // 로컬 storage에 저장된 경로
        return uploadPath + saveFileName;
    }

    /***
     * 업로드한 파일의 확장자(.py, .hwp, .png 등등) 추출 메소드
     * @param originalFilename
     * @return String 확장자
     */
    public String extractExt(String originalFilename){  // 파일 확장자 추출 메소드
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    /***
     * uuid를 이용해 파일 저장 -> 식별자
     * @param originalFilename
     * @return String 저장소에 저장될 파일명
     */
    public String createSavedFileName(String originalFilename) {    // 로컬 storage에 저장되는 savedFileName 생성 메소드
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();     // 식별자
        return uuid + "." + ext;
    }
}
