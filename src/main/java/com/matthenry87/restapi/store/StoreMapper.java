package com.matthenry87.restapi.store;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StoreMapper {

    StoreEntity toEntity(StoreResource store);

    StoreResource toModel(StoreEntity storeEntity);

}
