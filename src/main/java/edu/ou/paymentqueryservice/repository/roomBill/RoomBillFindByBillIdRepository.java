package edu.ou.paymentqueryservice.repository.roomBill;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.paymentqueryservice.common.constant.CodeStatus;
import edu.ou.paymentqueryservice.data.entity.RoomBillDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoomBillFindByBillIdRepository extends BaseRepository<Integer, List<RoomBillDocument>> {
    private final MongoTemplate mongoTemplate;
    private final ValidValidation validValidation;

    /**
     * Validate bill id
     *
     * @param billId bill id
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(Integer billId) {
        if (validValidation.isInValid(billId)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "bill identity"
            );
        }
    }

    /**
     * Find room bill detail
     *
     * @param billId bill id
     * @return room bill detail
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected List<RoomBillDocument> doExecute(Integer billId) {
        return mongoTemplate.find(
                new Query(
                        Criteria.where("billId")
                                .is(billId)
                ),
                RoomBillDocument.class
        );
    }

    @Override
    protected void postExecute(Integer input) {
        // do nothing
    }
}
