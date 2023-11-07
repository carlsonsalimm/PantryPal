package src.DetailedView;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Recipe extends HBox {

    private TextField title;
    private TextField instructions;

    Recipe() {
        this.setPrefSize(500, 80); // sets size of contact
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;"); // sets background color of contact
        
        title = new TextField(); // create task name text field
        title.setPrefSize(280, 20); // set size of text field
        title.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        title.setPadding(new Insets(10, 0, 0, 0)); // adds some padding to the text field
        
        instructions = new TextField(); // create task name text field
        instructions.setPrefSize(280, 20); // set size of text field
        instructions.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        instructions.setPadding(new Insets(0, 0, 10, 0)); // adds some padding to the text field
    }


    public TextField getTitle() {
        return this.title;
    }

    public TextField getInstructions() {
        return this.instructions;
    }

    public void setTitle(String s) {
        this.title.setText(s);
    }

    public void setInstructions(String s) {
        this.instructions.setText(s);
    }


    /*
     * Save tasks to a file called "contacts.csv"
     */
    public void saveContacts() {
        try{
            FileWriter fw = new FileWriter("contacts.csv");
            for (int i = 0; i < this.getChildren().size(); i++) {
                // if (this.getChildren().get(i) instanceof Contact) {
                //     String name = ((Contact) this.getChildren().get(i)).getName().getText();
                //     String phone = ((Contact) this.getChildren().get(i)).getPhone().getText();
                //     String email = ((Contact) this.getChildren().get(i)).getEmail().getText();

                //     fw.write(name + "," + phone + "," + email + "\r\n");
                // }
            }
            fw.close();
        } catch (Exception e){

        }
    }
}

/** 
class Footer extends HBox {

    private Button addButton;
    private Button deleteButton;
    private Button loadButton;
    private Button saveButton;
    private Button sortButton;

    Footer() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

        addButton = new Button("Add Contact"); // text displayed on add button
        addButton.setStyle(defaultButtonStyle); // styling the button
        deleteButton = new Button("Delete Selected Contacts"); // text displayed on delete contacts button
        deleteButton.setStyle(defaultButtonStyle); // styling the button
        loadButton = new Button("Load Contacts"); // text displayed on load button
        loadButton.setStyle(defaultButtonStyle); // styling the button
        saveButton = new Button("Save Contacts"); // text displayed on save contacts button
        saveButton.setStyle(defaultButtonStyle); // styling the button
        sortButton = new Button("Sort Contacts"); // text displayed on sort contacts button
        sortButton.setStyle(defaultButtonStyle); // styling the button

        HBox hb1 = new HBox(addButton, deleteButton);
        hb1.setAlignment(Pos.CENTER);
        hb1.setSpacing(10);

        HBox hb2 = new HBox(saveButton, loadButton, sortButton);
        hb2.setAlignment(Pos.CENTER);
        hb2.setSpacing(10);

        VBox vb = new VBox(hb1, hb2);
        vb.setSpacing(5);
        vb.setPadding(new Insets(5, 0, 5, 0));

        this.getChildren().addAll(vb); // adding buttons to footer
        this.setAlignment(Pos.CENTER); // aligning the buttons to center
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getLoadButton() {
        return loadButton;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getSortButton() {
        return sortButton;
    }
}

class Header extends HBox {

    Header() {
        this.setPrefSize(500, 60); // Size of the header
        this.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text("Contacts"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center
    }
}

class AppFrame extends BorderPane{

    private Header header;

    private Button deleteButton;
    private Button editButton;
    private Button saveButton;


    AppFrame()
    {
        // Initialise the header Object
        header = new Header();

        // Initialise the Footer Object
        footer = new Footer();

        ScrollPane sp = new ScrollPane(contactList);
        sp.setFitToWidth(true);
        sp.setFitToHeight(false);

        // Add header to the top of the BorderPane
        this.setTop(header);
        // Add scroller to the centre of the BorderPane
        this.setCenter(sp);
        // Add footer to the bottom of the BorderPane
        this.setBottom(footer);

        // Initialise Button Variables through the getters in Footer
        addButton = footer.getAddButton();
        deleteButton = footer.getDeleteButton();
        loadButton = footer.getLoadButton();
        saveButton = footer.getSaveButton();
        sortButton = footer.getSortButton();


        // Call Event Listeners for the Buttons
        addListeners();
    }

    public void addListeners()
    {

        // Add button functionality
        addButton.setOnAction(e -> {
            // Create a new contact
            Contact contact = new Contact();
            // Add contact to contact list
            contactList.getChildren().add(contact);
            
            

            // Add selectButton toggle to the Select button
            Button selecButton = contact.getSelectButton();
            selecButton.setOnAction(e1 -> {
                // Call toggleDone on click
                contact.toggleDone();
            });

            // Add uploadButton dialog to upload button
            Button uploadButton = contact.getUploadButton();
            uploadButton.setOnAction(e1 -> {
                contact.uploadImage(Main.getStage(), contact.getImageView());
            });
            // Update contact indices
            contactList.updateContactIndices();
        });
        
        // Delete selected contacts
        deleteButton.setOnAction(e -> {
            contactList.removeSelectedContacts();
        });

        loadButton.setOnAction(e -> {
            contactList.loadContacts();
            for (int i = 0; i < contactList.getChildren().size(); i++) {
                if (contactList.getChildren().get(i) instanceof Contact) {
                    Contact c = ((Contact) contactList.getChildren().get(i));

                    // Add selectButton toggle to the Select button
                    Button selectButton = c.getSelectButton();
                    selectButton.setOnAction(e1 -> {
                        c.toggleDone();
                    });

                    // Add uploadButton dialog to upload button
                    Button uploadButton = c.getUploadButton();
                    uploadButton.setOnAction(e1 -> {
                    c.uploadImage(Main.getStage(), c.getImageView());
            });
            }
        }
        });

        saveButton.setOnAction(e -> {
            contactList.saveContacts();
        });
        
        sortButton.setOnAction(e -> {
            contactList.sortContacts();
        });

    }
}
*/
public class PantryPal extends Application {

    private Stage stage;
    private Scene root;

    @Override
    public void start(Stage primaryStage) throws Exception {

        stage = primaryStage;
        // mock recipe
        Recipe test = new Recipe();
        Recipe rootRecipe = new Recipe();
        test.setTitle("New Title");
        test.setInstructions("avocado, blueberry, food \n 1) Do stuff \n 2) Do more stuff");
        // Setting the Layout of the Window- Should contain a Header, Footer and the TaskList
        DetailedView newView = new DetailedView(test, this);
        root = new Scene(new DetailedView(rootRecipe, this), 500, 600);

        // Set the title of the app
        primaryStage.setTitle("DetailedView Test");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(new Scene(newView, 500, 600));
        // Make window non-resizable
        primaryStage.setResizable(false);
        // Show the app
        primaryStage.show();
    }

    public void setScene(Scene scene) {
        this.getStage().setScene(scene);
    }

    public Stage getStage() {
        return this.stage;
    }

    public Scene getRoot() {
        return this.root;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
