package com.n26.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.DoubleSummaryStatistics;
import static com.n26.common.Constants.HALF_ROUND_UP;

/*
* Data Model for returning materialized view of past transactions.
* */

@Getter
@AllArgsConstructor
@ToString( includeFieldNames=true)
public class Statistics {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal sum;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal avg;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal max;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private  BigDecimal min;
    private long count;

    private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(HALF_ROUND_UP, BigDecimal.ROUND_HALF_UP);

    public Statistics() {
        this.sum = ZERO;
        this.avg =  ZERO;
        this.count = 0L;
        this.max =  ZERO;
        this.min =  ZERO;
    }

    private static BigDecimal getScaledDecimal(Double number) {
        return BigDecimal.valueOf(number).setScale(HALF_ROUND_UP, BigDecimal.ROUND_HALF_UP);
    }

    public Statistics(DoubleSummaryStatistics dss) {
        this.setAvg(Double.isInfinite(dss.getAverage()) ? 0.0 : dss.getAverage())
                .setCount(dss.getCount())
                .setMax(Double.isInfinite(dss.getMax()) ? 0.0 : dss.getMax())
                .setMin(Double.isInfinite(dss.getMin()) ? 0.0 : dss.getMin())
                .setSum(Double.isInfinite(dss.getSum()) ? 0.0 : dss.getSum());
    }

    public Statistics setAvg(Double avg) {
        this.avg = getScaledDecimal(avg);
        return this;
    }

    public Statistics setMax(Double max) {
        this.max = getScaledDecimal(max);
        return this;
    }

    public Statistics setMin(Double min) {
        this.min = getScaledDecimal(min);
        return this;
    }

    public Statistics setCount(long count) {
        this.count = count;
        return this;
    }

    public Statistics setSum(Double sum) {
        this.sum = getScaledDecimal(sum);
        return this;
    }
}
