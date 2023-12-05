package project;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

// import org.junit.platform.reporting.shadow.org.opentest4j.reporting.events.core.Sources;


class SpecifyIngredientsPage extends VBox {
    private String cancelButtonStyle = "-fx-background-radius: 100; -fx-font-style: italic; -fx-background-color: #D9D9D9;  -fx-font-weight: bold; -fx-font: 18 arial;";
    private Button cancelButton;

    private Button recordButton;
    private String recorderButtonStyle = "-fx-background-radius: 100; -fx-font-style: italic; -fx-background-color: #D9D9D9;  -fx-font-weight: bold; -fx-font: 18 arial;";

    private HBox cancelContainer;
    private HBox promptContainer;
    private HBox subPromptContainer;
    private HBox recordButtonContainer;

    private String promptStyle = "-fx-font-size: 20;-fx-font-weight: bold;";
    private String subPromptStyle = "-fx-font-size: 20;";

    private String errorMsgStyle = "-fx-font-size: 20;-fx-font-weight: bold; -fx-text-fill: #DF0000;";
    private Text errorMsg;
    private Boolean errorFlag = false;

    private Label prompt;
    private Label subPrompt;



    SpecifyIngredientsPage(String mealType) {


        // this.setPrefSize(600, 700); // Size of the header
        // this.setStyle("-fx-background-color: #FFFFFF;");
        // this.setSpacing(10);
        // this.setAlignment(Pos.CENTER);
        //button to cancel recording and return to recipe list page
        cancelButton = new Button("x");
        cancelButton.setStyle(cancelButtonStyle);
        cancelButton.setPadding(new Insets(10, 0, 0, 10));

        // Set up labels
        prompt = new Label("What ingredients do you have?");
        prompt.setStyle(promptStyle);
        subPrompt = new Label("Tell us what you have and how much!");
        subPrompt.setStyle(subPromptStyle);

        //button to record audio
        recordButton = new Button("Hold to Record");
        recordButton.setStyle(recorderButtonStyle);
        recordButton.setPrefSize(300, 50);
        //bodyContainer = new StackPane();
        
        cancelContainer = new HBox(cancelButton);
        cancelContainer.setAlignment(Pos.CENTER);
        promptContainer = new HBox(prompt);
        promptContainer.setAlignment(Pos.CENTER);
        subPromptContainer = new HBox(subPrompt);
        subPromptContainer.setAlignment(Pos.CENTER);
        recordButtonContainer = new HBox(recordButton);
        recordButtonContainer.setAlignment(Pos.CENTER);
        
        //place stack panges onto border pane

        this.getChildren().addAll(promptContainer, subPromptContainer, recordButtonContainer, cancelContainer);
        this.requestLayout();
    }

    public void setRecordHoldAction(EventHandler<? super MouseEvent> eventHandler){
        recordButton.setOnMousePressed(eventHandler);
    }

    public void setRecordReleaseAction(EventHandler<? super MouseEvent> eventHandler){
        recordButton.setOnMouseReleased(eventHandler);
    }

    public void setCancelButtonAction(EventHandler<ActionEvent> eventHandler){
        cancelButton.setOnAction(eventHandler);
    }

    public void errorMsg(){
        errorMsg = new Text("Sorry, we didn't catch that. Please Try Again");
        errorMsg.setStyle(errorMsgStyle);
        this.getChildren().add(errorMsg); 
    }

}