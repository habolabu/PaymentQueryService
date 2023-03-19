package edu.ou.paymentqueryservice.service.bill;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.util.DateUtils;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import edu.ou.paymentqueryservice.common.constant.CodeStatus;
import edu.ou.paymentqueryservice.data.entity.BillDocument;
import edu.ou.paymentqueryservice.data.pojo.request.bill.BillRevenueStatisticRequest;
import edu.ou.paymentqueryservice.data.pojo.response.revenueStatistic.RevenueStatisticDTO;
import edu.ou.paymentqueryservice.data.pojo.response.revenueStatistic.RevenueSummarizeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summarizingLong;

@Service
@RequiredArgsConstructor
public class BillRevenueStatisticService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<Query, List<BillDocument>> billFindAllRepository;
    private final ValidValidation validValidation;

    /**
     * Validate request
     *
     * @param request request
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(IBaseRequest request) {
        if (validValidation.isInValid(request, BillRevenueStatisticRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "bill revenue statistic"
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
        final BillRevenueStatisticRequest billRevenueStatisticRequest = (BillRevenueStatisticRequest) request;
        final String labelTemplate = "%d%d";

        if(Objects.isNull(billRevenueStatisticRequest.getFromYear())){
            billRevenueStatisticRequest.setFromYear(LocalDate.now().getYear());
        }

        if(Objects.isNull(billRevenueStatisticRequest.getToYear())){
            billRevenueStatisticRequest.setToYear(LocalDate.now().getYear());
        }

        final LocalDate fromTime = LocalDate
                .now()
                .withYear(billRevenueStatisticRequest.getFromYear())
                .withMonth(billRevenueStatisticRequest.getFromMonth())
                .withDayOfMonth(1);

        final LocalDate toTime = LocalDate
                .now()
                .withYear(billRevenueStatisticRequest.getToYear())
                .withMonth(billRevenueStatisticRequest.getToMonth())
                .withDayOfMonth(
                        YearMonth
                                .of(
                                        billRevenueStatisticRequest.getToYear(),
                                        billRevenueStatisticRequest.getToMonth()
                                )
                                .lengthOfMonth()
                );
        final List<RevenueStatisticDTO> revenueStatisticByDay = statisticByDay(fromTime, toTime);
        final Map<String, List<RevenueStatisticDTO>> revenueStatisticDetail = revenueStatisticByDay
                .stream()
                .collect(
                        groupingBy(
                                revenueStatisticDTO ->
                                        String.format(
                                                labelTemplate,
                                                revenueStatisticDTO.getDate().getYear(),
                                                revenueStatisticDTO.getDate().getMonthValue()
                                        )
                        )
                );

        final Map<String, LongSummaryStatistics> revenueStatisticSummarize = revenueStatisticByDay
                .stream()
                .collect(
                        groupingBy(
                                revenueStatisticDTO ->
                                        String.format(
                                                labelTemplate,
                                                revenueStatisticDTO.getDate().getYear(),
                                                revenueStatisticDTO.getDate().getMonthValue()
                                        ),
                                summarizingLong(revenueStatisticDTO -> revenueStatisticDTO.getRevenue().intValue())
                        )
                );

        final Map<String, Object> statisticResult = Map.of(
                "summarize", convertRevenueStatisticSummarize(
                        revenueStatisticSummarize,
                        billRevenueStatisticRequest.getFromYear(),
                        billRevenueStatisticRequest.getToYear()
                ),
                "detail", convertRevenueStatisticDetail(
                        revenueStatisticDetail,
                        billRevenueStatisticRequest.getFromYear(),
                        billRevenueStatisticRequest.getToYear()
                )
        );

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        statisticResult,
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
     * Statistic revenue by day
     *
     * @param fromTime from time
     * @param toTime   to time
     * @return List statistic revenue resul
     * @author Nguyen Trung Kien - OU
     */
    private List<RevenueStatisticDTO> statisticByDay(
            LocalDate fromTime,
            LocalDate toTime
    ) {
        final List<BillDocument> bills = billFindAllRepository.execute(
                new Query().addCriteria(
                        Criteria
                                .where("paidDate")
                                .gte(DateUtils.parse(fromTime.toString()))
                                .lte(DateUtils.parse(toTime.toString()))

                )
        );

        LocalDate fromDay = LocalDate.from(fromTime);
        LocalDate toDay = LocalDate.from(toTime);

        toDay = toDay.plusDays(1);

        final Set<Integer> billIdUsed = new HashSet<>();
        final List<RevenueStatisticDTO> revenueStatisticResult = new ArrayList<>();

        while (fromDay.isBefore(toDay)) {

            BigDecimal revenueTotal = new BigDecimal(0);

            for (final BillDocument bill : bills) {
                final LocalDate paidDate = bill
                        .getPaidDate()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                if (fromDay.equals(paidDate)
                        && !billIdUsed.contains(bill.getOId())) {
                    revenueTotal = revenueTotal.add(new BigDecimal(bill.getTotal()));
                    billIdUsed.add(bill.getOId());
                }
            }

            revenueStatisticResult.add(
                    new RevenueStatisticDTO()
                            .setDate(
                                    LocalDate.of(
                                            fromDay.getYear(),
                                            fromDay.getMonthValue(),
                                            fromDay.getDayOfMonth()
                                    ))
                            .setRevenue(revenueTotal)
            );

            fromDay = fromDay.plusDays(1);
        }
        revenueStatisticResult.sort(Comparator.comparing(RevenueStatisticDTO::getDate));
        return revenueStatisticResult;
    }

    /**
     * Convert revenue statistic summarize
     *
     * @param dataMap  data map
     * @param fromYear from year
     * @param toYear   to year
     * @return statistic summarize data
     * @author Nguyen Trung Kien - OU
     */
    private Map<String, Object> convertRevenueStatisticSummarize(
            Map<String, LongSummaryStatistics> dataMap,
            int fromYear,
            int toYear
    ) {
        final Map<String, Object> revenueStatisticSummarizeMapResult = new HashMap<>();
        while (fromYear <= toYear) {

            final List<Map<String, RevenueSummarizeDTO>> yearRevenueStatisticList = new ArrayList<>();
            final int tempYear = fromYear;

            dataMap
                    .keySet()
                    .stream()
                    .sorted((fKey, sKey) -> {
                        final String fMonth = fKey.substring(4);
                        final String sMonth = sKey.substring(4);
                        return Integer.parseInt(fMonth) - Integer.parseInt(sMonth);
                    })
                    .forEach(key -> {
                        final int year = Integer.parseInt(key.substring(0, 4));
                        if (year == tempYear) {
                            final String month = key.substring(4);
                            final RevenueSummarizeDTO revenueStatisticDto = new RevenueSummarizeDTO()
                                    .setAverage(dataMap.get(key).getAverage())
                                    .setMax(dataMap.get(key).getMax())
                                    .setSum(dataMap.get(key).getSum())
                                    .setCount(dataMap.get(key).getCount())
                                    .setAverage(dataMap.get(key).getAverage());
                            yearRevenueStatisticList.add(Map.of(month, revenueStatisticDto));
                        }
                    });
            revenueStatisticSummarizeMapResult.put(String.valueOf(fromYear), yearRevenueStatisticList);
            fromYear++;
        }
        return revenueStatisticSummarizeMapResult;
    }

    /**
     * Convert revenue statistic detail
     *
     * @param dataMap  data
     * @param fromYear from year
     * @param toYear   to year
     * @return statistic detail
     * @author Nguyen Trung Kien - OU
     */
    private Map<String, Object> convertRevenueStatisticDetail(
            Map<String, List<RevenueStatisticDTO>> dataMap,
            int fromYear,
            int toYear
    ) {
        final Map<String, Object> revenueStatisticDetailMapResult = new HashMap<>();
        while (fromYear <= toYear) {
            final List<Map<String, List<RevenueStatisticDTO>>> yearRevenueStatisticList = new ArrayList<>();
            final int tempYear = fromYear;

            dataMap
                    .keySet()
                    .stream()
                    .sorted((fKey, sKey) -> {
                        final String fMonth = fKey.substring(4);
                        final String sMonth = sKey.substring(4);
                        return Integer.parseInt(fMonth) - Integer.parseInt(sMonth);
                    })
                    .forEach(key -> {
                        final int year = Integer.parseInt(key.substring(0, 4));
                        if (year == tempYear) {
                            final String month = key.substring(4);
                            yearRevenueStatisticList.add(Map.of(month, dataMap.get(key)));
                        }
                    });
            revenueStatisticDetailMapResult.put(String.valueOf(fromYear), yearRevenueStatisticList);
            fromYear++;
        }
        return revenueStatisticDetailMapResult;
    }
}
