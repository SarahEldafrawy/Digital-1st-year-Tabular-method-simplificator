package guiApp;

public class Stack<T> {
	
	private class StackNode<T> {
		
		private StackNode<T> next;
		private T element;
		
		public StackNode()
		{
			this(null,null);
		}
		
		public StackNode(StackNode<T> nextNode ,T element)
		{
			this.next = nextNode;
			this.element = element;
		}
		
		public StackNode<T> getNext()
		{
			return this.next;
		}
		
		public T getValue()
		{
			return this.element;
		}
	}
	
	private StackNode<T> top;
	private int size;
	
	public Stack()
	{
		this.top = null;
		this.size = 0;
	}
	
	public void push(T element)
	{
		StackNode<T> entry = new StackNode<T>(this.top,element);
		this.top = entry;
		this.size++;
	}
	
	public T pop()
	{
		if(isEmpty())
			throw null;
		T temp = top();
		this.top = this.top.getNext();
		this.size--;
		return temp;
		
	}
	
	public T top()
	{
		if(!isEmpty())
			return this.top.getValue();
		throw null;
	}
	
	public boolean isEmpty()
	{
		if(size == 0)
			return true;
		return false;
	}
	
	public int size()
	{
		return this.size;
	}
	
	public void clear()
	{
		this.top = null;
		this.size = 0;
	}
}
