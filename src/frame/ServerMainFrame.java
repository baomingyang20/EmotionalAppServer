package frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ServerMainFrame extends JFrame implements ActionListener {

	private DefaultTableModel dtm;
	private JScrollPane jsp;
	private JTable jt;
	private JButton data;
	private static ServerMainFrame frame = new ServerMainFrame();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerMainFrame frame = new ServerMainFrame();
		// frame.init();
	}

	private ServerMainFrame() {
		init();
	}

	public static ServerMainFrame getFrame() {
		return frame;
	}

	public void init() {
		setSize(1000, 800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		JPanel north = new JPanel();
		north.setPreferredSize(new Dimension(this.getWidth(), this.getHeight() - 200));
		north.setBackground(Color.BLACK);
		north.setLayout(new BorderLayout());
		add(north, BorderLayout.NORTH);

		dtm = new DefaultTableModel(null, new String[] { "client", "operation", "date"});
		jt = new JTable(dtm);
		jsp = new JScrollPane(jt);
		north.add(jsp, BorderLayout.CENTER);

		JPanel south = new JPanel();
		add(south, BorderLayout.SOUTH);

		data = new JButton("查看数据");
		data.setPreferredSize(new Dimension(100, 50));
		south.add(data);
		data.addActionListener(this);

		setVisible(true);

	}

	public void addRow(String s[]) {
		dtm.addRow(s);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
