package edu.ou.paymentqueryservice.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.ou.paymentqueryservice.data.pojo.response.user.UserDTO;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Document("Bill")
@Data
public class BillDocument implements Serializable {
    @Id
    @JsonIgnore
    private String id;
    @JsonProperty("id")
    private int oId;
    @JsonIgnore
    private int billStatusId;
    private Date paidDate;
    @JsonIgnore
    private int paymentTypeId;
    private double total;
    @JsonIgnore
    private int userId;
    private Date createdAt;

    @Transient
    private PaymentTypeDocument paymentType;
    @Transient
    private BillStatusDocument billStatus;
    @Transient
    private List<ParkingBillDocument> parkingBills;
    @Transient
    private List<RoomBillDocument> roomBills;
    @Transient
    private UserDTO user;
}
