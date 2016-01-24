/**
 * Class to run calculator 
 * @author Jacob George
 * @version 0.2
 * 
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class Calculator extends JFrame implements ActionListener
{
	private JButton[] pads;
	private JPanel buttons;
	private JMenuBar menu;
	private JMenu help;
	private JTextField output;
	private boolean evaluated;
	
	/**
	 * Constructor for Calculator JFrame GUI
	 */
	public Calculator(){
		//Make a new JFrame object
		super("Calculator");
		setSize(260,400);
		//this.setResizable(false);
		this.getContentPane().setLayout(null);
		//output area
		output= new JTextField();
		output.setBounds(10,10,240,20);
		
		//buttons area
		output.setAlignmentX(Component.RIGHT_ALIGNMENT);
		buttons = new JPanel(null);
		buttons.setBounds(0,40,250,320);
		
		//menu area
		menu = new JMenuBar();
		help = new JMenu("Help");
		help.addActionListener(this);
		menu.add(help);
		
		
		//add buttons output and menu
		this.getContentPane().add(buttons);
		this.getContentPane().add(output);
		this.setJMenuBar(menu);
		pads = new JButton[18];
		int xPos = 10;
		int yPos = 10;
		int[] nums = {0,0,0,0,1,2,3,3,4,5,6,6,7,8,9};
		//Create buttons for 0-9 and +, -, /, * and space
		for(int k=0; k<18; k++){
			pads[k] = new JButton();
			pads[k].addActionListener(this);
			if(k != 16 && k!= 17){
				pads[k].setBounds(xPos, yPos, 50, 50);
			}
			else{
				pads[k].setBounds(xPos, yPos, 110, 50);
			}
			switch(k){
				case 1: pads[k].setText("(");
						pads[k].setBackground(Color.black);
						pads[k].setForeground(Color.white);
						break;
				case 2: pads[k].setText(")");
						pads[k].setBackground(Color.black);
						pads[k].setForeground(Color.white);
						break;
				case 3: pads[k].setText("+");
						pads[k].setBackground(Color.black);
						pads[k].setForeground(Color.white);
						break;
				case 7: pads[k].setText("-");
						pads[k].setBackground(Color.black);
						pads[k].setForeground(Color.white);
						break;
				case 11: pads[k].setText("*");
						pads[k].setBackground(Color.black);
						pads[k].setForeground(Color.white);
						break;
				case 15: pads[k].setText("/");
						pads[k].setBackground(Color.black);
						pads[k].setForeground(Color.white);
						break;
				case 16: pads[k].setText("_");
						pads[k].setBackground(Color.black);
						pads[k].setForeground(Color.white);
						break;
				case 17: pads[k].setText("=");
						pads[k].setBackground(Color.black);
						pads[k].setForeground(Color.blue);
						break;
				default: pads[k].setText(nums[k]+"");
						break;
			}
			
			pads[k].setFont(new Font("Arial",Font.PLAIN,24));
			buttons.add(pads[k]);
			if(k != 16){
				xPos += 60;
			}
			else{
				xPos += 120;
			}
			
			if(k % 4 == 3){
				xPos = 10;
				yPos += 60;
			}
			
		}
		
		
	}

    /**
     * Action listener for button press
     */
	public void actionPerformed(ActionEvent e){
        if(((JButton)e.getSource()).getText().equals("=")){
			evaluated=true;
			String answer;
			try{
				String infix = output.getText();
				String postfix = ExpressionTools.infixToPostfix(infix);
				int result = ExpressionTools.evalPostfix(postfix);
				answer = result + "";
			}
			catch(PostFixException e1){
				answer = e1.getMessage();
			}
			output.setText(answer);
		}
		else{
			if(evaluated){
				output.setText("");
			}
			evaluated=false;
			if(((JButton)e.getSource()).getText().equals("_")){
				output.setText(output.getText()+" ");
			}
			else{
				output.setText(output.getText()+((JButton)e.getSource()).getText());
			}
		}
		
	}
	
}


		
