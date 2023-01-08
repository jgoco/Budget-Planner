package ui;

import model.Account;

import javax.swing.*;
import java.awt.*;

// Represents a display with the total balance of the account and
// a graph bar that visually summarizes the category totals to the user
public class BarGraph extends JPanel {
    static final int MAX_BAR = 500;
    static final Font FONT = new Font("Times New Romans", Font.BOLD, 22);
    static final Color POSITIVE = new Color(37, 114, 47);
    static final Color NEGATIVE = new Color(167, 0, 2);

    Account acc;
    JPanel container;
    JPanel labels;
    JPanel graph;



    // EFFECTS:  creates a display that prints the balance of the account
    //           and draws a bar graph that summarizes the category totals
    public BarGraph(Account acc) {
        this.acc = acc;

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(Box.createRigidArea(new Dimension(0, 20)));

        drawBalance();
        drawGraph();

        this.repaint();
    }

    // MODIFIES: this
    // EFFECTS:  creates the balance display,
    //           when balance is positive, the text is green,
    //           when balance is negative, the text is red,
    //           when balance is zero, the text is black
    void drawBalance() {
        double balance = acc.getBalance();
        String balanceString = "Current balance: $" + String.format("%.2f", balance);
        JLabel balancePanel = new JLabel(balanceString);
        balancePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        balancePanel.setFont(FONT);
        if (balance == 0) {
            balancePanel.setForeground(Color.BLACK);
        } else if (balance < 0) {
            balancePanel.setForeground(NEGATIVE);
        } else {
            balancePanel.setForeground(POSITIVE);
        }
        this.add(balancePanel);
    }

    // MODIFIES: this
    // EFFECTS:  creates the bar graph display of the category totals
    void drawGraph() {
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.LINE_AXIS));
        this.add(container);

        labels = new JPanel();
        labels.setLayout(new BoxLayout(labels, BoxLayout.PAGE_AXIS));
        labels.setAlignmentX(Component.RIGHT_ALIGNMENT);
        labels.setSize(new Dimension(100, 700));
        container.add(labels);

        graph = new JPanel();
        graph.setLayout(new BoxLayout(graph, BoxLayout.PAGE_AXIS));
        graph.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(graph);
        graph.add(Box.createRigidArea(new Dimension(0, 20)));

        createGraph();

    }

    // MODIFIES: this
    // EFFECTS:  creates the bar graph display of the category totals
    void createGraph() {
        if (acc.isLogEmpty()) {
            createNoBars();
        } else {
            int income = doubleToInt(acc.getCategorySum("income"));
            int maxCategory = findMax();
            if (income == 0) {
                createBarsNoIncome(maxCategory);
            } else if (income < maxCategory) {
                createBarsWithLowIncome(maxCategory);
            } else {
                createBarsWithIncome(income);
            }
        }

    }

    // EFFECTS:  turns an double value into an integer
    int doubleToInt(double value) {
        return (int) value;
    }

    // MODIFIES: this
    // EFFECTS:  finds the category with the highest total
    int findMax() {
        int max = 0;

        for (String category : acc.getCategories()) {
            int value = doubleToInt(acc.getCategorySum(category));

            if (value > max) {
                max = value;
            }
        }

        return max;
    }

    // EFFECTS:  returns the appropriate bar width,
    //           calculated using a ratio over the income
    //           when income is the highest category total
    int getBarWidth(String category, int income) {
        int value = doubleToInt(acc.getCategorySum(category));
        int ratioValue = (value * MAX_BAR) / income;
        return ratioValue;
    }

    // EFFECTS:  returns the appropriate bar width,
    //           calculated using a ratio over the highest category total
    //           when income is not the highest category total
    int getBarWidthLowIncome(String category, int maxCategory) {
        int value = doubleToInt(acc.getCategorySum(category));
        int ratioValue = (value * MAX_BAR) / maxCategory;
        return ratioValue;
    }

    // MODIFIES: this
    // EFFECTS:  creates the bar graph display when log is empty
    void createNoBars() {
        for (String category : acc.getCategories()) {
            labels.add(Box.createRigidArea(new Dimension(0, 30)));
            JLabel categoryLabel = new JLabel(category);
            categoryLabel.setFont(FONT);
            categoryLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

            labels.add(categoryLabel);
            labels.add(Box.createRigidArea(new Dimension(0, 30)));

            JPanel categoryBar = new Bar(0, Color.WHITE);
            categoryBar.setAlignmentX(Component.LEFT_ALIGNMENT);
            graph.add(categoryBar);

        }

    }

    // MODIFIES: this
    // EFFECTS:  creates the bar graph display when income is zero
    void createBarsNoIncome(int maxCategory) {
        for (String category : acc.getCategories()) {
            labels.add(Box.createRigidArea(new Dimension(0, 30)));
            JLabel categoryLabel = new JLabel(category);
            categoryLabel.setFont(FONT);
            categoryLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

            labels.add(categoryLabel);
            labels.add(Box.createRigidArea(new Dimension(0, 30)));

            if (category.equals("income"))  {
                JPanel incomeBar = new Bar(0, POSITIVE);
                incomeBar.setAlignmentX(Component.LEFT_ALIGNMENT);
                graph.add(incomeBar);
            } else {
                JPanel categoryBar = new Bar(getBarWidthLowIncome(category, maxCategory), NEGATIVE);
                categoryBar.setAlignmentX(Component.LEFT_ALIGNMENT);
                graph.add(categoryBar);
            }

        }

    }

    // MODIFIES: this
    // EFFECTS:  creates the bar graph display when income is the highest category total
    void createBarsWithIncome(int income) {
        for (String category : acc.getCategories()) {
            labels.add(Box.createRigidArea(new Dimension(0, 30)));
            JLabel categoryLabel = new JLabel(category);
            categoryLabel.setFont(FONT);
            categoryLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

            labels.add(categoryLabel);
            labels.add(Box.createRigidArea(new Dimension(0, 30)));

            if (category.equals("income")) {
                JPanel incomeBar = new Bar(MAX_BAR, POSITIVE);
                incomeBar.setAlignmentX(Component.LEFT_ALIGNMENT);
                graph.add(incomeBar);
            } else {
                JPanel categoryBar = new Bar(getBarWidth(category, income), NEGATIVE);
                categoryBar.setAlignmentX(Component.LEFT_ALIGNMENT);
                graph.add(categoryBar);
            }

        }

    }

    // MODIFIES: this
    // EFFECTS:  creates the bar graph display when income is not the highest category total
    void createBarsWithLowIncome(int maxCategory) {
        for (String category : acc.getCategories()) {
            labels.add(Box.createRigidArea(new Dimension(0, 30)));
            JLabel categoryLabel = new JLabel(category);
            categoryLabel.setFont(FONT);
            categoryLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

            labels.add(categoryLabel);
            labels.add(Box.createRigidArea(new Dimension(0, 30)));

            if (category.equals("income")) {
                JPanel incomeBar = new Bar(getBarWidthLowIncome(category, maxCategory), POSITIVE);
                incomeBar.setAlignmentX(Component.LEFT_ALIGNMENT);
                graph.add(incomeBar);
            } else {
                JPanel categoryBar = new Bar(getBarWidthLowIncome(category, maxCategory), NEGATIVE);
                categoryBar.setAlignmentX(Component.LEFT_ALIGNMENT);
                graph.add(categoryBar);
            }

        }

    }


    // EFFECTS:  creates a BarGraph
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}


