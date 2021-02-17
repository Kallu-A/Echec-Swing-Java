package Move;

import Interface.PieceBtn;
import Piece.Piece;

/** un deplacement constitué de deux coordonnee  */
public class Move {

    public static final Move GRAND_ROQUE_BLANC = new Move( new Coord((short) 7,(short) 4), new Coord((short) 7, (short) 0) );
    public static final Move PETIT_ROQUE_BLANC = new Move( new Coord((short) 7,(short) 4), new Coord((short) 7, (short) 7));
    public static final Move GRAND_ROQUE_NOIR = new Move( new Coord((short) 0,(short) 4), new Coord((short) 0, (short) 0));
    public static final Move PETIT_ROQUE_NOIR = new Move( new Coord((short) 0,(short) 4), new Coord((short) 0, (short) 7));


    public Coord to, from;

    public Move(Coord to, Coord from){
        this.to = to;
        this.from = from;
    }

    /** renvoie le type de roque -1 pas un roque 0 petit roque 1 grand roque*/
    public static int isPetitGrandRoque(Move deplacement){

        if (isMoveEgaux(deplacement, GRAND_ROQUE_BLANC)) return 1;
        if (isMoveEgaux(deplacement, GRAND_ROQUE_NOIR)) return 1;
        if (isMoveEgaux(deplacement, PETIT_ROQUE_BLANC)) return 0;
        if (isMoveEgaux(deplacement, PETIT_ROQUE_NOIR))return 0;
        return -1;
    }

    /** test si deux Move sont les mêmes (.equal fonctionnent pas*/
    private static boolean isMoveEgaux(Move deplacement, Move deplacement2){
        if (deplacement.to.ligne == deplacement2.to.ligne &&
        deplacement.to.colonne == deplacement2.to.colonne &&
        deplacement.from.ligne == deplacement2.from.ligne &&
        deplacement.from.colonne == deplacement2.from.colonne) return true;
        else return false;
    }

    @Override
    public String toString() {
        return "Move{" +
                "to=" + to.toString() +
                ", from=" + from.toString() +
                '}';
    }

}
