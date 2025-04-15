import { Chart, ChartItem, registerables } from "chart.js";
import { IChartService } from "../interfaces/IChartService";

Chart.register(...registerables);

export class ChartService implements IChartService {
  createCompetenciasChart(ctx: ChartItem, data: Record<string, number>): void {
    const competenciasOrdenadas = Object.entries(data).sort(
      (a, b) => b[1] - a[1]
    );

    new Chart(ctx, {
      type: "bar",
      data: {
        labels: competenciasOrdenadas.map((c) => c[0]),
        datasets: [
          {
            label: "Candidatos por Competência",
            data: competenciasOrdenadas.map((c) => c[1]),
            backgroundColor: [
              "rgba(255, 99, 132, 0.7)",
              "rgba(54, 162, 235, 0.7)",
              "rgba(255, 206, 86, 0.7)",
              "rgba(75, 192, 192, 0.7)",
              "rgba(153, 102, 255, 0.7)",
              "rgba(255, 159, 64, 0.7)",
            ],
            borderWidth: 1,
          },
        ],
      },
      options: {
        responsive: true,
        scales: {
          y: {
            beginAtZero: true,
            ticks: { stepSize: 1 },
          },
        },
      },
    });
  }
}
