package com.david.chapter01;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

/**
 * 청구서 출력
 */
public class Statement {


    public  String statement(Invoice invoice, Map<String, Play> plays) {
        StatementData statementData = new StatementData(invoice, plays);

        return renderPlainText(statementData);
    }

    private String renderPlainText(StatementData data) {
        StringBuilder result = new StringBuilder(String.format("청구내역 (고객명: %s)\n", data.getCustomer()));

        for (Invoice.Performance perf : data.getPerformances()) {
            // 청구 내역 출력
            result.append(String.format("%s: %s %d석\n", data.playFor(perf).getName(), data.usd(data.amountFor(perf)), perf.getAudience()));

        }

        result.append(String.format("총액 %s\n", data.usd(data.totalAmount())));
        result.append(String.format("적립 포인트 %d점\n", data.totalVolumeCredits()));

        return result.toString();
    }


}

