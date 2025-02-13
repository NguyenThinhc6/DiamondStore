package com.diamond.store.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileRequest {

    private String ownerId;
    private MultipartFile file;
    private String[] tags;
}
