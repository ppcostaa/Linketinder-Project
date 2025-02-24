import Chart, { ChartOptions } from "chart.js";
import type { Candidato } from "./models/Candidato.js";
import type { Empresa } from "./models/Empresa.js";
import { ChartScales } from "chart.js";

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

  // Criar um mapa para contar competências
  const competenciaCount: Record<
    string,
    { candidatos: number; empresas: number }
  > = {};

  // Contabilizar competências dos candidatos
  for (const candidato of candidatos) {
    for (const competencia of candidato.competencias) {
      if (!competenciaCount[competencia]) {
        competenciaCount[competencia] = { candidatos: 0, empresas: 0 };
      }
      competenciaCount[competencia].candidatos++;
    }
  }

  // Contabilizar competências exigidas pelas empresas
  for (const empresa of empresas) {
    for (const competencia of empresa.competencias) {
      if (!competenciaCount[competencia]) {
        competenciaCount[competencia] = { candidatos: 0, empresas: 0 };
      }
      competenciaCount[competencia].empresas++;
    }
  }

  // Criar labels e datasets para o gráfico
  const labels = Object.keys(competenciaCount);
  const candidatoData = labels.map(
    (competencia) => competenciaCount[competencia].candidatos
  );
  const empresaData = labels.map(
    (competencia) => competenciaCount[competencia].empresas
  );

  // Renderizar o gráfico com Chart.js
  const ctx = document.getElementById("competenciaChart") as HTMLCanvasElement;

  // Verifica se já existe um gráfico ativo e destrói antes de criar um novo
  if (window.myChart instanceof Chart) {
    window.myChart.destroy();
  }

  window.myChart = new Chart(ctx, {
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
      scales: {
        y: {
          type: "linear",
          beginAtZero: true,
        } as ChartOptions,
      },
    },
  });
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
