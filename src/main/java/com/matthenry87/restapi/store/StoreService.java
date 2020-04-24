package com.matthenry87.restapi.store;

import com.matthenry87.restapi.exception.AlreadyExistsException;
import com.matthenry87.restapi.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.matthenry87.restapi.store.Status.OPEN;

@Service
class StoreService {

    private final StoreRepository storeRepository;

    StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    List<StoreEntity> getStores() {

        return storeRepository.findAll();
    }

    StoreEntity getStore(String id) {

        return storeRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    void createStore(StoreEntity store) {

        storeRepository.findByName(store.getName())
                .ifPresent(x -> { throw new AlreadyExistsException(); });

        store.setStatus(OPEN);

        storeRepository.save(store);
    }

    void updateStore(StoreEntity store) {

        StoreEntity storeEntity = storeRepository.findById(store.getId())
                .orElseThrow(NotFoundException::new);

        store.setItems(storeEntity.getItems());

        storeRepository.save(store);
    }

    void deleteStore(String id) {

        storeRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        storeRepository.deleteById(id);
    }

}
