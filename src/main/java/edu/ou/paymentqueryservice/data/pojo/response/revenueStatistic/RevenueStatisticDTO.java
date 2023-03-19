package edu.ou.paymentqueryservice.data.pojo.response.revenueStatistic;

import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RevenueStatisticDTO implements IBaseResponse {
    private LocalDate date;
    private BigDecimal revenue;
}
