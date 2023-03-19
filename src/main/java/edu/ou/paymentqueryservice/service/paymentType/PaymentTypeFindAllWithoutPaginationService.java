package edu.ou.paymentqueryservice.service.paymentType;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import edu.ou.paymentqueryservice.common.constant.CodeStatus;
import edu.ou.paymentqueryservice.data.entity.PaymentTypeDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentTypeFindAllWithoutPaginationService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<Query, List<PaymentTypeDocument>> paymentTypeFindAllRepository;

    @Override
    protected void preExecute(IBaseRequest request) {
        // do nothing
    }

    /**
     * Find all bill status
     *
     * @param request request
     * @return bill status
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        return new SuccessResponse<>(
                new SuccessPojo<>(
                        paymentTypeFindAllRepository.execute(new Query()),
                        CodeStatus.SUCCESS,
                        Message.Success.SUCCESSFUL
                )
        );
    }

    @Override
    protected void postExecute(IBaseRequest input) {
        // do nothing
    }

}
