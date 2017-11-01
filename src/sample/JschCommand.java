package sample;
import  com.jcraft.jsch.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by Sanji on 2017/10/30.
 */
public class JschCommand {

    private Session session = null;
    private Channel channel = null;

    private String sftpHost = "222.20.79.232";
    private int sftpPort = 50021;
    private String sftpUserName = "linsong";
    private String sftpPassword = "linsong";
    private int timeout = 30000;

    /**
     * 获取连接
     * @return
     */
    private ChannelExec getChannelExec() {
        try {
            if (channel != null && channel.isConnected()) {
                return (ChannelExec) channel;
            }
            JSch jSch = new JSch();
            if (session == null || !session.isConnected()) {
                session = jSch.getSession(sftpUserName, sftpHost, sftpPort);
                session.setPassword(sftpPassword);
                Properties config = new Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
                session.setTimeout(timeout);
                session.connect();
            }
            channel = session.openChannel("exec");
        } catch (Exception e) {
            if (session != null) {
                session.disconnect();
                session = null;
            }
            channel = null;
        }
        return channel == null ? null : (ChannelExec) channel;
    }

    /**
     * 关闭连接
     */
    private void closeChannel() {
        try {
            if (channel != null) {
                channel.disconnect();
                channel = null;
            }
            if (session != null) {
                session.disconnect();
                session = null;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * 执行服务器端命令
     */
    public boolean executeCommand(String command) {
        boolean flag = false;
        ChannelExec channelExec = getChannelExec();
        if (channelExec == null) {
            return false;
        }
        try {
            channelExec.setInputStream(null);
            channelExec.setErrStream(System.err);
            channelExec.setCommand(command);

            InputStream in = channelExec.getInputStream();
            channelExec.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            reader.close();
            closeChannel();

            flag = true;
        } catch (Exception e) {
            System.out.println(e);
            flag = false;
        }
        return flag;
    }

//    public static void main(String[] args) {
//        JschCommand jschCommand = new JschCommand();
//        System.out.println(jschCommand.executeCommand("date"));
//        System.out.println("----------------------------------");
//        System.out.println(jschCommand.executeCommand("ruby Demo.rb"));
//        System.out.println("----------------------------------");
//        System.out.println(jschCommand.executeCommand("touch abc.txt"));
//        System.out.println("----------------------------------");
//        System.out.println(jschCommand.executeCommand("rm abc.txt"));
//    }
}
