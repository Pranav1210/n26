package com.n26.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.DoubleSummaryStatistics;

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

    public Statistics() {
        this.sum = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
        this.avg = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
        this.count = 0L;
        this.max = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
        this.min = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public Statistics(DoubleSummaryStatistics dss) {
        this.setAvg(Double.isInfinite(dss.getAverage()) ? 0.0 : dss.getAverage())
                .setCount(dss.getCount())
                .setMax(Double.isInfinite(dss.getMax()) ? 0 : dss.getMax())
                .setMin(Double.isInfinite(dss.getMin()) ? 0 : dss.getMin())
                .setSum(Double.isInfinite(dss.getSum()) ? 0 : dss.getSum());
    }

    public Statistics setAvg(double avg) {
        this.avg = BigDecimal.valueOf(avg).setScale(2, BigDecimal.ROUND_HALF_UP);
        return this;
    }

    public Statistics setMax(double max) {
        this.max = BigDecimal.valueOf(max).setScale(2, BigDecimal.ROUND_HALF_UP);
        return this;
    }

    public Statistics setMin(double min) {
        this.min = BigDecimal.valueOf(min).setScale(2, BigDecimal.ROUND_HALF_UP);
        return this;
    }

    public Statistics setCount(long count) {
        this.count = count;
        return this;
    }

    public Statistics setSum(double sum) {
        this.sum = BigDecimal.valueOf(sum).setScale(2, BigDecimal.ROUND_HALF_UP);
        return this;
    }
}
