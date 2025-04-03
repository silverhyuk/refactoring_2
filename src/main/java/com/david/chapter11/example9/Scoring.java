package com.david.chapter11.example9;

/**
 * 예제 9: 함수를 명령으로 바꾸기 (Replace Function with Command)
 */
public class Scoring {
    
    /**
     * 리팩토링 전: 복잡한 로직이 하나의 함수에 포함
     */
    public int score(Candidate candidate, MedicalExam medicalExam, ScoringGuide scoringGuide) {
        int result = 0;
        int healthLevel = 0;
        boolean highMedicalRiskFlag = false;
        
        if (medicalExam.isSmoker()) {
            healthLevel += 10;
            highMedicalRiskFlag = true;
        }
        
        String certificationGrade = "regular";
        if (scoringGuide.stateWithLowCertification(candidate.getOriginState())) {
            certificationGrade = "low";
            result -= 5;
        }
        
        // 추가 계산 로직
        result -= Math.max(healthLevel - 5, 0);
        return result;
    }
    
    /**
     * 리팩토링 후: 명령 객체를 생성하여 호출
     */
    public int scoreWithCommand(Candidate candidate, MedicalExam medicalExam, ScoringGuide scoringGuide) {
        return new Scorer(candidate, medicalExam, scoringGuide).execute();
    }
}

/**
 * Command 객체
 */
class Scorer {
    private Candidate candidate;
    private MedicalExam medicalExam;
    private ScoringGuide scoringGuide;
    
    // 내부 상태
    private int result;
    private int healthLevel;
    private boolean highMedicalRiskFlag;
    private String certificationGrade;
    
    public Scorer(Candidate candidate, MedicalExam medicalExam, ScoringGuide scoringGuide) {
        this.candidate = candidate;
        this.medicalExam = medicalExam;
        this.scoringGuide = scoringGuide;
    }
    
    public int execute() {
        // 초기화
        this.result = 0;
        this.healthLevel = 0;
        this.highMedicalRiskFlag = false;
        
        // 로직 분리
        scoreSmoking();
        scoreCertification();
        scoreHealth();
        
        return this.result;
    }
    
    private void scoreSmoking() {
        if (medicalExam.isSmoker()) {
            this.healthLevel += 10;
            this.highMedicalRiskFlag = true;
        }
    }
    
    private void scoreCertification() {
        this.certificationGrade = "regular";
        if (scoringGuide.stateWithLowCertification(candidate.getOriginState())) {
            this.certificationGrade = "low";
            this.result -= 5;
        }
    }
    
    private void scoreHealth() {
        this.result -= Math.max(this.healthLevel - 5, 0);
    }
} 