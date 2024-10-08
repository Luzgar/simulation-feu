package com.back.simulation_foret.services;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
public class SimulationForetService {
    private String[][] forest;
    private double propagationProbability;
    private int height;
    private int width;

    public void setConfig(Integer height, Integer width, Double propagationProbability, List<Integer[]> fireStartPosition) {
        this.height = height;
        this.width = width;
        this.propagationProbability = propagationProbability;
        forest = new String[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                forest[i][j] = "wood";
            }
        }

        fireStartPosition.forEach((p)-> {
            forest[p[0]][p[1]] = "burning";
        });
    }

    public void simulateStep() {
        String[][] newForest = new String[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                newForest[i][j] = forest[i][j];
            }
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if ("burning".equals(forest[i][j])) {
                    newForest[i][j] = "ash";
                    List<Integer[]> neighbors = getNeighbors(forest, i, j);
                    for(Integer[] nb : neighbors) {
                        Random random = new Random();
                        if ("wood".equals(forest[nb[0]][nb[1]]) && random.nextDouble() < propagationProbability) {
                            newForest[nb[0]][nb[1]] = "burning";
                        }
                    }
                }
            }
        }
        forest = newForest;
    }

    private List<Integer[]> getNeighbors(String[][] forest, Integer x, Integer y) {
        List<Integer[]> n = new ArrayList<>();
        if(x > 0) n.add(new Integer[]{x-1, y});
        if(x < forest.length-1) n.add(new Integer[]{x+1, y});
        if(y > 0) n.add(new Integer[]{x, y-1});
        if(y < forest[0].length-1) n.add(new Integer[]{x, y + 1});
        return n;
    }

    public boolean isSimulationFinished() {
        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width - 1; j++) {
                if ("burning".equals(forest[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }
}

