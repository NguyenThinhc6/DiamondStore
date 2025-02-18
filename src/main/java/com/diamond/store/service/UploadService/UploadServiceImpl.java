package com.diamond.store.service.UploadService;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.diamond.store.dto.request.FileRequest;
import com.diamond.store.dto.response.FileResponse;
import com.diamond.store.exception.InternalServerErrorException;
import com.diamond.store.util.ApplicationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadServiceImpl implements UploadService {

    private final Cloudinary cloudinary;

    @Override
    public FileResponse uploadAvatar(FileRequest fileRequest) {

        return uploadFile(fileRequest.getFile(), fileRequest.getOwnerId(), fileRequest.getTags());

    }

    @Override
    public List<FileResponse> uploadProductImages(FileRequest fileRequest) {
        List<CompletableFuture<FileResponse>> uploadFutures = fileRequest.getFiles().stream()
                .map(file -> CompletableFuture.supplyAsync(() ->
                                uploadFile(file, UUID.randomUUID().toString(), fileRequest.getTags()),
                        Executors.newFixedThreadPool(fileRequest.getFiles().size())
                ))
                .toList();

        CompletableFuture.allOf(uploadFutures.toArray(new CompletableFuture[0])).join();

        return uploadFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteFile(String fileId) {
        try {
            if (isPublicIdExists(fileId)) {
                cloudinary.uploader().destroy(fileId, ObjectUtils.emptyMap());
            }

        } catch (IOException e) {
            throw new InternalServerErrorException(ApplicationMessage.DELETE_FILE_FAILED);
        }
    }


    private FileResponse uploadFile(MultipartFile file, String publicId, String[] tags) {

        log.info("Uploading file : {} ", file.getOriginalFilename());

        try {
            Map<String, Object> uploadParams = new HashMap<>();
            uploadParams.put("public_id", publicId);
            uploadParams.put("tags", String.join(",", tags));

            Map uploadResult = upload(file.getBytes(), uploadParams);

            FileResponse fileResponse = new FileResponse();
            fileResponse.setFileId(publicId);
            fileResponse.setUrl(uploadResult.get("url").toString());

            log.info("Upload result : {}", uploadResult);
            return fileResponse;
        } catch (IOException e) {
            throw new InternalServerErrorException(ApplicationMessage.UPLOAD_FAILED);
        }
    }

    private Map upload(byte[] file, Map<String, Object> uploadParams) throws IOException {
        return cloudinary.uploader().upload(file, uploadParams);
    }


    private String generateUniquePublicId(String basePublicId) {
        String uniqueId = basePublicId;
        int count = 1;

        // Lặp cho đến khi tìm được public_id chưa tồn tại
        synchronized (this) {
            while (isPublicIdExists(uniqueId)) {
                uniqueId = basePublicId + "_" + count;
                count++;
            }
        }


        return uniqueId;
    }

    private boolean isPublicIdExists(String publicId) {
        try {
            cloudinary.api().resource(publicId, ObjectUtils.emptyMap());
            return true;  // Public ID đã tồn tại
        } catch (Exception e) {
            return false;  // Public ID không tồn tại
        }
    }

    public static String toSlug(String input) {
        String normalized = input.toLowerCase();

        // Loại bỏ dấu tiếng Việt
        String withoutAccent = Normalizer.normalize(normalized, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        //  Thay khoảng trắng thành dấu gạch ngang, bỏ ký tự đặc biệt
        String slug = withoutAccent.replaceAll("[^a-z0-9\\s-]", "")  // Bỏ ký tự đặc biệt
                .trim()
                .replaceAll("\\s+", "-");       // Thay khoảng trắng thành dấu '-'

        return slug;
    }

}
