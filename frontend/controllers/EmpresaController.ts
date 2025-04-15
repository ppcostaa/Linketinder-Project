import { EmpresaService } from "../services/EmpresaService";
import { EmpresaRepository } from "../repositories/EmpresaRepository";
import { LocalStorageService } from "../services/LocalStorageService";
import { NotificationService } from "../services/NotificacaoService";
import { EmpresaValidator } from "../utils/EmpresaValidator";

export class EmpresaController {
  private service: EmpresaService;
  private form: HTMLFormElement;
  private notifier: NotificationService;

  constructor(formId: string) {
    this.form = document.getElementById(formId) as HTMLFormElement;
    if (!this.form) {
      throw new Error(`Formulário com ID ${formId} não encontrado`);
    }

    const storage = new LocalStorageService();
    const repository = new EmpresaRepository(storage);
    const validator = new EmpresaValidator();

    this.notifier = NotificationService.getInstance();

    this.service = new EmpresaService(repository, validator, this.notifier);

    this.setupEvents();
  }

  private setupEvents(): void {
    this.form.addEventListener("submit", (e) => this.handleSubmit(e));

    const cancelButton = document.getElementById("botaoCancelarEmpresa");
    if (cancelButton) {
      cancelButton.addEventListener("click", (e) => {
        e.preventDefault();
        this.handleCancel(e);
      });
    }
    const confirmButton = document.getElementById("botaoConfirmarEmpresa");
    if (confirmButton) {
      confirmButton.addEventListener("click", () => {
        this.form.dispatchEvent(new Event("submit"));
      });
    }
  }

  private handleSubmit(event: Event): void {
    event.preventDefault();
    try {
      const formData = this.getFormData();
      const resultadoCadastro = this.service.cadastrar(formData);

      if (resultadoCadastro) {
        setTimeout(() => {
          window.location.href = "../index.html";
          this.form.reset();
        }, 3000);
      }
    } catch (error) {
      console.error("Erro no cadastro:", error);
      this.service.notificar("Erro ao processar cadastro", "error");
    }
  }

  private handleCancel(event: Event): void {
    event.preventDefault();
    this.service.notificar("Cadastro cancelado!");
    setTimeout(() => {
      window.location.href = "../index.html";
    }, 2000);
  }

  private getFormData(): any {
    const getValue = (id: string): string => {
      const element = document.getElementById(id) as
        | HTMLInputElement
        | HTMLTextAreaElement
        | null;
      return element?.value.trim() || "";
    };
    return {
      nome: getValue("nome"),
      email: getValue("email"),
      estado: getValue("estado"),
      cep: getValue("cep"),
      cnpj: getValue("cnpj"),
      pais: getValue("pais"),
      descricao: getValue("descricaoEmpresa"),
      competencias: this.getSelectedCompetencias(),
    };
  }

  private getSelectedCompetencias(): string[] {
    try {
      const checkboxes = document.querySelectorAll(".custom-checkout:checked");
      return Array.from(checkboxes)
        .map((cb: Element) => {
          const input = cb as HTMLInputElement;
          return input.id.replace("competencia", "");
        })
        .filter(Boolean);
    } catch (error) {
      console.error("Erro ao obter competências:", error);
      return [];
    }
  }
}

document.addEventListener("DOMContentLoaded", () => {
  try {
    new EmpresaController("formCadastroEmpresa");
  } catch (error) {
    console.error("Falha ao inicializar Empresa Controller:", error);
    const notifier = NotificationService.getInstance();
    notifier.showErrors(["Erro ao carregar o formulário de cadastro"]);
  }
});
