package com.matthenry87.restapi.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface StoreRepository extends JpaRepository<StoreEntity, String> {

    Optional<StoreEntity> findByName(String name);

    Optional<StoreEntity> findByNameAndIdNot(String name, String id);

}
