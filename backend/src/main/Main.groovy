package main

import controllers.CandidatoController
import controllers.EmpresaController

static void main(String[] args) {
    Scanner scanner = new Scanner(System.in)
    CandidatoController candidatoController = new CandidatoController()
    EmpresaController empresaController = new EmpresaController()
    while (true) {
        println "\n======= Linketinder ======="
        println "1. Cadastrar Candidato"
        println "2. Cadastrar Empresa"
        println "3. Listar Candidatos"
        println "4. Listar Empresas"
        println "5. Editar Candidato"
        println "6. Editar Empresa"
        println "7. Remover Candidato"
        println "8. Remover Empresa"
        println "9. Sair"
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
                candidatoController.index()
                break
            case "4":
                empresaController.index()
                break
            case "5":
                candidatoController.editarCandidatoMenu()
                break
            case "6":
                empresaController.editarEmpresaMenu()
                break
            case "7":
                candidatoController.excluirCandidatoMenu()
                break
            case "8":
                empresaController.excluirEmpresaMenu()
                break
            case "9":
                println "Saindo..."
                return
            default:
                println "Opção inválida! Tente novamente. (•◡•) /"
        }
    }
}