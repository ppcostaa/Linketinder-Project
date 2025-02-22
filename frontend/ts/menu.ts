export function mostrarMenu(
  menu: HTMLElement,
  cadastroCandidato: HTMLElement,
  cadastroEmpresa: HTMLElement
) {
  menu.style.display = "block";
  cadastroCandidato.style.display = "none";
  cadastroEmpresa.style.display = "none";
}

export function mostrarCadastro(
  menu: HTMLElement,
  cadastroCandidato: HTMLElement,
  cadastroEmpresa: HTMLElement
) {
  menu.style.display = "none";
  cadastroCandidato.style.display = "block";
  cadastroEmpresa.style.display = "block";
}
