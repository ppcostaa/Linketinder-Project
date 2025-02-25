import { Candidato } from "../models/Candidato.js";

export const candidatos: Candidato[] = [];
const formCadastroCandidato = document.getElementById(
  "formCadastroCandidato"
) as HTMLFormElement;

document.addEventListener("DOMContentLoaded", () => {
  const botao = document.getElementById("botaoConfirmarCandidato");
  if (botao) {
    botao.addEventListener("click", (event) => {
      event.preventDefault();
      botaoConfirmarCandidato("cadastrarCandidato");
    });
  }
});

function botaoConfirmarCandidato(page: string) {
  const content = document.getElementById("cadastroCandidato");
  if (!content) return;
  if (!validarCamposCandidato()) {
    mostrarPopup("Todos os campos obrigatórios precisam ser preenchidos!");
    return;
  }

  switch (page) {
    case "cadastrarCandidato":
      cadastrarCandidato();
      setTimeout(() => {
        window.location.href = "../index.html";
        formCadastroCandidato?.reset();
      }, 3000);
      break;
    default:
      content.innerHTML = "<p>Erro ao cadastrar!</p>";
  }
}

function cadastrarCandidato() {
  const nome = (document.getElementById("nome") as HTMLInputElement)?.value;
  const email = (document.getElementById("email") as HTMLInputElement)?.value;
  const estado = (document.getElementById("estado") as HTMLInputElement)?.value;
  const cep = (document.getElementById("cep") as HTMLInputElement)?.value;
  const cpf = (document.getElementById("cpf") as HTMLInputElement)?.value;
  const idade = Number.parseInt(
    (document.getElementById("idade") as HTMLInputElement)?.value
  );
  const descricao = (document.getElementById("descricao") as HTMLInputElement)
    ?.value;
  const competencias = getSelectedCompetencias();

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
  const candidatosExistentes = JSON.parse(
    localStorage.getItem("candidatos") || "[]"
  );

  candidatosExistentes.push(candidato);

  localStorage.setItem("candidatos", JSON.stringify(candidatosExistentes));
  mostrarPopup("Candidato adicionado com sucesso!");
}
function validarCamposCandidato() {
  const camposObrigatorios = [
    "nome",
    "email",
    "estado",
    "cep",
    "cpf",
    "idade",
    "descricao",
  ];

  let valido = true;

  // biome-ignore lint/complexity/noForEach: <explanation>
  camposObrigatorios.forEach((id) => {
    const campo = document.getElementById(id) as
      | HTMLInputElement
      | HTMLTextAreaElement;
    if (!campo.value.trim()) {
      valido = false;
      campo.classList.add("campo-invalido");
    } else {
      campo.classList.remove("campo-invalido");
    }
  });

  return valido;
}
function getSelectedCompetencias(): string[] {
  const competencias: string[] = [];
  const checkboxes = document.querySelectorAll(".custom-checkout:checked");
  // biome-ignore lint/complexity/noForEach: <explanation>
  checkboxes.forEach((checkbox) => {
    const inputCheckbox = checkbox;
    competencias.push(inputCheckbox.id.replace("competencia", ""));
  });
  return competencias;
}

export function mostrarPopup(message: string) {
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

const botaoCancelarCandidato = document.getElementById(
  "botaoCancelarCandidato"
);
if (botaoCancelarCandidato) {
  botaoCancelarCandidato.addEventListener("click", (event) => {
    event.preventDefault();
    cancelarCadastro();
  });
}
