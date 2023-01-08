package ui;

import model.Account;
import model.Record;

import javax.swing.*;
import java.awt.*;

// Represents a new window that displays
// a summary of the balance and category totals and
// a complete log to the user
public class DisplayLog extends JFrame {
    static final Color TEXTBOX_COLOR = new Color(228, 218, 172);

    JFrame logWindow;
    Account acc;

    // EFFECTS:  creates a new window that displays
    //           a summary of the balance and category totals,
    //           as well as the complete log of the account
    public DisplayLog(Account acc) {
        logWindow = new JFrame("Log");
        logWindow.setSize(1000, 700);

        Toolkit tk = Toolkit.getDefaultToolkit();
        Image image = tk.getImage(BudgetPlannerApp.ICON);
        logWindow.setIconImage(image);

        this.acc = acc;

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        logWindow.add(container);

        container.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel summary = displaySummary();
        container.add(summary);
        JPanel log = displayLog();
        container.add(log);

        logWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        logWindow.setVisible(true);
    }

    // EFFECTS:  creates a text area to display the balance and category totals
    JPanel displaySummary() {

        JPanel summaryDisplay = new JPanel();

        JTextArea summary = new JTextArea(8, 30);
        summary.setEditable(false);
        summary.setBackground(TEXTBOX_COLOR);
        summaryDisplay.add(summary);

        summary.append("\nTotal account balance is: $" + String.format("%.2f", acc.getBalance()) + "\n\n");
        summary.append("Category summary:\n");

        if (acc.isLogEmpty()) {
            for (String category : acc.getCategories()) {
                summary.append(category + ": $0.00\n");
            }
        } else {
            for (String category : acc.getCategories()) {
                double value = acc.getCategorySum(category);
                summary.append(category + ": $" + String.format("%.2f", value) + "\n");
            }
        }
        return summaryDisplay;
    }

    // EFFECTS:  creates a text area to display the log
    JPanel displayLog() {
        JPanel logDisplay = new JPanel();

        JTextArea log = new JTextArea(8, 30);
        log.setEditable(false);
        log.setBackground(TEXTBOX_COLOR);

        JScrollPane scroll = new JScrollPane(log);
        setUpScroll(scroll, logDisplay);

        int num = 0;
        if (acc.getLog().isEmpty()) {
            log.append("The log is empty.");
        } else {
            log.append("Current recorded purchases:\n");
            for (Record r : acc.getLog()) {
                double val = r.getValue();
                log.append("[" + num + "] ");
                log.append(r.getTitle() + " (" + r.getCategory() + ") = $" + String.format("%.2f", val) + "\n");
                num = num + 1;
            }
            log.append("End of log.");
        }

        return logDisplay;
    }

    // MODIFIES: logDisplay
    // EFFECTS:  adds a scroll pane to the right side of the log
    void setUpScroll(JScrollPane scroll, JPanel logDisplay) {
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBackground(Color.lightGray);
        scroll.setPreferredSize(new Dimension(900, 400));
        scroll.setWheelScrollingEnabled(true);
        logDisplay.add(scroll);
    }


}
