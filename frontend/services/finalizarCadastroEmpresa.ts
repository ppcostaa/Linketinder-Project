import {
  cancelarCadastro,
  mostrarPopup,
} from "./finalizarCadastroCandidato.js";

class Empresa {
  constructor(
    public nome: string,
    public email: string,
    public estado: string,
    public cep: string,
    public descricao: string,
    public competencias: string[],
    public cnpj: string,
    public pais: string
  ) {}
}

const empresas: Empresa[] = [];
const formCadastroEmpresa = document.getElementById(
  "formCadastroEmpresa"
) as HTMLFormElement;

document.addEventListener("DOMContentLoaded", () => {
  const botao = document.getElementById("botaoConfirmarEmpresa");
  if (botao) {
    botao.addEventListener("click", (event) => {
      event.preventDefault();
      botaoConfirmarEmpresa("cadastrarEmpresa");
    });
    console.log("Botão encontrado e event listener adicionado");
  } else {
    console.error("Botão não encontrado");
  }
});

function botaoConfirmarEmpresa(page: string) {
  console.log("Botão clicado!");

  const content = document.getElementById("cadastroEmpresa");
  if (!content) return;

  switch (page) {
    case "cadastroEmpresa":
      cadastrarEmpresa();
      setTimeout(() => {
        window.location.href = "../index.html";
        formCadastroEmpresa?.reset();
      }, 3000);
      break;
    default:
      content.innerHTML = "<p>Bem-vindo ao Linketinder!</p>";
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

  empresas.push(empresa);
  mostrarPopup("Empresa adicionada com sucesso!");
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
