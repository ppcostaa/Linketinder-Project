Object.defineProperty(exports, "__esModule", { value: true });
exports.handleCandidatoFormSubmit = handleCandidatoFormSubmit;
const Candidato_1 = require("../models/Candidato");
const Cancelar_1 = require("../services/Cancelar");
const Popup_1 = require("../services/Popup");
const candidatos = [];
function handleCandidatoFormSubmit() {
  const nome = document.getElementById("nome").value;
  const email = document.getElementById("email").value;
  const estado = document.getElementById("estado").value;
  const cep = document.getElementById("cep").value;
  const descricao = document.getElementById("descricao").value;
  const competenciasInput = document.getElementById("competencias").value;
  const cpf = document.getElementById("cpf").value;
  const idade = Number.parseInt(document.getElementById("idade").value);
  const competencias = competenciasInput
    .split(",")
    .map((competencia) => competencia.trim());
  const candidato = new Candidato_1.Candidato(
    nome,
    email,
    estado,
    cep,
    descricao,
    competencias,
    cpf,
    idade
  );
  if (!candidato) {
    alert("Por favor, preencha todos os campos obrigatórios!");
    return;
  }
  candidatos.push(candidato);
  Popup_1.mostrarPopupConfirmar(
    `Candidato ${candidato.nome} cadastrado com sucesso!`
  );
  // function confirmarCadastro(candidato: Candidato) {
  //   setTimeout(() => {
  //     window.location.replace("../views/index.html");
  //   }, 2000);
  // }
  // confirmarCadastro(candidato);
}
// document.getElementById("botaoConfirmar").addEventListener("click", () => {
//   handleCandidatoFormSubmit;
// });
const botaoCancelar = document.getElementById("botaoCancelar");
botaoCancelar.addEventListener("click", Cancelar_1.handleCancelar);
