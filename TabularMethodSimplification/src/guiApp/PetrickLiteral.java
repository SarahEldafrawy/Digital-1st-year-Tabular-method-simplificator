package guiApp;

import java.util.LinkedList;

public class PetrickLiteral {

	private LinkedList<PrimeImplicant> primeImplicants;
	private LinkedList<Integer> indexOfImplicants;
	private static boolean isEqualFlag = false;
	
	public PetrickLiteral()
	{
		this.primeImplicants = new LinkedList<PrimeImplicant>();
		this.indexOfImplicants = new LinkedList<Integer>();
	}
	
	public void setPrimeImplicants(LinkedList<PrimeImplicant> temp)
	{
		for(int i = 0 ; i < temp.size(); i++)
			this.primeImplicants.addLast(temp.get(i));
		removeDuplicatePrimeImplicant();
	}
	
	public void addPrimeImplicant(PrimeImplicant multipliedImplicant)
	{
		this.primeImplicants.addLast(multipliedImplicant);
	}
	
	public LinkedList<PrimeImplicant> getPrimeImplicants()
	{
		return this.primeImplicants;
	}
	
	public void setIndexOfImplicants(LinkedList<Integer> indexOfImplicants) {
		
		for(int i = 0 ; i < indexOfImplicants.size() ; i++)
			this.indexOfImplicants.addLast(indexOfImplicants.get(i));
		removeDuplicateIndexOfImplicant();
	}
	
	public void addIndexOfImplicants(int indexOfImplicants) {
		
		this.indexOfImplicants.addLast(indexOfImplicants);
		removeDuplicateIndexOfImplicant();
	}

	public LinkedList<Integer> getIndexOfImplicants() {
		return indexOfImplicants;
	}
	
	private void removeDuplicatePrimeImplicant()
	{
		for(int i = 0 ; i < this.primeImplicants.size() - 1 ; i++)
		{
			for(int j = i+1 ; j < this.primeImplicants.size() ; j++)
			{
				if(this.primeImplicants.get(i).getIndex() == this.primeImplicants.get(j).getIndex())
				{
					this.primeImplicants.remove(j);
					j--;
				}
			}
		}
	}

	private void removeDuplicateIndexOfImplicant()
	{
		for(int i = 0 ; i < this.indexOfImplicants.size() - 1 ; i++)
		{
			for(int j = i+1 ; j < this.indexOfImplicants.size() ; j++)
			{
				if(this.indexOfImplicants.get(i) == this.indexOfImplicants.get(j))
				{
					this.indexOfImplicants.remove(j);
					j--;
				}
			}
		}
	}

	public static boolean compareIndexOfImplicants(LinkedList<Integer> list1, LinkedList<Integer> list2)
	{
		int numberOfDiff = 0;
		boolean foundFlag;
		isEqualFlag = false;
		
		for(int i = 0 ; i < list1.size() ; i++)
		{
			int index1 = list1.get(i);
			foundFlag = false;
			for(int j = 0 ; j < list2.size(); j++)
			{
				int index2 = list2.get(j);
				
				if(index1 == index2)
				{
					foundFlag = true;
					break;
				}
			}
			
			if(!foundFlag)
				numberOfDiff++;
		}
		
		if(numberOfDiff == 1)
			return true;
		else if(numberOfDiff > 1)
			return false;
		else
		{
			isEqualFlag = true;
			return false;
		}
	}
	
	public static boolean isEqualLiterals(LinkedList<Integer> list1, LinkedList<Integer> list2)
	{
		if(!compareIndexOfImplicants(list1, list2) && isEqualFlag)
			return true;
		return false;
	}

}
