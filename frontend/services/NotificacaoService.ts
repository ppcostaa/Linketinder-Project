import { INotificationService } from "../interfaces/INotificationService";

export class NotificationService implements INotificationService {
  private static instance: NotificationService;

  private popupContainer: HTMLDivElement = document.createElement("div");
  private constructor() {
    this.createPopupContainer();
  }

  public static getInstance(): NotificationService {
    if (!NotificationService.instance) {
      NotificationService.instance = new NotificationService();
    }
    return NotificationService.instance;
  }

  private createPopupContainer(): void {
    this.popupContainer = document.createElement("div");
    this.popupContainer.id = "popup-container";
    this.popupContainer.style.position = "fixed";
    this.popupContainer.style.top = "20px";
    this.popupContainer.style.right = "20px";
    this.popupContainer.style.zIndex = "1000";
    this.popupContainer.style.display = "flex";
    this.popupContainer.style.flexDirection = "column";
    this.popupContainer.style.gap = "10px";
    document.body.appendChild(this.popupContainer);
  }

  showSuccess(message: string): void {
    this.showPopup(message, "success");
  }

  showErrors(errors: string[]): void {
    const message =
      errors.length > 1
        ? `<div class="popup-header">Erros no formulário</div>
         <ul class="popup-errors">${errors
           .map((e) => `<li>${e}</li>`)
           .join("")}</ul>`
        : errors[0];

    this.showPopup(message, "error");
  }

  private showPopup(content: string, type: "success" | "error"): void {
    const popup = document.createElement("div");
    popup.className = `popup popup-${type}`;

    if (content.startsWith("<")) {
      popup.innerHTML = content;
    } else {
      popup.textContent = content;
    }

    this.popupContainer.appendChild(popup);

    setTimeout(() => {
      popup.classList.add("fade-out");
      setTimeout(() => popup.remove(), 300);
    }, 5000);
  }
}
