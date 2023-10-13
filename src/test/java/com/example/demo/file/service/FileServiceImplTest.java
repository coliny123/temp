package com.example.demo.file.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.FileInputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
class FileServiceImplTest {

    @Autowired private MockMvc mvc;
    @Test
    void fileUpload() throws Exception {
        //Given
        final String fileName = "testImage1"; //파일명
        final String contentType = "png"; //파일타입
        final String filePath = "src/test/resources/testImage/"+fileName+"."+contentType; //파일경로
        FileInputStream fileInputStream = new FileInputStream(filePath);

        //Mock파일생성
        MockMultipartFile image1 = new MockMultipartFile(
                "images", //name
                fileName + "." + contentType, //originalFilename
                contentType,
                fileInputStream
        );
        MockMultipartFile image2 = new MockMultipartFile(
                "images", //name
                fileName + "." + contentType, //originalFilename
                contentType,
                fileInputStream
        );

        //When & Then
        mvc.perform(
                multipart("/api/note")
                        .file(image1).file(image2)
                        .param("title", "제목1")
                        .param("description", "설명설명")
        ).andExpect(status().isOk());
    }
}