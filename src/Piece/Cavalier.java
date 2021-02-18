package Piece;


import Interface.Image.PieceID;
import Interface.Window;
import Move.Move;
import Move.Coord;

/** pièce le cavalier
 * @author kallu */
public class Cavalier extends Piece{

    public static final short[][] VECTEUR_CAVALIER = { {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                                                     {-1, -2}, {1, -2}, {1, 2}, {-1, 2}};

    public Cavalier(Couleur couleur){
        super(true, couleur);
        if (couleur == Couleur.NOIR) super.pieceID = PieceID.CAVALIER_NOIR;
        else super.pieceID = PieceID.CAVALIER_BLANC;
    }

    /** fait les coups possibles du cavalier*/
    @Override
    public boolean coupPossible(Window plateau, Move move){
        short ligne, colonne;
        Coord to = move.to;
        Coord from = move.from;
        //calcul des chemins possibles
        for (short[]  vecteur : VECTEUR_CAVALIER) {
            ligne = (short) (to.ligne + vecteur[0]);
            colonne = (short) (to.colonne + vecteur[1]);
            //cordonnés du coup arriver
            if ( Window.isDansPlateau(ligne, colonne) ){
                //test si la piece d'arriver est accessible pour la piece de départ 
                if ( plateau.getPieceBtn( to.ligne, to.colonne ).isPlaceAcessible( plateau.getPieceBtn(ligne, colonne) ) ) {
                    if (from.ligne == ligne && from.colonne == colonne) return true;
                }
            }
        }
        return false;
    }
}
