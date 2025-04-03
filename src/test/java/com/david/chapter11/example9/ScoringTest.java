package com.david.chapter11.example9;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoringTest {
    
    private Scoring scoring;
    private ScoringGuide scoringGuide;
    
    @BeforeEach
    void setUp() {
        scoring = new Scoring();
        scoringGuide = new ScoringGuide();
    }
    
    @Test
    void score_smoker_fromLowCertState() {
        Candidate candidate = new Candidate("홍길동", "WY");
        MedicalExam medicalExam = new MedicalExam(true, 120, 70);
        
        int result = scoring.score(candidate, medicalExam, scoringGuide);
        
        // -5(낮은 인증 주) - 5(흡연자 건강 레벨 10에서 5 초과분) = -10
        assertEquals(-10, result);
    }
    
    @Test
    void score_nonsmoker_fromRegularState() {
        Candidate candidate = new Candidate("김철수", "CA");
        MedicalExam medicalExam = new MedicalExam(false, 110, 65);
        
        int result = scoring.score(candidate, medicalExam, scoringGuide);
        
        // 0(일반 주) - 0(비흡연자 건강 레벨 0에서 5 초과분 없음) = 0
        assertEquals(0, result);
    }
    
    @Test
    void scoreWithCommand_producesSameResultAsFunctionForSmoker() {
        Candidate candidate = new Candidate("홍길동", "WY");
        MedicalExam medicalExam = new MedicalExam(true, 120, 70);
        
        int originalResult = scoring.score(candidate, medicalExam, scoringGuide);
        int commandResult = scoring.scoreWithCommand(candidate, medicalExam, scoringGuide);
        
        // 원본 함수와 명령 객체의 결과가 동일해야 함
        assertEquals(originalResult, commandResult);
    }
    
    @Test
    void scoreWithCommand_producesSameResultAsFunctionForNonsmoker() {
        Candidate candidate = new Candidate("김철수", "CA");
        MedicalExam medicalExam = new MedicalExam(false, 110, 65);
        
        int originalResult = scoring.score(candidate, medicalExam, scoringGuide);
        int commandResult = scoring.scoreWithCommand(candidate, medicalExam, scoringGuide);
        
        // 원본 함수와 명령 객체의 결과가 동일해야 함
        assertEquals(originalResult, commandResult);
    }
} 