package com.matthenry87.restapi.store;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class StoreModel {

    private Integer id;

    @NotEmpty(groups = {StoreController.UpdateStore.class, StoreController.CreateStore.class})
    private String name;

    @NotEmpty(groups = {StoreController.UpdateStore.class, StoreController.CreateStore.class})
    private String address;

    @NotEmpty(groups = {StoreController.UpdateStore.class, StoreController.CreateStore.class})
    private String phone;

    @NotNull(groups = StoreController.UpdateStore.class)
    private Status status;

}
