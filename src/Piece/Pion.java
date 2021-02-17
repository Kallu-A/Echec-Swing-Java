package Piece;


import Interface.Image.PieceID;
import Interface.Window;

import Move.Move;
import Move.Coord;

/** pièce le pion*/
public class Pion extends Piece {

    //utile pour le déplacement de 2
    private boolean jamaisJouer =true;

    public Pion(Couleur couleur){
        super(true, couleur);
        if (couleur == Couleur.NOIR) super.pieceID = PieceID.PION_NOIR;
        else super.pieceID = PieceID.PION_BLANC;
    }
    
    public void setJouer(){
        this.jamaisJouer = false;
    }

    public boolean getJamaisJouer(){
        return this.jamaisJouer;
    }

    /** détermine les coups possibles du pion*/
    @Override
    public boolean coupPossible(Window plateau, Move move){
        Pion pion = (Pion) plateau.getPieceBtn(move.to.ligne, move.to.colonne).getPiece();
        Couleur couleur = pion.getCouleur();
        boolean jamaisJouer = pion.getJamaisJouer();
        return ( coupCalculPossible(plateau, move, couleur, jamaisJouer) );
    }

    /** calcul des coups possibles du pion */
    private boolean coupCalculPossible(Window plateau, Move move, Couleur couleur, boolean jamaisJouer){
        short direction;
        Coord to = move.to;
        Coord from = move.from; 
        if (couleur == Couleur.NOIR) direction = 1;
        else direction = -1;
        //test de la colonne
        if (from.colonne == to.colonne ){
            //calcul des possiblités

           //test pour le premier coup de 1case                  
            if ( Window.isPlaceLibre( plateau.getPieceBtn( (short) (to.ligne + direction), to.colonne) ) ){
            if (from.colonne == to.colonne && from.ligne-to.ligne == direction) return true; 
            if ( Window.isPlaceLibre( plateau.getPieceBtn( (short) (to.ligne + 2 * direction), to.colonne) ) ){
                if (jamaisJouer && from.colonne == to.colonne && to.ligne+ (2* direction) == from.ligne) return true;
            }
            }
        } else {
        //cas ou on mange test si la colonne est bien adjacente 
            return (to.colonne - 1 == from.colonne || to.colonne + 1 == from.colonne) && to.ligne + direction == from.ligne
                    && plateau.getPieceBtn(to.ligne, to.colonne).isMangeable(plateau.getPieceBtn(from.ligne, from.colonne));
                    //si pas la même colonne est pas une pièce a manger alors faux
        }
        return false;
    }
}
