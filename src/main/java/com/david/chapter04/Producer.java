package com.david.chapter04;

public class Producer {
    private Province province;
    private String name;
    private int cost;
    private int production;


    public Producer(String name, int cost, int production) {
        this.name = name;
        this.cost = cost;
        this.production = production;
    }

    public String getName() { return name; }
    public int getCost() { return cost; }
    public void setCost(int cost) { this.cost = cost; }
    public int getProduction() { return production; }

    public void setProduction(int newProduction) {
        int adjustedProduction = Math.max(0, newProduction); // 음수 방지
        this.province.setTotalProduction(this.province.getTotalProduction() + adjustedProduction - this.production);
        this.production = adjustedProduction;
    }
}

