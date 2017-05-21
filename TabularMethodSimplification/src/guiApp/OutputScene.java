package guiApp;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class OutputScene {

	private GridPane outputGrid;
	private HBox outputHBox;
	private Scene outputScene;
	private Label stepsLabel;
	private Label primesLabel;
	private Label petricklabel;
	private TextArea stepsOuput;
	private TextArea primesOutput;
	private TextArea petrickOuput;
	private Button returnButton;
	private Button saveButton;

	
	public OutputScene(String stepsLabel , String primesLabel, String petrickLabel)
	{
		this.returnButton = new Button("Return");
		this.saveButton = new Button("Save");
		this.primesLabel = new Label(primesLabel);
		this.stepsLabel = new Label(stepsLabel);
		this.petricklabel = new Label(petrickLabel);
		this.outputGrid = new GridPane();
		this.outputHBox = new HBox(30);
		setGrid();
		setCss();
		setLabelsOnGrid();
		addElements();
	}

	private void setGrid()
	{
		 this.outputGrid.setPadding(new Insets(20, 20, 20, 20));
		 this.outputGrid.setAlignment(Pos.BASELINE_CENTER);
		 this.outputGrid.setVgap(20);
		 this.outputGrid.setHgap(10);
		 this.outputHBox.setAlignment(Pos.BASELINE_CENTER);
		 this.outputScene = new Scene(this.outputGrid, 650, 550);
	}
	
	private void setLabelsOnGrid()
	{
		GridPane.setConstraints(this.stepsLabel, 1, 1);
		GridPane.setConstraints(this.primesLabel, 1, 3);
		GridPane.setConstraints(this.petricklabel, 1, 5);
	}
	
	public void setTextAreas(String stepStr, String primesStr, String petrickStr)
	{
		this.stepsOuput = new TextArea(stepStr);
		this.primesOutput = new TextArea(primesStr);
		this.petrickOuput = new TextArea(petrickStr);
		setTextAreasOnGrid();
		addTextAreas();
	}
	
	private void setTextAreasOnGrid()
	{
		 GridPane.setConstraints(this.stepsOuput, 1, 2);
	     GridPane.setConstraints(this.primesOutput, 1, 4);
	     GridPane.setConstraints(this.petrickOuput, 1, 6);
	     this.stepsOuput.setEditable(false);
	     this.primesOutput.setEditable(false);
	     this.petrickOuput.setEditable(false);
	}
	
	private void addElements()
	{
		GridPane.setConstraints(this.outputHBox,1,8);
		this.outputHBox.getChildren().addAll(this.saveButton, this.returnButton);
		this.outputGrid.getChildren().addAll(this.outputHBox, this.petricklabel, this.primesLabel, this.stepsLabel);
	}
	
	private void addTextAreas()
	{
		this.outputGrid.getChildren().addAll(this.petrickOuput, this.primesOutput, this.stepsOuput);
	}
	
	public Scene getOutputScene()
	{
		return this.outputScene;
	}
	
	public Button getSaveButton() {
		return saveButton;
	}
	
	public Button getReturnButton()
	{
		return this.returnButton;
	}
	
	private void setCss()
	{
		this.outputGrid.getStylesheets().add(GUI.class.getResource("text.css").toExternalForm());
	}
}

