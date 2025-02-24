import {
  cancelarCadastro,
  mostrarPopup,
} from "./finalizarCadastroCandidato.js";
class Empresa {
  constructor(nome, email, estado, cep, descricao, competencias, cnpj, pais) {
    this.nome = nome;
    this.email = email;
    this.estado = estado;
    this.cep = cep;
    this.descricao = descricao;
    this.competencias = competencias;
    this.cnpj = cnpj;
    this.pais = pais;
  }
}
const empresas = [];
const formCadastroEmpresa = document.getElementById("formCadastroEmpresa");
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
function botaoConfirmarEmpresa(page) {
  console.log("Botão clicado!");
  const content = document.getElementById("cadastroEmpresa");
  if (!content) return;
  switch (page) {
    case "cadastroEmpresa":
      cadastrarEmpresa();
      setTimeout(() => {
        window.location.href = "../index.html";
        formCadastroEmpresa === null || formCadastroEmpresa === void 0
          ? void 0
          : formCadastroEmpresa.reset();
      }, 3000);
      break;
    default:
      content.innerHTML = "<p>Bem-vindo ao Linketinder!</p>";
  }
}
function cadastrarEmpresa() {
  var _a, _b, _c, _d, _e, _f, _g;
  const nome =
    (_a = document.getElementById("nome")) === null || _a === void 0
      ? void 0
      : _a.value;
  const email =
    (_b = document.getElementById("email")) === null || _b === void 0
      ? void 0
      : _b.value;
  const estado =
    (_c = document.getElementById("estado")) === null || _c === void 0
      ? void 0
      : _c.value;
  const cep =
    (_d = document.getElementById("cep")) === null || _d === void 0
      ? void 0
      : _d.value;
  const cnpj =
    (_e = document.getElementById("cnpj")) === null || _e === void 0
      ? void 0
      : _e.value;
  const pais =
    (_f = document.getElementById("pais")) === null || _f === void 0
      ? void 0
      : _f.value;
  const descricao =
    (_g = document.getElementById("descricao")) === null || _g === void 0
      ? void 0
      : _g.value;
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
