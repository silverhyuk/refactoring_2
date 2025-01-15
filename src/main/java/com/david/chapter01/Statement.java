package com.david.chapter01;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

/**
 * 청구서 출력
 */
public class Statement {


    public  String statement(Invoice invoice, Map<String, Play> plays) {
        return renderPlainText(StatementData.create(invoice, plays));
    }

    private String renderPlainText(StatementData data) {
        StringBuilder result = new StringBuilder(String.format("청구내역 (고객명: %s)\n", data.getCustomer()));

        for (Invoice.Performance perf : data.getPerformances()) {
            // 청구 내역 출력
            result.append(String.format("%s: %s %d석\n", data.playFor(perf).getName(), usd(data.amountFor(perf)), perf.getAudience()));
        }

        result.append(String.format("총액 %s\n", usd(data.totalAmount())));
        result.append(String.format("적립 포인트 %d점\n", data.totalVolumeCredits()));

        return result.toString();
    }

    public String htmlStatement(Invoice invoice, Map<String, Play> plays) {
        return renderHtml(StatementData.create(invoice, plays));
    }

    private String renderHtml(StatementData data) {
        StringBuilder result = new StringBuilder(String.format("<h1>청구내역 (고객명: %s)</h1>\n", data.getCustomer()));
        result.append("<table>\n");
        result.append("<tr><th>연극</th><th>좌석수</th><th>금액</th></tr>\n");
        for (Invoice.Performance perf : data.getPerformances()) {
            result.append(String.format("<tr><td>%s</td><td>%d석</td><td>%s</td></tr>\n", data.playFor(perf).getName(), perf.getAudience(), usd(data.amountFor(perf))));
        }
        result.append("</table>\n");
        result.append(String.format("<p>총액: <em>%s</em></p>\n", usd(data.totalAmount())));
        result.append(String.format("<p>적립 포인트: <em>%d</em>점</p>\n", data.totalVolumeCredits()));
        return result.toString();
    }

    public String usd(double aNumber) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        return formatter.format(aNumber / 100);
    }

}

