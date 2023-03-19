package edu.ou.paymentqueryservice.service.bill;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.util.PaginationUtils;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.human.external.user.UserFindDetailByIdQueueE;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import edu.ou.paymentqueryservice.common.constant.CodeStatus;
import edu.ou.paymentqueryservice.common.mapper.UserDtoMapper;
import edu.ou.paymentqueryservice.data.entity.BillDocument;
import edu.ou.paymentqueryservice.data.entity.BillStatusDocument;
import edu.ou.paymentqueryservice.data.entity.PaymentTypeDocument;
import edu.ou.paymentqueryservice.data.pojo.request.bill.BillFindAllRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BillFindAllService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<Query, List<BillDocument>> billFindAllRepository;
    private final IBaseRepository<Query, Integer> billGetPageAmountRepository;
    private final IBaseRepository<Integer, PaymentTypeDocument> paymentTypeFindByIdRepository;
    private final IBaseRepository<Integer, BillStatusDocument> billStatusFindByIdRepository;
    private final ValidValidation validValidation;
    private final RabbitTemplate rabbitTemplate;

    /**
     * Validate request
     *
     * @param request request
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(IBaseRequest request) {
        if (validValidation.isInValid(request, BillFindAllRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "bill"
            );
        }
    }

    /**
     * Find all bill
     *
     * @param request request
     * @return avatar list
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final BillFindAllRequest billFindAllWithParamsRequest = (BillFindAllRequest) request;
        final Query query = this.filter(billFindAllWithParamsRequest);

        final List<BillDocument> bills = billFindAllRepository.execute(query);
        bills.forEach(bill -> {
            bill.setBillStatus(billStatusFindByIdRepository.execute(bill.getBillStatusId()));
            bill.setPaymentType(paymentTypeFindByIdRepository.execute(bill.getPaymentTypeId()));
            final Object userData = rabbitTemplate.convertSendAndReceive(
                    UserFindDetailByIdQueueE.EXCHANGE,
                    UserFindDetailByIdQueueE.ROUTING_KEY,
                    bill.getUserId()
            );

            final Map<String, Object> userMap = (Map<String, Object>) userData;

            bill.setUser(UserDtoMapper.INSTANCE.fromMap(userMap));
        });
        final int pageAmount = billGetPageAmountRepository.execute(query.skip(0).limit(0));

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        Map.of(
                                "data", bills,
                                "totalPage", PaginationUtils.getPageAmount(pageAmount)
                        ),
                        CodeStatus.SUCCESS,
                        Message.Success.SUCCESSFUL
                )
        );
    }

    @Override
    protected void postExecute(IBaseRequest input) {
        // do nothing
    }

    /**
     * Filter response
     *
     * @param request request params
     * @author Nguyen Trung Kien - OU
     */
    private Query filter(BillFindAllRequest request) {
        final Query query = new Query();
        if (Objects.nonNull(request.getUserId())) {
            query.addCriteria(
                    Criteria.where("userId")
                            .is(request.getUserId())
            );
        }

        if (Objects.nonNull(request.getBillStatusId())) {
            query.addCriteria(
                    Criteria.where("billStatusId")
                            .is(request.getBillStatusId())
            );
        }

        if (Objects.nonNull(request.getPaymentTypeId())) {
            query.addCriteria(
                    Criteria.where("paymentTypeId")
                            .is(request.getPaymentTypeId())
            );
        }

        if (Objects.nonNull(request.getBTotal())
                && Objects.nonNull(request.getETotal())) {
            query.addCriteria(
                    Criteria.where("total")
                            .gte(request.getBTotal())
                            .lte(request.getETotal())

            );
        }

        if (Objects.nonNull(request.getBPaidDate())
                && Objects.nonNull(request.getEPaidDate())) {
            query.addCriteria(
                    Criteria.where("paidDate")
                            .gte(new Timestamp(request.getBPaidDate().getTime()))
                            .lte(new Timestamp(request.getEPaidDate().getTime()))

            );
        }

        if (Objects.nonNull(request.getBCreatedAt())
                && Objects.nonNull(request.getECreatedAt())) {
            query.addCriteria(
                    Criteria.where("createdAt")
                            .gte(new Timestamp(request.getBCreatedAt().getTime()))
                            .lte(new Timestamp(request.getECreatedAt().getTime()))

            );
        }

        query.skip(PaginationUtils.getSearchIndex(request.getPage()))
                .limit(PaginationUtils.getPageSize());

        return query.with(Sort.by("createdAt").descending());
    }
}
