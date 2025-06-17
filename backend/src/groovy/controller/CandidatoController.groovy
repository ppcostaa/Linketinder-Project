package controller

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import model.Candidato
import model.Competencia
import services.CandidatoService

import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.text.SimpleDateFormat

@WebServlet("/candidatos/*")
class CandidatoController extends HttpServlet {
    private final CandidatoService candidatoService = new CandidatoService()
    private final Gson gson = new GsonBuilder()
            .setDateFormat("dd/MM/yyyy")
            .create()

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json")
        response.setCharacterEncoding("UTF-8")

        try {
            String pathInfo = request.getPathInfo()

            if (pathInfo == null || pathInfo == "/") {
                List<Candidato> candidatos = candidatoService.buscarTodosCandidatos()
                String json = gson.toJson(candidatos)
                response.getWriter().write(json)
                response.setStatus(HttpServletResponse.SC_OK)
            } else {
                String[] pathParts = pathInfo.split("/")
                if (pathParts.length > 1) {
                    try {
                        int candidatoId = Integer.parseInt(pathParts[1])
                        Candidato candidato = candidatoService.buscarCandidatoPorId(candidatoId)

                        if (candidato != null) {
                            String json = gson.toJson(candidato)
                            response.getWriter().write(json)
                            response.setStatus(HttpServletResponse.SC_OK)
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Candidato não encontrado")
                        }
                    } catch (NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de candidato inválido")
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "URL inválida")
                }
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao processar requisição: " + e.getMessage())
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json")
        response.setCharacterEncoding("UTF-8")

        try {
            StringBuilder requestBody = new StringBuilder()
            BufferedReader reader = request.getReader()
            String line

            while ((line = reader.readLine()) != null) {
                requestBody.append(line)
            }

            Map<String, Object> requestData = gson.fromJson(requestBody.toString(), Map.class)

            if (!validarDadosCandidato(requestData, response)) {
                return
            }

            try {
                Candidato candidato = new Candidato()
                candidato.nome = requestData.get("nome").toString()
                candidato.sobrenome = requestData.get("sobrenome").toString()
                candidato.cpf = requestData.get("cpf").toString()

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy")
                candidato.dataNascimento = sdf.parse(requestData.get("dataNascimento").toString())

                List<Competencia> competencias = new ArrayList<>()
                if (requestData.containsKey("competencias") && requestData.get("competencias") instanceof List) {
                    List<Map> competenciasJson = (List<Map>) requestData.get("competencias")
                    competenciasJson.each { compJson ->
                        Competencia comp = new Competencia()
                        comp.competenciaId = compJson.competenciaId as Integer
                        comp.competenciaNome = compJson.competenciaNome as String
                        competencias.add(comp)
                    }
                }
                candidato.competencias = competencias

                String email = requestData.get("email").toString()
                String senha = requestData.get("senha").toString()
                String descricao = requestData.get("descricao").toString()
                String cep = requestData.get("cep").toString()
                String pais = requestData.get("pais").toString()

                Candidato novoCandidato = candidatoService.salvarCandidato(candidato, email, senha, descricao, cep, pais)

                if (novoCandidato != null) {
                    response.setStatus(HttpServletResponse.SC_CREATED)
                    String json = gson.toJson(novoCandidato)
                    response.getWriter().write(json)
                } else {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao criar candidato")
                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dados inválidos: " + e.getMessage())
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao processar requisição: " + e.getMessage())
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json")
        response.setCharacterEncoding("UTF-8")

        try {
            String pathInfo = request.getPathInfo()

            if (pathInfo != null && pathInfo.length() > 1) {
                String[] pathParts = pathInfo.split("/")
                if (pathParts.length > 1) {
                    try {
                        int candidatoId = Integer.parseInt(pathParts[1])

                        Candidato candidatoExistente = candidatoService.buscarCandidatoPorId(candidatoId)

                        if (candidatoExistente != null) {
                            StringBuilder requestBody = new StringBuilder()
                            BufferedReader reader = request.getReader()
                            String line

                            while ((line = reader.readLine()) != null) {
                                requestBody.append(line)
                            }

                            Map<String, Object> requestData = gson.fromJson(requestBody.toString(), Map.class)

                            if (requestData.containsKey("nome")) {
                                candidatoExistente.nome = requestData.get("nome").toString()
                            }

                            if (requestData.containsKey("sobrenome")) {
                                candidatoExistente.sobrenome = requestData.get("sobrenome").toString()
                            }

                            if (requestData.containsKey("cpf")) {
                                candidatoExistente.cpf = requestData.get("cpf").toString()
                            }

                            if (requestData.containsKey("dataNascimento")) {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy")
                                candidatoExistente.dataNascimento = sdf.parse(requestData.get("dataNascimento").toString())
                            }

                            if (requestData.containsKey("competencias") && requestData.get("competencias") instanceof List) {
                                List<Map> competenciasJson = (List<Map>) requestData.get("competencias")
                                List<Competencia> competencias = new ArrayList<>()

                                competenciasJson.each { compJson ->
                                    Competencia comp = new Competencia()
                                    comp.competenciaId = compJson.competenciaId as Integer
                                    comp.competenciaNome = compJson.competenciaNome as String
                                    competencias.add(comp)
                                }

                                candidatoExistente.competencias = competencias
                            }

                            boolean sucesso = candidatoService.editarCandidato(candidatoExistente)

                            if (sucesso) {
                                Candidato candidatoAtualizado = candidatoService.buscarCandidatoPorId(candidatoId)
                                String json = gson.toJson(candidatoAtualizado)
                                response.getWriter().write(json)
                                response.setStatus(HttpServletResponse.SC_OK)
                            } else {
                                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao atualizar candidato")
                            }
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Candidato não encontrado")
                        }
                    } catch (NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de candidato inválido")
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "URL inválida")
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "URL inválida")
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao processar requisição: " + e.getMessage())
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json")
        response.setCharacterEncoding("UTF-8")

        try {
            String pathInfo = request.getPathInfo()

            if (pathInfo != null && pathInfo.length() > 1) {
                String[] pathParts = pathInfo.split("/")
                if (pathParts.length > 1) {
                    try {
                        int candidatoId = Integer.parseInt(pathParts[1])

                        Candidato candidatoExistente = candidatoService.buscarCandidatoPorId(candidatoId)

                        if (candidatoExistente != null) {
                            boolean sucesso = candidatoService.excluirCandidato(candidatoId)

                            if (sucesso) {
                                response.setStatus(HttpServletResponse.SC_NO_CONTENT)
                            } else {
                                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao excluir candidato")
                            }
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Candidato não encontrado")
                        }
                    } catch (NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de candidato inválido")
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "URL inválida")
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "URL inválida")
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao processar requisição: " + e.getMessage())
        }
    }

    private boolean validarDadosCandidato(Map<String, Object> requestData, HttpServletResponse response) {
        List<String> camposObrigatorios = [
                "nome", "sobrenome", "cpf", "dataNascimento",
                "email", "senha", "descricao", "cep", "pais"
        ]

        List<String> camposFaltantes = camposObrigatorios.findAll { campo ->
            !requestData.containsKey(campo) || requestData.get(campo) == null || requestData.get(campo).toString().trim().isEmpty()
        }

        if (!camposFaltantes.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Campos obrigatórios faltando: " + camposFaltantes.join(", "))
            return false
        }

        return true
    }
}