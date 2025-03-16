import { Empresa } from "../models/Empresa.js";
import { validadores } from "../utils/validadores.js";
import { cancelarCadastro, mostrarErros, mostrarPopup, } from "./finalizarCadastroCandidato.js";
const empresas = [];
const formCadastroEmpresa = document.getElementById("formCadastroEmpresa");
document.addEventListener("DOMContentLoaded", () => {
    const botao = document.getElementById("botaoConfirmarEmpresa");
    if (botao) {
        botao.addEventListener("click", (event) => {
            event.preventDefault();
            botaoConfirmarEmpresa("cadastroEmpresa");
        });
    }
});
function botaoConfirmarEmpresa(page) {
    const content = document.getElementById("cadastroEmpresa");
    if (!content)
        return;
    if (!validarCamposEmpresa()) {
        mostrarPopup("Todos os campos obrigatórios precisam ser preenchidos!");
        return;
    }
    switch (page) {
        case "cadastroEmpresa":
            cadastrarEmpresa();
            setTimeout(() => {
                window.location.href = "../index.html";
                formCadastroEmpresa === null || formCadastroEmpresa === void 0 ? void 0 : formCadastroEmpresa.reset();
            }, 3000);
            break;
        default:
            content.innerHTML = "<p>Erro ao cadastrar!</p>";
    }
}
function cadastrarEmpresa() {
    var _a, _b, _c, _d, _e, _f, _g;
    const nome = (_a = document.getElementById("nome")) === null || _a === void 0 ? void 0 : _a.value;
    const email = (_b = document.getElementById("email")) === null || _b === void 0 ? void 0 : _b.value;
    const estado = (_c = document.getElementById("estado")) === null || _c === void 0 ? void 0 : _c.value;
    const cep = (_d = document.getElementById("cep")) === null || _d === void 0 ? void 0 : _d.value;
    const cnpj = (_e = document.getElementById("cnpj")) === null || _e === void 0 ? void 0 : _e.value;
    const pais = (_f = document.getElementById("pais")) === null || _f === void 0 ? void 0 : _f.value;
    const descricao = (_g = document.getElementById("descricao")) === null || _g === void 0 ? void 0 : _g.value;
    const competencias = getSelectedCompetencias();
    const empresa = new Empresa(nome, email, estado, cep, descricao, competencias, cnpj, pais);
    const empresasExistentes = JSON.parse(localStorage.getItem("empresas") || "[]");
    empresasExistentes.push(empresa);
    localStorage.setItem("empresas", JSON.stringify(empresasExistentes));
    mostrarPopup("Empresa adicionada com sucesso!");
}
function validarCamposEmpresa() {
    const campos = {
        nome: document.getElementById("nome").value,
        email: document.getElementById("email").value,
        cnpj: document.getElementById("cnpj").value,
        cep: document.getElementById("cep").value,
        pais: document.getElementById("pais").value,
        descricao: document.getElementById("descricao")
            .value,
        competencias: getSelectedCompetencias(),
    };
    const erros = [];
    if (!validadores.nome.test(campos.nome)) {
        erros.push("Nome da empresa inválido");
    }
    if (!validadores.email.test(campos.email)) {
        erros.push("E-mail corporativo inválido");
    }
    if (!validadores.cnpj.test(campos.cnpj)) {
        erros.push("CNPJ inválido (formato esperado: 00.000.000/0000-00)");
    }
    if (!validadores.cep.test(campos.cep)) {
        erros.push("CEP inválido (formato esperado: 00000-000)");
    }
    if (campos.competencias.length === 0) {
        erros.push("Selecione pelo menos uma competência requerida");
    }
    if (erros.length > 0) {
        mostrarErros(erros);
        return false;
    }
    return true;
}
function getSelectedCompetencias() {
    const competencias = [];
    const checkboxes = document.querySelectorAll(".custom-checkout:checked");
    // biome-ignore lint/complexity/noForEach: <explanation>
    checkboxes.forEach((checkbox) => {
        const inputCheckbox = checkbox;
        competencias.push(inputCheckbox.id.replace("competencia", ""));
    });
    return competencias;
}
const botaoCancelar = document.getElementById("botaoCancelarEmpresa");
if (botaoCancelar) {
    botaoCancelar.addEventListener("click", (event) => {
        event.preventDefault();
        cancelarCadastro();
    });
}
const botaoCancelarEmpresa = document.getElementById("botaoCancelarEmpresa");
if (botaoCancelarEmpresa) {
    botaoCancelarEmpresa.addEventListener("click", (event) => {
        event.preventDefault();
        cancelarCadastro();
    });
}
