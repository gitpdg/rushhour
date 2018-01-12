package GraphicGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LoadingWindow extends JFrame {
	//Affiche la fen�tre de lancement d'une partie.
	static final long serialVersionUID = 4;
	
	JPanel container = new JPanel();
	Bouton start;
	JComboBox<String> choices = new JComboBox<String>(); //Menu d�roulant pour choisir la partie
	int typeheuristic;
	boolean brutForce;
	
	public LoadingWindow(int type, boolean brutForce) throws IOException{
		
		super("Start new game");
		setSize(250,110);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		container.setBackground(Color.white);
		container.setLayout(new BorderLayout());
		
		JPanel pan = new JPanel();
		pan.setLayout(new FlowLayout());
		choices.setPreferredSize(new Dimension(100, 23));
		File folder = new File("Games/");
		String[] list = folder.list(new FileNameFilter()); //R�cup�re tous les fichiers du dossier "Games" dont le nom correspond aux crit�res de FileNameFilter.
		choices.addItem("...");
		for (int i = 0; i < list.length; i++){
			int l = list[i].length();
			choices.addItem(list[i].substring(0, l-4));
		}
		choices.addItemListener(new ItemState());
		pan.add(choices);
		
		start = new Bouton("Start");
		JPanel b = new JPanel();
		start.setEnabled(false);
		b.add(start);
		pan.add(b);
		container.add(pan, BorderLayout.SOUTH);
		
		JLabel label = new JLabel("Choose your game !");
		label.setForeground(Color.BLUE);
		Font police = new Font("Tahoma", Font.BOLD, 16);
		label.setFont(police);
		label.setHorizontalAlignment(JLabel.CENTER);
		container.add(label, BorderLayout.NORTH);
		
		setContentPane(container);
		
		start.addActionListener(new StartButtonListener());
		
		setVisible(true);
		this.typeheuristic = type;
		this.brutForce = brutForce;
	}
	
	public class ItemState implements ItemListener{
		public void itemStateChanged(ItemEvent e) {
			if (e.getItem() != "...") { //Quand on a choisit une partie diff�rentes de "..." on peut appuyer sur start.
				start.setEnabled(true);
			}
			else {
				start.setEnabled(false);
			}
		}
	}
	
	public class StartButtonListener implements ActionListener {
		//Impl�mentation de l'action r�alis�e par le bouton "start" : On ferme la fen�tre actuelle et on lance la fen�tre de la partie s�lectionn�e dans le menu d�roulant.
		public void actionPerformed(ActionEvent arg0) {
			String str = (String) choices.getSelectedItem();
			System.out.println("");
			System.out.println(str);
			String file = str + ".txt";
			try {
				GameWindow gui = new GameWindow(file, str, typeheuristic, brutForce);
				gui.setVisible(true);
				setVisible(false);
			}
			catch (IOException e){
				e.printStackTrace();
			}
		}
	}
	
}
