import { mostrarPopupCancelar } from "./Popup";

export function handleCancelar(event: Event) {
  event.preventDefault();
  mostrarPopupCancelar("Cadastro cancelado.");
  setTimeout(() => {
    window.location.replace("../views/index.html");
  }, 2000);
}
const botaoCancelar = document.getElementById("botaoCancelar");

botaoCancelar.addEventListener("click", handleCancelar);
