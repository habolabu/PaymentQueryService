package edu.ou.paymentqueryservice.repository.paymentType;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.paymentqueryservice.common.constant.CodeStatus;
import edu.ou.paymentqueryservice.data.entity.PaymentTypeDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentTypeFindByIdRepository extends BaseRepository<Integer, PaymentTypeDocument> {
    private final MongoTemplate mongoTemplate;
    private final ValidValidation validValidation;

    /**
     * Validate payment type id
     *
     * @param paymentTypeId payment type id
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(Integer paymentTypeId) {
        if (validValidation.isInValid(paymentTypeId)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "payment type identity"
            );
        }
    }

    /**
     * Find payment type detail
     *
     * @param paymentTypeId payment type id
     * @return payment type detail
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected PaymentTypeDocument doExecute(Integer paymentTypeId) {
        return mongoTemplate.findOne(
                new Query(
                        Criteria.where("oId")
                                .is(paymentTypeId)
                ),
                PaymentTypeDocument.class
        );
    }

    @Override
    protected void postExecute(Integer input) {
        // do nothing
    }
}
