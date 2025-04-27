package services


import DAO.CompetenciaDAO
import DAO.EmpresaDAO
import DAO.VagaDAO
import model.Competencia
import model.Empresa
import model.Vaga

class VagaService {
    Scanner scanner = new Scanner(System.in)
    VagaDAO vagaRepository = new VagaDAO()
    CompetenciaDAO competenciaRepository = new CompetenciaDAO()
    CompetenciaService competenciaService = new CompetenciaService()
    EmpresaDAO empresaRepository = new EmpresaDAO()

    def listarVagas() {
        List<Vaga> vagas = vagaRepository.listarVagas()
        if (vagas == null || vagas.isEmpty()) {
            println "Nenhuma vaga cadastrada. (•◡•) /"
        } else {
            println("✦•····· Lista de Vagas ·····•✦")
            vagas.each { vaga ->
                println "ID: ${vaga.vagaId}, \n" +
                        "Título: ${vaga.titulo} \n" +
                        "Estado: ${vaga.estado}, \n" +
                        "Cidade: ${vaga.cidade}, \n" +
                        "Competências: ${vaga.competencias}, \n" +
                        "Descrição: ${vaga.descricao}, \n" +
                        "✦•·····•✦•·····•✦"
            }
        }
    }

    void salvarVaga() {
        List<Empresa> empresas = empresaRepository.listarEmpresas()
        if (empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada. É necessário cadastrar uma empresa primeiro."
            return
        }

        println "Empresas disponíveis:"
        empresas.each { empresa ->
            println "${empresa.empresaId}: ${empresa.empresaNome}"
        }

        print "Selecione o ID da empresa para a qual a vaga será criada: "
        int empresaId = scanner.nextInt()
        scanner.nextLine()

        Empresa empresaSelecionada = empresas.find { it.empresaId == empresaId }
        if (!empresaSelecionada) {
            println "Empresa não encontrada."
            return
        }

        println "\nInforme os dados da Vaga:"
        print "Título: "
        String titulo = scanner.nextLine()
        print "Descrição: "
        String descricao = scanner.nextLine()
        print "Estado: "
        String estado = scanner.nextLine()
        print "Cidade: "
        String cidade = scanner.nextLine()
        List<Competencia> competencias = competenciaService.salvarCompetenciaPorUsuario()

        Vaga vaga = new Vaga(
                empresaId: empresaId,
                titulo: titulo,
                descricao: descricao,
                estado: estado,
                cidade: cidade,
                competencias: competencias
        )

        Vaga vagaSalva = vagaRepository.salvarVaga(vaga)
        if (vagaSalva) {
            println "Vaga criada com sucesso para a empresa ${empresaSelecionada.empresaNome}!"
        } else {
            println "Erro ao criar vaga."
        }
    }

    def editarVaga() {
        listarVagas()
        print "Informe o ID da vaga que deseja atualizar: "
        int idVaga = scanner.nextInt()
        scanner.nextLine()

        Vaga vagaParaEditar = vagaRepository.listarVagaPorId(idVaga)
        if (vagaParaEditar == null) {
            println "Vaga não encontrada. (╥﹏╥)"
            return
        }

        println "\nO que você deseja atualizar?"
        println "1. Título"
        println "2. Estado"
        println "3. Cidade"
        println "4. Competências"
        println "5. Descrição"
        print "(Digite os números separados por vírgula): "
        String opcoes = scanner.nextLine()

        List<Integer> opcoesSelecionadas = opcoes.split(',').collect { it.trim().toInteger() }
        List<Integer> opcoesValidas = [1, 2, 3, 4, 5]


        if (opcoesSelecionadas.any { !opcoesValidas.contains(it) }) {
            println "Opção inválida! Escolha números entre 1 e 5. (•◡•) /"
        }

        if (opcoesSelecionadas.contains(1)) {
            print "Digite o novo título: "
            vagaParaEditar.titulo = scanner.nextLine()
        }
        if (opcoesSelecionadas.contains(2)) {
            print "Digite o novo estado: "
            vagaParaEditar.estado = scanner.nextLine()
        }
        if (opcoesSelecionadas.contains(3)) {
            print "Digite o novo cidade: "
            vagaParaEditar.cidade = scanner.nextLine()
        }
        if (opcoesSelecionadas.contains(4)) {
            List<Competencia> competenciasDisponiveis = competenciaRepository.listarCompetencias()
            println "Competências disponíveis:"
            competenciasDisponiveis.each { competencia ->
                println "${competencia.competenciaId}: ${competencia.competenciaNome}"
            } print "Digite os IDs das competências separados por vírgula: "
            String input = scanner.nextLine()
            List<Integer> idsCompetencias = input.split(',').collect { it.trim().toInteger() }
            List<Competencia> competenciasSelecionadas = competenciasDisponiveis.findAll { competencia ->
                idsCompetencias.contains(competencia.competenciaId)
            }

            if (competenciasSelecionadas.isEmpty()) {
                println "Nenhuma competência válida selecionada."
                return
            }
            vagaParaEditar.competencias = competenciasSelecionadas
        }
        if (opcoesSelecionadas.contains(5)) {
            print "Digite a nova Descrição: "
            vagaParaEditar.descricao = scanner.nextLine()
        }

        boolean sucessoVaga = vagaRepository.editarVaga(vagaParaEditar)

        if (sucessoVaga) {
            println "Vaga atualizada com sucesso! （っ＾▿＾）"
        } else {
            println "Erro ao atualizar vaga. (╥﹏╥)"
        }
    }

    def excluirVaga() {
        listarVagas()
        print "Informe o ID da vaga que deseja deletar: "
        int idVagaExcluir = scanner.nextInt()
        scanner.nextLine()

        def vagaParaExcluir = vagaRepository.listarVagaPorId(idVagaExcluir)
        if (!vagaParaExcluir) {
            println "Vaga não encontrada. (╥﹏╥)"
            return false
        }

        def sucesso = vagaRepository.excluirVaga(idVagaExcluir)
        if (sucesso) {
            println "Vaga deletada com sucesso!（っ＾▿＾）"
            return true
        } else {
            println "Erro ao deletar vaga. (╥﹏╥)"
            return false
        }
    }
}