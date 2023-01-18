package com.commonplant.umc.service;

import com.commonplant.umc.config.exception.BadRequestException;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.commonplant.umc.config.exception.ErrorResponseStatus.FILE_SAVE_ERROR;

@Service
public class FirebaseService {
    @Value("${app.firebase-bucket}")
    private String firebaseBucket;

    public String uploadFiles(String fileName, MultipartFile file) {
        try{
            Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
            InputStream content = new ByteArrayInputStream(file.getBytes());
            Blob blob = bucket.create(fileName.toString(), content, file.getContentType());
        }catch (IOException e){
            throw new BadRequestException(FILE_SAVE_ERROR);
        }

        //https://firebasestorage.googleapis.com/v0/b/wave-weave.appspot.com/o/test1?alt=media
        String img_url = "https://firebasestorage.googleapis.com/v0/b/common-plant.appspot.com/o/"
                +fileName+"?alt=media";
        return img_url;
    }
    public void deleteFiles(String fileName){
        Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
        System.out.println(bucket.getGeneratedId());
        try{
            boolean blob = bucket.getStorage().delete(firebaseBucket,fileName);
        }catch (Exception e){
            throw new BadRequestException(FILE_SAVE_ERROR);
        }
    }
}