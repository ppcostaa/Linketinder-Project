import { Chart } from "chart.js";

window.onload = () => {
  const ctx = (
    document.getElementById("graficoCompetencias") as HTMLCanvasElement
  )?.getContext("2d");
  if (ctx) {
    const myChart = new Chart(ctx, {
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
  } else {
    console.error("Contexto não encontrado");
  }
};
