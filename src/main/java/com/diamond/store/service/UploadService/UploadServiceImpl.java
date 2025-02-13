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
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadServiceImpl implements UploadService {

    private final Cloudinary cloudinary;

    @Override
    public FileResponse uploadAvatar(FileRequest fileRequest) {
        log.info("Uploading file : {} ", fileRequest.getFile().getOriginalFilename() );

        try {


            Map<String, Object> uploadParams = new HashMap<>();
            uploadParams.put("public_id", fileRequest.getOwnerId());
            uploadParams.put("tags", String.join(",", fileRequest.getTags()));

            Map uploadResult =     upload(fileRequest.getFile().getBytes(),uploadParams);
            FileResponse fileResponse = new FileResponse();
            fileResponse.setFileId(fileRequest.getOwnerId());
            fileResponse.setUrl(uploadResult.get("url").toString());

            return fileResponse;
        } catch (IOException e) {
            throw new InternalServerErrorException(ApplicationMessage.UPLOAD_FAILED);
        }

    }


    private Map upload(byte[] file, Map<String, Object>  uploadParams) throws IOException {
        return cloudinary.uploader().upload(file,uploadParams);
    }


/*

    private  String generateUniquePublicId(String basePublicId) {
        String uniqueId = basePublicId;
        int count = 1;

        // Lặp cho đến khi tìm được public_id chưa tồn tại
        synchronized(this){
            while (isPublicIdExists(uniqueId)) {
                uniqueId = basePublicId + "(" + count + ")";
                count++;
            }
        }


        return uniqueId;
    }
*/

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
