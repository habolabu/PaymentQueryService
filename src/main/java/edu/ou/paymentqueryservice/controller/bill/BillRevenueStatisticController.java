package edu.ou.paymentqueryservice.controller.bill;

import edu.ou.coreservice.common.constant.SecurityPermission;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.service.base.IBaseService;
import edu.ou.paymentqueryservice.common.constant.EndPoint;
import edu.ou.paymentqueryservice.data.pojo.request.bill.BillRevenueStatisticRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(EndPoint.Bill.BASE)
@RequiredArgsConstructor
public class BillRevenueStatisticController {
    private final IBaseService<IBaseRequest, IBaseResponse> billRevenueStatisticService;


    /**
     * Statistic bill revenue in time range
     *
     * @param fromMonth from month
     * @param fromYear  from year
     * @param toMonth   to month
     * @param toYear    to year
     * @return statistic result
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.STATISTIC)
    @GetMapping(EndPoint.Bill.STATISTIC)
    public ResponseEntity<IBaseResponse> statisticBillRevenueInTimeRange(
            @RequestParam(required = false, defaultValue = "1") int fromMonth,
            @RequestParam(required = false) Integer fromYear,
            @RequestParam(required = false, defaultValue = "12") int toMonth,
            @RequestParam(required = false) Integer toYear
    ) {
        return new ResponseEntity<>(
                billRevenueStatisticService.execute(
                        new BillRevenueStatisticRequest()
                                .setFromMonth(fromMonth)
                                .setToMonth(toMonth)
                                .setFromYear(fromYear)
                                .setToYear(toYear)
                ),
                HttpStatus.OK
        );
    }
}
