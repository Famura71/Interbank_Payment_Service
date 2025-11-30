package Api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import Database.DAO.UserDao;
import Database.Entities.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserDao userDao;

    @Override
    public void init() throws ServletException {
        try {
            System.out.println("LoginServlet init() başlatılıyor...");

            // Global context'ten bean'i al
            ApplicationContext context = (ApplicationContext)
                getServletContext().getAttribute("springContext");

            if (context == null) {
                throw new ServletException("Spring Context bulunamadı!");
            }

            userDao = context.getBean(UserDao.class);

            System.out.println("LoginServlet başarıyla başlatıldı!");
        } catch (Exception e) {
            System.err.println("LoginServlet init() başarısız: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("LoginServlet başlatılamadı", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // Form verilerini al
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String bankName = request.getParameter("bankName");

        try {
            // Database'den kullaniciyi kontrol et
            User user = userDao.login(email, password, bankName);

            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("bank", bankName);

                out.print(buildSuccessResponse(user, bankName));
            } else {
                out.print("{\"success\": false, \"message\": \"Kullanici adi veya sifre hatali!\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\": false, \"message\": \"Bir hata olustu: " + escapeJson(e.getMessage()) + "\"}");
        }
    }

    public void destroy() {
        // Context'i kapatma! Listener halleder
    }

    private String buildSuccessResponse(User user, String bankName) {
        String safeName = escapeJson(user.getName());
        String safeEmail = escapeJson(user.getEmail());
        String safeBank = escapeJson(bankName);
        long safeId = user.getId() != null ? user.getId() : 0L;
        double balance = Double.isFinite(user.getBalance()) ? user.getBalance() : 0.0;

        return String.format(
            Locale.US,
            "{\"success\": true, \"message\": \"Giris basarili\", \"user\": {\"id\": %d, \"name\": \"%s\", \"email\": \"%s\", \"balance\": %.2f, \"bankName\": \"%s\"}}",
            safeId,
            safeName,
            safeEmail,
            balance,
            safeBank
        );
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
