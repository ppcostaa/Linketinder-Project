"use strict";
var _a;
Object.defineProperty(exports, "__esModule", { value: true });
exports.cadastrarCandidato = cadastrarCandidato;
const candidatos = JSON.parse(localStorage.getItem("candidatos") || "[]");
function cadastrarCandidato(nome, email, estado, cep, descricao, competencias, cpf, idade) {
    const candidato = {
        nome,
        email,
        estado,
        cep,
        descricao,
        competencias,
        cpf,
        idade,
    };
    // Recuperar os candidatos já cadastrados no localStorage
    const candidatos = JSON.parse(localStorage.getItem("candidatos") || "[]");
    // Adicionar o novo candidato
    candidatos.push(candidato);
    // Atualizar os dados no localStorage
    localStorage.setItem("candidatos", JSON.stringify(candidatos));
    alert("Candidato cadastrado com sucesso!");
}
(_a = document
    .getElementById("botaoConfirmarCandidato")) === null || _a === void 0 ? void 0 : _a.addEventListener("click", () => {
    const nome = document.getElementById("nome").value;
    const email = document.getElementById("email").value;
    const estado = document.getElementById("estado")
        .value;
    const cep = document.getElementById("cep").value;
    const cpf = document.getElementById("cpf").value;
    const idade = Number.parseInt(document.getElementById("idade").value);
    const descricao = document.getElementById("descricao").value;
    const competencias = getSelectedCompetencias();
    // Chamar a função para cadastrar o candidato com todos os dados
    cadastrarCandidato(nome, email, estado, cep, descricao, competencias, cpf, idade);
    // Redirecionar para a página principal após cadastro
    window.location.href = "../index.html";
});
// Função para obter as competências selecionadas
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
