import { Candidato } from "../models/Candidato";
export class CandidatoService {
    repository;
    validator;
    notifier;
    constructor(repository, validator, notifier) {
        this.repository = repository;
        this.validator = validator;
        this.notifier = notifier;
    }
    cadastrar(dados) {
        try {
            const validation = this.validator.validate(dados);
            if (!validation.isValid) {
                this.notifier.showErrors(validation.errors);
                return false;
            }
            const candidato = new Candidato(dados.nome, dados.email, dados.estado, dados.cep, dados.descricao, dados.competencias, dados.cpf, dados.idade, dados.linkedin);
            this.repository.save(candidato);
            this.notifier.showSuccess("Candidato cadastrado com sucesso!");
            return true;
        }
        catch (error) {
            console.error("Erro no serviço de cadastro:", error);
            this.notifier.showErrors(["Erro interno ao processar cadastro"]);
            return false;
        }
    }
    notificar(mensagem, tipo = "success") {
        if (tipo === "success") {
            this.notifier.showSuccess(mensagem);
        }
        else {
            this.notifier.showErrors([mensagem]);
        }
    }
}
