package com.back.simulation_foret.models;

import lombok.Data;

import java.util.List;

@Data
public class SimulationConfig {
    Integer gridWidth;
    Integer gridHeight;
    Double propagationProbability;
    List<Integer[]> fireStartPosition;
}
