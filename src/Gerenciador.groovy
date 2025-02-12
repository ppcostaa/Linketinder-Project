class Gerenciador {
    List<Candidato> candidatos
    List<Empresa> empresas
    Scanner scanner = new Scanner(System.in)

    Gerenciador(Sistema sistema) {
        this.candidatos = sistema.candidatos
        this.empresas = sistema.empresas
    }

    void cadastrarCandidato() {
        println "Digite o nome do candidato:"
        String nome = scanner.nextLine()

        println "Digite o e-mail do candidato:"
        String email = scanner.nextLine()

        println "Digite o estado do candidato:"
        String estado = scanner.nextLine()

        println "Digite o CEP do candidato:"
        String cep = scanner.nextLine()

        println "Digite uma breve descrição:"
        String descricao = scanner.nextLine()

        println "Digite as competências do candidato (separadas por vírgula):"
        List<String> competencias = scanner.nextLine().split(",").collect { it.trim() }

        println "Digite o CPF do candidato:"
        String cpf = scanner.nextLine()

        println "Digite a idade do candidato:"
        int idade = scanner.nextInt()
        scanner.nextLine()

        Candidato novoCandidato = new Candidato(nome, email, estado, cep, descricao, competencias, cpf, idade)
        candidatos.add(novoCandidato)

        println "Candidato cadastrado com sucesso!\n"
    }

    void cadastrarEmpresa() {
        println "Digite o nome da empresa:"
        String nome = scanner.nextLine()

        println "Digite o e-mail corporativo:"
        String email = scanner.nextLine()

        println "Digite o CNPJ:"
        String cnpj = scanner.nextLine()

        println "Digite o país da empresa:"
        String pais = scanner.nextLine()

        println "Digite o estado da empresa:"
        String estado = scanner.nextLine()

        println "Digite o CEP da empresa:"
        String cep = scanner.nextLine()

        println "Digite uma breve descrição da empresa:"
        String descricao = scanner.nextLine()

        println "Digite as competências esperadas (separadas por vírgula):"
        List<String> competencias = scanner.nextLine().split(",").collect { it.trim() }

        Empresa novaEmpresa = new Empresa(nome, email, cnpj, pais, estado, cep, descricao, competencias)
        empresas.add(novaEmpresa)

        println "Empresa cadastrada com sucesso!\n"
    }

    void listarCandidatos() {
        println "\nLista de Candidatos:"
        candidatos.each { println it.toString() }
    }

    void listarEmpresas() {
        println "\nLista de Empresas:"
        empresas.each { println it.toString() }
    }
}

