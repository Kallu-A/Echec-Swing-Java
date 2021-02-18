package Interface;

import Interface.Image.PieceID;
import Move.Move;
import Move.Coord;
import Piece.*;

/** extends Windows contient les fonctions Roque Echec*/
public class JeuEchec extends Window{

    public JeuEchec() {
    }

    /** Action lors du clique d'un bouton*/
    @Override
    protected void boutonAction(java.awt.event.ActionEvent e){
        //si on dois récuperer le coup de départ
        PieceBtn pieceBtn = (PieceBtn) e.getSource();
        affichageInfo.setText( pieceBtn.getPiece().toString());

        // vérifie si c'est le coup de départ et que c'est une piece
        if (PieceBtn.etat == EtatCoup.PIECEDEPART) {
            //vérifie qu'on joue la bonne couleur
            if (getTourCouleur() != pieceBtn.getPiece().getCouleur()) {
                traitAffichage();
                return;
            }
            if ( pieceBtn.getPiece().isUnePiece() )
                PieceBtn.pieceDepart = pieceBtn;
            else {
                affichageInfo.setText("Vous devez selectioner une pièce");
                return;
            }
        }
        else {
            //On doit récuperer le coup arriver
            //test pas de la même couleur
            if ( PieceBtn.pieceDepart.getPiece().getCouleur() == pieceBtn.getPiece().getCouleur()) {
                //on test que les cases sélectionnées sont bien Roi et tour qui sont les conditions du roque
                if (!(PieceBtn.pieceDepart.getPiece() instanceof Roi || pieceBtn.getPiece() instanceof Tour) ) {
                    PieceBtn.pieceDepart = pieceBtn;
                    return;
                }
                if (isRoquePossible(PieceBtn.pieceDepart.getPiece().getCouleur(),
                        new Move(PieceBtn.pieceDepart.getCoord(), pieceBtn.getCoord()) )){
                    tourIncrementer();
                    PieceBtn.pieceArriver = pieceBtn;
                    PieceBtn.coupJouer();
                    traitAffichage();
                }
                else {
                    PieceBtn.coupJouer();
                }
                return;

            }
            //si condition sont rempli on recupère la pièce d'arriver
            PieceBtn.pieceArriver = (PieceBtn) e.getSource();
            //si la pièceArriver est dans les coups possible de celle de départ
            if (PieceBtn.pieceDepart.getPiece().coupPossible(window,
                    new Move(PieceBtn.pieceDepart.getCoord(), PieceBtn.pieceArriver.getCoord()))) {
                //si le roi de la pièce de départ est en echec on arrête le coup
                if (isEchec(PieceBtn.pieceDepart.getPiece().getCouleur(),
                        new Move(PieceBtn.pieceDepart.getCoord(), PieceBtn.pieceArriver.getCoord() ) ) ){
                    affichageInfo.setText("Votre coup vous met en échec");
                    return;
                }
                //coup entièrement valide
                //permet de mettre a jouer si c'est un pion pour gérer le déplacement de 2
                if (PieceBtn.pieceDepart.getPiece().getPieceID() == PieceID.PION_NOIR ||
                        PieceBtn.pieceDepart.getPiece().getPieceID() == PieceID.PION_BLANC)  ((Pion) PieceBtn.pieceDepart.getPiece()).setJouer();
                PieceBtn.pieceDepart.setMouvement(PieceBtn.pieceArriver, imageIcon.VIDE_ICON);
                tourIncrementer();
                traitAffichage();
                pionToReine();
            }
            else affichageInfo.setText("Coup impossible");
        }
        //remet a 0 les pièces jouers
        PieceBtn.coupJouer();
    }

    /** récupere la position d'un roi d'une couleur */
    protected Coord getRoiInfo(Couleur couleur){
        //parcours le plateau pour chercher le roi de la couleur
        for (short ligne=0; ligne<DIMENSION_BOARD; ligne++ ){
            for (short  colonne=0; colonne<DIMENSION_BOARD; colonne++){
                if (this.getPieceBtn(ligne, colonne).getPiece() instanceof Roi &&
                        this.getPieceBtn(ligne, colonne).getPiece().getCouleur().equals(couleur) ) {
                    return  new Coord(ligne, colonne);
                }
            }
        }
        //si pas trouver de roi on quitte
        System.out.println("Erreur : il manque un roi");
        return null;
    }

    /** fait un faux deplacement*/
    protected Piece setFakeDeplacement(Move deplacement){
        Piece tampon = getPieceBtn(deplacement.to.ligne, deplacement.to.colonne).getPiece();
        getPieceBtn(deplacement.to.ligne, deplacement.to.colonne).setPieceEgaleA(getPieceBtn(deplacement.from.ligne, deplacement.from.colonne), imageIcon.VIDE_ICON);
        return tampon;
    }
    /** inverse le faux deplacement*/
    protected void setFakeDeplacementReverse(Piece piece, Move deplacement){
        getPieceBtn(deplacement.from.ligne, deplacement.from.colonne).setPieceEgaleA(getPieceBtn(deplacement.to.ligne, deplacement.to.colonne), imageIcon.VIDE_ICON);
        getPieceBtn(deplacement.to.ligne, deplacement.to.colonne).setPiece(piece);
        getPieceBtn(deplacement.to.ligne, deplacement.to.colonne).getPiece().setPieceID(piece.getPieceID());
    }

    /** test le roque est possible */
    private boolean isRoquePossible(Couleur couleurMenacante, Move deplacementRoque){
        int roque;
        // récupère si un grand roque ou un petit si pas un roque renvoie faux
        roque = Move.isPetitGrandRoque(deplacementRoque);
        if (roque == -1) {
            affichageInfo.setText("Vous ne pouvez pas roque");
            return false;
        }

        Roi roi = (Roi) getPieceBtn(deplacementRoque.to.ligne, deplacementRoque.to.colonne).getPiece();
        if (!roi.isJamaisJouer()) {
            affichageInfo.setText("Le roi a déjà été joué");
            return false;
        }
        // -1 pas un roque 0 petit roque 1 grand roque
        short ligne = deplacementRoque.to.ligne;
        if (roque == 1){
            //grand roque
            for (short c= 4; c >= 0; c-- ){
                if ( c != 0 &&  c != 4  &&  getPieceBtn(ligne, c).getPiece().getCouleur() != Couleur.VIDE){
                    affichageInfo.setText("Une pièce bloque le grand roque");
                    return false;
                }
                if ( c> 1){
                    if (isMenacer(new Coord(ligne, c), couleurMenacante)) {
                        affichageInfo.setText("Impossible chemin du Roi menacé");
                        return false;
                    }
                }
            }
            //fait le deplacement du grand roque
            getPieceBtn(deplacementRoque.to.ligne, deplacementRoque.to.colonne).setPieceEgaleA(
                    getPieceBtn(ligne, (short) 2), imageIcon.VIDE_ICON);
            getPieceBtn(deplacementRoque.from.ligne, deplacementRoque.from.colonne).setPieceEgaleA(
                    getPieceBtn(ligne, (short) 3), imageIcon.VIDE_ICON);
        } else {
            //petit roque
            for (short c= 4; c < DIMENSION_BOARD; c++ ){
                if ( (c != 4 &&  c != 7)  &&  getPieceBtn(ligne, c).getPiece().getCouleur() != Couleur.VIDE){
                    affichageInfo.setText("Une pièce bloque le grand roque");
                    return false;
                }
                if ( c != 7 ){
                    if (isMenacer(new Coord(ligne, c), couleurMenacante)) {
                        affichageInfo.setText("Impossible chemin du Roi menacé");
                        return false;
                    }
                }
            }
            //fait le deplacement du petit roque
            getPieceBtn(deplacementRoque.to.ligne, deplacementRoque.to.colonne).setPieceEgaleA(
                    getPieceBtn(ligne, (short) 6), imageIcon.VIDE_ICON);
            getPieceBtn(deplacementRoque.from.ligne, deplacementRoque.from.colonne).setPieceEgaleA(
                    getPieceBtn(ligne, (short) 5), imageIcon.VIDE_ICON);
        }
        roi.setJamaisJouer(false);
        return true;
    }

    /** renvoie si le roi d'une couleur est en echec ou non */
    private boolean isEchecTest(Couleur couleur){
        Coord roi = getRoiInfo(couleur);
        if (roi == null) {
            System.out.println("roi pas trouver");
            return false;
        }
        return isMenacer(roi, Couleur.VIDE);
    }

    /**regarde si la piece est menacer par une des piece adverse */
    protected boolean isMenacer(Coord piece, Couleur couleurMenacante){
        //couleurMenacante = Vide correspond a l'echec normale sinon c'est pour le roque
        PieceBtn pieceTempo;
        Piece pieceMenacer = getPieceBtn(piece.ligne, piece.colonne).getPiece();
        Couleur couleurPieceMenacer;

        if (couleurMenacante == Couleur.VIDE) couleurPieceMenacer = pieceMenacer.getCouleur();
        else couleurPieceMenacer = couleurMenacante;

        for (short ligne=0; ligne<DIMENSION_BOARD; ligne++ ){
            for (short  colonne=0; colonne<DIMENSION_BOARD; colonne++){
                pieceTempo = getPieceBtn(ligne, colonne);
                if ( !pieceTempo.getPiece().getPieceID().equals(PieceID.VIDE)){
                    //si la piece ne peut pas  manger  la piece menacer alors on break
                    //test si la pieceMenacer est dans les coups possibles de piece tempo
                    if (!pieceTempo.getPiece().getCouleur().equals(couleurPieceMenacer)) {
                        if ( pieceTempo.getPiece().coupPossible(this, new Move(pieceTempo.getCoord(), piece ) ) ) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /** renvoie si le roi de la couleur avec un coup est en echec */
    private boolean isEchec(Couleur couleur, Move deplacement){
        boolean echec;
        Piece pieceRetirer = setFakeDeplacement(deplacement);

        //test si le roi echec
        echec = isEchecTest(couleur);
        //defait le coup
        setFakeDeplacementReverse(pieceRetirer, deplacement);
        return echec;
    }

    /** converti les pions en reine*/
    private void pionToReine(){
        for (short j = 0; j<DIMENSION_BOARD; j++){
            if (getPieceBtn((short) 0,j).getPiece().getPieceID() == PieceID.PION_BLANC){
                getPieceBtn((short) 0, j).setPieceBtn(PieceID.REINE_BLANC, imageIcon.REINE_BLANC_ICON);
            }
            if (getPieceBtn((short) 7,j).getPiece().getPieceID() == PieceID.PION_NOIR){
                getPieceBtn((short) 7, j).setPieceBtn(PieceID.REINE_NOIR, imageIcon.REINE_NOIR_ICON);
            }
        }
    }

}
