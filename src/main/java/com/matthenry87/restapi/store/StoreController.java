package com.matthenry87.restapi.store;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;
    private final StoreMapper storeMapper;

    @GetMapping
    public List<StoreModel> get() {

        return storeService.getStores().stream()
                .map(storeMapper::toModel)
                .toList();
    }

    @GetMapping("/{id}")
    public StoreModel getById(@PathVariable Integer id) {

        var storeEntity = storeService.getStore(id);

        return storeMapper.toModel(storeEntity);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StoreModel post(@RequestBody @Validated(CreateStore.class) StoreModel storeModel) {

        var storeEntity = storeMapper.toEntity(storeModel);

        storeService.createStore(storeEntity);

        storeModel.setId(storeEntity.getId());
        storeModel.setStatus(Status.OPEN);

        return storeModel;
    }

    @PutMapping("/{id}")
    public StoreModel put(@RequestBody @Validated(UpdateStore.class) StoreModel storeModel, @PathVariable Integer id) {

        storeModel.setId(id);

        var storeEntity = storeMapper.toEntity(storeModel);

        storeService.updateStore(storeEntity);

        return storeModel;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {

        storeService.deleteStore(id);
    }

interface UpdateStore {}
interface CreateStore {}

}
