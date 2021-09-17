package com.matthenry87.restapi.store;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
@NoArgsConstructor
class StoreEntity {

    @Id private String id;
    private String name;
    private String address;
    private String phone;
    private Status status;

}
