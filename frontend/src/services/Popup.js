"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.mostrarPopupConfirmar = mostrarPopupConfirmar;
exports.mostrarPopupCancelar = mostrarPopupCancelar;
function mostrarPopupConfirmar(message) {
    const popup = document.createElement("div");
    popup.className = "popupConfirmar";
    popup.innerText = message;
    document.body.appendChild(popup);
    setTimeout(() => {
        popup.remove();
    }, 3000);
}
function mostrarPopupCancelar(message) {
    const popup = document.createElement("div");
    popup.className = "popupCancelar";
    popup.innerText = message;
    document.body.appendChild(popup);
    setTimeout(() => {
        popup.remove();
    }, 3000);
}
