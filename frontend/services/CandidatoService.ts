import { CandidatoRepository } from "../repositories/CandidatoRepository";
import { IValidator } from "../interfaces/IValidator";
import { INotificationService } from "../interfaces/INotificationService";
import { Candidato } from "../models/Candidato";

export class CandidatoService {
  constructor(
    private repository: CandidatoRepository,
    private validator: IValidator,
    private notifier: INotificationService
  ) {}

  cadastrar(dados: any): boolean {
    try {
      const validation = this.validator.validate(dados);
      if (!validation.isValid) {
        this.notifier.showErrors(validation.errors);
        return false;
      }

      const candidato = new Candidato(
        dados.nome,
        dados.email,
        dados.estado,
        dados.cep,
        dados.descricao,
        dados.competencias,
        dados.cpf,
        dados.idade,
        dados.linkedin
      );

      this.repository.save(candidato);
      this.notifier.showSuccess("Candidato cadastrado com sucesso!");
      return true;
    } catch (error) {
      console.error("Erro no serviço de cadastro:", error);
      this.notifier.showErrors(["Erro interno ao processar cadastro"]);
      return false;
    }
  }

  notificar(mensagem: string, tipo: "success" | "error" = "success"): void {
    if (tipo === "success") {
      this.notifier.showSuccess(mensagem);
    } else {
      this.notifier.showErrors([mensagem]);
    }
  }
}
