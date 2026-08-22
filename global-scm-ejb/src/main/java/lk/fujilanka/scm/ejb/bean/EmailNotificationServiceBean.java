package lk.fujilanka.scm.ejb.bean;

import jakarta.ejb.Stateless;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lk.fujilanka.scm.ejb.local.EmailNotificationServiceLocal;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class EmailNotificationServiceBean implements EmailNotificationServiceLocal {

    private static final Logger LOGGER = Logger.getLogger(EmailNotificationServiceBean.class.getName());

    // Resolve credentials dynamically from Environment Variables with safe fallback
    private static final String SMTP_USER = System.getenv().getOrDefault("SCM_SMTP_USER", 
            System.getProperty("scm.smtp.user", "tashiyajay0@gmail.com"));
            
    private static final String SMTP_PASS = System.getenv().getOrDefault("SCM_SMTP_PASS", 
            System.getProperty("scm.smtp.pass", "tgqxioxbjagfgttz"));

    @Override
    public boolean sendOnboardingEmail(String recipientEmail, String username, String tempPassword) {
        return sendOnboardingEmail(recipientEmail, username, tempPassword, "AUTHORIZED_USER");
    }

    @Override
    public boolean sendOnboardingEmail(String recipientEmail, String username, String tempPassword, String roleName) {
        if (recipientEmail == null || recipientEmail.isBlank()) {
            LOGGER.warning("Onboarding email skipped: No recipient email provided for user " + username);
            return false;
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASS);
            }
        };

        Session session = Session.getInstance(props, auth);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_USER, "GlobalTrade SCM Portal Access"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("🔑 Account Provisioned - GlobalTrade Enterprise SCM Access Details");

            String htmlBody = "<!DOCTYPE html>"
                + "<html>"
                + "<head><meta charset='UTF-8'></head>"
                + "<body style='margin:0; padding:0; background-color:#090d16; font-family:-apple-system, BlinkMacSystemFont, \"Segoe UI\", Roboto, sans-serif;'>"
                + "  <table width='100%' cellpadding='0' cellspacing='0' style='background-color:#090d16; padding: 40px 10px;'>"
                + "    <tr>"
                + "      <td align='center'>"
                + "        <table width='100%' style='max-width: 600px; background-color:#0f172a; border: 1px solid #334155; border-radius: 16px; overflow:hidden; box-shadow: 0 20px 40px rgba(0,0,0,0.6);'>"
                + "          <tr>"
                + "            <td style='padding: 36px 32px 24px; background: linear-gradient(135deg, #1e293b 0%, #0f172a 100%); border-bottom: 1px solid #334155;'>"
                + "              <div style='display:inline-block; padding: 4px 12px; background: rgba(37,99,235,0.2); color: #38bdf8; border: 1px solid rgba(56,189,248,0.3); border-radius: 20px; font-size: 11px; font-weight: 700; margin-bottom: 12px;'>ENTERPRISE SCM v2.5</div>"
                + "              <h1 style='margin:0; font-size: 24px; font-weight: 800; color: #f8fafc; letter-spacing: -0.5px;'>Welcome to GlobalTrade SCM</h1>"
                + "              <p style='margin: 8px 0 0; color: #94a3b8; font-size: 14px;'>Your enterprise supply chain account has been provisioned successfully.</p>"
                + "            </td>"
                + "          </tr>"
                + "          <tr>"
                + "            <td style='padding: 32px; color: #cbd5e1; font-size: 14px; line-height: 1.6;'>"
                + "              <p style='margin-top:0;'>Hello <strong style='color:#f8fafc;'>" + username + "</strong>,</p>"
                + "              <p>You have been granted authorized access to the <strong>GlobalTrade Supply Chain Management Portal</strong>. Below are your temporary sign-in credentials:</p>"
                + "              <table width='100%' style='background-color:#1e293b; border: 1px solid #334155; border-radius: 12px; margin: 20px 0; border-collapse: separate; padding: 16px;'>"
                + "                <tr>"
                + "                  <td style='padding: 8px 12px; color:#94a3b8; font-size:13px;'>Account Username:</td>"
                + "                  <td style='padding: 8px 12px; color:#f8fafc; font-weight:700; font-size:14px; text-align:right; font-family:monospace;'>" + username + "</td>"
                + "                </tr>"
                + "                <tr>"
                + "                  <td style='padding: 8px 12px; color:#94a3b8; font-size:13px;'>Assigned Role:</td>"
                + "                  <td style='padding: 8px 12px; color:#38bdf8; font-weight:700; font-size:13px; text-align:right;'><span style='background:rgba(37,99,235,0.2); padding: 3px 8px; border-radius: 6px; border:1px solid rgba(56,189,248,0.3);'>" + roleName + "</span></td>"
                + "                </tr>"
                + "                <tr>"
                + "                  <td style='padding: 8px 12px; color:#94a3b8; font-size:13px;'>One-Time Password:</td>"
                + "                  <td style='padding: 8px 12px; color:#10b981; font-weight:800; font-size:17px; text-align:right; font-family:monospace; letter-spacing: 1px;'>" + tempPassword + "</td>"
                + "                </tr>"
                + "              </table>"
                + "              <div style='background: rgba(245, 158, 11, 0.1); border-left: 4px solid #f59e0b; padding: 12px 16px; border-radius: 4px; margin-bottom: 24px;'>"
                + "                <strong style='color:#fbbf24; font-size:13px;'>⚠️ Mandatory First-Time Login Action:</strong>"
                + "                <div style='color:#cbd5e1; font-size:12px; margin-top:4px;'>For security compliance, you will be prompted to set your permanent password immediately upon your first sign-in.</div>"
                + "              </div>"
                + "              <table width='100%' cellpadding='0' cellspacing='0'>"
                + "                <tr>"
                + "                  <td align='center'>"
                + "                    <a href='http://localhost:8080/global-scm-web/' style='display:inline-block; padding: 14px 28px; background: linear-gradient(135deg, #2563eb, #1d4ed8); color: #ffffff; text-decoration: none; border-radius: 10px; font-weight: 700; font-size: 14px; box-shadow: 0 4px 15px rgba(37,99,235,0.4);'>Sign In & Set Permanent Password →</a>"
                + "                  </td>"
                + "                </tr>"
                + "              </table>"
                + "            </td>"
                + "          </tr>"
                + "        </table>"
                + "      </td>"
                + "    </tr>"
                + "  </table>"
                + "</body>"
                + "</html>";

            message.setContent(htmlBody, "text/html; charset=utf-8");

            Transport.send(message);
            LOGGER.info("Enterprise onboarding email dispatched successfully to " + recipientEmail + " for user " + username);
            return true;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to send onboarding email to " + recipientEmail + ": " + e.getMessage(), e);
            return false;
        }
    }
}
