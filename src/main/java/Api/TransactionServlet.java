package Api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import Message.Kafka.TransactionProducer;
import Database.DAO.UserDao;
import Database.Entities.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@WebServlet("/transaction")
public class TransactionServlet extends HttpServlet {

    private TransactionProducer transactionProducer;
    private UserDao userDao;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        try {
            System.out.println("TransactionServlet init() başlatılıyor...");

            ApplicationContext context = (ApplicationContext)
                getServletContext().getAttribute("springContext");

            if (context == null) {
                throw new ServletException("Spring Context bulunamadı!");
            }

            transactionProducer = context.getBean(TransactionProducer.class);
            userDao = context.getBean(UserDao.class);

            System.out.println("TransactionServlet başarıyla başlatıldı!");
        } catch (Exception e) {
            System.err.println("TransactionServlet init() başarısız: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("TransactionServlet başlatılamadı", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Form parametrelerini al
            String transactionType = request.getParameter("type"); // deposit, withdraw, transfer
            String userEmail = request.getParameter("userEmail");
            String amountStr = request.getParameter("amount");
            String description = request.getParameter("description");
            String bankName = request.getParameter("bankName"); // Bank A, Bank B, Bank C
            
            // Transfer için ek parametreler
            String receiverName = request.getParameter("receiverName");
            String receiverBank = request.getParameter("receiverBank");

            // Validasyon
            if (transactionType == null || userEmail == null || amountStr == null || bankName == null) {
                out.print("{\"success\": false, \"message\": \"Eksik parametreler!\"}");
                return;
            }

            double amount = Double.parseDouble(amountStr);

            // Kullanıcıyı database'den al
            User user = userDao.getByEmail(userEmail);
            if (user == null) {
                out.print("{\"success\": false, \"message\": \"Kullanıcı bulunamadı!\"}");
                return;
            }

            // Transaction ID oluştur (Unique)
            String transactionId = generateTransactionId(transactionType, user.getId());

            // JSON mesajı oluştur
            ObjectNode jsonMessage = objectMapper.createObjectNode();
            jsonMessage.put("transactionId", transactionId);
            jsonMessage.put("type", transactionType);
            jsonMessage.put("userEmail", userEmail);
            jsonMessage.put("userId", user.getId());
            jsonMessage.put("userName", user.getName());
            jsonMessage.put("amount", amount);
            jsonMessage.put("bankName", bankName);
            jsonMessage.put("description", description != null ? description : "");
            jsonMessage.put("timestamp", LocalDateTime.now().toString());

            // Transfer için ek bilgiler
            if ("transfer".equals(transactionType)) {
                if (receiverName == null || receiverBank == null) {
                    out.print("{\"success\": false, \"message\": \"Transfer için alıcı bilgileri eksik!\"}");
                    return;
                }
                jsonMessage.put("receiverName", receiverName);
                jsonMessage.put("receiverBank", receiverBank);
            }

            // Kafka'ya gönder
            String jsonString = objectMapper.writeValueAsString(jsonMessage);
            transactionProducer.sendTransaction(transactionId, jsonString);

            // Başarılı yanıt
            out.print(String.format(Locale.US,
                "{\"success\": true, \"message\": \"Transaction başarıyla gönderildi!\", \"transactionId\": \"%s\"}",
                transactionId));

        } catch (NumberFormatException e) {
            out.print("{\"success\": false, \"message\": \"Geçersiz miktar!\"}");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\": false, \"message\": \"Bir hata oluştu: " + escapeJson(e.getMessage()) + "\"}");
        }
    }

    /**
     * Transaction ID oluştur: TYPE-USERID-TIMESTAMP
     * Örnek: DEPOSIT-123-20251202153045
     */
    private String generateTransactionId(String type, Long userId) {
        String timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return String.format("%s-%d-%s", type.toUpperCase(), userId, timestamp);
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
