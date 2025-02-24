document.addEventListener("DOMContentLoaded", () => {
  document
    .getElementById("botaoCadastroCandidato")
    ?.addEventListener("click", () => navigate("cadastroCandidato"));
  document
    .getElementById("botaoCadastroEmpresa")
    ?.addEventListener("click", () => navigate("cadastroEmpresa"));
  document
    .getElementById("botaoPerfilCandidato")
    ?.addEventListener("click", () => navigate("perfilCandidato"));
  document
    .getElementById("botaoPerfilEmpresa")
    ?.addEventListener("click", () => navigate("perfilEmpresa"));
});

function navigate(page: string) {
  const content = document.getElementById("content");
  if (!content) return;

  switch (page) {
    case "cadastroCandidato":
      window.location.href = "views/CadastroCandidato.html";
      break;
    case "cadastroEmpresa":
      content.innerHTML =
        "<h2>Cadastro de Empresa</h2><p>Formulário para cadastrar empresas.</p>";
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
              .map((candidato, index) => {
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
    // biome-ignore lint/suspicious/noExplicitAny: <explanation>
    // biome-ignore lint/complexity/noForEach: <explanation>
    candidatos.forEach((candidato: any) => {
      html += `<li>${candidato.nome}</li>`;
    });
    html += "</ul>";
    content.innerHTML = html;
  } else {
    content.innerHTML = "<p>Nenhum candidato cadastrado.</p>";
  }
}
