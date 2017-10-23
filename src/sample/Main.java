package sample;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Main extends Application {
    private static JSch jSch;
    private static Session session;
    public static List<String> exe(String dnsName, String User, String Pwd, int port, String remoteshellfile) throws IOException {
        //JSch jSch = new JSch();

        List<String> result = new ArrayList<String>();
        ChannelExec channelExec = null;
        try {
//            //Authenticate through Private Key File
//            jSch.addIdentity(User, Pwd,);
//            //jSch.addIdentity(privKey);
//
//            //Give the user and dnsName
//            Session session = jSch.getSession("root", dnsName, port);
//            //Required if not a trusted host
//            java.util.Properties config = new java.util.Properties();
//            config.put("StrictHostKeyChecking", "no");
//            session.setConfig(config);
//            System.out.println("Connecting SSH to " + dnsName + " - Please wait for few minutes... ");
//            session.connect();


            jSch = new JSch();
            session = jSch.getSession(User, dnsName, port);
            session.setPassword(Pwd);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();

            //Open a shell
            channelExec = (ChannelExec) (session.openChannel("exec"));

            InputStream in = channelExec.getInputStream();
            channelExec.setCommand(" bash  " + remoteshellfile);

            // Execute the command
            channelExec.connect();
            System.out.println("Executing " + remoteshellfile);

            // Read the output from the input stream we set above
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;

            //Read each line from the buffered reader and add it to result list
            // You can also simple print the result here
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }

            //retrieve the exit status of the remote command corresponding to this channel
            int exitStatus = channelExec.getExitStatus();

            //Safely disconnect channel and disconnect session. If not done then it may cause resource leak
            channelExec.disconnect();
            session.disconnect();

            System.out.println("status " + exitStatus);
            if (exitStatus < 0) {
                System.out.println("Done, but exit status not set!");
            } else if (exitStatus > 0) {
                System.out.println("Done, but with error!");
            } else {
                System.out.println("Done!");
            }

        } catch (JSchException e) {
            e.printStackTrace();
        } finally {
            if (channelExec != null) {
                channelExec.disconnect();
            }
        }
        return result;

    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("execshell");
        primaryStage.setScene(new Scene(root, 1100, 800));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
        //String privKey = "C:\\Users\\Administrator\\.ssh\\id_rsa";
//        try {
//            List<String> rs = Main.exe("222.20.79.232", privKey, 50021, "/home/linsong/test.sh");
//            System.out.print("connect!");
//            for (int i = 0; i < rs.size(); i++) {
//                System.out.println(rs.get(i));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
