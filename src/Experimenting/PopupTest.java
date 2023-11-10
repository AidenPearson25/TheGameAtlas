package Experimenting;

import java.awt.CardLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;

public class PopupTest extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
      public void run() {
          try {
              PopupTest frame = new PopupTest();
              frame.setVisible(true);
              frame.setTitle("The Game Atlas");
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
  });
	}
	
	public PopupTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 1051, 531);
    JPanel contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPane.setLayout(new CardLayout(0, 0));
    setContentPane(contentPane);
    
    JPopupMenu popupMenu = new JPopupMenu();
    
    JMenuItem menuItemCreateSpringProject = new JMenuItem("Spring Project");
    popupMenu.add(menuItemCreateSpringProject);
     
    JMenuItem menuItemCreateHibernateProject = new JMenuItem("Hibernate Project");
    popupMenu.add(menuItemCreateHibernateProject);
     
    JMenuItem menuItemCreateStrutsProject = new JMenuItem("Struts Project");
    popupMenu.add(menuItemCreateStrutsProject);
    
    JPanel test = new JPanel();
    contentPane.add(test);
    test.add(popupMenu);
	}
}
