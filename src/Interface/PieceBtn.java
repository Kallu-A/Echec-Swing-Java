package Interface;

import Interface.Image.PieceID;
import Move.Coord;
import Piece.Piece;
import Piece.Couleur;

import javax.swing.*;


/** bouton qui contien une pièce*/
public class PieceBtn extends JButton {

    public static EtatCoup etat = EtatCoup.PIECEDEPART;
    public static PieceBtn pieceDepart;
    public static PieceBtn pieceArriver;

    private Piece piece;
    private final Coord coord;

    public PieceBtn(Coord coord) {
        super();
        this.piece = new Piece(false, Couleur.VIDE);
        this.coord = coord;

    }

    public Piece getPiece() {
        return this.piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }



    public Coord getCoord() {
        return coord;
    }


    /** va s'occuper de savoir le click correspond a quoi */
    public static void coupJouer(){
        if (etat == EtatCoup.PIECEDEPART) etat = EtatCoup.PIECEARRIVER;
        else etat = EtatCoup.PIECEDEPART;
    }

    /** fait le mouvement d'une pièce et indique si il a été fait*/
    public boolean setMouvement(PieceBtn caseArriver, Icon etatLaisser){
        if (pieceDepart.equals(pieceArriver)) return false;
        this.setPieceEgaleA(caseArriver, etatLaisser);
        return true;
    }

    /** mais la pièce a la case a affecter */
    private void setPieceEgaleA(PieceBtn caseAffecter, Icon etatLaisser){
        caseAffecter.setIcon(getIcon());
        caseAffecter.getPiece().setPieceID(DicoPieceIcon.getIDFromIcon(getIcon()));
        caseAffecter.piece = this.piece;
        this.setPieceBtn(PieceID.VIDE, etatLaisser);
    }

    /** mais la piece égale a son PieceID et son icon*/
    public void setPieceBtn(PieceID pieceID, Icon defaultIcon){
        this.piece = DicoPieceIcon.transformerIDenPiece(pieceID);
        setIcon(defaultIcon);
    }
    @Override
    public void setIcon(Icon defaultIcon) {
        super.setIcon(defaultIcon);
    }

    /**  renvoie si les pieces peuvent s'accéder ou non  */
    public boolean isPlaceAcessible(PieceBtn pieceViser){
        return (this.piece.getCouleur() != pieceViser.piece.getCouleur());
    }

    /** renvoie si la piece en argument est mangeable par celle appelé */
    public boolean isMangeable(PieceBtn pieceViser){
        if (this.getPiece().getCouleur() == Couleur.BLANC && pieceViser.getPiece().getCouleur() == Couleur.NOIR) return true;
        if (this.getPiece().getCouleur() == Couleur.NOIR && pieceViser.getPiece().getCouleur() == Couleur.BLANC) return true;
        return false;
    }


}
