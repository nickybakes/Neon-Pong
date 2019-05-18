
public class Pair<T1, T2> {
	
	private T1 first;
	private T2 second;
	
	Pair(T1 newFirst, T2 newSecond) {
		
		first = newFirst;
		second = newSecond;
	}
	
	public T1 get1() {
		
		return first;
	}
	
	public T2 get2() {
		
		return second;
	}
	
	public void set1(T1 newFirst) {
		
		first = newFirst;
	}
	
	public void set2(T2 newSecond) {
		
		second = newSecond;
	}
}
