"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const chart_js_1 = require("chart.js");
window.onload = () => {
    var _a;
    const ctx = (_a = document.getElementById("graficoCompetencias")) === null || _a === void 0 ? void 0 : _a.getContext("2d");
    if (ctx) {
        const myChart = new chart_js_1.Chart(ctx, {
            type: "bar",
            data: {
                labels: ["Java", "Python", "React", "Angular"],
                datasets: [
                    {
                        label: "# de Candidatos",
                        data: [12, 19, 3, 5], // Exemplo de quantidade de candidatos com essas competências
                        backgroundColor: "rgba(255, 99, 132, 0.2)",
                        borderColor: "rgba(255, 99, 132, 1)",
                        borderWidth: 1,
                    },
                ],
            },
            options: {
                scales: {
                    y: {
                        beginAtZero: true,
                    },
                },
            },
        });
    }
    else {
        console.error("Contexto não encontrado");
    }
};
