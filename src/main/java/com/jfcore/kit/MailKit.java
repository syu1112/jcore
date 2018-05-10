package com.jfcore.kit;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import com.jfinal.kit.PropKit;

public class MailKit {
    private String hostname;
    private String username;
    private String password;
    private String fromname;
    private List<String> mailtoList = new ArrayList<String>();
    
    public MailKit() {
        this("mail.properties");
    }

    public MailKit(String configName) {
        this.hostname = PropKit.use(configName).get("default_host_name");
        this.username = PropKit.use(configName).get("default_user_name");
        this.password = PropKit.use(configName).get("default_pass_word");
        this.fromname = PropKit.use(configName).get("default_from_name");
    }
    
    public void mailto(String mail) {
        mailtoList.add(mail);
    }

    public void sendMail(String subject, String msg) throws EmailException {
        SimpleEmail simple = new SimpleEmail();
        simple.setHostName(hostname);
        simple.setAuthentication(username, password);
        simple.setFrom(username, fromname);
        simple.setCharset("UTF-8");
        simple.setSubject(subject);
        simple.setMsg(msg);
        for (String email : mailtoList) {
            simple.addTo(email);
        }
        simple.send();
    }
    
    public static void main(String[] args) throws Exception {
        MailKit mail = new MailKit();
        mail.mailto("672468896@qq.com");
        mail.sendMail("监控标题", "监控内容");
    }
    
//
//    public void sendHtmlMail(String subject, String message) throws EmailException {
//        sendHtmlMail(subject, message, getCommonMailTos());
//    }
//
//    public void sendHtmlMail(String subject, String message, List<String> mailTos) throws EmailException {
//        HtmlEmail email = new HtmlEmail();
//        buildMail(email, subject, message, mailTos);
//        email.setHtmlMsg(message);
//        email.send();
//    }
//
//    /**
//     * 发送简单的邮件
//     * 
//     * @param subject
//     *            主题
//     * @param message
//     *            邮件正文
//     * @throws EmailException
//     */
//    public void sendSimpleMail(String subject, String message) throws EmailException {
//        sendSimpleMail(subject, message, getCommonMailTos());
//    }
//
//    /**
//     * 发送简单的邮件并指定收件人
//     * 
//     * @param subject
//     *            主题
//     * @param message
//     *            邮件正文
//     * @param mailTos
//     *            收件人
//     * @throws EmailException
//     */
//    public void sendSimpleMail(String subject, String message, List<String> mailTos) throws EmailException {
//        Email email = new SimpleEmail();
//        buildMail(email, subject, message, mailTos);
//        email.send();
//    }
//
//    /**
//     * 发送带有附件的邮件
//     * 
//     * @param subject
//     *            主题
//     * @param message
//     *            邮件正文
//     * @param attachments
//     *            附件
//     * @throws EmailException
//     * @throws IOException
//     */
//    public void sendMultiPartMail(String subject, String message, List<File> attachments) throws EmailException, IOException {
//        sendMultiPartMail(subject, message, attachments, attachment);
//    }
//
//    /**
//     * 发送带有附件的邮件并指定附件压缩后的文件名称
//     * 
//     * @param subject
//     *            主题
//     * @param message
//     *            邮件正文
//     * @param attachments
//     *            附件
//     * @param compressFileName
//     *            附件压缩后的文件名称
//     * @throws EmailException
//     * @throws IOException
//     */
//    public void sendMultiPartMail(String subject, String message, List<File> attachments, String compressFileName) throws EmailException, IOException {
//        sendMultiPartMail(subject, message, attachments, compressFileName, null);
//    }
//
//    /**
//     * 发送带有附件的邮件并指定收件人
//     * 
//     * @param subject
//     *            主题
//     * @param message
//     *            邮件正文
//     * @param attachments
//     *            附件
//     * @param mailTos
//     *            收件人
//     * @throws EmailException
//     * @throws IOException
//     */
//    public void sendMultiPartMail(String subject, String message, List<File> attachments, List<String> mailTos) throws EmailException, IOException {
//        sendMultiPartMail(subject, message, attachments, attachment, mailTos);
//    }
//
//    /**
//     * 发送带有附件的邮件并指定收件人和指定附件压缩后的文件名称
//     * 
//     * @param subject
//     *            主题
//     * @param message
//     *            邮件正文
//     * @param attachments
//     *            附件
//     * @param compressFileName
//     *            附件压缩后的文件名称
//     * @param mailTos
//     *            收件人
//     * @throws EmailException
//     * @throws IOException
//     */
//    public void sendMultiPartMail(String subject, String message, List<File> attachments, String compressFileName, List<String> mailTos) throws EmailException, IOException {
//        MultiPartEmail email = new MultiPartEmail();
//        buildMail(email, subject, message, mailTos);
//
//        File compressFile = new File(compressFileName);
//        // compress first.
//        CompressUtil.zipCompress(attachments, compressFile);
//
//        EmailAttachment attachment = new EmailAttachment();
//
//        attachment.setName(compressFile.getName());
//        attachment.setPath(compressFile.getPath());
//        attachment.setDisposition(EmailAttachment.ATTACHMENT);
//
//        email.attach(attachment);
//
//        email.send();
//    }
//
//    // ///////////////////////////////////////////////////////////////////////////////////////////////////////
//    // Internal Use
//    // ///////////////////////////////////////////////////////////////////////////////////////////////////////
//    /**
//     * 公用的邮件配置
//     * 
//     * @param email
//     * @param subject
//     * @param message
//     * @param mailTos
//     * @throws EmailException
//     */
//    private void buildMail(Email email, String subject, String message, List<String> mailTos) throws EmailException {
//        email.setHostName(hostname);
//        email.setAuthentication(username, password);
//        email.setFrom(username, fromname);
//        email.setCharset("UTF-8");
//        email.setSubject(subject);
//        email.setMsg(message.replaceAll("\t", "        "));// format msg
//
//        // avoid empty mail tos.
//        if (mailTos == null || mailTos.isEmpty()) {
//            mailTos = getCommonMailTos();
//        }
//
//        for (String mailTo : mailTos) {
//            email.addTo(mailTo, getShortName(mailTo));
//        }
//    }
//
//    private String getShortName(String email) {
//        String name = getConfig("mail_from_name");
//        if (StringUtils.isNotEmpty(name)) {
//            return name;
//        }
//        if (StringUtils.isEmpty(email)) {
//            return "";
//        }
//        return email.substring(0, email.indexOf("@"));
//    }
//
//    public static void main(String[] args) throws Exception {
//        // SimpleDateFormat format = new
//        // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        // sendSimpleMail("测试发邮件", "当前时间："+format.format(new Date()));
//        // sendHtmlMail("测试发邮件", "<h1>当前时间</h1>：" + format.format(new Date()));
//
//        List<String> mailTos = new ArrayList<String>();
//        mailTos.add("672468896@qq.com");
//
//        MailClient client = new MailClient("mail.properties");
//        client.sendHtmlMail("测试", "内容", mailTos);
//        System.out.println("done");
//    }

}
