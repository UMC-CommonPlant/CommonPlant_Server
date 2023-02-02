package com.commonplant.umc.service;

import com.commonplant.umc.domain.Info;
import com.commonplant.umc.repository.WordRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Service
public class InfoService {
    public static final String COLLECTION_NAME = "plant_data";
    private final FirebaseService firebaseService;

    public ArrayList<String> searchInfo(String name) throws ExecutionException, InterruptedException {
        ArrayList<String> plantNames = new ArrayList<String>();
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference collectionReference = firestore.collection(COLLECTION_NAME);
        Query query = collectionReference;
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            String plantName = document.getId();
            if (plantName.contains(name)) {
                plantNames.add(plantName);
            }
        }
        return plantNames;
    }

    public Info getPlantInfo(String name) throws ExecutionException, InterruptedException {

        Firestore firestore = FirestoreClient.getFirestore();

        DocumentReference documentReference = firestore.collection(COLLECTION_NAME).document(name);
        ApiFuture<DocumentSnapshot> apiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = apiFuture.get();
        Info info = null;
        if (documentSnapshot.exists()) {
            info = documentSnapshot.toObject(Info.class);
            return info;
        } else {
            return null;
        }
    }

    public Info addPlantInfo(Info info, MultipartFile file) {
        String imgUrl = null;
        Firestore firestore = FirestoreClient.getFirestore();
        info.setCreated_at(Timestamp.now());

        if (file.getSize() > 0) {
            imgUrl = firebaseService.uploadFiles("commonPlant_" + info.getName(), file);
        }
        info.setImgUrl(imgUrl);
        firestore.collection(COLLECTION_NAME).document(info.getName()).set(info);
        return info;
    }
}
