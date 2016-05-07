package observer;
import java.util.*;

public abstract class Subject {
	protected ArrayList<Observer> observers;
	
	public Subject(){
		this.observers = new ArrayList<>();
	}
	
	public void registerObserver(Observer o){
		observers.add(o);
	}
	
	public void removeObserver(Observer o){
		int i = observers.indexOf(o);
		if(i>=0){
			observers.remove(i);
		}
	}
	
	public abstract void updateAllObservers();
}
