package com.matthenry87.restapi.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/store")
public class StoreController {

    private final ObjectMapper objectMapper;
    private final StoreService storeService;
    private final StoreMapper storeMapper;
    private final Validator validator;

    public StoreController(ObjectMapper objectMapper, StoreService storeService, StoreMapper storeMapper,
                           @Qualifier("mvcValidator") Validator validator) {
        this.objectMapper = objectMapper;
        this.storeService = storeService;
        this.storeMapper = storeMapper;
        this.validator = validator;
    }

    @GetMapping
    public List<StoreModel> get() {

        return storeService.getStores().stream()
                .map(storeMapper::toModel)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public StoreModel getById(@PathVariable String id) {

        var storeEntity = storeService.getStore(id);

        return storeMapper.toModel(storeEntity);
    }

    @PostMapping
    public ResponseEntity<StoreModel> post(@RequestBody @Validated(CreateStore.class) StoreModel storeModel) {

        var storeEntity = storeMapper.toEntity(storeModel);

        storeService.createStore(storeEntity);

        storeModel.setId(storeEntity.getId());
        storeModel.setStatus(Status.OPEN);

        return new ResponseEntity<>(storeModel, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public StoreModel put(@RequestBody @Validated(UpdateStore.class) StoreModel storeModel, @PathVariable String id) {

        storeModel.setId(id);

        var storeEntity = storeMapper.toEntity(storeModel);

        storeService.updateStore(storeEntity);

        return storeModel;
    }

    @PatchMapping("/{id}")
    public StoreModel patch(@RequestBody Map<String, Object> map, @PathVariable String id, BindingResult bindingResult) {
        // Get entity from DB
        var existingStoreEntity = storeService.getStore(id);

        // Convert to model so we can leverage the validation annotations
        var existingStoreModel = storeMapper.toModel(existingStoreEntity);

        var updatedStoreModel = overlayMapValues(map, existingStoreModel);

        validateModel(bindingResult, updatedStoreModel);

        var storeEntity = storeMapper.toEntity(existingStoreModel);

        storeService.updateStore(storeEntity);

        return updatedStoreModel;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable String id) {

        storeService.deleteStore(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void validateModel(BindingResult bindingResult, StoreModel updatedStoreModel) {
        // Trigger manual validation
        validator.validate(updatedStoreModel, bindingResult);

        if(bindingResult.hasErrors()) {

            // Throw some kind of exception that can be handled in the ExceptionHandler
            //throw new MethodArgumentNotValidException(null, bindingResult);
        }
    }

    private StoreModel overlayMapValues(@RequestBody Map<String, Object> map, StoreModel existingStoreModel) {

        StoreModel updatedStoreModel = null;

        try {

            // TODO Figure out how to just use map instead of converting to json
            var json = objectMapper.writeValueAsString(map);

            // Overlay the map onto existing values from DB
            updatedStoreModel = objectMapper.readerForUpdating(existingStoreModel).readValue(json);

        } catch (JsonProcessingException e) {

            e.printStackTrace();
        }

        return updatedStoreModel;
    }

interface UpdateStore {}
interface CreateStore {}

}
