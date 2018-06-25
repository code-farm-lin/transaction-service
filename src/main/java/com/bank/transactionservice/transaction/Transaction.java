package com.bank.transactionservice.transaction;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ApiModel(description="transaction of bank accounts.")
@Document
public class Transaction {
    @Id
    private String id;

    @Indexed
    @NotNull
    @ApiModelProperty(notes="Account number of this transaction")
    private String accountId;
    @ApiModelProperty(notes="Amount of the transaction")
    private Date transactionDate;

    @NotNull
    @ApiModelProperty(notes="Amount of the transaction. Positive is transfer in and negative is transfer out")
    private BigDecimal amount;

    @NotNull
    @Pattern(
            regexp="IN|OUT", flags=Pattern.Flag.CASE_INSENSITIVE, message = "Direction must be either IN or OUT"
    )
    @ApiModelProperty(notes="Direction of transction. IN refers to cash into the account while OUT refers to cash out of account")
    @NotNull
    private String direction;

}
