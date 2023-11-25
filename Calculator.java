// Import necessary libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class Calculator {
    // Declare variables for the frame, display, operator, first number, operation status, and last entry
    private JFrame frame;
    private JTextField display;
    private String operator;
    private double first = 0;
    private boolean performingOperation = false;
    private String lastEntry = "";

    public Calculator() {
        // Create a new JFrame and set its properties
        frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);

        // Get the content pane of the frame and set its layout
        Container panel = frame.getContentPane();
        panel.setLayout(new BorderLayout());

        // Create a JTextField for the display and add it to the panel
        display = new JTextField();
        panel.add(display, BorderLayout.NORTH);

        // Create a JPanel for the buttons and set its layout
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(5, 4));
        panel.add(buttons, BorderLayout.CENTER);

        // Add buttons for the digits 0-9
        for (int i = 9; i >= 0; i--) {
            addButton(buttons, String.valueOf(i));
        }

        // Add buttons for the operations
        addButton(buttons, "+");
        addButton(buttons, "-");
        addButton(buttons, "*");
        addButton(buttons, "/");
        addButton(buttons, "=");
        addButton(buttons, "C");
        addButton(buttons, "AC");
        // Add '+/-' button for changing sign
        addButton(buttons, "+/-");
        addButton(buttons, ".");

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Calculator();
    }

    private void addButton(Container parent, String name) {
        // Create a new JButton and set its properties
        JButton button = new JButton(name);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the source of the action event
                JButton b = (JButton)e.getSource();
                
                // Check for digit buttons and limit input length to 8 digits
                if ("0123456789".contains(b.getText())) {
                    if (display.getText().length() < 8) {
                        // Append the digit to the display
                        if (performingOperation) {
                            display.setText("");
                            performingOperation = false;
                        }
                        display.setText(display.getText() + b.getText());
                    }
                    return; // Stop further processing for digit buttons
                }

                buttonEvaluation(b);

                // Store the last entry
                lastEntry = b.getText();
            }
        });
        parent.add(button);
    }

    private void buttonEvaluation(JButton b){

        if(b.getText().equals("C")) {
            // If the last entry was an operation, update the display to the value that preceded it
            // If the last entry was a number, clear the display
            if (lastEntry.equals("+") || lastEntry.equals("-") || lastEntry.equals("*") || lastEntry.equals("/")) {
                display.setText(String.valueOf(first));
                operator = "";
            } else {
                display.setText("");
            }
        } else if(b.getText().equals("AC")) {
            // Clear all internal work areas and set the display to 0
            display.setText("0");
            first = 0;
            operator = "";
            performingOperation = false;
        } else if(b.getText().equals("=")) {
            // Perform the operation if the operator is not null and the display is not empty
            // If either is true, display "ERROR" in the text field
            if(operator != null && !display.getText().isEmpty()) {
                double result = 0;
                try {
                    result = switch (operator) {
                        case "+" -> first + Double.parseDouble(display.getText());
                        case "-" -> first - Double.parseDouble(display.getText());
                        case "*" -> first * Double.parseDouble(display.getText());
                        case "/" -> first / Double.parseDouble(display.getText());
                        default -> result;
                    };

                    // Format the result to 3 decimal places and removing trailing zeros
                    String resultStr = getResultStr(result);

                    // Display "ERR" if the result exceeds 8 digits
                    if (resultStr.length() > 8) {
                        display.setText("ERR");
                    } else {
                        display.setText(resultStr);
                    }
                } catch (ArithmeticException ex) {
                    display.setText("ERR");
                }
            } else {
                display.setText("ERROR");
            }
            first = 0;
            operator = "";
        } else if(b.getText().equals("+") || b.getText().equals("-") || b.getText().equals("*") || b.getText().equals("/")) {
            // Store the first number and the operation if the display is not empty
            if(!display.getText().isEmpty()) {
                first = Double.parseDouble(display.getText());
                operator = b.getText();
                performingOperation = true;
            }
        } else if(b.getText().equals("+/-")){
            // Get the current value displayed in the JTextField
            double currentValue = Double.parseDouble(display.getText());
            // Multiply the current value by -1 to change its sign
            double newValue = currentValue * -1;
            // Set the JTextField text to the new value
            display.setText(String.valueOf(newValue));
        } else if(b.getText().equals(".")){
            if (!display.getText().contains(".")) {
                display.setText(display.getText() + ".");
            }
        } else {
            // Append the digit to the display
            if (performingOperation) {
                display.setText("");
                performingOperation = false;
            }
            display.setText(display.getText() + b.getText());
        }

    }

    private static String getResultStr(double result) {
        String resultStr;
        if (result == Math.floor(result)) {
            // If the result is a whole number, display it as an integer
            resultStr = String.format("%.0f", result);
        } else {
            // If the result is not a whole number, display it with up to 3 decimal places and remove trailing zeros
            DecimalFormat df = new DecimalFormat("#.###");
            resultStr = df.format(result);
        }
        return resultStr;
    }
}

