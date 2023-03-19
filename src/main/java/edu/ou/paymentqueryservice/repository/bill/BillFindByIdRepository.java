package edu.ou.paymentqueryservice.repository.bill;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.paymentqueryservice.common.constant.CodeStatus;
import edu.ou.paymentqueryservice.data.entity.BillDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BillFindByIdRepository extends BaseRepository<Integer, BillDocument> {
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
     * Find bill detail
     *
     * @param billId bill id
     * @return bill detail
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected BillDocument doExecute(Integer billId) {
        return mongoTemplate.findOne(
                new Query(
                        Criteria.where("oId")
                                .is(billId)
                ),
                BillDocument.class
        );
    }

    @Override
    protected void postExecute(Integer input) {
        // do nothing
    }
}
