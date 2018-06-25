package com.bank.customerservice.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ApiModel(description="Details description of customer account")
@Document
public class Customer {



    @ApiModelProperty(notes = "Auto generated value")
    @Id
    private String id;

    @Size(min = 2, message = "Fist name must have more than 2 characters")
    @ApiModelProperty(notes = "First name at least have 2 characters")
    private String firstName;

    @NotNull
    @Size(min = 1, message = "Last name must not be empty")
    @ApiModelProperty(notes = "Last name must not be empty")
    private String lastName;


}