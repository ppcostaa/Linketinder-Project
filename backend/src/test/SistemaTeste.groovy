//package test
//
//import model.Candidato
//import model.Competencia
//import model.Empresa
//import model.Usuario
//import model.Vaga
//
//class SistemaTeste {
//        static void main(String[] args) {
//            // Teste de atualização e busca de registros
//            testeBuscaAtualiza()
//
//            // Teste do relacionamento N:N entre candidato e competências
//            testeRelacionamentoCompetencias()
//
//            // Teste do relacionamento 1:N entre empresa e vagas
//            testeRelacionamentoVagas()
//        }
//
//        static void testeBuscaAtualiza() {
//            // Buscar candidato por ID
//            GerenciadorUsuario gerenciadorUsuario = new GerenciadorUsuario()
//            GerenciadorCandidato gerenciadorCandidato = new GerenciadorCandidato()
//            Candidato candidato = gerenciadorCandidato.findById(1)
//            Usuario usuario = gerenciadorUsuario.findById()
//
//            if (candidato) {
//                println "Candidato encontrado: ${candidato.nome} ${candidato.sobrenome}"
//
//                // Atualizar informações do candidato
//                usuario.descricao = "Desenvolvedor com experiência em Java e Python"
//                boolean atualizado = gerenciadorCandidato.update(candidato)
//
//                if (atualizado) {
//                    println "Candidato atualizado com sucesso!"
//                }
//            }
//
//            // Buscar empresa por ID
//            GerenciadorEmpresa gerenciadorEmpresa = new GerenciadorEmpresa()
//            Empresa empresa = gerenciadorEmpresa.findById(1)
//
//            if (empresa) {
//                println "Empresa encontrada: ${empresa.nomeEmpresa}"
//                println "Total de vagas: ${empresa.vagas.size()}"
//            }
//        }
//
//        static void testeRelacionamentoCompetencias() {
//            GerenciadorCompetencia gerenciadorCompetencia = new GerenciadorCompetencia()
//            GerenciadorCandidato gerenciadorCandidato = new GerenciadorCandidato()
//
//            // Listar todas competências
//            List<Competencia> todasCompetencias = gerenciadorCompetencia.findAll()
//            println "Total de competências: ${todasCompetencias.size()}"
//            todasCompetencias.each { println "- ${it.nomeCompetencia}" }
//
//            // Buscar candidato e suas competências
//            Candidato candidato = gerenciadorCandidato.findById(1)
//            if (candidato) {
//                println "Competências do candidato ${candidato.nome}:"
//                candidato.competencias.each { println "- ${it.nomeCompetencia}" }
//
//                // Adicionar nova competência
//                Competencia novaCompetencia = new Competencia(nomeCompetencia: "Docker")
//                candidato.competencias.add(novaCompetencia)
//
//                // Atualizar candidato com nova competência
//                gerenciadorCandidato.update(candidato)
//
//                // Verificar se a competência foi adicionada
//                candidato = gerenciadorCandidato.findById(1)
//                println "Competências atualizadas:"
//                candidato.competencias.each { println "- ${it.nomeCompetencia}" }
//            }
//        }
//
//        static void testeRelacionamentoVagas() {
//            GerenciadorEmpresa gerenciadorEmpresa = new GerenciadorEmpresa()
//            GerenciadorVaga gerenciadorVaga = new GerenciadorVaga()
//
//            // Buscar empresa e suas vagas
//            Empresa empresa = gerenciadorEmpresa.findById(1)
//            if (empresa) {
//                println "Vagas da empresa ${empresa.nomeEmpresa}:"
//                empresa.vagas.each { println "- ${it.nomeVaga}" }
//
//                // Criar uma nova vaga
//                Vaga novaVaga = new Vaga(
//                        idEmpresa: empresa.idEmpresa,
//                        nomeVaga: "Desenvolvedor Frontend",
//                        descricaoVaga: "Desenvolvimento de interfaces com Angular",
//                        localEstado: "Rio de Janeiro",
//                        localCidade: "Rio de Janeiro"
//                )
//
//                // Salvar a vaga no banco
//                novaVaga = gerenciadorVaga.create(novaVaga)
//                println "Nova vaga cadastrada: ${novaVaga.nomeVaga}"
//
//                // Buscar empresa novamente para ver as vagas atualizadas
//                empresa = gerenciadorEmpresa.findById(1)
//                println "Total de vagas após inclusão: ${empresa.vagas.size()}"
//
//                // Atualizar uma vaga existente
//                if (!empresa.vagas.isEmpty()) {
//                    Vaga vagaExistente = empresa.vagas[0]
//                    vagaExistente.descricaoVaga += " (Remoto)"
//                    gerenciadorVaga.update(vagaExistente)
//                    println "Vaga atualizada: ${vagaExistente.nomeVaga}"
//                }
//            }
//        }
//    }
//
