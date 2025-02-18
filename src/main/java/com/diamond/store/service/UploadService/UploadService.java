package com.diamond.store.service.UploadService;

import com.diamond.store.dto.request.FileRequest;
import com.diamond.store.dto.response.FileResponse;

import java.util.List;

public interface UploadService {

    FileResponse uploadAvatar(FileRequest fileRequest);

    List<FileResponse> uploadProductImages(FileRequest fileRequest);

    void deleteFile(String fileId);

}
