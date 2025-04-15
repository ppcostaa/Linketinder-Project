import services.CandidatoService
import services.CompetenciaService
import services.EmpresaService
import services.VagaService

class Sistema {

    static void iniciarSistema(String[] args) {
        Scanner scanner = new Scanner(System.in)
        CandidatoService candidatoService = new CandidatoService()
        EmpresaService empresaService = new EmpresaService()
        VagaService vagaService = new VagaService()
        CompetenciaService competenciaService = new CompetenciaService()
        while (true) {
            println "\n======= Linketinder ======="
            println("✦•····· Cadastros ·····•✦");
            println "1. Cadastrar Candidato"
            println "2. Cadastrar Empresa"
            println "3. Cadastrar Vaga"
            println "4. Cadastrar Competência"
            println("✦•····· Listagens ·····•✦");
            println "5. Listar Candidatos"
            println "6. Listar Empresas"
            println "7. Listar Vagas"
            println "8. Listar Competências"
            println("✦•····· Edições ·····•✦");
            println "9. Editar Candidato"
            println "10. Editar Empresa"
            println "11. Editar Vaga"
            println "12. Editar Competência"
            println("✦•····· Exclusões ·····•✦");
            println "13. Remover Candidato"
            println "14. Remover Empresa"
            println "15. Remover Vaga"
            println "16. Remover Competência"
            println("✦•··········•✦");
            println "17. Sair"
            print "Escolha uma opção: "

            String opcao = scanner.nextLine()

            switch (opcao) {
                case "1":
                    candidatoService.salvarCandidato()
                    break
                case "2":
                    empresaService.salvarEmpresa()
                    break
                case "3":
                    vagaService.salvarVaga()
                    break
                case "4":
                    competenciaService.salvarCompetencia()
                    break
                case "5":
                    candidatoService.listarCandidatos()
                    break
                case "6":
                    empresaService.listarEmpresas()
                    break
                case "7":
                    vagaService.listarVagas()
                    break
                case "8":
                    competenciaService.listarCompetencias()
                    break
                case "9":
                    candidatoService.editarCandidato()
                    break
                case "10":
                    empresaService.editarEmpresa()
                    break
                case "11":
                    vagaService.editarVaga()
                    break
                case "12":
                    competenciaService.editarCompetencia()
                    break
                case "13":
                    candidatoService.excluirCandidato()
                    break
                case "14":
                    empresaService.excluirEmpresa()
                    break
                case "15":
                    vagaService.excluirVaga()
                    break
                case "16":
                    competenciaService.excluirCompetencia()
                case "17":
                    println "Saindo..."
                    return
                default:
                    println "Opção inválida! Tente novamente. (•◡•) /"
            }
        }
    }
}
