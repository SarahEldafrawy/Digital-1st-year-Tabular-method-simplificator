package guiApp;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class GUI extends Application implements EventHandler<ActionEvent> {
	
	InputManage in;
	String stepsStr;
    String primesStr;
    String petrickStr;
    
    WelcomeScene welcome;
    ManualInputScene manualInput;
    OutputScene outputs;
    
    public static void main(String[] args) {
        launch(args);
    }
   
    @Override
    public void start(Stage window) {
        window.setTitle("Tabular Method");        
        
        window.setOnCloseRequest(e->{
        	
        	e.consume();
        	boolean answer = ExitBox.display("Exit", "Are you sure you want to exit?");
     	   	if(answer)
     	   		window.close();
        });
        welcome = new WelcomeScene("Tabular Method Simplification\nChoose a method of entry");  
        manualInput = new ManualInputScene("Insert Minterms:","Insert Don't Cares if there is:","Insert Number of Bits:");
        outputs = new OutputScene("Steps: " , "Implicants: ", "Simpliest Form: ");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        welcome.getManualButton().setOnAction(e -> window.setScene(manualInput.getManualScene()));
        welcome.getHelpButton().setOnAction(e -> 
        	HelpBox.display("Help Menu","\tQuine McCluskey(Tabular method) is a method used to simplify boolean expressions."
        			+ " You have to specify the number of variablesin the expression. Second you have to state the minterms which"
        			+ " the function satisfies. Optional: if there is don't cares in the function you"
        			+ " can state them too.")
        		);
        welcome.getBrowseButton().setOnAction( e -> {
			in = new InputManage();
			boolean flag = false;
			File file = fileChooser.showOpenDialog(window);
			try {
				if(file!=null)
					flag = in.startFile(file);
			} catch (IOException io) {
				AlertBox.display("Wrong File", "Please make sure that your file is not corrupted!!");
			}
			
			if(file!=null && !flag)
			{
				AlertBox.display("Wrong Input","Please make sure to validate your inputs");
			}
		}); 
        
        FileChooser fileSaver = new FileChooser();
        //Set extension filter //check if we need it and add it to fileChooser1
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileSaver.getExtensionFilters().add(extFilter);
        fileSaver.setTitle("Save Resource File");
        outputs.getSaveButton().setOnAction( e -> {
			
        	in = new InputManage();
			File file = fileSaver.showSaveDialog(window);
			if(file != null)
			{
				try {
					in.saveToFile(file);
				} catch (IOException io) {
					AlertBox.display("Wrong File","Please make sure to choose a valid location");
				}
			}
		}); 
        
       welcome.getExitButton().setOnAction(e -> {
    	   
    	   boolean answer = ExitBox.display("Exit", "Are you sure you want to exit?");
    	   if(answer)
    		   window.close();
       });
       
        
       manualInput.getSubmitButton().setOnAction(e-> {
    	   if (submit(manualInput.getMintermInput().getText(),manualInput.getDontCaresInput().getText(),manualInput.getNumberOfBitsInput().getValue())) {
    		   window.setScene(outputs.getOutputScene());
    	   } else {
    		   AlertBox.display("Wrong Input","Please make sure to validate your inputs");
    	   }
    	});
       manualInput.getReturnButton().setOnAction(e -> window.setScene(welcome.getScene()));
       
       outputs.getReturnButton().setOnAction(e->window.setScene(welcome.getScene()));
       
       window.setScene(welcome.getScene());
       window.show();
    	
    }
    
    public boolean submit (String minterms, String dontCares, Integer bits) {
    	
    	boolean flag;
    	stepsStr = "";
        primesStr = "";
        petrickStr = "";
    	in = new InputManage();
    	flag = in.startManual(minterms, dontCares, bits);
    	if(!flag)
    		return false;
    	stepsStr = getOutputs(in.getSteps());
    	primesStr = getOutputs(in.getPrimeImplicants());
    	petrickStr = getOutputs(in.getPetrick());
    	outputs.setTextAreas(stepsStr, primesStr, petrickStr);
    	return true;
    }
    
    
    public String getOutputs(LinkedList<String> temp)
    {
    	StringBuilder result = new StringBuilder();
    	for(int i = 0 ; i < temp.size(); i++)
		{
			for(int j = 0 ; j < temp.get(i).length()-1 ; j++)
			{
				if(temp.get(i).charAt(j) == ',')
					result.append(",  ");
				else
					result.append(temp.get(i).charAt(j));
			}
			result.append("\n");
		}
    	return result.toString();
    }

	@Override
	public void handle(ActionEvent arg0) {	
	}
}
