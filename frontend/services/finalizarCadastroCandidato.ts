class Candidato {
  constructor(
    public nome: string,
    public email: string,
    public estado: string,
    public cep: string,
    public descricao: string,
    public competencias: string[],
    public cpf: string,
    public idade: number
  ) {}
}

const candidatos: Candidato[] = [];

document.addEventListener("DOMContentLoaded", () => {
  const botao = document.getElementById("botaoConfirmarCandidato");
  if (botao) {
    botao.addEventListener("click", (event) => {
      event.preventDefault();
      botaoConfirmarCandidato("cadastrarCandidato");
    });
    console.log("Botão encontrado e event listener adicionado");
  } else {
    console.error("Botão não encontrado");
  }
});

function botaoConfirmarCandidato(page: string) {
  console.log("Botão clicado!");

  const content = document.getElementById("cadastroCandidato");
  const form = document.getElementById("formCadastro") as HTMLFormElement;
  if (!content) return;

  switch (page) {
    case "cadastrarCandidato":
      cadastrarCandidato();
      setTimeout(() => {
        window.location.href = "../index.html";
        form?.reset();
      }, 3000);
      break;
    default:
      content.innerHTML = "<p>Bem-vindo ao Linketinder!</p>";
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

  candidatos.push(candidato);
  mostrarPopup("Candidato adicionado com sucesso!");
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

function mostrarPopup(message: string) {
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
