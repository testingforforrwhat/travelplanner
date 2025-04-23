//
//package com.test.travelplanner.storage;
//
//
//import com.google.cloud.storage.Acl;
//import com.google.cloud.storage.BlobInfo;
//import com.google.cloud.storage.Storage;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.UUID;
//
//
//@Service
//public class ImageStorageService {
//
//
//    private final String bucketName;
//    private final Storage storage;
//
//
//    public ImageStorageService(@Value("${travelplanner.gcs.bucket}") String bucketName, Storage storage) {
//        this.bucketName = bucketName;
//        this.storage = storage;
//    }
//
//
//    public String upload(MultipartFile file) {
//        String filename = UUID.randomUUID().toString();
//        BlobInfo blobInfo;
//        try {
//            blobInfo = storage.createFrom(
//                    BlobInfo
//                            .newBuilder(bucketName, filename)
//                            .setContentType("image/jpeg")
//                            .setAcl(List.of(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER)))
//                            .build(),
//                    file.getInputStream());
//        } catch (IOException exception) {
//            throw new IllegalStateException("Failed to upload file to GCS");
//        }
//        return blobInfo.getMediaLink();
//    }
//}
