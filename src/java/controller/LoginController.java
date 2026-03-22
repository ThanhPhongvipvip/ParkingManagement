package controller;

import dal.UserDAO;
import model.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String user = request.getParameter("username");
        String pass = request.getParameter("password");

        UserDAO dao = new UserDAO();
        User account = dao.checkLogin(user, pass);

        if (account == null) {
            request.setAttribute("error", "Tài khoản hoặc mật khẩu không chính xác, hoặc tài khoản đã bị khóa!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("account", account);

            int role = account.getRoleID();
            switch (role) {
                case 1:
                case 2:
                    // Gộp chung Admin (1) và Staff (2) về cùng 1 dashboard
                    response.sendRedirect("admin-dashboard");
                    break;
                case 3:
                    response.sendRedirect("views/customer-dashboard.jsp");
                    break;
                default:
                    response.sendRedirect("login");
                    break;
            }
        }
    }
}