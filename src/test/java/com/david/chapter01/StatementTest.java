package com.david.chapter01;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StatementTest {
    @Test
    void testStatement() {
        Invoice.Performance performance1 = new Invoice.Performance("hamlet", 55);
        Invoice.Performance performance2 = new Invoice.Performance("as-like", 35);
        Invoice.Performance performance3 = new Invoice.Performance("othello", 40);


        Invoice invoice = new Invoice();
        invoice.setCustomer("BigCo");
        invoice.setPerformances(List.of(performance1, performance2, performance3));

        Play play1 = new Play();
        play1.setName("Hamlet");
        play1.setType("tragedy");

        Play play2 = new Play();
        play2.setName("As You Like It");
        play2.setType("comedy");

        Play play3 = new Play();
        play3.setName("Othello");
        play3.setType("tragedy");

        Map<String, Play> plays = new HashMap<>();
        plays.put("hamlet", play1);
        plays.put("as-like", play2);
        plays.put("othello", play3);

        String result = new Statement().statement(invoice, plays);

        // 예상 결과를 설정합니다.
        String expected = "청구내역 (고객명: BigCo)\n" +
                "Hamlet: $650.00 55석\n" +
                "As You Like It: $580.00 35석\n" +
                "Othello: $500.00 40석\n" +
                "총액 $1,730.00\n" +
                "적립 포인트 47점\n";

        // 결과를 검증합니다.
        assertEquals(expected, result);
    }

    @Test
    void testHtmlStatement() {
        Invoice.Performance performance1 = new Invoice.Performance("hamlet", 55);
        Invoice.Performance performance2 = new Invoice.Performance("as-like", 35);
        Invoice.Performance performance3 = new Invoice.Performance("othello", 40);

        Invoice invoice = new Invoice();
        invoice.setCustomer("BigCo");
        invoice.setPerformances(List.of(performance1, performance2, performance3));

        Play play1 = new Play();
        play1.setName("Hamlet");
        play1.setType("tragedy");

        Play play2 = new Play();
        play2.setName("As You Like It");
        play2.setType("comedy");

        Play play3 = new Play();
        play3.setName("Othello");
        play3.setType("tragedy");

        Map<String, Play> plays = new HashMap<>();
        plays.put("hamlet", play1);
        plays.put("as-like", play2);
        plays.put("othello", play3);

        String result = new Statement().htmlStatement(invoice, plays);

        // 예상 결과를 설정합니다.
        String expected = "<h1>청구내역 (고객명: BigCo)</h1>\n" +
            "<table>\n" +
            "<tr><th>연극</th><th>좌석수</th><th>금액</th></tr>\n" +
            "<tr><td>Hamlet</td><td>55석</td><td>$650.00</td></tr>\n" +
            "<tr><td>As You Like It</td><td>35석</td><td>$580.00</td></tr>\n" +
            "<tr><td>Othello</td><td>40석</td><td>$500.00</td></tr>\n" +
            "</table>\n" +
            "<p>총액: <em>$1,730.00</em></p>\n" +
            "<p>적립 포인트: <em>47</em>점</p>\n";

        // 결과를 검증합니다.
        assertEquals(expected, result);
    }
}