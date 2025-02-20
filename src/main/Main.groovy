static void main(String[] args) {
    def sistema = new Sistema()
    sistema.listarEmpresas()
    sistema.listarCandidatos()
    Gerenciador gerenciador = new Gerenciador(sistema)
    Scanner scanner = new Scanner(System.in)

    while (true) {
        println "\n======= Linketinder ======="
        println "1. Cadastrar Candidato"
        println "2. Cadastrar Empresa"
        println "3. Listar Candidatos"
        println "4. Listar Empresas"
        println "5. Sair"
        print "Escolha uma opção: "

        String opcao = scanner.nextLine()

        switch (opcao) {
            case "1":
                gerenciador.cadastrarCandidato()
                break
            case "2":
                gerenciador.cadastrarEmpresa()
                break
            case "3":
                gerenciador.listarCandidatos()
                break
            case "4":
                gerenciador.listarEmpresas()
                break
            case "5":
                println "Saindo..."
                return
            default:
                println "Opção inválida! Tente novamente."
        }
    }
}
