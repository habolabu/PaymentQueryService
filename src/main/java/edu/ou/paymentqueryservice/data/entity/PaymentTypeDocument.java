package edu.ou.paymentqueryservice.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document("PaymentType")
@Data
public class PaymentTypeDocument implements Serializable {
    @Id
    @JsonIgnore
    private String id;
    private int oId;
    private String name;
    private String slug;
}
