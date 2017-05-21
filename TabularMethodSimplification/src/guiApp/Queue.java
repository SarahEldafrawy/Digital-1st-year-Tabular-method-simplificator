package guiApp;

public class Queue<T> {

	private class QueueNode<T>
	{
		private T element;
		private QueueNode<T> next;
		
		public QueueNode()
		{
			this(null,null);
		}
		
		public QueueNode(T element, QueueNode<T> next)
		{
			this.element = element;
			this.next = next;
		}
		
		public void setElement(T element)
		{
			this.element = element;
		}
		
		public void setNext(QueueNode<T> next)
		{
			this.next = next;
		}
		
		public T getElement()
		{
			return this.element;
		}
		
		public QueueNode<T> getNext()
		{
			return this.next;
		}
	}
	
	private QueueNode<T> head;
	private QueueNode<T> tail;
	private int size;
	
	public Queue()
	{
		this.head = null;
		this.tail = null;
		this.size =  0;
	}
	
	public void enqueue(T item) {
		QueueNode<T> temp = new QueueNode<>(item,null);
		if(isEmpty())
		{
			this.tail = temp;
			this.head = this.tail;
		}
		else
		{
			this.tail.setNext(temp);
			this.tail = temp;
		}
		this.size++;
	}
	
	public T dequeue() {
		if(isEmpty())
			throw null;
		T temp = this.head.getElement();
		this.head = this.head.getNext();
		this.size--;
		return temp;
	}

	public boolean isEmpty() {
		if(this.size == 0)
			return true;
		return false;
	}

	public int size() {
		return this.size;
	}

}
