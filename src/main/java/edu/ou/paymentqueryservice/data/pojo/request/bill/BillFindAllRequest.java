package edu.ou.paymentqueryservice.data.pojo.request.bill;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class BillFindAllRequest implements IBaseRequest {
    @NotNull
    @Min(
            value = 1,
            message = "The value must be greater than or equals to 1"
    )
    private int page;
    private Integer userId;
    private Integer billStatusId;
    private Integer paymentTypeId;
    private Integer bTotal;
    private Integer eTotal;
    private Date bPaidDate;
    private Date ePaidDate;
    private Date bCreatedAt;
    private Date eCreatedAt;
}
