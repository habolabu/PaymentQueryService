package edu.ou.paymentqueryservice.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document("ParkingBill")
@Data
public class ParkingBillDocument implements Serializable {
    @Id
    @JsonIgnore
    private String id;
    @JsonProperty("id")
    private int oId;
    private int billId;
    private int dayAmount;
    private int parkingId;
    private double total;
    private int parkingTypeId;
}
