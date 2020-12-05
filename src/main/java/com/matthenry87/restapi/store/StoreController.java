package com.matthenry87.restapi.store;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;
    private final StoreMapper storeMapper;

    @GetMapping
    public List<StoreResource> getAllStores() {

        return storeService.getStores().stream()
                .map(storeMapper::toModel)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public StoreResource getStoreById(@PathVariable String id) {

        var storeEntity = storeService.getStore(id);

        return storeMapper.toModel(storeEntity);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StoreResource createStore(@RequestBody @Validated(CreateStore.class) StoreResource storeResource) {

        var storeEntity = storeMapper.toEntity(storeResource);

        storeService.createStore(storeEntity);

        return storeMapper.toModel(storeEntity);
    }

    @PutMapping("/{id}")
    public StoreResource updateStore(@RequestBody @Validated(UpdateStore.class) StoreResource storeResource, @PathVariable String id) {

        storeResource.setId(id);

        var storeEntity = storeMapper.toEntity(storeResource);

        storeService.updateStore(storeEntity);

        return storeResource;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteStore(@PathVariable String id) {

        storeService.deleteStore(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

interface UpdateStore {}
interface CreateStore {}

}
