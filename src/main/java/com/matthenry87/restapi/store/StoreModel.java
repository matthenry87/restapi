package com.matthenry87.restapi.store;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class StoreModel {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String address;

    @NotEmpty
    private String phone;

    @NotNull(groups = StoreController.UpdateStore.class)
    private Status status;

}
