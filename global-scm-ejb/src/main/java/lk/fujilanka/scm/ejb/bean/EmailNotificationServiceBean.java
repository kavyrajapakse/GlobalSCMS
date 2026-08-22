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

    private static final String GMAIL_USER = "tashiyajay0@gmail.com";
    private static final String GMAIL_APP_PASS = "tgqxioxbjagfgttz";

    @Override
    public boolean sendOnboardingEmail(String recipientEmail, String username, String tempPassword) {
        return sendOnboardingEmail(recipientEmail, username, tempPassword, "AUTHORIZED_USER");
    }

    @Override
    public boolean sendOnboardingEmail(String recipientEmail, String username, String tempPassword, String roleName) {
        if (recipientEmail == null || recipientEmail.isBlank()) {
            recipientEmail = GMAIL_USER; // Default fallback to admin email
        }

        LOGGER.info("[Gmail SMTP Service]: Initiating real email dispatch to: " + recipientEmail + " for user: " + username);

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(GMAIL_USER, GMAIL_APP_PASS);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(GMAIL_USER, "GlobalTrade SCM Enterprise"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("🚢 Welcome to GlobalTrade SCM — Account Onboarding & Temporary Password");

            String htmlBody = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head><meta charset='UTF-8'></head>" +
                    "<body style='margin: 0; padding: 30px 10px; background-color: #0f172a; font-family: -apple-system, BlinkMacSystemFont, \"Segoe UI\", Roboto, Helvetica, Arial, sans-serif; color: #334155;'>" +
                    "  <div style='max-width: 600px; margin: 0 auto; background: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 20px 40px rgba(0,0,0,0.3); border: 1px solid #e2e8f0;'>" +
                    "    <!-- Header -->" +
                    "    <div style='background: linear-gradient(135deg, #1e3a8a 0%, #2563eb 100%); padding: 36px 32px; text-align: center; color: #ffffff;'>" +
                    "      <div style='display: inline-block; background: rgba(255,255,255,0.15); padding: 8px 16px; border-radius: 20px; font-size: 12px; font-weight: 700; letter-spacing: 1px; text-transform: uppercase; margin-bottom: 12px; border: 1px solid rgba(255,255,255,0.3);'>ENTERPRISE ACCESS</div>" +
                    "      <h1 style='margin: 0; font-size: 26px; font-weight: 800; letter-spacing: -0.5px;'>GlobalTrade SCM Portal</h1>" +
                    "      <p style='margin: 8px 0 0; font-size: 14px; opacity: 0.9;'>Autonomous Supply Chain, Logistics & Trade Platform</p>" +
                    "    </div>" +
                    "    <!-- Body -->" +
                    "    <div style='padding: 36px 32px;'>" +
                    "      <h2 style='font-size: 20px; color: #0f172a; margin-top: 0; margin-bottom: 8px;'>Welcome aboard, " + username + "! 👋</h2>" +
                    "      <p style='font-size: 14px; line-height: 1.6; color: #64748b; margin-bottom: 24px;'>Your enterprise account has been created by the System Administrator. Below are your temporary sign-in credentials to access the portal.</p>" +
                    "      <!-- Credentials Box -->" +
                    "      <div style='background: #f8fafc; border: 1px solid #e2e8f0; border-radius: 12px; padding: 20px; margin-bottom: 24px;'>" +
                    "        <table style='width: 100%; border-collapse: collapse; font-size: 14px;'>" +
                    "          <tr>" +
                    "            <td style='padding: 8px 0; color: #64748b; font-weight: 600;'>Assigned Role:</td>" +
                    "            <td style='padding: 8px 0; text-align: right;'><span style='background: #eff6ff; color: #2563eb; padding: 4px 10px; border-radius: 12px; font-weight: 700; font-size: 12px; border: 1px solid #bfdbfe;'>" + roleName + "</span></td>" +
                    "          </tr>" +
                    "          <tr>" +
                    "            <td style='padding: 8px 0; color: #64748b; font-weight: 600;'>Username:</td>" +
                    "            <td style='padding: 8px 0; text-align: right; font-weight: 700; color: #0f172a;'>" + username + "</td>" +
                    "          </tr>" +
                    "          <tr>" +
                    "            <td style='padding: 8px 0; color: #64748b; font-weight: 600;'>Account Status:</td>" +
                    "            <td style='padding: 8px 0; text-align: right; color: #10b981; font-weight: 700;'>● ACTIVE (First-Login Required)</td>" +
                    "          </tr>" +
                    "        </table>" +
                    "      </div>" +
                    "      <!-- Temp Password Banner -->" +
                    "      <div style='background: #eff6ff; border: 2px dashed #3b82f6; border-radius: 12px; padding: 20px; text-align: center; margin-bottom: 28px;'>" +
                    "        <div style='font-size: 11px; font-weight: 700; color: #2563eb; text-transform: uppercase; letter-spacing: 1px; margin-bottom: 8px;'>Your Temporary One-Time Password</div>" +
                    "        <div style='font-size: 26px; font-family: \"SF Mono\", Monaco, Consolas, \"Courier New\", monospace; font-weight: 800; color: #1e3a8a; letter-spacing: 3px; background: #ffffff; padding: 10px 24px; border-radius: 8px; display: inline-block; box-shadow: 0 2px 6px rgba(0,0,0,0.06);'>" + tempPassword + "</div>" +
                    "        <div style='font-size: 12px; color: #64748b; margin-top: 8px;'>⚠️ This password expires immediately upon first sign-in.</div>" +
                    "      </div>" +
                    "      <!-- How to Get Started -->" +
                    "      <h3 style='font-size: 15px; color: #0f172a; margin-bottom: 12px;'>📋 Quick 3-Step Setup:</h3>" +
                    "      <ol style='font-size: 13px; color: #64748b; line-height: 1.8; margin: 0; padding-left: 20px; margin-bottom: 28px;'>" +
                    "        <li>Click the button below to navigate to the <strong>GlobalTrade SCM Portal</strong>.</li>" +
                    "        <li>Sign in with your username (<code>" + username + "</code>) and the temporary password above.</li>" +
                    "        <li>You will be immediately prompted to <strong>choose your permanent password</strong>.</li>" +
                    "      </ol>" +
                    "      <!-- CTA Button -->" +
                    "      <div style='text-align: center; margin-bottom: 24px;'>" +
                    "        <a href='http://localhost:8080/global-scm/' style='display: inline-block; width: 85%; padding: 16px 24px; background: linear-gradient(135deg, #2563eb 0%, #1d4ed8 100%); color: #ffffff; text-decoration: none; border-radius: 10px; font-weight: 700; font-size: 15px; box-shadow: 0 4px 14px rgba(37,99,235,0.35); text-align: center;'>🔐 Sign In & Set Permanent Password &rarr;</a>" +
                    "      </div>" +
                    "    </div>" +
                    "    <!-- Footer -->" +
                    "    <div style='background: #f8fafc; border-top: 1px solid #e2e8f0; padding: 20px 32px; text-align: center; font-size: 12px; color: #94a3b8; line-height: 1.5;'>" +
                    "      <p style='margin: 0;'>GlobalTrade SCM Platform • Enterprise Security Compliance</p>" +
                    "      <p style='margin: 4px 0 0;'>This is an automated system dispatch. Please do not reply directly to this email.</p>" +
                    "    </div>" +
                    "  </div>" +
                    "</body>" +
                    "</html>";

            message.setContent(htmlBody, "text/html; charset=utf-8");

            Transport.send(message);
            LOGGER.info("[Gmail SMTP Service]: GORGEOUS ONBOARDING EMAIL DELIVERED SUCCESSFULLY TO " + recipientEmail + "!");
            return true;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "[Gmail SMTP Error]: Failed to deliver email to " + recipientEmail, e);
            return false;
        }
    }
}
