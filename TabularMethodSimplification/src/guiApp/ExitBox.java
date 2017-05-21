package guiApp;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ExitBox {

	private static boolean answer;
	
	public static boolean display(String title, String message) {
    	
        Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(350);
        window.setMinHeight(250);
        window.setOnCloseRequest(e->{
        	e.consume();
        	answer = false;
        	window.close();
        });
       
        Label label = new Label();
        label.setText(message);
        
        Button yesButton = new Button("Yes");
        yesButton.setOnAction(e -> {
        	answer = true;
        	window.close();
        });
        
        Button noButton = new Button("No");
        noButton.setOnAction(e -> {
        	answer = false;
        	window.close();
        });
        
        HBox buttonHolder = new HBox(30);
        buttonHolder.getChildren().addAll(yesButton, noButton);
        
        GridPane layout = new GridPane();
        layout.setAlignment(Pos.BASELINE_CENTER);
        layout.setVgap(30);
        layout.setHgap(10);
        layout.setPadding(new Insets(5, 5, 5, 5));
        buttonHolder.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        GridPane.setConstraints(label, 1, 2);
        GridPane.setConstraints(buttonHolder,1,3);
        layout.getChildren().addAll(label,buttonHolder);
         
        scene.getStylesheets().add(GUI.class.getResource("text.css").toExternalForm()); 

        window.setScene(scene);
        window.showAndWait();
        
        return answer;
    }
}
