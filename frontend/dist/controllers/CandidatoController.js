import { CandidatoRepository } from "../repositories/CandidatoRepository";
import { CandidatoService } from "../services/CandidatoService";
import { LocalStorageService } from "../services/LocalStorageService";
import { NotificationService } from "../services/NotificacaoService";
import { CandidatoValidator } from "../utils/CandidatoValidator";
export class CandidatoController {
    service;
    form;
    notifier;
    constructor(formId) {
        this.form = document.getElementById(formId);
        if (!this.form) {
            throw new Error(`Formulário com ID ${formId} não encontrado`);
        }
        const storage = new LocalStorageService();
        const repository = new CandidatoRepository(storage);
        const validator = new CandidatoValidator();
        this.notifier = NotificationService.getInstance();
        this.service = new CandidatoService(repository, validator, this.notifier);
        this.setupEvents();
    }
    setupEvents() {
        this.form.addEventListener("submit", (e) => this.handleSubmit(e));
        const cancelButton = document.getElementById("botaoCancelarCandidato");
        if (cancelButton) {
            cancelButton.addEventListener("click", (e) => {
                e.preventDefault();
                this.handleCancel(e);
            });
        }
        const confirmButton = document.getElementById("botaoConfirmarCandidato");
        if (confirmButton) {
            confirmButton.addEventListener("click", () => {
                this.form.dispatchEvent(new Event("submit"));
            });
        }
    }
    handleSubmit(event) {
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
        }
        catch (error) {
            console.error("Erro no cadastro:", error);
            this.service.notificar("Erro ao processar cadastro", "error");
        }
    }
    handleCancel(event) {
        event.preventDefault();
        this.service.notificar("Cadastro cancelado!");
        setTimeout(() => {
            window.location.href = "../index.html";
        }, 2000);
    }
    getFormData() {
        const getValue = (id) => {
            const element = document.getElementById(id);
            return element?.value.trim() || "";
        };
        return {
            nome: getValue("nome"),
            email: getValue("email"),
            estado: getValue("estado"),
            cep: getValue("cep"),
            cpf: getValue("cpf"),
            idade: Number(getValue("idade")) || 0,
            descricao: getValue("descricao"),
            competencias: this.getSelectedCompetencias(),
            linkedin: getValue("linkedin"),
        };
    }
    getSelectedCompetencias() {
        try {
            const checkboxes = document.querySelectorAll(".custom-checkout:checked");
            return Array.from(checkboxes)
                .map((cb) => {
                const input = cb;
                return input.id.replace("competencia", "");
            })
                .filter(Boolean);
        }
        catch (error) {
            console.error("Erro ao obter competências:", error);
            return [];
        }
    }
}
document.addEventListener("DOMContentLoaded", () => {
    try {
        new CandidatoController("formCadastroCandidato");
    }
    catch (error) {
        console.error("Falha ao inicializar CandidatoController:", error);
        const notifier = NotificationService.getInstance();
        notifier.showErrors(["Erro ao carregar o formulário de cadastro"]);
    }
});
