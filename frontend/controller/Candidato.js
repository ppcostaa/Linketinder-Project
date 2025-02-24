"use strict";
// biome-ignore lint/suspicious/noExplicitAny: <explanation>
const candidatos = [];
function cadastrarCandidato() {
    const nome = document.getElementById("nome").value;
    const email = document.getElementById("email").value;
    const descricao = document.getElementById("descricao").value;
    const candidato = { nome, email, descricao };
    candidatos.push(candidato);
    alert("Candidato cadastrado com sucesso!");
    window.location.href = "../index.html";
}
function deletarCandidato(index) {
    candidatos.splice(index, 1);
    alert("Candidato deletado com sucesso!");
}
