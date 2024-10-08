package com. back. simulation_foret. controllers;

import com.back.simulation_foret.SimulationForetApplication;
import com.back.simulation_foret.models.FireUpdate;
import com.back.simulation_foret.models.SimulationConfig;
import com.back.simulation_foret.services.SimulationForetService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "*")
public class SimulationForetController {

    private SseEmitter sseEmitter;

    @PostMapping("/api/fire/start")
    public void startSimulation(@RequestBody SimulationConfig config) throws IOException {


        if (sseEmitter != null) {
            sseEmitter.complete();
        }
        sseEmitter = new SseEmitter();

        new Thread(() -> {
            try {
                SimulationForetService simulation = new SimulationForetService();
                simulation.setConfig(config.getGridHeight(), config.getGridWidth(), config.getPropagationProbability(), config.getFireStartPosition());
                while (!simulation.isSimulationFinished()) {
                    simulation.simulateStep();
                    sendUpdate(simulation);
                    Thread.sleep(500);
                }
                sendUpdate(simulation);
                sseEmitter.complete();
            } catch (Exception e) {
                sseEmitter.completeWithError(e);
            }
        }).start();
    }

    @GetMapping(value = "/api/fire/updates", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getSimulationUpdates() {
        return sseEmitter;
    }

    private void sendUpdate(SimulationForetService simulation) throws IOException {
        if (sseEmitter != null) {
            String[][] forest = simulation.getForest();
            boolean isOver = simulation.isSimulationFinished();
            FireUpdate update = new FireUpdate(forest, isOver);
            sseEmitter.send(update);
        }
    }
}