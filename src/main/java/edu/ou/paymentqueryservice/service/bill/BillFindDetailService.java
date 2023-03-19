package edu.ou.paymentqueryservice.service.bill;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
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
import edu.ou.paymentqueryservice.data.entity.*;
import edu.ou.paymentqueryservice.data.pojo.request.bill.BillFindDetailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BillFindDetailService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<Integer, BillDocument> billFindByIdRepository;
    private final IBaseRepository<Integer, List<ParkingBillDocument>> parkingBillFindByBillIdRepository;
    private final IBaseRepository<Integer, List<RoomBillDocument>> roomBillFindByBillIdRepository;
    private final IBaseRepository<Integer, PaymentTypeDocument> paymentTypeFindByIdRepository;
    private final IBaseRepository<Integer, BillStatusDocument> billStatusFindByIdRepository;
    private final ValidValidation validValidation;
    private final RabbitTemplate rabbitTemplate;

    /**
     * Validate request
     *
     * @param request request of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(IBaseRequest request) {
        if (validValidation.isInValid(request, BillFindDetailRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "bill"
            );
        }
    }

    /**
     * Find bill detail
     *
     * @param input input of task
     * @return bill detail
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest input) {
        final BillFindDetailRequest billFindDetailRequest = (BillFindDetailRequest) input;
        final BillDocument bill = billFindByIdRepository.execute(billFindDetailRequest.getBillId());

        if (Objects.isNull(bill)) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "bill",
                    "bill identity",
                    billFindDetailRequest.getBillId()
            );
        }

        final List<ParkingBillDocument> parkingBills = parkingBillFindByBillIdRepository.execute(bill.getOId());
        final List<RoomBillDocument> roomBills = roomBillFindByBillIdRepository.execute(bill.getOId());
        final BillStatusDocument billStatus = billStatusFindByIdRepository.execute(bill.getBillStatusId());
        final PaymentTypeDocument paymentType = paymentTypeFindByIdRepository.execute(bill.getPaymentTypeId());
        final Object userData = rabbitTemplate.convertSendAndReceive(
                UserFindDetailByIdQueueE.EXCHANGE,
                UserFindDetailByIdQueueE.ROUTING_KEY,
                bill.getUserId()
        );
        final Map<String, Object> userMap = (Map<String, Object>) userData;

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        bill
                                .setParkingBills(parkingBills)
                                .setRoomBills(roomBills)
                                .setBillStatus(billStatus)
                                .setPaymentType(paymentType)
                                .setUser(UserDtoMapper.INSTANCE.fromMap(userMap)),
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
