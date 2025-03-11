package com.david.chapter08.pipeline.before;

import java.util.ArrayList;
import java.util.List;

public class OfficeAnalyzer {
    
    public List<Office> getIndiaOffices(String[] lines) {
        List<Office> result = new ArrayList<>();
        
        // 반복문을 사용한 데이터 처리
        for (int i = 1; i < lines.length; i++) { // 첫 번째 줄(헤더)은 건너뜀
            String line = lines[i];
            if (line.trim().isEmpty()) continue; // 빈 줄 무시
            
            String[] fields = line.split(",");
            if (fields[1].trim().equals("India")) { // 인도 데이터만 필터링
                result.add(new Office(fields[0].trim(), fields[2].trim()));
            }
        }
        
        return result;
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