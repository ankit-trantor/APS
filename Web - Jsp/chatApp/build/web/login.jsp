<%@page import="model.LoginResponse"%>
<%@page import="controller.dao.LoginDAO"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="model.User"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.Enumeration"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    response.setContentType("application/json");

    if (request.getMethod().equalsIgnoreCase("POST")) {

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = request.getReader();
        String line;
        // Loop para pegar o texto em json
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        User userResponse = null;
        PrintWriter printWriter = response.getWriter();
        LoginResponse loginResponse = new LoginResponse();

        User userRequest = new Gson().fromJson(stringBuilder.toString(), User.class);

        if (userRequest != null) {
            userResponse = new LoginDAO().getUserLogged(userRequest.getUsername());

            if (userResponse != null) {

                if (userResponse.getUsername().equals(userRequest.getUsername()) && userResponse.getPassword().equals(userRequest.getPassword())) {
                    loginResponse.setUser(userResponse);
                    loginResponse.getUser().setPassword(null); // para nao devolver a senha
                    loginResponse.setSuccess(true);
                } else {
                    loginResponse.setSuccess(false);
                    loginResponse.setUserOrPassworInvalid(true);
                    loginResponse.setUser(new User());
                }
            } else {
                loginResponse.setSuccess(false);
                loginResponse.setUser(new User());
                loginResponse.setUserNotFound(true);
            }
        }

        printWriter.println(new Gson().toJson(loginResponse));
    }
%>