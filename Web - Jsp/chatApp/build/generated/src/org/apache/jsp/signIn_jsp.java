package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import controller.dao.LoginDAO;
import model.SignInResponse;
import com.google.gson.Gson;
import model.User;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Enumeration;

public final class signIn_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");

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

    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
