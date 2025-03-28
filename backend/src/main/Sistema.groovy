package main

import controllers.CandidatoController
import controllers.CompetenciaController
import controllers.EmpresaController
import controllers.VagaController


class Sistema {

static void iniciarSistema(String[] args) {
    Scanner scanner = new Scanner(System.in)
    CandidatoController candidatoController = new CandidatoController()
    EmpresaController empresaController = new EmpresaController()
    VagaController vagaController = new VagaController()
    CompetenciaController competenciaController = new CompetenciaController()
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
                candidatoController.salvarCandidatoMenu()
                break
            case "2":
                empresaController.salvarEmpresaMenu()
                break
            case "3":
                vagaController.salvarVaga()
                break
            case "4":
                competenciaController.salvarCompetencia()
                break
            case "5":
                candidatoController.index()
                break
            case "6":
                empresaController.index()
                break
            case "7":
                vagaController.index()
                break
            case "8":
                competenciaController.index()
            case "9":
                candidatoController.editarCandidatoMenu()
                break
            case "10":
                empresaController.editarEmpresaMenu()
                break
            case "11":
                vagaController.editarVaga()
                break
            case "12":
                competenciaController.editarCompetencia()
                break
            case "13":
                candidatoController.excluirCandidatoMenu()
                break
            case "14":
                empresaController.excluirEmpresaMenu()
                break
            case "15":
                vagaController.excluirVaga()
                break
            case "16":
                competenciaController.excluirCompetencia()
            case "17":
                println "Saindo..."
                return
            default:
                println "Opção inválida! Tente novamente. (•◡•) /"
        }
    }
}
}
