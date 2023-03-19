package edu.ou.paymentqueryservice.data.pojo.response.revenueStatistic;


import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import lombok.Data;

@Data
public class RevenueSummarizeDTO implements IBaseResponse {
    private long count;
    private long sum;
    private long min;
    private long max;
    private double average;
}
