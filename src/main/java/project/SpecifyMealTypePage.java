package project;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class SpecifyMealTypePage extends BorderPane {
    
    private AudioRecording recordButton;

    SpecifyMealTypePage() {
        //button to record audio
        recordButton = new AudioRecording();
        //button place at center of page
        this.setCenter(recordButton);

        //button to cancel recording and return to recipe list page
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> Main.setPage(Main.temp));
        //button place at bottom center of page
        this.setBottom(cancelButton);
        this.setAlignment(cancelButton, Pos.CENTER);

        
    }
}
