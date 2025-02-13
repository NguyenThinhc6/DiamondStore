package com.diamond.store.service.UploadService;

import com.diamond.store.dto.request.FileRequest;
import com.diamond.store.dto.response.FileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

        FileResponse uploadAvatar(FileRequest fileRequest);
}
