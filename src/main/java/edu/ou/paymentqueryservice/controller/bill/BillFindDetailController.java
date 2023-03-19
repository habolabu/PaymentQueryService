package edu.ou.paymentqueryservice.controller.bill;

import edu.ou.coreservice.common.constant.SecurityPermission;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.service.base.IBaseService;
import edu.ou.paymentqueryservice.common.constant.EndPoint;
import edu.ou.paymentqueryservice.data.pojo.request.bill.BillFindDetailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(EndPoint.Bill.BASE)
@RequiredArgsConstructor
public class BillFindDetailController {

    private final IBaseService<IBaseRequest, IBaseResponse> billFindDetailService;

    /**
     * find detail of exist bill
     *
     * @param billId bill id
     * @return detail of exist bill
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.AUTHENTICATED)
    @GetMapping(EndPoint.Bill.DETAIL)
    public ResponseEntity<IBaseResponse> findBillDetail(
            @PathVariable int billId
    ) {
        return new ResponseEntity<>(
                billFindDetailService.execute(new BillFindDetailRequest().setBillId(billId)),
                HttpStatus.OK
        );
    }
}
