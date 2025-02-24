"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.cadastrarCandidato = cadastrarCandidato;
document.addEventListener("DOMContentLoaded", () => {
    var _a;
    (_a = document
        .getElementById("botaoConfirmarCandidato")) === null || _a === void 0 ? void 0 : _a.addEventListener("click", () => {
        cadastrarCandidato("cadastroCandidato");
    });
});
function cadastrarCandidato(page) {
    const content = document.getElementById("cadastroCandidato");
    if (!content)
        return;
    switch (page) {
        case "cadastroCandidato":
            window.location.href = "../index.html";
            break;
        default:
            content.innerHTML = "<p>Bem-vindo ao Linketinder!</p>";
    }
    // const nome = (document.getElementById("nome") as HTMLInputElement).value;
    // const email = (document.getElementById("email") as HTMLInputElement).value;
    // const descricao = (
    //   document.getElementById("descricao") as HTMLTextAreaElement
    // ).value;
    // const competencias = getSelectedCompetencias();
    // const estado = (document.getElementById("estado") as HTMLInputElement).value;
    // const cep = (document.getElementById("cep") as HTMLInputElement).value;
    // const cpf = (document.getElementById("cpf") as HTMLInputElement).value;
    // const idade = Number.parseInt(
    //   (document.getElementById("idade") as HTMLInputElement).value
    // );
    // const candidato = new Candidato(
    //   nome,
    //   email,
    //   estado,
    //   cep,
    //   descricao,
    //   competencias,
    //   cpf,
    //   idade
    // );
    // candidatos.push(candidato);
    // alert("Candidato cadastrado com sucesso!");
}
// function getSelectedCompetencias() {
//   // biome-ignore lint/suspicious/noExplicitAny: <explanation>
//   const competencias: any[] = [];
//   const checkboxes = document.querySelectorAll(".custom-checkout:checked");
//   // biome-ignore lint/suspicious/noExplicitAny: <explanation>
//   // biome-ignore lint/complexity/noForEach: <explanation>
//   checkboxes.forEach((checkbox: any) => {
//     competencias.push(checkbox.id.replace("competencia", ""));
//   });
//   return competencias;
// }
