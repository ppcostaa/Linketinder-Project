import { Empresa } from "../models/Empresa.js";
import { validadores } from "../utils/validadores.js";
import {
  cancelarCadastro,
  mostrarErros,
  mostrarPopup,
} from "./finalizarCadastroCandidato.js";

const empresas: Empresa[] = [];
const formCadastroEmpresa = document.getElementById(
  "formCadastroEmpresa"
) as HTMLFormElement;

document.addEventListener("DOMContentLoaded", () => {
  const botao = document.getElementById("botaoConfirmarEmpresa");
  if (botao) {
    botao.addEventListener("click", (event) => {
      event.preventDefault();
      botaoConfirmarEmpresa("cadastroEmpresa");
    });
  }
});

function botaoConfirmarEmpresa(page: string) {
  const content = document.getElementById("cadastroEmpresa");
  if (!content) return;
  if (!validarCamposEmpresa()) {
    mostrarPopup("Todos os campos obrigatórios precisam ser preenchidos!");
    return;
  }

  switch (page) {
    case "cadastroEmpresa":
      cadastrarEmpresa();
      setTimeout(() => {
        window.location.href = "../index.html";
        formCadastroEmpresa?.reset();
      }, 3000);
      break;
    default:
      content.innerHTML = "<p>Erro ao cadastrar!</p>";
  }
}

function cadastrarEmpresa() {
  const nome = (document.getElementById("nome") as HTMLInputElement)?.value;
  const email = (document.getElementById("email") as HTMLInputElement)?.value;
  const estado = (document.getElementById("estado") as HTMLInputElement)?.value;
  const cep = (document.getElementById("cep") as HTMLInputElement)?.value;
  const cnpj = (document.getElementById("cnpj") as HTMLInputElement)?.value;
  const pais = (document.getElementById("pais") as HTMLInputElement)?.value;
  const descricao = (document.getElementById("descricao") as HTMLInputElement)
    ?.value;
  const competencias = getSelectedCompetencias();

  const empresa = new Empresa(
    nome,
    email,
    estado,
    cep,
    descricao,
    competencias,
    cnpj,
    pais
  );

  const empresasExistentes = JSON.parse(
    localStorage.getItem("empresas") || "[]"
  );

  empresasExistentes.push(empresa);

  localStorage.setItem("empresas", JSON.stringify(empresasExistentes));
  mostrarPopup("Empresa adicionada com sucesso!");
}
function validarCamposEmpresa() {
  const campos = {
    nome: (document.getElementById("nome") as HTMLInputElement).value,
    email: (document.getElementById("email") as HTMLInputElement).value,
    cnpj: (document.getElementById("cnpj") as HTMLInputElement).value,
    cep: (document.getElementById("cep") as HTMLInputElement).value,
    pais: (document.getElementById("pais") as HTMLInputElement).value,
    descricao: (document.getElementById("descricao") as HTMLTextAreaElement)
      .value,
    competencias: getSelectedCompetencias(),
  };

  const erros: string[] = [];

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
