package guiApp;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class WelcomeScene {
	
	private GridPane welcomeGrid; 
	private VBox welcomeVBox;
	private Scene welcomeScene;
	private Button helpButton;
	private Button manualButton;
	private Button browseButton;
	private Button exitButton;
	private Label welcomeLabel;
	
	public WelcomeScene(String welcomeMsg)
	{
		this.welcomeLabel = new Label(welcomeMsg);
		this.welcomeLabel.setTextFill(Color.WHITE);
		this.welcomeLabel.setFont(Font.font(null,FontWeight.BOLD,20));
		this.helpButton = new Button("Help");
		this.manualButton = new Button("Manual");
		this.browseButton = new Button("Browse");
		this.exitButton = new Button("Exit");
		this.welcomeVBox = new VBox(15);
		this.welcomeGrid = new GridPane();
		setGrid();
		setCss();
		addElements();

	}
	
	private void setGrid()
	{
		 this.welcomeGrid.setPadding(new Insets(10, 10, 10, 10));
		 this.welcomeGrid.setAlignment(Pos.CENTER);
		 this.welcomeVBox.setPadding(new Insets(10, 10, 10, 10));
		 this.welcomeVBox.setAlignment(Pos.CENTER);
		 this.welcomeVBox.setStyle("-fx-background-color:#1B9FC1;");
		 this.welcomeVBox.setMinHeight(740);
		 this.welcomeVBox.setMinWidth(340);
		 this.welcomeScene = new Scene(welcomeGrid, 650, 450);
	}
	
	private void addElements()
	{
		
		this.welcomeGrid.getChildren().add(this.welcomeVBox);
		this.welcomeVBox.getChildren().addAll(this.welcomeLabel, this.manualButton, this.browseButton, this.helpButton , this.exitButton);
	}
	
	public Button getManualButton() {
		return manualButton;
	}
	
	public Button getHelpButton() {
		return helpButton;
	}

	public Button getBrowseButton() {
		return browseButton;
	}

	public Button getExitButton() {
		return exitButton;
	}
	
	public Scene getScene()
	{
		return this.welcomeScene;
	}
	
	private void setCss()
	{
		this.welcomeGrid.getStylesheets().add(GUI.class.getResource("text.css").toExternalForm());
	}
}
