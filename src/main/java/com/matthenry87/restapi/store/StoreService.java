package com.matthenry87.restapi.store;

import com.matthenry87.exception.AlreadyExistsException;
import com.matthenry87.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.matthenry87.logging.LoggingConstants.EVENT_NAME_KEY;
import static com.matthenry87.restapi.store.Status.OPEN;
import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings({"PlaceholderCountMatchesArgumentCount", "unused", "java:S2201"})
class StoreService {

    private final StoreRepository storeRepository;
    private final RestTemplate restTemplate;

    List<StoreEntity> getStores() {

        return storeRepository.findAll();
    }

    StoreEntity getStore(String id) {

        return storeRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    void createStore(StoreEntity store) {

        restTemplate.getForObject("/facts/590c752b5363e000200d5141", String.class);

        log.info("This is a log statement");

        log.info("This is a log statement with custom fields", keyValue(EVENT_NAME_KEY, "STORE_CREATED"));

        storeRepository.findByName(store.getName())
                .ifPresent(x -> { throw new AlreadyExistsException("store name"); });

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
