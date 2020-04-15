package com.matthenry87.restapi.store;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends MongoRepository<StoreEntity, String> {

    Optional<StoreEntity> findByName(String name);

}
