document.addEventListener("DOMContentLoaded", () => {
    const botaoCadastroCandidato = document.getElementById("botaoCadastroCandidato");
    const botaoCadastroEmpresa = document.getElementById("botaoCadastroEmpresa");
    const botaoPerfilCandidato = document.getElementById("botaoPerfilCandidato");
    const botaoPerfilEmpresa = document.getElementById("botaoPerfilEmpresa");
    const botaoListarCandidatos = document.getElementById("botaoListarCandidatos");
    botaoCadastroCandidato === null || botaoCadastroCandidato === void 0 ? void 0 : botaoCadastroCandidato.addEventListener("click", () => navigate("cadastroCandidato"));
    botaoCadastroEmpresa === null || botaoCadastroEmpresa === void 0 ? void 0 : botaoCadastroEmpresa.addEventListener("click", () => navigate("cadastroEmpresa"));
    botaoPerfilCandidato === null || botaoPerfilCandidato === void 0 ? void 0 : botaoPerfilCandidato.addEventListener("click", () => navigate("perfilCandidato"));
    botaoPerfilEmpresa === null || botaoPerfilEmpresa === void 0 ? void 0 : botaoPerfilEmpresa.addEventListener("click", () => navigate("perfilEmpresa"));
    botaoListarCandidatos === null || botaoListarCandidatos === void 0 ? void 0 : botaoListarCandidatos.addEventListener("click", () => navigate("listarCandidatos"));
});
function navigate(page) {
    const content = document.getElementById("content");
    const candidatos = JSON.parse(localStorage.getItem("candidatos") || "[]");
    const empresas = JSON.parse(localStorage.getItem("empresas") || "[]");
    if (!content)
        return;
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
                .map((empresa) => {
                const competencias = empresa.competencias && empresa.competencias.length > 0
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
                .map((candidato) => {
                const competencias = candidato.competencias && candidato.competencias.length > 0
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
export {};
