package edu.ou.paymentqueryservice.data.pojo.request.bill;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class BillRevenueStatisticRequest implements IBaseRequest {
    @Min(
            value = 1,
            message = "Value must be greater than or equals to 1"
    )
    @Max(
            value = 12,
            message = "Value must be less than or equals to 12"
    )
    private int fromMonth;

    private Integer fromYear;

    @Min(
            value = 1,
            message = "Value must be greater than or equals to 1"
    )
    @Max(
            value = 12,
            message = "Value must be less than or equals to 12"
    )
    private int toMonth;

    private Integer toYear;
}
