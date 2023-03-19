package edu.ou.paymentqueryservice.controller.billStatus;

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
@RequestMapping(EndPoint.BillStatus.BASE)
@RequiredArgsConstructor
public class BillStatusFindAllWithoutPaginationController {
    private final IBaseService<IBaseRequest, IBaseResponse> billStatusFindAllWithoutPaginationService;


    /**
     * Find all bill status
     *
     * @return list bill status
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.AUTHENTICATED)
    @GetMapping(EndPoint.BillStatus.ALL)
    public ResponseEntity<IBaseResponse> findAllBillStatusWithoutPagination() {
        return new ResponseEntity<>(
                billStatusFindAllWithoutPaginationService.execute(null),
                HttpStatus.OK
        );
    }
}
