package guiApp;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class Grouping {

	private LinkedList<PrimeImplicant> PrimeImplicants;
	private Queue<PrimeImplicant>[] tabularTable;
	private Queue<PrimeImplicant>[] auxTable;
	private int numberOfBits;
	private boolean flag;
	private int[] minterms;
	private BufferedWriter bw;
	private StringBuilder setupTemp;
	private LinkedList<String> steps;
	private LinkedList<String> primes;
	private LinkedList<String> petricks;

	public Grouping(int numberOfBits) {
		this.flag = true;
		this.numberOfBits = numberOfBits;
		this.PrimeImplicants = new LinkedList<>();
		this.steps = new LinkedList<>();
		this.primes = new LinkedList<>();
		this.petricks = new LinkedList<>();
		this.auxTable = new Queue[this.numberOfBits];
		this.tabularTable = new Queue[this.numberOfBits + 1];
		for (int i = 0; i < this.tabularTable.length; i++) {
			this.tabularTable[i] = new Queue<PrimeImplicant>();

			if (i < this.tabularTable.length - 1)
				this.auxTable[i] = new Queue<PrimeImplicant>();
		}
		try {
			this.bw = new BufferedWriter(new FileWriter("prime.csv"));

		} catch (Exception e) {

		}
	}

	public LinkedList<String> start(String minterms, String dontCares) {
		transferToPI(minterms, dontCares);
		while (this.flag) {
			this.setupTemp = new StringBuilder();
			this.flag = false;

			groupingMainLoop();
			if (this.flag) {
				writeToFile();
				this.steps.add(this.setupTemp.toString());
			}

		}

		return this.steps;
	}

	private void groupingMainLoop() {

		for (int i = 0; i < this.tabularTable.length - 1; i++) {
			compareTwoImplicants(i);
			checkForNotVisited(i);
		}
		checkForNotVisited(this.tabularTable.length - 1);

		this.tabularTable = this.auxTable.clone();
		if (this.tabularTable.length != 1) {
			this.auxTable = new Queue[this.tabularTable.length - 1];
			for (int i = 0; i < this.auxTable.length; i++) {
				this.auxTable[i] = new Queue<PrimeImplicant>();
			}
		} else
			this.flag = false;

	}

	private void compareTwoImplicants(int i) {

		int j = 0, k = 0;
		int diffBetwImplicants = 0;
		boolean redundantFlag;
		PrimeImplicant implicant1, implicant2;
		while (j < this.tabularTable[i].size()) {
			implicant1 = this.tabularTable[i].dequeue();
			k = 0;
			while (k < this.tabularTable[i + 1].size()) {
				implicant2 = this.tabularTable[i + 1].dequeue();
				diffBetwImplicants = implicant2.getValue() - implicant1.getValue();

				if (diffBetwImplicants > 0) {

					if (Integer.bitCount(diffBetwImplicants) == 1) {

						if (Arrays.equals(implicant1.getArrayBits(), implicant2.getArrayBits())) {

							PrimeImplicant newImplicant = new PrimeImplicant(implicant1.getValue(), this.numberOfBits);
							newImplicant.setArrayBits(implicant1.getArrayBits().clone());
							newImplicant.setDifferenceBit(diffBetwImplicants);
							implicant1.setVisited(true);
							implicant2.setVisited(true);

							redundantFlag = redundant(newImplicant, i);

							if (!redundantFlag) {
								this.auxTable[i].enqueue(newImplicant);
								setupImplicant(newImplicant);
							}

							this.flag = true;
						}
					}
				}
				k++;
				this.tabularTable[i + 1].enqueue(implicant2);
			}
			j++;
			this.tabularTable[i].enqueue(implicant1);
		}
	}

	private void checkForNotVisited(int i) {
		int j = 0;
		while (j < this.tabularTable[i].size()) {
			PrimeImplicant temp = this.tabularTable[i].dequeue();
			if (!temp.isVisited()) {

				if (temp.flagD())
					continue;
				else
					this.PrimeImplicants.addLast(temp);
			}
			this.tabularTable[i].enqueue(temp);
			j++;
		}
	}

	private void setupImplicant(PrimeImplicant implicant) {

		char[] xstr = implicant.getArrayBits().clone();
		String printstr = Integer.toBinaryString(implicant.getValue());

		for (int i = printstr.length() - 1; i >= 0; i--) {
			if (xstr[printstr.length() - i - 1] == '0' && printstr.charAt(i) == '1')
				xstr[printstr.length() - i - 1] = '1';
		}

		for (int i = xstr.length - 1; i >= 0; i--) {
			if (xstr[i] != 'x') {
				char temp = (char) ('A' + xstr.length - i - 1);
				setupTemp.append(temp);
				if (xstr[i] == '0') {
					setupTemp.append("'");
				}
			}
		}
		setupTemp.append(",");
	}

	private boolean redundant(PrimeImplicant implicant, int index) {
		int i = 0;
		PrimeImplicant temp;
		while (i < this.auxTable[index].size()) {
			temp = this.auxTable[index].dequeue();
			if (temp.getValue() == implicant.getValue()) {
				if (Arrays.equals(temp.getArrayBits(), implicant.getArrayBits())) {
					this.auxTable[index].enqueue(temp);
					return true;
				}
			}
			this.auxTable[index].enqueue(temp);
			i++;
		}
		return false;
	}

	private void transferToPI(String expression, String dontCares) {
		if (expression.length() == 0)
			throw null;

		Queue<Integer> mintermsQueue = new Queue<Integer>();
		for (int i = 0; i < expression.length(); i++) {
			if (expression.charAt(i) == ' ' || expression.charAt(i) == ',')
				continue;
			int minterm = Integer.parseInt(getDigit(expression, i));
			i += getDigit(expression, i).length();
			mintermsQueue.enqueue(minterm);
			this.tabularTable[Integer.bitCount(minterm)].enqueue(new PrimeImplicant(minterm, this.numberOfBits));
		}

		for (int i = 0; i < dontCares.length(); i++) {
			if (dontCares.charAt(i) == ' ' || dontCares.charAt(i) == ',')
				continue;
			int minterm = Integer.parseInt(getDigit(dontCares, i));
			i += getDigit(dontCares, i).length();
			PrimeImplicant temp = new PrimeImplicant(minterm, this.numberOfBits);
			temp.setflagD();
			this.tabularTable[Integer.bitCount(minterm)].enqueue(temp);
		}

		this.minterms = new int[mintermsQueue.size()];
		int constantSize = mintermsQueue.size();
		for (int i = 0; i < constantSize; i++) {
			this.minterms[i] = mintermsQueue.dequeue();
		}
	}

	private String getDigit(String expression, int index) {
		if (expression.length() == 0)
			throw null;
		StringBuilder digit = new StringBuilder();
		digit.append(expression.charAt(index));
		for (int i = index + 1; i < expression.length(); i++) {
			if (expression.charAt(i) == ' ' || expression.charAt(i) == ',')
				break;
			digit.append(expression.charAt(i));
		}

		return digit.toString();
	}

	private void petrickOptimization() {
		
		LinkedList<PrimeImplicant>[] coveringPrimeImplicants = new LinkedList[this.minterms.length];
		for (int i = 0; i < coveringPrimeImplicants.length; i++) {
			coveringPrimeImplicants[i] = new LinkedList<>();
		}
		LinkedList<PetrickLiteral> expression = new LinkedList<>();
		Stack<LinkedList<PetrickLiteral>> multiplyingStack = new Stack<>();
		coveringPrimeImplicants = petrickSetup(coveringPrimeImplicants);
		
		LinkedList<PrimeImplicant> essentials = essentialImplicants(coveringPrimeImplicants);
		rowDominance(coveringPrimeImplicants);
		colDominance(coveringPrimeImplicants);
		
//		expression = toPetrick(coveringPrimeImplicants[0]);	 
//		multiplyingStack.push(expression);
		 
		for (int i = 0; i < coveringPrimeImplicants.length; i++) {
			if(coveringPrimeImplicants[i] == null)
				continue;
			if(multiplyingStack.isEmpty())
			{
				multiplyingStack.push(toPetrick(coveringPrimeImplicants[i]));
				continue;
			}
				
			multiplyingStack.push(simplifyPetrickExpression(multiplyPetrickExpression(toPetrick(coveringPrimeImplicants[i]), multiplyingStack.pop())));
		}

		if(!multiplyingStack.isEmpty())
			expression = multiplyingStack.pop();
		
		if(expression.size() == 0)
		{
			this.setupTemp = new StringBuilder();
			for(int j = 0 ; j < essentials.size() ; j++)
			{
				setupImplicant(essentials.get(j));
			}
			writeToFile();
			this.petricks.add(this.setupTemp.toString());
		}
		else
		{
			for (int i = 0; i < expression.size(); i++) {
				this.setupTemp = new StringBuilder();
				PetrickLiteral temp = expression.get(i);
				for (int j = 0; j < temp.getPrimeImplicants().size(); j++) {
					setupImplicant(temp.getPrimeImplicants().get(j));
				}
				for(int j = 0 ; j < essentials.size() ; j++)
				{
					setupImplicant(essentials.get(j));
				}
				writeToFile();
				this.petricks.add(this.setupTemp.toString());
			}
		}
		
	}

	private LinkedList<PrimeImplicant>[] petrickSetup(LinkedList<PrimeImplicant>[] coveringPrimeImplicants) {
		for (int i = 0; i < this.PrimeImplicants.size(); i++) {
			this.PrimeImplicants.get(i).setIndex(i + 1);
		}

		for (int i = 0; i < this.minterms.length; i++) {
			int temp = this.minterms[i];
			char[] tempMap = mintermBitMap(temp);
			for (int j = 0; j < this.PrimeImplicants.size(); j++) {
				PrimeImplicant expectedImplicant = this.PrimeImplicants.get(j);
				if (compareBits(tempMap, expectedImplicant.getArrayBits())) {
					coveringPrimeImplicants[i].addLast(expectedImplicant);
				}
			}
		}
		return coveringPrimeImplicants;
	}

	private char[] mintermBitMap(int minterm) {
		String intMap = Integer.toBinaryString(minterm);
		char[] bitMap = new char[this.numberOfBits];

		for (int i = 0; i < bitMap.length; i++)
			bitMap[i] = '0';
		for (int i = intMap.length() - 1; i >= 0; i--) {
			bitMap[intMap.length() - i - 1] = intMap.charAt(i);
		}

		return bitMap;
	}

	private boolean compareBits(char[] a, char[] b) {
		for (int i = 0; i < a.length; i++) {
			if (a[i] == b[i])
				continue;
			else if (b[i] == 'x')
				continue;
			return false;
		}
		return true;
	}

	private LinkedList<PetrickLiteral> toPetrick(LinkedList<PrimeImplicant> primeImplicants) {

		LinkedList<PetrickLiteral> expression = new LinkedList<PetrickLiteral>();

		for (int i = 0; i < primeImplicants.size(); i++) {
			PrimeImplicant temp = primeImplicants.get(i);
			PetrickLiteral entry = new PetrickLiteral();
			entry.addPrimeImplicant(temp);
			entry.addIndexOfImplicants(temp.getIndex());
			expression.addLast(entry);
		}

		return expression;
	}

	private LinkedList<PetrickLiteral> multiplyPetrickExpression(LinkedList<PetrickLiteral> exp1,LinkedList<PetrickLiteral> exp2) {
		LinkedList<PetrickLiteral> expression = new LinkedList<PetrickLiteral>();
		for (int i = 0; i < exp1.size(); i++) {
			PetrickLiteral literal1 = exp1.get(i);
			for (int j = 0; j < exp2.size(); j++) {
				PetrickLiteral literal2 = exp2.get(j);
				PetrickLiteral entry = new PetrickLiteral();
				entry.setPrimeImplicants(literal1.getPrimeImplicants());
				entry.setPrimeImplicants(literal2.getPrimeImplicants());
				entry.setIndexOfImplicants(literal1.getIndexOfImplicants());
				entry.setIndexOfImplicants(literal2.getIndexOfImplicants());
				expression.addLast(entry);
			}
		}

		return expression;
	}

	private LinkedList<PetrickLiteral> simplifyPetrickExpression(LinkedList<PetrickLiteral> exp) {
		if (exp.size() < 2)
			return exp;

		for (int i = 0; i < exp.size() - 1; i++) {
			PetrickLiteral literal1 = exp.get(i);
			for (int j = i + 1; j < exp.size(); j++) {
				PetrickLiteral literal2 = exp.get(j);

				int diff = literal1.getIndexOfImplicants().size() - literal2.getIndexOfImplicants().size();

				if (diff > 0 && PetrickLiteral.compareIndexOfImplicants(literal1.getIndexOfImplicants(),
						literal2.getIndexOfImplicants())) {
					exp.remove(i);
					i--;
					break;
				} else if (diff < 0 && PetrickLiteral.compareIndexOfImplicants(literal2.getIndexOfImplicants(),
						literal1.getIndexOfImplicants())) {
					exp.remove(j);
					j--;
				} else if (diff == 0 && PetrickLiteral.isEqualLiterals(literal1.getIndexOfImplicants(),
						literal2.getIndexOfImplicants())) {
					exp.remove(j);
					j--;
				}
			}
		}
		return exp;
	}

	private void writeToFile() {
		try {
			String str = this.setupTemp.toString();

			for (int i = 0; i < str.length() - 1; i++) {
				bw.write(str.charAt(i));
			}
			bw.newLine();
		} catch (Exception e) {

		}

	}

	private void setupAllPrime() {
		for (int i = 0; i < this.PrimeImplicants.size(); i++) {
			PrimeImplicant temp = this.PrimeImplicants.get(i);
			setupImplicant(temp);
			temp.setValueBit(temp.getValue());
		}
		this.primes.add(this.setupTemp.toString());
	}

	public LinkedList<String> getPetrick() {

		petrickOptimization();
		try {
			bw.close();
		} catch (Exception e) {

		}

		return this.petricks;
	}

	public LinkedList<String> getPrimeImplicants() {
		setupAllPrime();
		writeToFile();
		return this.primes;
	}

	private void colDominance (LinkedList<PrimeImplicant>[] coveringPrimeImplicants) {
		
		int size = this.PrimeImplicants.size();
		for (int i = 0; i < coveringPrimeImplicants.length; i++) {
			if(coveringPrimeImplicants[i] == null)
				continue;
			if (coveringPrimeImplicants[i].size() == size) {
				coveringPrimeImplicants[i] = null;
			}
		}
	}
	
	private void rowDominance (LinkedList<PrimeImplicant>[] coveringPrimeImplicants) {
	
		LinkedList<LinkedList<Integer>> primeMap = new LinkedList<>();
		LinkedList<PrimeImplicant> addPrimes = new LinkedList<>();
		for(int i = 0 ; i < this.PrimeImplicants.size() ; i++)
		{
			PrimeImplicant implicantToTrace = this.PrimeImplicants.get(i);
			LinkedList<Integer> primeMinterms = new LinkedList<>();
			for(int j = 0 ; j < coveringPrimeImplicants.length ; j++)
			{
				if(coveringPrimeImplicants[j] != null && coveringPrimeImplicants[j].contains(implicantToTrace))
				{
					primeMinterms.add(this.minterms[j]);
				}
			}
			
			if(primeMinterms.size() != 0)
			{
				primeMap.add(primeMinterms);
				addPrimes.add(implicantToTrace);
			}
				
		}
		
		this.PrimeImplicants = (LinkedList<PrimeImplicant>) addPrimes.clone();
		rowDominanceLoop(primeMap, coveringPrimeImplicants);
	}

	private void rowDominanceLoop (LinkedList<LinkedList<Integer>> primeMap,LinkedList<PrimeImplicant>[] coveringPrimeImplicants) {
						
		for(int i = 0 ; i < primeMap.size()-1 ; i++)
		{
			LinkedList<Integer> row1 = primeMap.get(i);
			for(int j = i + 1 ; j < primeMap.size() ; j++)
			{
				LinkedList<Integer> row2 = primeMap.get(j);
				int valueOfDeletion = rowDominanceCompare(row1, row2);
				
				if(valueOfDeletion == 0 || valueOfDeletion == 1)
				{
					if(valueOfDeletion == 0)
					{
						rowDominanceRemove(primeMap, i, coveringPrimeImplicants);
						i--;
						break;
					}
					else
					{
						rowDominanceRemove(primeMap, j, coveringPrimeImplicants);
						j--;
					}
				}
			}
		}
		
	}
		
	private int rowDominanceCompare(LinkedList<Integer> row1, LinkedList<Integer> row2)
	{
		boolean containFlag = true;
		
		if(row1.equals(row2))
			return -1;
		
		for(int i = 0 ; i < row1.size() && containFlag ; i++)
		{
			for(int j = 0 ; j < row2.size() && containFlag ; j++)
			{	
				containFlag = false;
				if(row1.get(i) == row2.get(j))
				{
					containFlag = true;
					break;
				}
			}
		}
		
		if(containFlag)
			return 0;
		
		containFlag = true;
		
		for(int i = 0 ; i < row2.size() && containFlag ; i++)
		{
			for(int j = 0 ; j < row1.size() && containFlag ; j++)
			{	
				containFlag = false;
				if(row2.get(i) == row1.get(j))
				{
					containFlag = true;
					break;
				}
			}
		}
		
		if(containFlag)
			return 1;
		
		return -1;
	}
	
	private void rowDominanceRemove (LinkedList<LinkedList<Integer>> primeMap, int index, LinkedList<PrimeImplicant>[] coveringPrimeImplicants) {
	
		PrimeImplicant ImplicantToRemove = PrimeImplicants.get(index);
		for (int i = 0; i < coveringPrimeImplicants.length; i++) {
			if(coveringPrimeImplicants[i] == null)
				continue;
			if (coveringPrimeImplicants[i].contains(ImplicantToRemove)) {
				coveringPrimeImplicants[i].remove(ImplicantToRemove);
			}
		}	
		this.PrimeImplicants.remove(index);
		primeMap.remove(index);
	}
	
	private LinkedList<PrimeImplicant> essentialImplicants(LinkedList<PrimeImplicant>[] coveringPrimeImplicants)
	{
		LinkedList<PrimeImplicant> essentials = new LinkedList<>();
		
		for(int i = 0 ; i < coveringPrimeImplicants.length ; i++)
		{
			if(coveringPrimeImplicants[i] != null && coveringPrimeImplicants[i].size() == 1)
			{
				PrimeImplicant temp = coveringPrimeImplicants[i].getFirst();
				essentials.addLast(temp);
				for(int j = 0 ; j < coveringPrimeImplicants.length ; j++)
				{
					if(coveringPrimeImplicants[j] != null && coveringPrimeImplicants[j].contains(temp))
						coveringPrimeImplicants[j] = null;
				}
				coveringPrimeImplicants[i] = null;
			}
		}
		
		for(int i = 0 ; i < coveringPrimeImplicants.length ; i++)
		{
			if(coveringPrimeImplicants[i] == null)
				continue;
			if(coveringPrimeImplicants[i].size() == 0)
				coveringPrimeImplicants[i] = null;
		}
		return (LinkedList<PrimeImplicant>) essentials.clone();
		
	}

}
