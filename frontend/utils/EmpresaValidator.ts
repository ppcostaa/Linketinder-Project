import { IValidator } from "../interfaces/IValidator";
import { validadores } from "./validadores";

export class EmpresaValidator implements IValidator {
  validate(data: any): { isValid: boolean; errors: string[] } {
    const erros: string[] = [];

    if (!validadores.nome.test(data.nome)) {
      erros.push("Nome da empresa inválido");
    }

    if (!validadores.email.test(data.email)) {
      erros.push("E-mail corporativo inválido");
    }

    if (!validadores.cnpj.test(data.cnpj)) {
      erros.push("CNPJ inválido (formato esperado: 00.000.000/0000-00)");
    }

    if (!validadores.cep.test(data.cep)) {
      erros.push("CEP inválido (formato esperado: 00000-000)");
    }

    if (data.competencias.length === 0) {
      erros.push("Selecione pelo menos uma competência requerida");
    }

    return {
      isValid: erros.length === 0,
      errors: erros,
    };
  }
}
