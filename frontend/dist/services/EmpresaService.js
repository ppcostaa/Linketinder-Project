import { Empresa } from "../models/Empresa";
export class EmpresaService {
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
            const empresa = new Empresa(dados.nome, dados.email, dados.estado, dados.cep, dados.descricao, dados.competencias, dados.cnpj, dados.pais);
            this.repository.save(empresa);
            this.notifier.showSuccess("Empresa cadastrada com sucesso!");
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
