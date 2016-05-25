<%@page import="controller.dao.LoginDAO"%>
<%@page import="model.SignInResponse"%>
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

        PrintWriter printWriter = response.getWriter();
        SignInResponse signInResponse = new SignInResponse();

        User userRequest = new Gson().fromJson(stringBuilder.toString(), User.class);

        if (userRequest != null) {
            boolean userExist = new LoginDAO().getUserUsernameExist(userRequest.getUsername());

            if (userExist) {
                signInResponse.setSuccess(false);
                signInResponse.setUser(new User());
                signInResponse.setUsernameExist(true);
            } else {
                boolean emailExist = new LoginDAO().getUserEmailExist(userRequest.getEmail());

                if (emailExist) {
                    signInResponse.setSuccess(false);
                    signInResponse.setUser(new User());
                    signInResponse.setEmailExist(true);
                } else {
                    User userResponse = new LoginDAO().insertUser(userRequest);

                    if (userResponse != null) {
                        signInResponse.setUser(userResponse);
                        signInResponse.setSuccess(true);
                    } else {
                        signInResponse.setUser(new User());
                        signInResponse.setSuccess(false);
                    }
                }
            }

            printWriter.println(new Gson().toJson(signInResponse));
        }
    }
%>