package guiApp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;


public class InputManage {

	private int numberOfBits;
	private String minterms;
	private String dontCares;
	private LinkedList<String> steps;
	private LinkedList<String> primeImplicants;
	private LinkedList<String> petrick;
	
	public InputManage()
	{
		this.minterms = "";
		this.dontCares = "";
		this.numberOfBits = 0;
		this.petrick = new LinkedList<>();
		this.primeImplicants = new LinkedList<>();
		this.steps = new LinkedList<>();
	}
	
	public boolean startManual(String minterms, String dontCares, int numberOfBits)
	{	
		if(!validate(numberOfBits,minterms,dontCares))
			return false;
		Grouping tabular = new Grouping(numberOfBits);
		this.steps = (LinkedList<String>) tabular.start(minterms, dontCares).clone();	
		this.primeImplicants = (LinkedList<String>) tabular.getPrimeImplicants().clone();
		this.petrick = (LinkedList<String>) tabular.getPetrick().clone();
		return true;
	}
	
	public boolean startFile(File file) throws IOException
	{
		getFromFile(file);
		
		if(!validate(this.numberOfBits,this.minterms,this.dontCares))
			return false;
		Grouping tabular = new Grouping(this.numberOfBits);
		this.steps = (LinkedList<String>) tabular.start(this.minterms, this.dontCares).clone();	
		this.primeImplicants = (LinkedList<String>) tabular.getPrimeImplicants().clone();
		this.petrick = (LinkedList<String>) tabular.getPetrick().clone();
		return true;
		
		
		
	}

	public void getFromFile(File file) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String str = "";
		int lineNumber = 1;
		while((str=br.readLine()) != null)
		{
			if(lineNumber == 1)
			{
				this.numberOfBits = Integer.parseInt(getDigit(str,0));
			}
			else if(lineNumber == 2)
			{
				this.minterms = str;
			}
			else if(lineNumber == 3)
			{
				this.dontCares = str;
			}
			else
				break;
			lineNumber++;
		}
		
		br.close();
	}
	
	public void saveToFile(File file) throws IOException
	{	
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		BufferedReader br = new BufferedReader(new FileReader("prime.csv"));
		
		String row = "";
		while((row=br.readLine())!=null)
		{
			bw.write(row);
			bw.newLine();
		}
		br.close();
		bw.close();
	}

	private String getDigit(String expression, int index)
	{
		if(expression.length() == 0)
			throw null;
		StringBuilder digit = new StringBuilder();
		digit.append(expression.charAt(index));
		for(int i = index + 1 ; i < expression.length() ; i++)
		{
			if(expression.charAt(i) == ' ' || expression.charAt(i) == ',')
				break;
			digit.append(expression.charAt(i));
		}
		
		return digit.toString();
	}

	public LinkedList<String> getSteps() {
		return this.steps;
	}

	public LinkedList<String> getPrimeImplicants() {
		return this.primeImplicants;
	}

	public LinkedList<String> getPetrick() {
		
		if(this.petrick.size() == 0)
			return this.petrick;
		reducePetrick();
		for(int i = 0 ; i < this.petrick.size(); i++)
		{
			String temp = this.petrick.getFirst();
			this.petrick.removeFirst();
			StringBuilder collector = new StringBuilder(temp);
			for(int j = 0 ; j < temp.length()-1; j++)
			{
				if(temp.charAt(j) == ',')
					collector.setCharAt(j, '+');
			}
			temp = collector.toString();
			this.petrick.addLast(temp);
		}
		
		return this.petrick;
	}
	
	private void reducePetrick()
	{
		int min = getTrueLength(this.petrick.get(0));
		for(int i = 1 ; i < this.petrick.size() ; i++)
		{
			if(min > getTrueLength(this.petrick.get(i)))
				min = getTrueLength(this.petrick.get(i));
		}
		
		for(int i = 0 ; i < this.petrick.size() ; i++)
		{
			if(getTrueLength(this.petrick.get(i)) > min)
			{
				this.petrick.remove(i);
				i--;
			}
		}
		
	}

	private int getTrueLength(String expression)
	{
		int length = 0;
		for(int i = 0 ; i < expression.length() ; i++)
		{
			if(expression.charAt(i) != ' ' && expression.charAt(i) != '\'' && expression.charAt(i) != ',')
				length++;
		}
		return length;
	}
	public boolean validate(int numberOfMinterms, String minterms, String dontCares)
	{
		
		if(minterms.length() == 0)
			return false;
		if(containsChar(minterms))
			return false;
		if(containsChar(dontCares))
			return false;
		if(!belongsToRange(minterms,numberOfMinterms))
			return false;
		if(!belongsToRange(dontCares,numberOfMinterms))
			return false;
		if(hasSameTerms(minterms,dontCares))
			return false;
			
		return true;
	}
	
	private boolean containsChar(String str)
	{
		
		for(int i = 0 ; i < str.length() ; i++)
		{
			if(str.charAt(i) != ' ' && str.charAt(i) != ',' && !Character.isDigit(str.charAt(i)))
				return true;
		}
		return false;
	}

	private boolean belongsToRange(String str, int maxRange)
	{
		if(str.length() == 0)
			return true;
		int max = Integer.parseInt(getDigit(str,0));
		for(int i = getDigit(str,0).length(); i < str.length() ; i++)
		{
			if(str.charAt(i) == ' ' || str.charAt(i) == ',')
				continue;
			
			int temp = Integer.parseInt(getDigit(str,i));	
			if(temp > max)
				max = temp;
		}
		
		
		if(Math.pow(2,maxRange) >= max)
		{
			return true;
		}
		return false;
	}

	private boolean hasSameTerms(String str1, String str2)
	{
		int[] minterms = new int[str1.length()];
		int[] dontCares = new int[str2.length()];
		int k = 0;
		for(int i = 0 ; i < str1.length() ; i++)
		{
			if(str1.charAt(i) == ' ' || (str1.charAt(i)) == ',')
					continue;
			minterms[k] = Integer.parseInt(getDigit(str1,i));
			i += getDigit(str1,i).length() - 1;
			k++;
		}
		
		if(str2.length() != 0)
		{
			k = 0;
			for(int i = 0 ; i < str2.length() ; i++)
			{
				if(str2.charAt(i) == ' ' || str2.charAt(i) == ',')
						continue;
				dontCares[k] = Integer.parseInt(getDigit(str2,i));
				i += getDigit(str2,i).length() - 1;
				k++;
			}
		}
			
		
		for(int i = 0 ; i  < minterms.length-1 ; i++)
		{
			if(minterms[i] == 0 && i != 0)
				break;
			for(int j = i+1 ; j < minterms.length ; j++)
			{
				if(minterms[j] == 0)
					break;
				if(minterms[i] == minterms[j])
					return true;
			}
		}
		
		for(int i = 0 ; i  < dontCares.length-1 ; i++)
		{
			if(dontCares[i] == 0 && i != 0)
				break;
			for(int j = i+1 ; j < dontCares.length ; j++)
			{
				if(dontCares[j] == 0)
					break;
				if(dontCares[i] == dontCares[j])
					return true;
			}
		}
		
		for(int i = 0 ; i  < minterms.length ; i++)
		{
			if(minterms[i] == 0  && i != 0)
				break;
			for(int j = 0 ; j < dontCares.length ; j++)
			{
				if(dontCares[j] == 0 && j != 0)
					break;
				
				if(minterms[i] == dontCares[j])
					return true;
			}
		}
		
		return false;
	}
	
}
