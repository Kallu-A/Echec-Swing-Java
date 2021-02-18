package Interface;

import Interface.Image.PieceID;
import Interface.Image.PieceImageIcon;
import Move.Coord;
import Piece.Couleur;

import javax.swing.*;

import javax.swing.plaf.nimbus.NimbusLookAndFeel;


import java.awt.*;

/** interface graphique et jeu d'échec
 * @author kallu */
public class Window extends JFrame {

    public static int DIMENSION_BOARD = 8;

    protected final JPanel contentPane;
    private JPanel boardPanel;
    private PieceBtn[][] boardBtn;
    protected final PieceImageIcon imageIcon;
    protected JLabel affichageInfo;
    //permet de stocker la fenetre pour coupPossible
    protected final Window window;

    private int tour = 0;

    public Window() {
        //init windows
        super("Kallu chess");
        imageIcon = new PieceImageIcon();
        window = this;
        this.setSize(900, 900 / 12 * 9);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.contentPane = (JPanel) this.getContentPane();
        contentPane.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        setInitPieceBtn();
        this.setUI();
        createToolBar();

        boardPanel.setBackground(new Color(102, 110,105 ));
        contentPane.setBackground(new Color(90, 90, 90));

        this.setVisible(true);
    }

    /** initialise toutes les piecesBtn (appeler qu'une fois)*/
    private void setInitPieceBtn(){
        this.boardPanel = new JPanel(new GridLayout(DIMENSION_BOARD, DIMENSION_BOARD));
        boardBtn = new PieceBtn[DIMENSION_BOARD][DIMENSION_BOARD];
        for (int i = 0; i< DIMENSION_BOARD; i++){
            for (int j = 0; j< DIMENSION_BOARD; j++){
                boardBtn[i][j] = new PieceBtn(new Coord( (short) i, (short) j));

                //action en cas de clique
                boardBtn[i][j].addActionListener(this::boutonAction);

                boardPanel.add(boardBtn[i][j]);
                if ( (i+j) %2 == 1) boardBtn[i][j].setBackground(new Color(102, 110,105 ));
                else boardBtn[i][j].setBackground(new Color(186, 164, 143));
            }
        }
        setPieceBtn();
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        contentPane.add(boardPanel, BorderLayout.CENTER);
    }

    /** Action lors du clique d'un bouton sert a l'override*/
    protected void boutonAction(java.awt.event.ActionEvent e){ }

    /** remet a 0 les piècesbtn */
    protected void setPieceBtn(){
        //le vide
        for (int i = 2; i < DIMENSION_BOARD-2; i++){
            for (int j = 0; j < DIMENSION_BOARD; j++){
                boardBtn[i][j].setPieceBtn(PieceID.VIDE, imageIcon.VIDE_ICON);
            }
        }
        //les pions
        for (int j = 0; j < DIMENSION_BOARD; j++){
            boardBtn[1][j].setPieceBtn(PieceID.PION_NOIR, imageIcon.PION_NOIR_ICON);
            boardBtn[6][j].setPieceBtn(PieceID.PION_BLANC, imageIcon.PION_BLANC_ICON);
        }

        //les pièces spéciales
        // blanc
        boardBtn[0][0].setPieceBtn(PieceID.TOUR_NOIR, imageIcon.TOUR_NOIR_ICON);
        boardBtn[0][7].setPieceBtn(PieceID.TOUR_NOIR, imageIcon.TOUR_NOIR_ICON);
        boardBtn[0][1].setPieceBtn(PieceID.CAVALIER_NOIR, imageIcon.CAVALIER_NOIR_ICON);
        boardBtn[0][6].setPieceBtn(PieceID.CAVALIER_NOIR, imageIcon.CAVALIER_NOIR_ICON);
        boardBtn[0][5].setPieceBtn(PieceID.FOU_NOIR, imageIcon.FOU_NOIR_ICON);
        boardBtn[0][2].setPieceBtn(PieceID.FOU_NOIR, imageIcon.FOU_NOIR_ICON);
        boardBtn[0][3].setPieceBtn(PieceID.REINE_NOIR, imageIcon.REINE_NOIR_ICON);
        boardBtn[0][4].setPieceBtn(PieceID.ROI_NOIR, imageIcon.ROI_NOIR_ICON);

        //noir
        boardBtn[7][0].setPieceBtn(PieceID.TOUR_BLANC, imageIcon.TOUR_BLANC_ICON);
        boardBtn[7][7].setPieceBtn(PieceID.TOUR_BLANC, imageIcon.TOUR_BLANC_ICON);
        boardBtn[7][1].setPieceBtn(PieceID.CAVALIER_BLANC, imageIcon.CAVALIER_BLANC_ICON);
        boardBtn[7][6].setPieceBtn(PieceID.CAVALIER_BLANC, imageIcon.CAVALIER_BLANC_ICON);
        boardBtn[7][5].setPieceBtn(PieceID.FOU_BLANC, imageIcon.FOU_BLANC_ICON);
        boardBtn[7][2].setPieceBtn(PieceID.FOU_BLANC, imageIcon.FOU_BLANC_ICON);
        boardBtn[7][3].setPieceBtn(PieceID.REINE_BLANC, imageIcon.REINE_BLANC_ICON);
        boardBtn[7][4].setPieceBtn(PieceID.ROI_BLANC, imageIcon.ROI_BLANC_ICON);
    }

    /** créer la toolBar*/
    private void createToolBar(){
        JToolBar toolBar = new JToolBar();
        Font f = new Font("Fira sans", Font.PLAIN, 23);
        toolBar.setLayout( new FlowLayout());
        affichageInfo = new JLabel("Au joueur blanc de commencer");
        JButton relancerPartie = new JButton("<html> <body> <font color='9999FF'>" +
                "Relancer une partie" +
                "</body> </html>");

        affichageInfo.setFont(f);
        relancerPartie.setFont(f);
        //boutton relancer la partie
        relancerPartie.addActionListener(e -> {
            int input = JOptionPane.showConfirmDialog(window, "Êtes-vous sur ?");
            if (input == 0 ) {
                setPieceBtn();
                tour = 0;
                traitAffichage();
            }
        });
        toolBar.setMargin(new Insets(0, 0, 20, 0));
        toolBar.setFloatable(false);

        toolBar.setPreferredSize(new Dimension(0,60));
        toolBar.add(relancerPartie);
        toolBar.addSeparator();
        toolBar.add(affichageInfo);

        contentPane.add(toolBar, BorderLayout.NORTH);
    }

    /** définie un look and feel */
    private void setUI() {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException error) {
                JOptionPane.showMessageDialog(contentPane, "Aucun look n'est applicable", "Erreur", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /** vérifie si dans le plateau */
    public static boolean isDansPlateau(short ligne, short colonne){
        return (ligne >= 0 && ligne < DIMENSION_BOARD) && (colonne >= 0 && colonne < DIMENSION_BOARD);
    }

    /** renvoie la pieceBtn dans les coordonées*/
    public PieceBtn getPieceBtn(short ligne, short colonne) {
        return boardBtn[ligne][colonne];
    }

    /** renvoie si la piece en argument est vide ou non  */
    public static boolean isPlaceLibre(PieceBtn pieceViser){
        return ( pieceViser.getPiece().getCouleur() == Couleur.VIDE);
    }

    /** affiche le trait*/
    protected void traitAffichage(){
        if (getTourCouleur() == Couleur.BLANC ) affichageInfo.setText("trait au blanc");
        else affichageInfo.setText("trait au noir");
    }

    protected Couleur getTourCouleur(){
        if (tour%2 == 0) return Couleur.BLANC;
        else return Couleur.NOIR;
    }

    /** incremente le tour*/
    protected void tourIncrementer(){
        this.tour++;
    }



}
