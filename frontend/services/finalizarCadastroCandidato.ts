export function cadastrarCandidato(
  nome: string,
  email: string,
  estado: string,
  cep: string,
  descricao: string,
  competencias: string[],
  cpf: string,
  idade: number
) {
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

document
  .getElementById("botaoConfirmarCandidato")
  ?.addEventListener("click", () => {
    const nome = (document.getElementById("nome") as HTMLInputElement).value;
    const email = (document.getElementById("email") as HTMLInputElement).value;
    const estado = (document.getElementById("estado") as HTMLInputElement)
      .value;
    const cep = (document.getElementById("cep") as HTMLInputElement).value;
    const cpf = (document.getElementById("cpf") as HTMLInputElement).value;
    const idade = Number.parseInt(
      (document.getElementById("idade") as HTMLInputElement).value
    );
    const descricao = (
      document.getElementById("descricao") as HTMLTextAreaElement
    ).value;
    const competencias = getSelectedCompetencias();

    // Chamar a função para cadastrar o candidato com todos os dados
    cadastrarCandidato(
      nome,
      email,
      estado,
      cep,
      descricao,
      competencias,
      cpf,
      idade
    );

    // Redirecionar para a página principal após cadastro
    window.location.href = "../index.html";
  });

// Função para obter as competências selecionadas
function getSelectedCompetencias(): string[] {
  const competencias: string[] = [];
  const checkboxes = document.querySelectorAll(".custom-checkout:checked");
  // biome-ignore lint/complexity/noForEach: <explanation>
  checkboxes.forEach((checkbox: Element) => {
    const inputCheckbox = checkbox as HTMLInputElement;
    competencias.push(inputCheckbox.id.replace("competencia", ""));
  });
  return competencias;
}
