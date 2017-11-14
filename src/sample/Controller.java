package sample;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;


public class Controller {
    private String fpath;
    private String fname;
    @FXML
    private ImageView imageView;

    @FXML
    private void initialize(){
        //javafx.scene.control.ScrollPane scrollPane = new ScrollPane();
        Image image = new Image("file:下载.jpg");
        setImage(image);
        //imageView.setPreserveRatio(true);
//        imageView.setImage(image);
//        imageView.setStyle("");
//        imageView.setLayoutX(100);
//        try {
//            BufferedImage image = ImageIO.read(new File("下载.jpg"));
//            image = resize(image, (int)imageView.getFitWidth(), (int)imageView.getFitHeight());
//            //ImageIo.write函数保存，再重新读出来
//            imageView.setImage(new Image(new FileInputStream("新文件")));
//        } catch (IOException e) {
//            e.printStackTrace();
//            return;
//        }
    }

    @FXML
    private void handleLink(){
//        try {
//            List<String> rs = Main.exe("222.20.79.232","linsong" ,"linsong", 50021, "/home/linsong/test.sh");
//            Main main = new Main();
//
//            System.out.print("connect!");
////            System.out.print(rs);
////            for (int i = 0; i < rs.size(); i++) {
////                System.out.println(rs.get(i));
////            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //ExecCmdOnLinux();
        JschCommand jschCommand = new JschCommand();
        //jschCommand.executeCommand("/home/linsong/anaconda2/bin/python2.7  /home/linsong/test.py");
        jschCommand.executeCommand("bash /home/linsong/test.sh");

        //jschCommand.executeCommand("sh /home/linsong/test.sh");
     //   jschCommand.executeCommand("ls");
    }
    @FXML
    private void handleUpload(){ //上传
        SFTPTest test = new SFTPTest();
        Map<String, String> sftpDetails = new HashMap<String, String>();
        // 设置主机ip，端口，用户名，密码
        sftpDetails.put(SFTPConstants.SFTP_REQ_HOST, "222.20.79.232");
        sftpDetails.put(SFTPConstants.SFTP_REQ_USERNAME, "linsong");
        sftpDetails.put(SFTPConstants.SFTP_REQ_PASSWORD, "linsong");
        sftpDetails.put(SFTPConstants.SFTP_REQ_PORT, "50021");

        //String src = "C:\\Users\\lsz0517\\Desktop\\timg.jpg"; // 本地文件名
        String newfpath = fpath.replace("/", "\\");
        String dst = "/home/linsong/py-faster-rcnn/data/demo/"; // 目标文件名

        SFTPChannel channel = test.getSFTPChannel();
        ChannelSftp chSftp = null;
        try {
            chSftp = channel.getChannel(sftpDetails, 60000);
        } catch (JSchException e) {
            e.printStackTrace();
        }
        try {
            chSftp.put(new FileInputStream(newfpath), dst+fname, ChannelSftp.OVERWRITE);// 代码段3
            System.out.println("上传成功！");
        } catch (SftpException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        chSftp.quit();
        try {
            channel.closeChannel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void warning(String contentText){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    @FXML
    public void handleUpdown() { //下载
        SFTPTest test = new SFTPTest();
        Map<String, String> sftpDetails = new HashMap<String, String>();
        // 设置主机ip，端口，用户名，密码
        sftpDetails.put(SFTPConstants.SFTP_REQ_HOST, "222.20.79.232");
        sftpDetails.put(SFTPConstants.SFTP_REQ_USERNAME, "linsong");
        sftpDetails.put(SFTPConstants.SFTP_REQ_PASSWORD, "linsong");
        sftpDetails.put(SFTPConstants.SFTP_REQ_PORT, "50021");

        SFTPChannel channel = test.getSFTPChannel();
        ChannelSftp chSftp = null;
        try {
            chSftp = channel.getChannel(sftpDetails, 60000);
        } catch (JSchException e) {
            e.printStackTrace();
        }

        String updownfilepath = "/home/linsong/test-result/";
        String updownfilename = updownfilepath + fname.replace("." , "_result.");
        SftpATTRS attr = null;
        try {
            attr = chSftp.stat(updownfilename);
        } catch (SftpException e) {
            e.printStackTrace();
        }
        long fileSize = attr.getSize();

        String dst = "C:\\Users\\lsz0517\\Desktop\\测试文件夹";
//        try {
//            OutputStream out = new FileOutputStream(dst);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        try {
            chSftp.get(updownfilename, dst, new FileProgressMonitor(fileSize)); // 代码段1
            System.out.println("下载成功！");
            String files;
            files = "file:" + dst.replace("\\", "/") + "/" + fname.replace("." , "_result.");
            Image image = new Image(files);
            imageView.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            chSftp.quit();
            try {
                channel.closeChannel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void choosefile() {
        JFileChooser chooser = new JFileChooser(); //创建选择文件对象
        chooser.setDialogTitle("请选择文件");//设置标题
        chooser.setMultiSelectionEnabled(true);  //设置只能选择文件
        FileNameExtensionFilter filter = new FileNameExtensionFilter("jpg", "jpg");//定义可选择文件类型
        chooser.setFileFilter(filter); //设置可选择文件类型
        chooser.showOpenDialog(null); //打开选择文件对话框,null可设置为你当前的窗口JFrame或Frame
        File file = chooser.getSelectedFile(); //file为用户选择的图片文件
        String files;
        fpath = file.getAbsolutePath();
        fname = file.getName();
        files = "file:" + fpath;
        Image image = new Image(files);
        imageView.setImage(image);
    }

    private void setImage(Image image){
        imageView.setFitHeight(image.getHeight());
        imageView.setFitWidth(image.getWidth());
        //598,366
        if (image.getHeight() < 323){
            imageView.setLayoutY((366-image.getHeight())/2);
        }
        if (image.getWidth() < 461){
            imageView.setLayoutX((598-image.getWidth())/2);
        }
        imageView.setImage(image);
    }

    public static BufferedImage zoomInImage(BufferedImage originalImage, int width, int height) {
        BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());

        Graphics g = newImage.getGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return newImage;
    }

    public static BufferedImage resize(BufferedImage source, int targetW, int targetH) {
        int width = source.getWidth();// 图片宽度
        int height = source.getHeight();// 图片高度
        return zoomInImage(source, targetW, targetH);
    }
}
