import { validadores } from "./validadores";
export class CandidatoValidator {
    validate(data) {
        const erros = [];
        if (!validadores.nome.test(data.nome)) {
            erros.push("Nome inválido");
        }
        if (!validadores.email.test(data.email)) {
            erros.push("E-mail inválido");
        }
        if (!validadores.cpf.test(data.cpf)) {
            erros.push("CPF inválido");
        }
        if (data.telefone && !validadores.telefone.test(data.telefone)) {
            erros.push("Telefone inválido");
        }
        if (data.linkedin && !validadores.linkedin.test(data.linkedin)) {
            erros.push("LinkedIn inválido");
        }
        return {
            isValid: erros.length === 0,
            errors: erros,
        };
    }
}
