import {
  type Candidato,
  candidatos,
} from "./services/finalizarCadastroCandidato.js";

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

  if (!content) return;

  switch (page) {
    case "cadastroCandidato":
      window.location.href = "views/CadastroCandidato.html";
      break;
    case "cadastroEmpresa":
      window.location.href = "views/CadastroEmpresa.html";
      break;
    case "perfilCandidato":
      content.innerHTML =
        "<h2>Perfil do Candidato</h2><p>Lista de vagas disponíveis.</p>";
      break;
    case "perfilEmpresa":
      content.innerHTML = `
          <h2>Perfil da Empresa</h2>
          <p>Lista de candidatos anônimos:</p>
          <ul id="listaCandidatos">
            ${candidatos
              .map((candidato: Candidato) => {
                return `<li>${candidato.nome} - ${candidato.competencias.join(
                  ", "
                )}</li>`;
              })
              .join("")}
          </ul>`;
      break;
    case "listarCandidatos":
      listarCandidatos();
      break;
    default:
      content.innerHTML = "<p>Bem-vindo ao Linketinder!</p>";
  }
}

function listarCandidatos(): void {
  const candidatos = JSON.parse(
    localStorage.getItem("candidatos") || "[]"
  ) as Candidato[];
  const content = document.getElementById("content");

  if (!content) return;

  if (candidatos.length > 0) {
    let html = "<h2>Candidatos Cadastrados</h2><ul>";
    // biome-ignore lint/complexity/noForEach: <explanation>
    candidatos.forEach((candidato: Candidato) => {
      html += `<li>${candidato.nome}</li>`;
    });
    html += "</ul>";
    content.innerHTML = html;
  } else {
    content.innerHTML = "<p>Nenhum candidato cadastrado.</p>";
  }
}
