import { Candidato } from "../models/Candidato.js";
import { validadores } from "../utils/validadores.js";
export const candidatos = [];
const formCadastroCandidato = document.getElementById("formCadastroCandidato");
document.addEventListener("DOMContentLoaded", () => {
    const botao = document.getElementById("botaoConfirmarCandidato");
    if (botao) {
        botao.addEventListener("click", (event) => {
            event.preventDefault();
            botaoConfirmarCandidato("cadastrarCandidato");
        });
    }
});
function botaoConfirmarCandidato(page) {
    const content = document.getElementById("cadastroCandidato");
    if (!content)
        return;
    if (!validarCamposCandidato()) {
        mostrarPopup("Todos os campos obrigatórios precisam ser preenchidos!");
        return;
    }
    switch (page) {
        case "cadastrarCandidato":
            cadastrarCandidato();
            setTimeout(() => {
                window.location.href = "../index.html";
                formCadastroCandidato === null || formCadastroCandidato === void 0 ? void 0 : formCadastroCandidato.reset();
            }, 3000);
            break;
        default:
            content.innerHTML = "<p>Erro ao cadastrar!</p>";
    }
}
function cadastrarCandidato() {
    var _a, _b, _c, _d, _e, _f, _g;
    const nome = (_a = document.getElementById("nome")) === null || _a === void 0 ? void 0 : _a.value;
    const email = (_b = document.getElementById("email")) === null || _b === void 0 ? void 0 : _b.value;
    const estado = (_c = document.getElementById("estado")) === null || _c === void 0 ? void 0 : _c.value;
    const cep = (_d = document.getElementById("cep")) === null || _d === void 0 ? void 0 : _d.value;
    const cpf = (_e = document.getElementById("cpf")) === null || _e === void 0 ? void 0 : _e.value;
    const idade = Number.parseInt((_f = document.getElementById("idade")) === null || _f === void 0 ? void 0 : _f.value);
    const descricao = (_g = document.getElementById("descricao")) === null || _g === void 0 ? void 0 : _g.value;
    const competencias = getSelectedCompetencias();
    const candidato = new Candidato(nome, email, estado, cep, descricao, competencias, cpf, idade);
    const candidatosExistentes = JSON.parse(localStorage.getItem("candidatos") || "[]");
    candidatosExistentes.push(candidato);
    localStorage.setItem("candidatos", JSON.stringify(candidatosExistentes));
    mostrarPopup("Candidato adicionado com sucesso!");
}
function validarCamposCandidato() {
    var _a, _b;
    const campos = {
        nome: document.getElementById("nome").value,
        email: document.getElementById("email").value,
        cpf: document.getElementById("cpf").value,
        telefone: (_a = document.getElementById("telefone")) === null || _a === void 0 ? void 0 : _a.value,
        linkedin: (_b = document.getElementById("linkedin")) === null || _b === void 0 ? void 0 : _b.value,
        idade: document.getElementById("idade").value,
        descricao: document.getElementById("descricao")
            .value,
        competencias: getSelectedCompetencias(),
    };
    const erros = [];
    if (!validadores.nome.test(campos.nome)) {
        erros.push("Nome inválido (mínimo 2 caracteres, apenas letras e espaços)");
    }
    if (!validadores.email.test(campos.email)) {
        erros.push("Formato de e-mail inválido");
    }
    if (!validadores.cpf.test(campos.cpf)) {
        erros.push("CPF inválido (formato esperado: 000.000.000-00)");
    }
    if (campos.telefone && !validadores.telefone.test(campos.telefone)) {
        erros.push("Telefone inválido (formato esperado: (DD) 91234-5678)");
    }
    if (campos.linkedin && !validadores.linkedin.test(campos.linkedin)) {
        erros.push("LinkedIn inválido (formato esperado: linkedin.com/in/seu-perfil)");
    }
    if (campos.competencias.length === 0) {
        erros.push("Selecione pelo menos uma competência");
    }
    if (erros.length > 0) {
        mostrarErros(erros);
        return false;
    }
    return true;
}
export function mostrarErros(erros) {
    const popup = document.createElement("div");
    popup.className = "popup-erro";
    popup.innerHTML = `
    <h4>Erros no formulário:</h4>
    <ul>${erros.map((erro) => `<li>${erro}</li>`).join("")}</ul>
  `;
    document.body.appendChild(popup);
    setTimeout(() => popup.remove(), 5000);
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
export function mostrarPopup(message) {
    const popup = document.createElement("div");
    popup.className = "popup";
    popup.innerText = message;
    document.body.appendChild(popup);
    popup.style.position = "fixed";
    popup.style.top = "20px";
    popup.style.left = "50%";
    popup.style.transform = "translateX(-50%)";
    popup.style.backgroundColor = "#fc2cee";
    popup.style.color = "#242323";
    popup.style.padding = "16px";
    popup.style.borderRadius = "4px";
    popup.style.zIndex = "1000";
    setTimeout(() => {
        popup.remove();
    }, 3000);
}
export function cancelarCadastro() {
    mostrarPopup("Cadastro cancelado!");
    setTimeout(() => {
        window.location.href = "../index.html";
    }, 2000);
}
const botaoCancelar = document.getElementById("botaoCancelarCandidato");
if (botaoCancelar) {
    botaoCancelar.addEventListener("click", (event) => {
        event.preventDefault();
        cancelarCadastro();
    });
}
const botaoCancelarCandidato = document.getElementById("botaoCancelarCandidato");
if (botaoCancelarCandidato) {
    botaoCancelarCandidato.addEventListener("click", (event) => {
        event.preventDefault();
        cancelarCadastro();
    });
}
