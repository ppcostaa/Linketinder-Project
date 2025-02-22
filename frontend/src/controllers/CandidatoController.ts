import { Candidato } from "../models/Candidato";
import { handleCancelar } from "../services/Cancelar";
import { mostrarPopupConfirmar } from "../services/Popup";

const candidatos = [];
export function handleCandidatoFormSubmit() {
  const nome = (document.getElementById("nome") as HTMLInputElement).value;
  const email = (document.getElementById("email") as HTMLInputElement).value;
  const estado = (document.getElementById("estado") as HTMLInputElement).value;
  const cep = (document.getElementById("cep") as HTMLInputElement).value;
  const descricao = (document.getElementById("descricao") as HTMLInputElement)
    .value;
  const competenciasInput = (
    document.getElementById("competencias") as HTMLInputElement
  ).value;
  const cpf = (document.getElementById("cpf") as HTMLInputElement).value;
  const idade = Number.parseInt(
    (document.getElementById("idade") as HTMLInputElement).value
  );

  const competencias = competenciasInput
    .split(",")
    .map((competencia) => competencia.trim());

  const candidato = new Candidato(
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
  mostrarPopupConfirmar(`Candidato ${candidato.nome} cadastrado com sucesso!`);

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

botaoCancelar.addEventListener("click", handleCancelar);
