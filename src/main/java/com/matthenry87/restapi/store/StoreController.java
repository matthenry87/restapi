package com.matthenry87.restapi.store;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;
    private final StoreMapper storeMapper;

    public StoreController(StoreService storeService, StoreMapper storeMapper) {
        this.storeService = storeService;
        this.storeMapper = storeMapper;
    }

    @GetMapping
    public ResponseEntity<List<StoreModel>> get() {

        List<StoreEntity> stores = storeService.getStores();

        List<StoreModel> storeModels = stores.stream()
                .map(storeMapper::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(storeModels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreModel> getById(@PathVariable String id) {

        StoreEntity storeEntity = storeService.getStore(id);

        StoreModel storeModel = storeMapper.toModel(storeEntity);

        return ResponseEntity.ok(storeModel);
    }

    @PostMapping
    public ResponseEntity<StoreModel> post(@RequestBody @Valid StoreModel storeModel) {

        StoreEntity storeEntity = storeMapper.toEntity(storeModel);

        storeService.createStore(storeEntity);

        storeModel.setId(storeEntity.getId());
        storeModel.setStatus(Status.OPEN);

        return new ResponseEntity<>(storeModel, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Validated(UpdateStore.class)
    public ResponseEntity<StoreModel> put(@RequestBody @Validated(UpdateStore.class) @Valid StoreModel storeModel,
                                          @PathVariable String id) {

        StoreEntity storeEntity = storeMapper.toEntity(storeModel);

        storeEntity.setId(id);

        storeService.updateStore(storeEntity);

        return ResponseEntity.ok(storeModel);
    }

interface UpdateStore {}

}

