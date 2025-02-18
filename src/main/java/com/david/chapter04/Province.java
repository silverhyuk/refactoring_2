package com.david.chapter04;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Province {
    private String name;
    private List<Producer> producers;
    private int totalProduction;
    private int demand;
    private int price;

    public Province(String name, int demand, int price, List<Producer> producers) {
        this.name = name;
        this.demand = demand;
        this.price = price;
        this.producers = new ArrayList<>();
        this.totalProduction = 0;
        for (Producer p : producers) {
            addProducer(p);
        }
    }

    public void addProducer(Producer producer) {
        this.producers.add(producer);
        this.totalProduction += producer.getProduction();
    }

    public int getShortfall() {
        return this.demand - this.totalProduction;
    }

    public int getProfit() {
        return getDemandValue() - getDemandCost();
    }

    private int getDemandValue() {
        return getSatisfiedDemand() * this.price;
    }

    private int getSatisfiedDemand() {
        return Math.min(this.demand, this.totalProduction);
    }

    private int getDemandCost() {
        int remainingDemand = this.demand;
        int result = 0;
        this.producers.sort(Comparator.comparingInt(Producer::getCost));

        for (Producer p : this.producers) {
            int contribution = Math.min(remainingDemand, p.getProduction());
            remainingDemand -= contribution;
            result += contribution * p.getCost();
        }
        return result;
    }

    // Getters and Setters
    public String getName() { return name; }
    public List<Producer> getProducers() { return new ArrayList<>(producers); }
    public int getTotalProduction() { return totalProduction; }
    public void setTotalProduction(int totalProduction) { this.totalProduction = totalProduction; }
    public int getDemand() { return demand; }
    public void setDemand(int demand) { this.demand = demand; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
}

