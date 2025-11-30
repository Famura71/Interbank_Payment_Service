package Api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import Database.Config.HibernateConfig;
import Database.DAO.UserDao;
import Database.Entities.User;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    private UserDao userDao;
    private ApplicationContext context;
    
    @Override
    public void init() throws ServletException {
        // Spring context'i başlat
        context = new AnnotationConfigApplicationContext(HibernateConfig.class);
        userDao = context.getBean(UserDao.class);
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
            // Database'den kullanıcıyı kontrol et
            User user = userDao.login(email, password, bankName);
            
            if (user != null) {
                // Giriş başarılı
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("bank", bankName);
                
                out.print("{\"success\": true, \"message\": \"Giriş başarılı\"}");
            } else {
                // Giriş başarısız
                out.print("{\"success\": false, \"message\": \"Kullanıcı adı veya şifre hatalı!\"}");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\": false, \"message\": \"Bir hata oluştu: " + e.getMessage() + "\"}");
        }
    }
    
    @Override
    public void destroy() {
        if (context != null) {
            ((AnnotationConfigApplicationContext) context).close();
        }
    }
}
