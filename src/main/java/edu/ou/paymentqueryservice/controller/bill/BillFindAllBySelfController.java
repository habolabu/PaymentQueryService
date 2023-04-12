package edu.ou.paymentqueryservice.controller.bill;

import edu.ou.coreservice.common.constant.SecurityPermission;
import edu.ou.coreservice.common.util.SecurityUtils;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.service.base.IBaseService;
import edu.ou.paymentqueryservice.common.constant.EndPoint;
import edu.ou.paymentqueryservice.data.pojo.request.bill.BillFindAllByUserIdRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(EndPoint.Bill.BASE)
@RequiredArgsConstructor
public class BillFindAllBySelfController {
    private final IBaseService<IBaseRequest, IBaseResponse> billFindAllByUserIdService;
    private final RabbitTemplate rabbitTemplate;

    /**
     * Find all bill of current user
     *
     * @param page page index
     * @return list bill of current user
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.AUTHENTICATED)
    @GetMapping()
    public ResponseEntity<IBaseResponse> findAllBillBySelf(
            @RequestParam(required = false, defaultValue = "1") Integer page
    ) {
        return new ResponseEntity<>(
                billFindAllByUserIdService.execute(
                        new BillFindAllByUserIdRequest()
                                .setUserId(SecurityUtils.getCurrentAccount(rabbitTemplate).getUserId())
                                .setPage(page)
                ),
                HttpStatus.OK
        );
    }
}
