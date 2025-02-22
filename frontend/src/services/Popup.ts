export function mostrarPopupConfirmar(message: string) {
  const popup = document.createElement("div");
  popup.className = "popupConfirmar";
  popup.innerText = message;
  document.body.appendChild(popup);

  setTimeout(() => {
    popup.remove();
  }, 3000);
}
export function mostrarPopupCancelar(message: string) {
  const popup = document.createElement("div");
  popup.className = "popupCancelar";
  popup.innerText = message;
  document.body.appendChild(popup);

  setTimeout(() => {
    popup.remove();
  }, 3000);
}
