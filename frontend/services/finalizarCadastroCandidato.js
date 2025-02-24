"use strict";
class Candidato {
    constructor(nome, email, estado, cep, descricao, competencias, cpf, idade) {
        this.nome = nome;
        this.email = email;
        this.estado = estado;
        this.cep = cep;
        this.descricao = descricao;
        this.competencias = competencias;
        this.cpf = cpf;
        this.idade = idade;
    }
}
const candidatos = [];
document.addEventListener("DOMContentLoaded", () => {
    const botao = document.getElementById("botaoConfirmarCandidato");
    if (botao) {
        botao.addEventListener("click", (event) => {
            event.preventDefault();
            botaoConfirmarCandidato("cadastrarCandidato");
        });
        console.log("Botão encontrado e event listener adicionado");
    }
    else {
        console.error("Botão não encontrado");
    }
});
function botaoConfirmarCandidato(page) {
    console.log("Botão clicado!");
    const content = document.getElementById("cadastroCandidato");
    const form = document.getElementById("formCadastro");
    if (!content)
        return;
    switch (page) {
        case "cadastrarCandidato":
            cadastrarCandidato();
            setTimeout(() => {
                window.location.href = "../index.html";
                form === null || form === void 0 ? void 0 : form.reset();
            }, 3000);
            break;
        default:
            content.innerHTML = "<p>Bem-vindo ao Linketinder!</p>";
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
    candidatos.push(candidato);
    mostrarPopup("Candidato adicionado com sucesso!");
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
function mostrarPopup(message) {
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
