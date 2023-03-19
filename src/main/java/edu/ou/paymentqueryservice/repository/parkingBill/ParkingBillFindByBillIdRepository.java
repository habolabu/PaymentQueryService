package edu.ou.paymentqueryservice.repository.parkingBill;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.paymentqueryservice.common.constant.CodeStatus;
import edu.ou.paymentqueryservice.data.entity.ParkingBillDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ParkingBillFindByBillIdRepository extends BaseRepository<Integer, List<ParkingBillDocument>> {
    private final MongoTemplate mongoTemplate;
    private final ValidValidation validValidation;

    /**
     * Validate parking bill id
     *
     * @param billId parking bill id
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
     * Find bill status detail
     *
     * @param billId bill status id
     * @return bill status detail
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected List<ParkingBillDocument> doExecute(Integer billId) {
        return mongoTemplate.find(
                new Query(
                        Criteria.where("billId")
                                .is(billId)
                ),
                ParkingBillDocument.class
        );
    }

    @Override
    protected void postExecute(Integer input) {
        // do nothing
    }
}
