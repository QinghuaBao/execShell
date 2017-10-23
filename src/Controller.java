package sample;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Controller {
    @FXML
    private void initialize(){
    }

    @FXML
    private void handleButton(){
        //warning("test11");
        //String privKey = "C:\\Users\\Administrator\\.ssh\\id_rsa";
        try {
            List<String> rs = Main.exe("222.20.79.232","linsong" ,"linsong", 50021, "/home/linsong/test.sh");
            System.out.print("connect!");
            //System.out.print(rs);
            for (int i = 0; i < rs.size(); i++) {
                System.out.println(rs.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleUpload(){

        JFileChooser chooser = new JFileChooser(); //创建选择文件对象
        chooser.setDialogTitle("请选择文件");//设置标题
        chooser.setMultiSelectionEnabled(true);  //设置只能选择文件
        FileNameExtensionFilter filter = new FileNameExtensionFilter("jpg", "jpg");//定义可选择文件类型
        chooser.setFileFilter(filter); //设置可选择文件类型
        chooser.showOpenDialog(null); //打开选择文件对话框,null可设置为你当前的窗口JFrame或Frame
        File file = chooser.getSelectedFile(); //file为用户选择的图片文件


    }

    public static void warning(String contentText){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }


    public void handleUpdown() {
    }
}
