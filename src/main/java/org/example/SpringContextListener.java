package org.example;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import Database.Config.HibernateConfig;

@WebListener
public class SpringContextListener implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("🚀 Global Spring Context başlatılıyor...");
        
        ApplicationContext context = new AnnotationConfigApplicationContext(HibernateConfig.class);
        sce.getServletContext().setAttribute("springContext", context);
        
        System.out.println("✅ Global Spring Context başarıyla oluşturuldu!");
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ApplicationContext context = (ApplicationContext) 
            sce.getServletContext().getAttribute("springContext");
        
        if (context != null) {
            ((AnnotationConfigApplicationContext) context).close();
            System.out.println("🛑 Global Spring Context kapatıldı.");
        }
    }
}
