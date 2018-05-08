package eg.edu.alexu.csd.oop.draw.cs20.view;

import java.awt.Color;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import eg.edu.alexu.csd.oop.draw.cs20.control.Grid;
import eg.edu.alexu.csd.oop.draw.cs20.model.MyDrawingEngine;

public class PaintInterface extends JFrame {
	MyDrawingEngine engine = new MyDrawingEngine();
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Grid grid;
	private JMenuBar menu;
	private JButton button[] = new JButton[13];
	private JButton colorBtn[] = new JButton[2];
	private JMenu fileMenu, editMenu;
	private JMenuItem menu1[] = new JMenuItem[5];
	private JMenuItem menu2[] = new JMenuItem[2];
	private JPopupMenu popup;
	public static boolean loaded = false;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {

				try {
					PaintInterface frame = new PaintInterface();
					frame.setTitle("Paint");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public PaintInterface() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1080, 850);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
		contentPane.setLayout(null);
		contentPane.setFocusable(true);
		contentPane.requestFocusInWindow();
		contentPane.setBackground(Color.getHSBColor(221, 204, 211));
		setContentPane(contentPane);
		MenuBar();
		ToolBar();
		buttonListener();
		KeyListener();
		editListener();
		fileListener();
		ColorButtons();
		intializeGrid();
		editColorButton();
		colorBtnListener();
	}
//this function to set the view of the menu bar
	private void MenuBar() {
		String Menu1[] = { "New", "Save", "Load", "Load Plugin", "Exit" };
		String Menu2[] = { "Undo", "Redo" };
		menu = new JMenuBar();
		menu.setBounds(0, 0, 1080, 30);
		menu.setBackground(Color.getHSBColor(24, 120, 75));
		fileMenu = new JMenu("File");
		for (int i = 0; i < 5; i++) {
			menu1[i] = new JMenuItem(Menu1[i]);
			menu1[i].setFocusable(false);
			fileMenu.add(menu1[i]);
		}
		menu.add(fileMenu);
		editMenu = new JMenu("Edit");
		for (int i = 0; i < 2; i++) {
			menu2[i] = new JMenuItem(Menu2[i]);
			menu2[i].setFocusable(false);
			editMenu.add(menu2[i]);
		}
		menu.add(editMenu);
		contentPane.add(menu);
	}
//this function to set the view of button(tool bar)
	private void ToolBar() {
		String shpnames[] = { "line", "ellipse", "circle", "rectangle", "square", "triangle" };
		String butnames[] = { "colorChooser", "delete", "Plug in", "move", "resize", "select", "copy" };
		for (int i = 0; i < button.length; i++) {
			ImageIcon image = null;
			if (i < 6) {
				try {
					image = new ImageIcon(ImageIO.read(resources.resourceLoader("images/" + shpnames[i] + "Icon.png")));
				} catch (IOException e) {
					e.printStackTrace();
				}
				button[i] = new JButton(image);
				button[i].setToolTipText(shpnames[i]);
			} else {
				if (butnames[i - 6] == "Plug in") {
					button[i] = new JButton(butnames[i - 6]);
				} else {
					try {
						image = new ImageIcon(
								ImageIO.read(resources.resourceLoader("images/" + butnames[i - 6] + "Icon.png")));
					} catch (IOException e) {
						e.printStackTrace();
					}
					button[i] = new JButton(image);
				}
				button[i].setToolTipText(butnames[i - 6]);
			}
			button[i].setBounds(0, 30 + (i * 55), 80, 55);
			button[i].setBackground(Color.getHSBColor(24, 120, 75));
			button[i].setFocusable(false);
			contentPane.add(button[i]);
		}
	}
//make the grid(jpanel) to draw in it
//make var grid from Grid class	
	private void intializeGrid() {
		grid = new Grid();
		grid.setBounds(100, 60, 850, 700);
		grid.setBackground(Color.WHITE);
		grid.setLayout(null);
		grid.setFillColor(Color.WHITE);
		grid.setOutColor(Color.BLACK);
		contentPane.add(grid);
		grid.repaint();
	}
//to set the view of the two buttons for colors
	private void ColorButtons() {
		ImageIcon image = new ImageIcon();
		try {
			image = new ImageIcon(ImageIO.read(resources.resourceLoader("images/bruchIcon.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		colorBtn[0] = new JButton(image);
		colorBtn[0].setBounds(960, 350, 100, 40);
		colorBtn[0].setToolTipText("Outline Color");
		colorBtn[0].setBackground(Color.getHSBColor(24, 120, 75));
		colorBtn[0].setFocusable(false);
		contentPane.add(colorBtn[0]);
		try {
			image = new ImageIcon(ImageIO.read(resources.resourceLoader("images/fillIcon.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		colorBtn[1] = new JButton(image);
		colorBtn[1].setBounds(960, 440, 100, 40);
		colorBtn[1].setToolTipText("Fill Color");
		colorBtn[1].setBackground(Color.getHSBColor(24, 120, 75));
		colorBtn[1].setFocusable(false);
		contentPane.add(colorBtn[1]);
	}
	// this to change color while drawing and should first select the shape and if it isn't selected we will 
   //	warn him and it also have the actions for this button

	private void editColorButton() {
		popup = new JPopupMenu();
		JMenuItem outlineColor = new JMenuItem("Edit Outline Color");
		popup.add(outlineColor);
		JMenuItem FColor = new JMenuItem("Edit Fill Color");
		popup.add(FColor);
		button[6].addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				popup.show(button[6], button[6].getWidth(), button[6].getHeight() / 80);
			}
		});
		outlineColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Color c = JColorChooser.showDialog(((Component) e.getSource()).getParent(),
							"Choose the new Outline Color", Color.black);
					grid.changoutcolor(c);
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "Please Select a shape");
				}
			}
		});
		FColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Color c = JColorChooser.showDialog(((Component) e.getSource()).getParent(),
							"Choose the new Fill Color", Color.white);
					grid.changfillcolor(c);
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "Please Select a shape");
				}
			}
		});
	}
//add actions to two color buttons 
	private void colorBtnListener() {
		colorBtn[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color c;
				c = JColorChooser.showDialog(((Component) e.getSource()).getParent(), "Outline Color", Color.black);
				grid.setOutColor(c);
			}
		});
		colorBtn[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color c;
				c = JColorChooser.showDialog(((Component) e.getSource()).getParent(), "Fill Color", Color.white);
				grid.setFillColor(c);
			}
		});

	}
//add actions to buttons in tool bar
	private void buttonListener() {
		for (int i = 0; i < 6; i++) {
			final int j = i + 1;
			button[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					grid.setDrawMode(j);
				}
			});
		}
		button[8].setVisible(false);
		button[7].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteShape();
			}
		});
		button[8].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				grid.DrawPlugIn();
			}
		});
		button[9].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				grid.actionNum = 1;

			}
		});

		button[10].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				grid.actionNum = 2;

			}
		});

		button[11].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				grid.setManysel();
			}
		});
		button[12].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					grid.copyShape();
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "Please Select a shape");
				}
			}
		});
	}
//add actions to edit menu
	private void editListener() {
		menu2[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				grid.undo();
			}
		});
		menu2[1].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				grid.redo();
			}
		});

	}
//add actions to file menu
	private void fileListener() {
		menu1[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				contentPane.remove(grid);
				intializeGrid();
			}
		});
		menu1[1].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				grid.SaveFiles();
			}
		});
		menu1[2].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				grid.LoadFiles();
			}
		});
		menu1[3].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				grid.LoadPlugIn();
				button[8].setVisible(true);
			}
		});
		menu1[4].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(null, "Do you want to save?", "", JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION)
					grid.SaveFiles();
				System.exit(0);
			}
		});
	}
//shortcuts
	private void KeyListener() {
		menu1[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		menu1[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		menu1[2].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
		menu1[3].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		menu1[4].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));

		menu2[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
		menu2[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK));

	}
//add exceptions to delete function if he didin't select the shape
	private void deleteShape() {
		try {
			grid.deleteShape();
		} catch (Exception e2) {
			JOptionPane.showMessageDialog(null, "Please Select a shape");
		}
	}

}
