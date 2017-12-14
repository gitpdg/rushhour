package GraphicGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class LoadingWindow extends JFrame {
	JFrame window;
	JPanel container = new JPanel();
	Bouton start;
	JComboBox<String> choices = new JComboBox<String>();
	
	public LoadingWindow() throws IOException{
		super("Start new game");
		setSize(200,100);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		container.setBackground(Color.white);
		container.setLayout(new FlowLayout());
		
		choices.setPreferredSize(new Dimension(100, 23));
		choices.addItem("...");
		choices.addItem("Game01");
		choices.addItemListener(new ItemState());
		container.add(choices);
		
		start = new Bouton("Start");
		JPanel b = new JPanel();
		start.setEnabled(false);
		b.add(start);
		container.add(b);
		
		this.setContentPane(container);
		
		start.addActionListener(new StartButtonListener());
		
		setVisible(true);
	}
	
	public class ItemState implements ItemListener{
		public void itemStateChanged(ItemEvent e) {
			if (e.getItem() != "...") {
				start.setEnabled(true);
			}
		}
	}
	
	public class StartButtonListener implements ActionListener {
		
		public void actionPerformed(ActionEvent arg0) {
			String str = (String) choices.getSelectedItem();
			String file = str + ".txt";
			try {
				GameWindow gui = new GameWindow(file, str);
				gui.setVisible(true);
			}
			catch (IOException e){
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws IOException{
		LoadingWindow gui = new LoadingWindow();
		gui.setVisible(true);
	}
}
