import { INotificationService } from "../interfaces/INotificationService";
import { IValidator } from "../interfaces/IValidator";
import { Empresa } from "../models/Empresa";
import { EmpresaRepository } from "../repositories/EmpresaRepository";

export interface IEmpresaService {
  cadastrar(dados: any): boolean;
  notificar(mensagem: string, tipo?: "success" | "error"): void;
}
export class EmpresaService {
  constructor(
    private repository: EmpresaRepository,
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

      const empresa = new Empresa(
        dados.nome,
        dados.email,
        dados.estado,
        dados.cep,
        dados.descricao,
        dados.competencias,
        dados.cnpj,
        dados.pais
      );

      this.repository.save(empresa);
      this.notifier.showSuccess("Empresa cadastrada com sucesso!");
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
