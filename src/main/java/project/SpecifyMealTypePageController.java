package project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.CookieHandler;
import org.json.Cookie;

import javafx.event.ActionEvent;

public class SpecifyMealTypePageController {
    private SpecifyMealTypePage view;
    private Model model;

    public SpecifyMealTypePageController(SpecifyMealTypePage view ,Model model){
        this.view = view;
        this.model = model;

        this.view.setRecordHoldAction(event -> {
            try {
                handleRecordHoldButton(event);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });


        this.view.setRecordReleaseAction(event -> {
            try {
                handleRecordReleasetButton(event);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    private void handleRecordHoldButton(ActionEvent event) throws IOException{
       
    }

    private void handleRecordReleasetButton(ActionEvent event) throws IOException{
      
    }

   
}
