import java.util.LinkedList;
//put(E o) puts cards at the bottom of the deck. when someone wins a round, their card and the one they beat are put at the bottom
//remove is helpful for when two cards are at war. they are removed 
public class FIFOQueue<E> extends LinkedList<E> {
    public FIFOQueue() {}
    
    public void put(E o) {
        addLast(o);
    }
    
    public E remove() {
        if (!this.isEmpty()) {
            return removeFirst();
        } else {
            System.err.println("You can\'t do that!");
            return null;
        } 
    }
    
    public E peek() {
        return getFirst();
    }
}
