package com.david.chapter08.pipeline.after;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OfficeAnalyzer {
    
    public List<Office> getIndiaOffices(String[] lines) {
        // 스트림 파이프라인을 사용한 데이터 처리
        return Arrays.stream(lines)
                .skip(1) // 첫 번째 줄(헤더)은 건너뜀
                .filter(line -> !line.trim().isEmpty()) // 빈 줄 무시
                .map(line -> line.split(",")) // 각 줄을 필드 배열로 변환
                .filter(fields -> fields[1].trim().equals("India")) // 인도 데이터만 필터링
                .map(fields -> new Office(fields[0].trim(), fields[2].trim())) // 필요한 데이터 추출
                .collect(Collectors.toList()); // 결과를 리스트로 변환
    }
}

class Office {
    private String city;
    private String phone;
    
    public Office(String city, String phone) {
        this.city = city;
        this.phone = phone;
    }
    
    public String getCity() {
        return city;
    }
    
    public String getPhone() {
        return phone;
    }
    
    @Override
    public String toString() {
        return "Office [city=" + city + ", phone=" + phone + "]";
    }
}