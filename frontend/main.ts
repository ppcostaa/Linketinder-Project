import {
  Chart,
  type ChartConfiguration,
  ChartOptions,
  type CategoryScaleOptions,
  type ChartScales,
  type LinearScaleOptions,
} from "chart.js";

import type { Candidato } from "./models/Candidato.js";
import type { Empresa } from "./models/Empresa.js";
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
// Declara a propriedade global no `window`
declare global {
  interface Window {
    myChart?: Chart;
  }
}

function renderCompetenciaChart() {
  const candidatos: Candidato[] = JSON.parse(
    localStorage.getItem("candidatos") || "[]"
  );
  const empresas: Empresa[] = JSON.parse(
    localStorage.getItem("empresas") || "[]"
  );

  const competenciaCount: Record<
    string,
    { candidatos: number; empresas: number }
  > = {};

  for (const candidato of candidatos) {
    for (const competencia of candidato.competencias) {
      if (!competenciaCount[competencia]) {
        competenciaCount[competencia] = { candidatos: 0, empresas: 0 };
      }
      competenciaCount[competencia].candidatos++;
    }
  }

  for (const empresa of empresas) {
    for (const competencia of empresa.competencias) {
      if (!competenciaCount[competencia]) {
        competenciaCount[competencia] = { candidatos: 0, empresas: 0 };
      }
      competenciaCount[competencia].empresas++;
    }
  }

  const labels = Object.keys(competenciaCount);
  const candidatoData = labels.map(
    (competencia) => competenciaCount[competencia].candidatos
  );
  const empresaData = labels.map(
    (competencia) => competenciaCount[competencia].empresas
  );

  const ctx = document.getElementById(
    "competenciaChart"
  ) as HTMLCanvasElement | null;
  if (!ctx) return; // Garante que o elemento canvas existe antes de continuar

  // Se já existir um gráfico, destruir antes de criar um novo
  if (window.myChart) {
    window.myChart.destroy();
  }

  const scales: { x: CategoryScaleOptions; y: LinearScaleOptions } = {
    // Defina corretamente os tipos de escala
    x: {
      type: "category", // Define corretamente o eixo X como categórico
    },
    y: {
      type: "linear", // Define corretamente o eixo Y como numérico
      beginAtZero: true,
    },
  };

  const config: ChartConfiguration = {
    type: "bar",
    data: {
      labels,
      datasets: [
        {
          label: "Candidatos",
          data: candidatoData,
          backgroundColor: "rgba(54, 162, 235, 0.6)",
          borderColor: "rgba(54, 162, 235, 1)",
          borderWidth: 1,
        },
        {
          label: "Empresas",
          data: empresaData,
          backgroundColor: "rgba(255, 99, 132, 0.6)",
          borderColor: "rgba(255, 99, 132, 1)",
          borderWidth: 1,
        },
      ],
    },
    options: {
      responsive: true,
      scales, // Agora utilizamos `scales` com a tipagem correta
    },
  };

  window.myChart = new Chart(ctx, config);
}

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
                    </li>`;
          })
          .join("")}
    </ul>
  `;
      break;
    case "listarCandidatos":
      content.innerHTML = `
          <h2>Competências dos Candidatos e Empresas</h2>
          <canvas id="competenciaChart"></canvas>
        `;
      renderCompetenciaChart(); // Chama a função para exibir o gráfico
      break;
    default:
      content.innerHTML = "<p>Bem-vindo ao Linketinder!</p>";
  }
}
