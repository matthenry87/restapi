package com.matthenry87.restapi.store;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
class StoreEntity {

    @Id
    private String id;
    private String name;
    private String address;
    private String phone;
    private Status status;

}
