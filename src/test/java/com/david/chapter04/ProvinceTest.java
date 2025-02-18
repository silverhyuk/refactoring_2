package com.david.chapter04;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProvinceTest {
    private Province asia;

    @BeforeEach
    public void setUp() {
        // 모든 테스트 실행 전 새로운 Fixture(테스트 데이터)를 생성
        asia = SampleData.sampleProvinceData();
    }

    @Test
    public void testShortfall() {
        // 생산 부족분 검증
        assertEquals(5, asia.getShortfall());
    }

    @Test
    public void testProfit() {
        // 총수익 검증
        assertEquals(230, asia.getProfit());
    }

}
