package com.commonplant.umc.service;

import com.commonplant.umc.domain.Info;
import com.commonplant.umc.repository.WordRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Service
public class InfoService{
    public static final String COLLECTION_NAME = "plant_data";

    public ArrayList<String> searchInfo(String name) throws ExecutionException, InterruptedException {
        ArrayList<String> plantNames = new ArrayList<String>();
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference collectionReference = firestore.collection(COLLECTION_NAME);
        //Query query = collectionReference.whereEqualTo("humidi0ty", "70% 이상");
        Query query = collectionReference;
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        for(DocumentSnapshot document : querySnapshot.get().getDocuments()){
            plantNames.add(document.getId());
        }
        return plantNames;
    }
    public Info getPlantInfo(String name) throws ExecutionException, InterruptedException {

        Firestore firestore = FirestoreClient.getFirestore();

        DocumentReference documentReference = firestore.collection(COLLECTION_NAME).document(name);
        ApiFuture<DocumentSnapshot> apiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = apiFuture.get();
        Info info = null;
        if(documentSnapshot.exists()){
            info = documentSnapshot.toObject(Info.class);
            return info;
        }
        else{
            return null;
        }
    }

    public Info addPlantInfo(Info info){
        Firestore firestore = FirestoreClient.getFirestore();
        info.setCreated_at(Timestamp.now());
        firestore.collection(COLLECTION_NAME).document(info.getName()).set(info);
        return info;
    }
}