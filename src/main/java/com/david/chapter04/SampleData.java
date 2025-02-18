package com.david.chapter04;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SampleData {

    public static Province sampleProvinceData() {

        Province province = new Province("Asia", 30, 20, new ArrayList<>());
        List<Producer> producers = Arrays.asList(
            new Producer( "Byzantium", 10, 9),
            new Producer( "Attalia", 12, 10),
            new Producer( "Sinope", 10, 6)
        );

        // Producer 객체가 Province를 참조하도록 설정
        for (Producer producer : producers) {
            province.addProducer(producer);
        }

        return province;
    }

    public static void main(String[] args) {
        try {
            Province province = sampleProvinceData();
            System.out.println("부족분: " + province.getShortfall());
            System.out.println("총수익: " + province.getProfit());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

