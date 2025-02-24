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
    default:
      content.innerHTML = "<p>Bem-vindo ao Linketinder!</p>";
  }
}
