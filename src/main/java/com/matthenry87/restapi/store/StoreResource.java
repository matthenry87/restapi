package com.matthenry87.restapi.store;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Getter
@Setter
public class StoreResource {

    private String id;

    @NotEmpty(groups = {StoreController.UpdateStore.class, StoreController.CreateStore.class})
    private String name;

    @NotEmpty(groups = {StoreController.UpdateStore.class, StoreController.CreateStore.class})
    private String address;

    @NotEmpty(groups = {StoreController.UpdateStore.class, StoreController.CreateStore.class})
    private String phone;

    @NotNull(groups = StoreController.UpdateStore.class) // Only required on updates
    private Status status;

    private Instant createdDateTime;

}
