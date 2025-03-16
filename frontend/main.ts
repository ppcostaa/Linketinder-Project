import type { Candidato } from "./models/Candidato.js";
import type { Empresa } from "./models/Empresa.js";
import { Chart, ChartItem, registerables } from "chart.js";
Chart.register(...registerables);

document.addEventListener("DOMContentLoaded", () => {
  const botaoCadastroCandidato = document.getElementById(
    "botaoCadastroCandidato"
  ) as HTMLButtonElement | null;
  const botaoCadastroEmpresa = document.getElementById(
    "botaoCadastroEmpresa"
  ) as HTMLButtonElement | null;
  const botaoPerfilCandidato = document.getElementById(
    "botaoPerfilCandidato"
  ) as HTMLButtonElement | null;
  const botaoPerfilEmpresa = document.getElementById(
    "botaoPerfilEmpresa"
  ) as HTMLButtonElement | null;
  const botaoListarCandidatos = document.getElementById(
    "botaoListarCandidatos"
  ) as HTMLButtonElement | null;

  botaoCadastroCandidato?.addEventListener("click", () =>
    navigate("cadastroCandidato")
  );
  botaoCadastroEmpresa?.addEventListener("click", () =>
    navigate("cadastroEmpresa")
  );
  botaoPerfilCandidato?.addEventListener("click", () =>
    navigate("perfilCandidato")
  );
  botaoPerfilEmpresa?.addEventListener("click", () =>
    navigate("perfilEmpresa")
  );
  botaoListarCandidatos?.addEventListener("click", () =>
    navigate("listarCandidatos")
  );
});

function navigate(page: string): void {
  const content = document.getElementById("content");
  const candidatos = JSON.parse(localStorage.getItem("candidatos") || "[]");
  const empresas = JSON.parse(localStorage.getItem("empresas") || "[]");

  if (!content) return;

  switch (page) {
    case "cadastroCandidato":
      window.location.href = "views/CadastroCandidato.html";
      break;
    case "cadastroEmpresa":
      window.location.href = "views/CadastroEmpresa.html";
      break;
    case "perfilCandidato":
      content.innerHTML = `
    <h2>Perfil do Candidato</h2>
    <p>Lista de vagas disponíveis.</p>
    <ul id="listaEmpresas">
    ${empresas
      .map((empresa: Empresa) => {
        const competencias =
          empresa.competencias && empresa.competencias.length > 0
            ? empresa.competencias.join(", ")
            : "Nenhuma competência foi selecionada.";
        return `<li>
                  <strong>${empresa.nome}</strong>
                  - Competências exigidas: ${competencias}
                </li>`;
      })
      .join("")}
    </ul>
  `;
      break;

    case "perfilEmpresa":
      content.innerHTML = `
          <h2>Perfil da Empresa</h2>
          <p class="listagem">Lista de candidatos:</p>
          <ul id="listaCandidatos">
            ${candidatos
              .map((candidato: Candidato) => {
                const competencias =
                  candidato.competencias && candidato.competencias.length > 0
                    ? candidato.competencias.join(", ")
                    : "Nenhuma competência foi selecionada.";
                return `<li>
                          <strong>${candidato.nome}</strong> 
                          - Competências: ${competencias}
                          <br><a href="${candidato.linkedin}" target="_blank">LinkedIn</a>
                        </li>`;
              })
              .join("")}
          </ul>
          <div style="margin-top: 2rem;">
            <canvas id="graficoCompetencias"></canvas>
          </div>
        `;

      setTimeout(() => {
        const canvas = document.getElementById(
          "graficoCompetencias"
        ) as HTMLCanvasElement;
        if (canvas) {
          const ctx = canvas.getContext("2d");
          if (ctx) {
            const competenciasCount: { [key: string]: number } = {};

            // biome-ignore lint/complexity/noForEach: <explanation>
            // biome-ignore lint/suspicious/noExplicitAny: <explanation>
            candidatos.forEach((candidato: { competencias: any[] }) => {
              // biome-ignore lint/complexity/noForEach: <explanation>
              candidato.competencias.forEach((competencia: string | number) => {
                competenciasCount[competencia] =
                  (competenciasCount[competencia] || 0) + 1;
              });
            });

            const competenciasOrdenadas = Object.entries(
              competenciasCount
            ).sort((a: [string, number], b: [string, number]) => b[1] - a[1]);

            new Chart(ctx, {
              type: "bar",
              data: {
                labels: competenciasOrdenadas.map(
                  (c: [string, number]) => c[0]
                ),
                datasets: [
                  {
                    label: "Candidatos por Competência",
                    data: competenciasOrdenadas.map(
                      (c: [string, number]) => c[1]
                    ),
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
                    ticks: {
                      stepSize: 1,
                    },
                  },
                },
              },
            });
          }
        }
      }, 0);
      break;
    default:
      content.innerHTML = "<p>Bem-vindo ao Linketinder!</p>";
  }
}
