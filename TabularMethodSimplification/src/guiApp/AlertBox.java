package guiApp;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class AlertBox {

    public static void display(String title, String message) {
    	
        Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(350);
        window.setMinHeight(250);

        Label label = new Label();
        label.setText(message);
        GridPane.setConstraints(label, 0, 2);

        Button closeButton = new Button("Okay");
        GridPane.setConstraints(closeButton, 0, 4);
        closeButton.setOnAction(e -> window.close());
        
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setSpacing(40);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        scene.getStylesheets().add(GUI.class.getResource("text.css").toExternalForm()); 

        window.setScene(scene);
        window.showAndWait();
    }

}
