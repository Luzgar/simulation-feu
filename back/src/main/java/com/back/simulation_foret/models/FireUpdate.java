package com.back.simulation_foret.models;

import lombok.Data;

@Data
public class FireUpdate {
    private String[][] forest;
    private boolean finished;

    public FireUpdate(String[][] forest, boolean isOver) {
        this.forest = forest;
        this.finished = isOver;
    }

}
