package com.matthenry87.restapi.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matthenry87.restapi.exception.AlreadyExistsException;
import com.matthenry87.restapi.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.matthenry87.restapi.store.Status.OPEN;

@Service
class StoreService {

    private final ObjectMapper objectMapper;
    private final StoreRepository storeRepository;

    StoreService(ObjectMapper objectMapper, StoreRepository storeRepository) {
        this.objectMapper = objectMapper;
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


    public void patchStore(String id, Map<String, Object> map) {

        var existingStore = storeRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        try {

            // TODO Figure out how to just use map instead of converting to json
            var json = objectMapper.writeValueAsString(map);

            StoreEntity updatedStore = objectMapper.readerForUpdating(existingStore).readValue(json);

            storeRepository.save(updatedStore);

        } catch (JsonProcessingException e) {

            e.printStackTrace();
        }

    }
}
