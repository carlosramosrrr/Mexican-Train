import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Creates the set of Dominos for the game
 * @author Carlos Ramos Guereca
 */
public class Set {
    private static final int nDominos = 55;
    private static List<Domino> dominos = new ArrayList<Domino>();

    /**
    Constructor creates all valid dominos in the set.
     */
    public Set() {
        int increment = 0;
        for(int i = 0; i <= 9; i++){
            for (int j = 0 + increment; j <=9; j++){

                dominos.add(new Domino(i,j));
            }
            increment++;
        }
    }
    /**
     * shuffles the set of dominios
     */
    public void shuffle(){
        Collections.shuffle(dominos);
    }
    /**
     * return the current set of dominos
     * @return
     */
    public List getDominos() {
        return dominos;
    }

    /**
     * get a single domino from the deck
     * a domino is unique and get only be obtained once.
     */
    public Domino getDomino(int index) {
        Domino temp = dominos.get(index);
        dominos.remove(index);
        return temp;
    }
    /**
     * get the size of the set
     * @return
     */
    public static int getSetSize() {
        return nDominos;
    }

    /**
     * get the current size of the set
     * @return
     */
    public static int getCurrentSize(){
        return dominos.size();
    }
}
