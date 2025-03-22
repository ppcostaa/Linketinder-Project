////package main
////import database.Conexao
////
////
////class Sistema {
////
////    def sql
////
////    Sistema() {
////        this.sql = Conexao.obterConexao()
////    }
////
////    void listarEmpresas() {
////        def empresas = sql.rows("SELECT * FROM EMPRESAS")
////        println "===== LISTA DE EMPRESAS ====="
////        empresas.each { empresa ->
////            println """Empresa: ${empresa.NOME_EMPRESA}
////Email: ${empresa.EMAIL}
////Descrição: ${empresa.DESCRICAO}
////Competências: ${empresa.COMPETENCIAS}
////CNPJ: ${empresa.CNPJ}
////País: ${empresa.PAIS}
////--------------------------------------"""
////        }
////    }
////
////    def listarCandidatos() {
////        def candidatos = sql.rows("SELECT * FROM CANDIDATOS")
////        if (candidatos.isEmpty()) {
////            println "Nenhum candidato encontrado."
////        } else {
////            candidatos.each { candidato ->
////                println """Candidato: ${candidato.NOME}
////Email: ${candidato.EMAIL}
////Descrição: ${candidato.DESCRICAO}
////Competências: ${candidato.COMPETENCIAS}
////Idade: ${candidato.IDADE}
////CPF: ${candidato.CPF}
////--------------------------------------"""
////            }
////        }
////    }
////
////}
//package main
//
//import utils.*
//import model.*
//
//class Sistema {
//    static void main(String[] args) {
//        cadastrarCandidato()
//
//        cadastrarEmpresa()
//    }
//
//    static void cadastrarCandidato() {
//        try {
//            Candidato sandubinha = new Candidato(
//                    nome: 'Sandubinha',
//                    sobrenome: 'Silva',
//                    dataNascimento: new Date(90, 4, 15), // 15/05/1990
//                    cpf: '12345678900'
//            )
//
//            List<Competencia> competencias = [
//                    new Competencia(nomeCompetencia: 'Python'),
//                    new Competencia(nomeCompetencia: 'Java'),
//                    new Competencia(nomeCompetencia: 'Angular')
//            ]
//            sandubinha.competencias = competencias
//
//            GerenciadorCandidato gerenciadorCandidato = new GerenciadorCandidato()
//            sandubinha = gerenciadorCandidato.create(
//                    sandubinha,
//                    'sandubinha@email.com',
//                    'senha123',
//                    'Busco novas oportunidades de trabalho.'
//            )
//
//            println "Candidato ${sandubinha.nome} cadastrado com sucesso! ID: ${sandubinha.idCandidato}"
//        } catch (Exception e) {
//            println "Erro ao cadastrar candidato: ${e.message}"
//            e.printStackTrace()
//        }
//    }
//
//    static void cadastrarEmpresa() {
//        try {
//            Empresa pastelsoft = new Empresa(
//                    nomeEmpresa: 'Pastelsoft',
//                    cnpj: '12.345.678/0001-90'
//            )
//
//            GerenciadorEmpresa gerenciadorEmpresa = new GerenciadorEmpresa()
//            pastelsoft = gerenciadorEmpresa.create(
//                    pastelsoft,
//                    'contato@pastelsoft.com',
//                    'senha303',
//                    'Software ERP para redes de restaurantes.'
//            )
//
//            println "Empresa ${pastelsoft.nomeEmpresa} cadastrada com sucesso! ID: ${pastelsoft.idEmpresa}"
//
//            // Cria uma vaga
//            Vaga vaga = new Vaga(
//                    idEmpresa: pastelsoft.idEmpresa,
//                    nomeVaga: 'Desenvolvedor Java',
//                    descricaoVaga: 'Desenvolvimento de sistemas para ERP.',
//                    localEstado: 'São Paulo',
//                    localCidade: 'São Paulo'
//            )
//
//            GerenciadorVaga gerenciadorVaga = new GerenciadorVaga()
//            vaga = gerenciadorVaga.create(vaga)
//
//            println "Vaga '${vaga.nomeVaga}' cadastrada com sucesso! ID: ${vaga.idVaga}"
//
//            pastelsoft = gerenciadorEmpresa.findById(pastelsoft.idEmpresa)
//            println "Empresa ${pastelsoft.nomeEmpresa} possui ${pastelsoft.vagas.size()} vagas cadastradas."
//        } catch (Exception e) {
//            println "Erro ao cadastrar empresa ou vaga: ${e.message}"
//            e.printStackTrace()
//        }
//    }
//}