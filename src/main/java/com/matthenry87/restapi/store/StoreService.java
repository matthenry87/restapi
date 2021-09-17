package com.matthenry87.restapi.store;

import com.matthenry87.restapi.exception.AlreadyExistsException;
import com.matthenry87.restapi.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.matthenry87.restapi.store.Status.OPEN;

@Service
@RequiredArgsConstructor
class StoreService {

    private final StoreRepository storeRepository;

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

        storeRepository.findByNameAndIdNot(store.getName(), store.getId())
                .ifPresent(x -> { throw new AlreadyExistsException("store with name already exists"); });

        storeRepository.findById(store.getId())
                .orElseThrow(NotFoundException::new);

        storeRepository.save(store);
    }

    void deleteStore(String id) {

        storeRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        storeRepository.deleteById(id);
    }

}
