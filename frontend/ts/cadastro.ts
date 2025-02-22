export function cadastrar(
  nome: string,
  email: string,
  menu: HTMLElement,
  cadastroCandidato: HTMLElement
) {
  if (nome && email) {
    alert(`Cadastro realizado com sucesso!\nNome: ${nome}\nE-mail: ${email}`);
    // Depois de cadastrar, exibe o menu novamente
    menu.style.display = "block";
    cadastroCandidato.style.display = "none";
  } else {
    alert("Por favor, preencha todos os campos.");
  }
}
