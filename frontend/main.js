"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
document.addEventListener("DOMContentLoaded", () => {
    var _a, _b, _c, _d;
    (_a = document
        .getElementById("botaoCadastroCandidato")) === null || _a === void 0 ? void 0 : _a.addEventListener("click", () => navigate("cadastroCandidato"));
    (_b = document
        .getElementById("botaoCadastroEmpresa")) === null || _b === void 0 ? void 0 : _b.addEventListener("click", () => navigate("cadastroEmpresa"));
    (_c = document
        .getElementById("botaoPerfilCandidato")) === null || _c === void 0 ? void 0 : _c.addEventListener("click", () => navigate("perfilCandidato"));
    (_d = document
        .getElementById("botaoPerfilEmpresa")) === null || _d === void 0 ? void 0 : _d.addEventListener("click", () => navigate("perfilEmpresa"));
});
function navigate(page) {
    const content = document.getElementById("content");
    if (!content)
        return;
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
            content.innerHTML =
                "<h2>Perfil da Empresa</h2><p>Lista de candidatos anônimos.</p>";
            break;
        default:
            content.innerHTML = "<p>Bem-vindo ao Linketinder!</p>";
    }
}
