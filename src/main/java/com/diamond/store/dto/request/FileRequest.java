package com.diamond.store.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileRequest {

    private String ownerId;
    private MultipartFile file;
    private String[] tags;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<MultipartFile> files;
}
