async function startSimulation() {
    const config = await loadConfig();
    
    const isLocal = document.getElementById("isLocal").checked;
    if (isLocal) {
        let forest = createForest(config.gridHeight, config.gridWidth);

        config.fireStartPosition.forEach(([x, y]) => {
            forest[x][y] = 'burning';
        })

        renderForest(forest);

        let intervalId = setInterval(() => {
            forest = stepSimulation(forest, config.propagationProbability);
            renderForest(forest);

            if(isSimulationFinished(forest)) {
                alert('Simulation terminée !');
                clearInterval(intervalId);
            }
        }, 500);

    } else {
        fetch('http://localhost:9000/api/fire/start', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(config),
        })
        .then(() => {
            listenForUpdates();
        })
        .catch(error => console.error('Error:', error));
    }
}


async function loadConfig() {
    return await fetch('./forest-simulation.config.json').then(r => r.json());
}

function listenForUpdates() {
    const eventSource = new EventSource('http://localhost:9000/api/fire/updates');

    eventSource.onmessage = function(event) {
        const data = JSON.parse(event.data);
        renderForest(data.forest);
        if (data.finished) {
            eventSource.close();
            alert('Simulation terminée !');
        }
    };

    eventSource.onerror = function(error) {
        console.error('SSE error:', error);
        eventSource.close();
    };
}

function createForest(gridHeight, gridWidth) {
    let forest = [];
    for (let i = 0; i < gridHeight; i++) {
        let row = [];
        for (let j = 0; j < gridWidth; j++) {
            row.push("wood");
        }
        forest.push(row);
    }
    return forest;
}

function renderForest(forest) {
    const forestElement = document.getElementById("forest");
    forestElement.style.gridTemplateRows = `repeat(${forest.length}, 20px)`;
    forestElement.style.gridTemplateColumns = `repeat(${forest[0].length}, 20px)`;
    
    forestElement.innerHTML = "";

    forest.forEach((row, i) => {
        row.forEach((cell, j) => {
            let cellElement = document.createElement("div");
            cellElement.classList.add("cell", cell);
            forestElement.appendChild(cellElement);
        });
    });
}


function stepSimulation(forest, propagationProbability) {
    let newForest = forest.map(row => [...row]);

    for (let i = 0; i < forest.length; i++) {
        for (let j = 0; j < forest[i].length; j++) {
            if (forest[i][j] === "burning") {
                newForest[i][j] = "ash";
                let neighbors = getNeighbors(forest, i, j);
                neighbors.forEach(([ni, nj]) => {
                    if (forest[ni][nj] === "wood" && Math.random() < propagationProbability) {
                        newForest[ni][nj] = "burning";
                    }
                });
            }
        }
    }
    return newForest;
}

function getNeighbors(forest, x, y) {
    let neighbors = [];
    if (x > 0) neighbors.push([x - 1, y]);
    if (x < forest.length - 1) neighbors.push([x + 1, y]);
    if (y > 0) neighbors.push([x, y - 1]); 
    if (y < forest[0].length - 1) neighbors.push([x, y + 1]); 
    return neighbors;
}


function isSimulationFinished(forest) {
    return !forest.some(row => row.includes("burning"));
}