package controller

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import model.Competencia
import model.Vaga
import services.VagaService

import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/vagas/*")
class VagaController extends HttpServlet {
    private final VagaService vagaService = new VagaService()
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
                List<Vaga> vagas = vagaService.buscarTodasVagas()
                String json = gson.toJson(vagas)
                response.getWriter().write(json)
                response.setStatus(HttpServletResponse.SC_OK)
            } else {
                String[] pathParts = pathInfo.split("/")
                if (pathParts.length > 1) {
                    try {
                        int vagaId = Integer.parseInt(pathParts[1])
                        Vaga vaga = vagaService.buscarVagaPorId(vagaId)

                        if (vaga != null) {
                            String json = gson.toJson(vaga)
                            response.getWriter().write(json)
                            response.setStatus(HttpServletResponse.SC_OK)
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Vaga não encontrada")
                        }
                    } catch (NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de vaga inválido")
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

            if (!validarDadosVaga(requestData, response)) {
                return
            }

            try {
                Vaga vaga = new Vaga()
                vaga.titulo = requestData.get("titulo").toString()
                vaga.descricao = requestData.get("descricao").toString()

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
                vaga.competencias = competencias

                vaga.estado = requestData.get("estado").toString()
                vaga.cidade = requestData.get("cidade").toString()

                Vaga novaVaga = vagaService.salvarVaga(vaga)

                if (novaVaga != null) {
                    response.setStatus(HttpServletResponse.SC_CREATED)
                    String json = gson.toJson(novaVaga)
                    response.getWriter().write(json)
                } else {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao criar vaga")
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
                        int vagaId = Integer.parseInt(pathParts[1])

                        Vaga vagaExistente = vagaService.buscarVagaPorId(vagaId)

                        if (vagaExistente != null) {
                            StringBuilder requestBody = new StringBuilder()
                            BufferedReader reader = request.getReader()
                            String line

                            while ((line = reader.readLine()) != null) {
                                requestBody.append(line)
                            }

                            Map<String, Object> requestData = gson.fromJson(requestBody.toString(), Map.class)

                            if (requestData.containsKey("titulo")) {
                                vagaExistente.titulo = requestData.get("titulo").toString()
                            }

                            if (requestData.containsKey("descricao")) {
                                vagaExistente.descricao = requestData.get("descricao").toString()
                            }

                            if (requestData.containsKey("cidade")) {
                                vagaExistente.cidade = requestData.get("cidade").toString()
                            }

                            if (requestData.containsKey("estado")) {
                                vagaExistente.estado = requestData.get("estado").toString()
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

                                vagaExistente.competencias = competencias
                            }

                            boolean sucesso = vagaService.editarVaga(vagaExistente)

                            if (sucesso) {
                                Vaga vagaAtualizada = vagaService.buscarVagaPorId(vagaId)
                                String json = gson.toJson(vagaAtualizada)
                                response.getWriter().write(json)
                                response.setStatus(HttpServletResponse.SC_OK)
                            } else {
                                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao atualizar vaga")
                            }
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Vaga não encontrada")
                        }
                    } catch (NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de vaga inválido")
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
                        int vagaId = Integer.parseInt(pathParts[1])

                        Vaga vagaExistente = vagaService.buscarVagaPorId(vagaId)

                        if (vagaExistente != null) {
                            boolean sucesso = vagaService.excluirVaga(vagaId)

                            if (sucesso) {
                                response.setStatus(HttpServletResponse.SC_NO_CONTENT)
                            } else {
                                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao excluir vaga")
                            }
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Vaga não encontrada")
                        }
                    } catch (NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de vaga inválido")
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

    private boolean validarDadosVaga(Map<String, Object> requestData, HttpServletResponse response) {
        List<String> camposObrigatorios = [
                "titulo", "estado", "cidade", "descricao"
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
