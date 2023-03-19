package edu.ou.paymentqueryservice.repository.billStatus;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.paymentqueryservice.common.constant.CodeStatus;
import edu.ou.paymentqueryservice.data.entity.BillStatusDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BillStatusFindByIdRepository extends BaseRepository<Integer, BillStatusDocument> {
    private final MongoTemplate mongoTemplate;
    private final ValidValidation validValidation;

    /**
     * Validate bill status id
     *
     * @param billStatusId bill status id
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(Integer billStatusId) {
        if (validValidation.isInValid(billStatusId)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "bill status identity"
            );
        }
    }

    /**
     * Find bill status detail
     *
     * @param bilLStatusId bill status id
     * @return bill status detail
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected BillStatusDocument doExecute(Integer bilLStatusId) {
        return mongoTemplate.findOne(
                new Query(
                        Criteria.where("oId")
                                .is(bilLStatusId)
                ),
                BillStatusDocument.class
        );
    }

    @Override
    protected void postExecute(Integer input) {
        // do nothing
    }
}
