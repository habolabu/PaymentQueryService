package edu.ou.paymentqueryservice.repository.paymentType;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.paymentqueryservice.common.constant.CodeStatus;
import edu.ou.paymentqueryservice.data.entity.PaymentTypeDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PaymentTypeFindAllRepository extends BaseRepository<Query, List<PaymentTypeDocument>> {
    private final MongoTemplate mongoTemplate;
    private final ValidValidation validValidation;

    /**
     * Validate input
     *
     * @param query input of task
     *              @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(Query query) {
        if (validValidation.isInValid(query)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "parking type find all query"
            );
        }
    }

    /**
     * Find all payment type
     *
     * @param query query
     * @return list of payment type
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected List<PaymentTypeDocument> doExecute(Query query) {
        return mongoTemplate.find(
                query,
                PaymentTypeDocument.class
        );
    }

    @Override
    protected void postExecute(Query input) {
        // do nothing
    }
}
