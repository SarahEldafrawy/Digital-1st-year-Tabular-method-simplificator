package guiApp;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class HelpBox {

    public static void display(String title, String message) {
    	
        Stage window = new Stage();

        window.setTitle(title);
        window.setMaxWidth(750);
        window.setMaxHeight(650);

        Label label = new Label();
        label.setText(message);
        label.setWrapText(true);
        label.setMinHeight(150);
        GridPane.setConstraints(label, 0, 2);

        Button closeButton = new Button("Thanks!");
        GridPane.setConstraints(closeButton, 0, 3);
        closeButton.setOnAction(e -> window.close());
        
        VBox layout = new VBox(5);
        layout.getChildren().addAll(label, closeButton);
        layout.setPadding(new Insets(30, 30, 30, 30));
        layout.setSpacing(40);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        scene.getStylesheets().add(GUI.class.getResource("text.css").toExternalForm()); 

        window.setScene(scene);
        window.showAndWait();
    }

}
