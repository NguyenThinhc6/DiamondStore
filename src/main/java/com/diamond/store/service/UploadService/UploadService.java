package com.diamond.store.service.UploadService;

import com.diamond.store.dto.request.FileRequest;
import com.diamond.store.dto.response.FileResponse;

public interface UploadService {

    FileResponse uploadAvatar(FileRequest fileRequest);


    void deleteFile(String fileId);

}
