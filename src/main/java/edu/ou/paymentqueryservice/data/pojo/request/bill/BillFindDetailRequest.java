package edu.ou.paymentqueryservice.data.pojo.request.bill;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

@Data
public class BillFindDetailRequest implements IBaseRequest {
    private int billId;
}
