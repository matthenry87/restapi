package com.matthenry87.restapi.store;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Getter
@Setter
public class StoreEntity {

    private String id;
    private String name;
    private String address;
    private String phone;
    private Status status;
    @CreatedDate private Instant createdDateTime;

}
