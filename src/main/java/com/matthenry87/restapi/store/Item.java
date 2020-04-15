package com.matthenry87.restapi.store;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
class Item {

    @Id
    private String id;
    private String name;
    private double price;

}
