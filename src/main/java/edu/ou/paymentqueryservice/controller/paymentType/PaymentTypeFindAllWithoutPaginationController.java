package edu.ou.paymentqueryservice.controller.paymentType;

import edu.ou.coreservice.common.constant.SecurityPermission;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.service.base.IBaseService;
import edu.ou.paymentqueryservice.common.constant.EndPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(EndPoint.PaymentType.BASE)
@RequiredArgsConstructor
public class PaymentTypeFindAllWithoutPaginationController {
    private final IBaseService<IBaseRequest, IBaseResponse> paymentTypeFindAllWithoutPaginationService;


    /**
     * Find all payment type
     *
     * @return list payment type
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.AUTHENTICATED)
    @GetMapping(EndPoint.BillStatus.ALL)
    public ResponseEntity<IBaseResponse> findAllPaymentTypeWithoutPagination() {
        return new ResponseEntity<>(
                paymentTypeFindAllWithoutPaginationService.execute(null),
                HttpStatus.OK
        );
    }
}
