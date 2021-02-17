package Piece;


import Interface.Image.PieceID;
import Interface.Window;

import Move.Move;
import Move.Coord;
/** piece de la reine*/
public class Reine extends Piece {
       
    public Reine(Couleur couleur){
        super(true, couleur);
        if (couleur == Couleur.NOIR) super.pieceID = PieceID.REINE_NOIR;
        else super.pieceID = PieceID.REINE_BLANC;
    }

    /** calcul si le coup est possible pour la reine*/
    @Override
    public boolean coupPossible(Window plateau, Move move){
        //même vecteur que le roi mais sans limite de distance
        short ligne, colonne ,i;
        Coord to = move.to;
        Coord from = move.from;
        //calcul des vecteurs
        for (short[]  vecteur : Roi.VECTEUR_ROI) {
            i=1;
            while (true){
                ligne = (short) (to.ligne + i * vecteur[0]);
                colonne = (short) (to.colonne + i * vecteur[1]);
                if (Window.isDansPlateau(ligne, colonne) ){
                    // test si la place est libre 
                    if ( Window.isPlaceLibre( plateau.getPieceBtn(ligne, colonne) ) ){
                        //si la position du vecteur rejoins celle de l'arriver alors possible
                        if (from.ligne == ligne && from.colonne == colonne) return true;
                    } else {
                        //si il y'a une piece test si elle est mangeable et si le vecteur rejoins l'arriver si oui alors vrai sinon break;
                        if (plateau.getPieceBtn(to.ligne, to.colonne).isMangeable(plateau.getPieceBtn(from.ligne, from.colonne) )
                            && from.ligne == ligne && from.colonne == colonne) return true;
                        break;
                    }
                } else break; 
                i++;
            }
        }
        return false;
    }
}

