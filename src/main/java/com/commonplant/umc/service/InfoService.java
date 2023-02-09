package com.commonplant.umc.service;

import com.commonplant.umc.domain.Info;
import com.commonplant.umc.dto.info.InfoResponse;
import com.commonplant.umc.repository.WordRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Service
public class InfoService {
    public static final String COLLECTION_NAME = "plant_data";
    private final FirebaseService firebaseService;

    public List<InfoResponse.getSearchList> searchInfo(String name) {
        List<InfoResponse.getSearchList> plantNames = new ArrayList<>();
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference collectionReference = firestore.collection(COLLECTION_NAME);
        Query query = collectionReference;
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        Info info = null;

//        if (name.matches()) { //만약 검색어에 영어가 포함되어 있다면
//
//        } else { //아니라면
//
//        }

        try {
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                String plantName = document.getId();
                if (plantName.contains(name)) {
                    DocumentReference documentReference = collectionReference.document(plantName);
                    ApiFuture<DocumentSnapshot> apiFuture = documentReference.get();
                    DocumentSnapshot documentSnapshot = apiFuture.get();
                    if (documentSnapshot.exists()) {
                        info = documentSnapshot.toObject(Info.class);
                        assert info != null;
                        plantNames.add(new InfoResponse.getSearchList(info));
                    }
                }
            }
        } catch (ExecutionException e) {
            System.out.println("ExecutionException");
        } catch (InterruptedException e) {
            System.out.println("InterruptedException");
        } catch (AssertionError e) {
            System.out.println("AssertionError");
        }

        return plantNames;
    }

    public Info getPlantInfo(String name) {

        Firestore firestore = FirestoreClient.getFirestore();
        Info info = null;

        try {
            DocumentReference documentReference = firestore.collection(COLLECTION_NAME).document(name);
            ApiFuture<DocumentSnapshot> apiFuture = documentReference.get();
            DocumentSnapshot documentSnapshot = apiFuture.get();
            if (documentSnapshot.exists()) {
                info = documentSnapshot.toObject(Info.class);
            }
        } catch (ExecutionException e) {
            System.out.println("ExecutionException");
        } catch (InterruptedException e) {
            System.out.println("InterruptedException");
        }
        return info;
    }

    public Info addPlantInfo(Info info, MultipartFile file) {
        String infoName = "info";
        String imgUrl = null;
        String random = RandomStringUtils.random(6,33,125,true,false);

        String fileName;
        Firestore firestore = FirestoreClient.getFirestore();
        info.setCreated_at(Timestamp.now());
        fileName = info.getName().replaceAll(" ", "_");

        if (file.getSize() > 0) {
            imgUrl = firebaseService.uploadFiles("commonPlant_" +infoName+ random, file);
        }
        info.setImgUrl(imgUrl);
        firestore.collection(COLLECTION_NAME).document(info.getName()).set(info);
        return info;
    }

    public List<InfoResponse.getSearchList> recommendInfo(String name) {
        List<InfoResponse.getSearchList> plantNames = new ArrayList<>();
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference collectionReference = firestore.collection(COLLECTION_NAME);
        Query query = collectionReference.whereArrayContains("tag", name);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        Info info = null;
        try {
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                String plantName = document.getId();
                DocumentReference documentReference = collectionReference.document(plantName);
                ApiFuture<DocumentSnapshot> apiFuture = documentReference.get();
                DocumentSnapshot documentSnapshot = apiFuture.get();
                if (documentSnapshot.exists()) {
                    info = documentSnapshot.toObject(Info.class);
                    assert info != null;
                    plantNames.add(new InfoResponse.getSearchList(info));
                }
            }
        } catch (ExecutionException e) {
            System.out.println("ExecutionException");
        } catch (InterruptedException e) {
            System.out.println("InterruptedException");
        } catch (AssertionError e) {
            System.out.println("AssertionError");
        }

        return plantNames;
    }
}
