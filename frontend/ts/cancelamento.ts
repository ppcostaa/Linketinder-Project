export function cancelar(menu: HTMLElement, cadastroCandidato: HTMLElement) {
  // Se o usuário cancelar, exibe o menu novamente
  menu.style.display = "block";
  cadastroCandidato.style.display = "none";
}
