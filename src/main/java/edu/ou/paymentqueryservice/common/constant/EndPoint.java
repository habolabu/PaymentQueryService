package edu.ou.paymentqueryservice.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EndPoint {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Bill {
        public static final String BASE = "/bill";
        public static final String ALL = "/all";
        public static final String DETAIL = "/{billId}";
        public static final String STATISTIC = "/statistic";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class BillStatus {
        public static final String BASE = "/bill-status";
        public static final String ALL = "/all";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class PaymentType {
        public static final String BASE = "/payment-type";
        public static final String ALL = "/all";
    }

}
