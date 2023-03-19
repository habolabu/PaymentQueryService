package edu.ou.paymentqueryservice.repository.bill;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.paymentqueryservice.common.constant.CodeStatus;
import edu.ou.paymentqueryservice.data.entity.BillDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BillFindAllRepository extends BaseRepository<Query, List<BillDocument>> {
    private final MongoTemplate mongoTemplate;
    private final ValidValidation validValidation;

    /**
     * Validate input
     *
     * @param query input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(Query query) {
        if (validValidation.isInValid(query)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "bill find all query"
            );
        }
    }

    /**
     * Find all bill
     *
     * @param query query
     * @return list of bill
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected List<BillDocument> doExecute(Query query) {
        return mongoTemplate.find(
                query,
                BillDocument.class
        );
    }

    @Override
    protected void postExecute(Query input) {
        // do nothing
    }

}
