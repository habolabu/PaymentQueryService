package edu.ou.paymentqueryservice.controller.bill;

import edu.ou.coreservice.common.constant.SecurityPermission;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.service.base.IBaseService;
import edu.ou.paymentqueryservice.common.constant.EndPoint;
import edu.ou.paymentqueryservice.data.pojo.request.bill.BillFindAllRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(EndPoint.Bill.BASE)
@RequiredArgsConstructor
public class BillFindAllController {
    private final IBaseService<IBaseRequest, IBaseResponse> billFindAllService;


    /**
     * Find all bill
     *
     * @param page          page index
     * @param userId        user id
     * @param billStatusId  bill status id
     * @param paymentTypeId payment type id
     * @param bTotal        begin total
     * @param eTotal        end total
     * @param bPaidDate     begin paid date
     * @param ePaidDate     end paid date
     * @param bCreatedAt    begin created at
     * @param eCreatedAt    end created at
     * @return list of bill
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.VIEW_BILL)
    @GetMapping(EndPoint.Bill.ALL)
    public ResponseEntity<IBaseResponse> findAllBill(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer billStatusId,
            @RequestParam(required = false) Integer paymentTypeId,
            @RequestParam(required = false) Double bTotal,
            @RequestParam(required = false) Double eTotal,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            Date bPaidDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @RequestParam(required = false)
            Date ePaidDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            Date bCreatedAt,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @RequestParam(required = false)
            Date eCreatedAt
    ) {
        return new ResponseEntity<>(
                billFindAllService.execute(
                        new BillFindAllRequest()
                                .setPage(page)
                                .setUserId(userId)
                                .setBillStatusId(billStatusId)
                                .setPaymentTypeId(paymentTypeId)
                                .setBTotal(bTotal)
                                .setETotal(eTotal)
                                .setBPaidDate(bPaidDate)
                                .setEPaidDate(ePaidDate)
                                .setBCreatedAt(bCreatedAt)
                                .setECreatedAt(eCreatedAt)
                ),
                HttpStatus.OK
        );
    }
}
