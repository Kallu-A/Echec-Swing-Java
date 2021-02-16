package Piece;

import Interface.Image.PieceID;
import Interface.PieceBtn;
import Interface.Window;
import Move.Move;

public class Piece {

    private boolean unePiece;
    private Couleur couleur;
    protected PieceID pieceID = PieceID.VIDE;

    public Piece(boolean unePiece, Couleur couleur){
        this.unePiece = unePiece;
        this.couleur = couleur;
    }

    /** sert a l'override */
    public boolean coupPossible(Window plateau, Move move){
        return false;
    }

    /** renvoie si c'est une pièce */
    public boolean isUnePiece() {
        return this.unePiece;
    }

    /** setter piece si il y a ou non */
    public void setUnePiece(boolean unePiece) {
        this.unePiece = unePiece;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public void setCouleur(Couleur couleur) {
        this.couleur = couleur;
    }

    public PieceID getPieceID() {
        return pieceID;
    }

    /** set l'id de la piece*/
    public void setPieceID(PieceID pieceID) {
        this.pieceID = pieceID;
    }

    @Override
    public String toString() {
        return "Case sélectionnée:" + pieceID;
    }
}

