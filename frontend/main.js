import { candidatos } from "./services/finalizarCadastroCandidato.js";
document.addEventListener("DOMContentLoaded", () => {
  const botaoCadastroCandidato = document.getElementById(
    "botaoCadastroCandidato"
  );
  const botaoCadastroEmpresa = document.getElementById("botaoCadastroEmpresa");
  const botaoPerfilCandidato = document.getElementById("botaoPerfilCandidato");
  const botaoPerfilEmpresa = document.getElementById("botaoPerfilEmpresa");
  const botaoListarCandidatos = document.getElementById(
    "botaoListarCandidatos"
  );
  botaoCadastroCandidato === null || botaoCadastroCandidato === void 0
    ? void 0
    : botaoCadastroCandidato.addEventListener("click", () =>
        navigate("cadastroCandidato")
      );
  botaoCadastroEmpresa === null || botaoCadastroEmpresa === void 0
    ? void 0
    : botaoCadastroEmpresa.addEventListener("click", () =>
        navigate("cadastroEmpresa")
      );
  botaoPerfilCandidato === null || botaoPerfilCandidato === void 0
    ? void 0
    : botaoPerfilCandidato.addEventListener("click", () =>
        navigate("perfilCandidato")
      );
  botaoPerfilEmpresa === null || botaoPerfilEmpresa === void 0
    ? void 0
    : botaoPerfilEmpresa.addEventListener("click", () =>
        navigate("perfilEmpresa")
      );
  botaoListarCandidatos === null || botaoListarCandidatos === void 0
    ? void 0
    : botaoListarCandidatos.addEventListener("click", () =>
        navigate("listarCandidatos")
      );
});
function navigate(page) {
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
              .map((candidato) => {
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
function listarCandidatos() {
  const candidatos = JSON.parse(localStorage.getItem("candidatos") || "[]");
  const content = document.getElementById("content");
  if (!content) return;
  if (candidatos.length > 0) {
    let html = "<h2>Candidatos Cadastrados</h2><ul>";
    // biome-ignore lint/complexity/noForEach: <explanation>
    candidatos.forEach((candidato) => {
      html += `<li>${candidato.nome}</li>`;
    });
    html += "</ul>";
    content.innerHTML = html;
  } else {
    content.innerHTML = "<p>Nenhum candidato cadastrado.</p>";
  }
}
