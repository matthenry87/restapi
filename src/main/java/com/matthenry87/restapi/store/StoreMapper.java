package com.matthenry87.restapi.store;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StoreMapper {

    @Mapping(target = "items", ignore = true)
    StoreEntity toEntity(StoreModel store);

    StoreModel toModel(StoreEntity storeEntity);

}
