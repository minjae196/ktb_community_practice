package com.example.demo.dto.image;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUploadRequestDTO {

    private MultipartFile file;

    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    public boolean isFileSizeValid(){
        return file != null && file.getSize() <= MAX_FILE_SIZE;
    }
}
