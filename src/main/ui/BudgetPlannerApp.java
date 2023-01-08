package ui;

import model.Account;
import model.CategoryInfo;
import model.Record;
import persistence.Reader;
import persistence.Writer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

// NOTICE:
// 1) Application's ICON attributed to Freepik (https://www.flaticon.com/authors/freepik), and taken from
//    https://www.flaticon.com/free-icon/piggy-bank_748067?term=piggy%20bank&page=1&position=43
// 2) Application's SOUND attributed to Mike Koenig and taken from
//    http://soundbible.com/419-Tiny-Button-Push.html

// Budget Planner application with a graphical user interface
public class BudgetPlannerApp {
    private static final String FILE = "./data/account.txt";
    private static final String SOUND = "./data/addButtonClick.wav";
    static final String ICON = "./data/piggy-bank.png";

    JFrame frame;
    JPanel mainMenu;
    BarGraph barGraph;
    JPanel recordMenu;
    JPanel addRecord;
    JPanel editRecord;
    JPanel removeRecord;

    Account acc;

    // EFFECTS:  run Budget Planner application
    public BudgetPlannerApp() {
        frame = new JFrame("Budget Planner");
        frame.setSize(1000, 800);

        Toolkit tk = Toolkit.getDefaultToolkit();
        Image image = tk.getImage(ICON);
        frame.setIconImage(image);

        initializeAccount();
        displayMainMenu();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);      // sets window to middle of screen
        frame.setVisible(true);
    }

    // MODIFIES: this, account
    // EFFECTS:  loads the account from FILE, if the file exists;
    //           otherwise, if previous account doesn't exist,
    //           initializes a new empty account
    private void initializeAccount() {
        try {
            acc = Reader.readAccount(new File(FILE));
        } catch (IOException e) {
            acc = new Account();
        }
    }

    // --------------------------------------------------------

    // Main menu methods

    // MODIFIES: this
    // EFFECTS:  displays the main menu to the user;
    //           includes an options menu and display panel
    void displayMainMenu() {
        setUpMainMenu();

        //---------------------------OPTIONS MENU---------------------------//

        JPanel options = new JPanel();
        mainMenu.add(options);

        JButton recordsButton = createRecordMenuButton();
        options.add(recordsButton);

        JButton logButton = createLogButton();
        options.add(logButton);

        JButton saveButton = createSaveButton();
        options.add(saveButton);

        //---------------------------DISPLAY PANEL---------------------------//

        mainMenu.add(Box.createRigidArea(new Dimension(0, 5)));

        barGraph = new BarGraph(acc);
        mainMenu.add(barGraph);
    }

    // MODIFIES: this
    // EFFECTS:  sets up the framework for the main menu
    void setUpMainMenu() {
        mainMenu = new JPanel();
        mainMenu.setLayout(new BoxLayout(mainMenu, BoxLayout.PAGE_AXIS));
        frame.add(mainMenu);
    }

    // MODIFIES: this
    // EFFECTS:  creates a record menu button;
    //           when pressed, displays the record menu
    JButton createRecordMenuButton() {
        JButton recordsButton = new JButton("Add, Edit, or Remove Records");
        recordsButton.addActionListener(e -> {
            mainMenu.setVisible(false);
            displayRecordMenu();
        }
        );
        return recordsButton;
    }

    // MODIFIES: acc
    // EFFECTS:  creates a log button;
    //           when pressed, displays the log and summary in a new window
    JButton createLogButton() {
        JButton logButton = new JButton("Display Log and Summary");
        logButton.addActionListener(e -> {
            for (String category : acc.getCategories()) {
                acc.updateCategory(category);
            }
            new DisplayLog(acc);
        }
        );
        return logButton;
    }

    // EFFECTS:  creates a save button;
    //           when pressed, saves the current log to FILE
    JButton createSaveButton() {
        JButton saveButton = new JButton("Save Log");
        saveButton.addActionListener(e -> {
            try {
                Writer writer = new Writer(new File(FILE));
                writer.write(acc);
                writer.close();
                System.out.println("Saved to " + FILE + "\n");
            } catch (FileNotFoundException except1) {
                System.out.println("Unable to save account to " + FILE + "\n");
            } catch (UnsupportedEncodingException except2) {
                except2.printStackTrace();    // due to programming error
            }
        }
        );
        return saveButton;
    }


    // --------------------------------------------------------

    // Record menu methods

    // MODIFIES: this
    // EFFECTS:  displays the record menu to the user;
    //           includes an options menu and submenu pane
    void displayRecordMenu() {
        setupRecordMenu();

        //---------------------------OPTIONS MENU---------------------------//
        JPanel overHeadMenu = createRecordOverHeadMenu();
        recordMenu.add(overHeadMenu);

        //---------------------------SUBMENU PANE---------------------------//
        JPanel subMenu = createRecordSubMenu();
        recordMenu.add(subMenu);

    }

    // MODIFIES: this
    // EFFECTS:  sets up the framework for the record menu
    void setupRecordMenu() {
        frame.setTitle("Records");
        recordMenu = new JPanel();
        recordMenu.setLayout(new BoxLayout(recordMenu, BoxLayout.PAGE_AXIS));
        frame.add(recordMenu);
    }

    //---------------------------OPTIONS MENU---------------------------//

    // EFFECTS:  creates record menu's option menu
    JPanel createRecordOverHeadMenu() {
        JPanel overHeadMenu = new JPanel();

        JButton addButton = createAddButton();
        overHeadMenu.add(addButton);

        JButton editButton = createEditButton();
        overHeadMenu.add(editButton);

        JButton removeButton = createRemoveButton();
        overHeadMenu.add(removeButton);

        JButton cancelButton = createCancelButton();
        overHeadMenu.add(cancelButton);

        return overHeadMenu;
    }

    // MODIFIES: this
    // EFFECTS:  creates an add button;
    //           when pressed, displays the add record menu
    //           in the submenu pane
    JButton createAddButton() {
        JButton addButton = new JButton("Add Record");
        addButton.addActionListener(e -> setRecordMenuVisibility(true, false,false)
        );
        return addButton;
    }

    // MODIFIES: this
    // EFFECTS:  creates an edit button;
    //           when pressed, displays the edit record menu
    //           in the submenu pane
    JButton createEditButton() {
        JButton editButton = new JButton("Edit Record");
        editButton.addActionListener(e -> setRecordMenuVisibility(false, true,false)
        );
        return editButton;
    }

    // MODIFIES: this
    // EFFECTS:  creates a remove button;
    //           when pressed, displays the remove record menu
    //           in the submenu pane
    JButton createRemoveButton() {
        JButton removeButton = new JButton("Remove Record");
        removeButton.addActionListener(e -> setRecordMenuVisibility(false, false,true)
        );
        return removeButton;
    }

    // MODIFIES: this
    // EFFECTS:  creates a cancel button;
    //           when pressed, exits the record menu
    //           and displays the main menu
    JButton createCancelButton() {
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> closeMenu(recordMenu)
        );
        return cancelButton;
    }

    //---------------------------SUBMENU PANE---------------------------//

    // MODIFIES: this
    // EFFECTS:  creates record menu's submenu pane
    JPanel createRecordSubMenu() {
        JPanel subMenu = new JPanel();

        addRecord = addRecordMenu();
        subMenu.add(addRecord);
        addRecord.setVisible(false);

        editRecord = editRecordMenu();
        subMenu.add(editRecord);
        editRecord.setVisible(false);

        removeRecord = removeRecordMenu();
        subMenu.add(removeRecord);
        removeRecord.setVisible(false);

        return subMenu;
    }

    // REQUIRES: one true and two false as parameters
    // MODIFIES: this
    // EFFECTS:  sets the visibility of the add, edit, and remove record menu;
    //           to display the appropriate record menu's submenu pane
    void setRecordMenuVisibility(boolean add, boolean edit, boolean remove) {
        addRecord.setVisible(add);
        editRecord.setVisible(edit);
        removeRecord.setVisible(remove);
    }

    // --------------------------------------------------------

    // Record menu options

    // MODIFIES: this, acc, record
    // EFFECTS:  creates the add record menu in the submenu pane;
    //           when add record button is pressed,
    //           adds a records with title, category, and value
    //           as filled in the form
    private JPanel addRecordMenu() {
        JPanel addMenu = new JPanel();
        addMenu.setLayout(new BoxLayout(addMenu, BoxLayout.PAGE_AXIS));    //PAGE_AXIS = top to bottom

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        addMenu.add(form);

        JTextField titleField = createTitleField(form);
        JComboBox chooseCategory = categorySelector(form);
        JTextField valueField = createValueField(form);

        addMenu.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton addRecordButton = createAddRecordButton(addMenu);

        addRecordButton.addActionListener(e -> {
            String recTitle = titleField.getText();
            String recCategory = (String)chooseCategory.getSelectedItem();
            String recValue = valueField.getText();
            addRecord(recTitle, recCategory, recValue);
        }
        );

        return addMenu;
    }

    // EFFECTS:  creates an add record button
    JButton createAddRecordButton(JPanel menu) {
        menu.add(Box.createRigidArea(new Dimension(0, 30)));
        JButton addRecordButton = new JButton("Add Record");
        addRecordButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        menu.add(addRecordButton);
        return addRecordButton;
    }

    // MODIFIES: this, acc, record
    // EFFECTS:  adds the record with title, category and stringValue as new parameters
    //           at the end of acc's log, and confirms addition with a "click" noise
    private void addRecord(String title, String category, String stringValue) {
        if (!title.contains(",")) {
            try {
                double value = Double.parseDouble(stringValue);
                if (value >= 0.01) {
                    Record record = new Record(title, category, value);
                    acc.addRecord(record);
                    playSound();
                    closeMenu(recordMenu);
                }
            } catch (Exception except) {
                except.printStackTrace();
            }
        }
    }

    // EFFECTS:  plays a "click" sound
    void playSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(SOUND));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception except) {
            except.printStackTrace();
        }
    }

    // MODIFIES: this, acc, record
    // EFFECTS:  creates the edit record menu in the submenu pane
    //           when edit record button is pressed,
    //           edits the record chosen with title, category, and value
    //           as filled in the form
    private JPanel editRecordMenu() {
        JPanel editMenu = new JPanel();
        editMenu.setLayout(new BoxLayout(editMenu, BoxLayout.PAGE_AXIS));

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        editMenu.add(form);

        JTextField recordChosen = createChooseRecordField(form);
        JTextField titleField = createTitleField(form);
        JComboBox chooseCategory = categorySelector(form);
        JTextField valueField = createValueField(form);

        JButton editRecordButton = createEditRecordButton(editMenu);
        editRecordButton.addActionListener(e -> {
            String location = recordChosen.getText();
            String recTitle = titleField.getText();
            String recCategory = (String)chooseCategory.getSelectedItem();
            String recValue = valueField.getText();
            editRecord(location, recTitle, recCategory, recValue);
        }
        );

        return editMenu;
    }

    // EFFECTS:  creates an edit record button
    JButton createEditRecordButton(JPanel menu) {
        menu.add(Box.createRigidArea(new Dimension(0, 30)));
        JButton editRecordButton = new JButton("Edit Record");
        editRecordButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        menu.add(editRecordButton);
        return editRecordButton;
    }

    // MODIFIES: this, acc, record
    // EFFECTS:  edits the record in acc's log at location
    //           with title, category and stringValue as new parameters
    private void editRecord(String location, String title, String category, String stringValue) {
        if (!title.contains(",")) {
            try {
                int pos = Integer.parseInt(location);
                if (pos >= 0 && pos < acc.getLog().size()) {
                    double value = Double.parseDouble(stringValue);
                    if (value > 0.00) {
                        acc.editRecord(pos, title, category, value);
                        closeMenu(recordMenu);
                    }
                }
            } catch (Exception except) {
                except.printStackTrace();
            }
        }
    }

    // MODIFIES: this, acc, record
    // EFFECTS:  creates the remove record menu in the submenu pane
    //           when remove record button is pressed,
    //           removes the record chosen as filled in the form
    private JPanel removeRecordMenu() {
        JPanel removeMenu = new JPanel();
        removeMenu.setLayout(new BoxLayout(removeMenu, BoxLayout.PAGE_AXIS));    //PAGE_AXIS = top to bottom

        JPanel form = new JPanel(new GridLayout(1, 2, 10, 10));
        removeMenu.add(form);

        JTextField enterRecordChosen = createChooseRecordField(form);

        JButton removeRecordButton = createRemoveRecordButton(removeMenu);
        removeRecordButton.addActionListener(e -> {
            String location = enterRecordChosen.getText();
            if (location != null) {
                removeRecord(location);
            }
        }
        );

        return removeMenu;
    }

    // EFFECTS:  creates a remove record button
    JButton createRemoveRecordButton(JPanel menu) {
        menu.add(Box.createRigidArea(new Dimension(0, 30)));
        JButton removeRecordButton = new JButton("Remove Record");
        removeRecordButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        menu.add(removeRecordButton);
        return removeRecordButton;
    }

    // MODIFIES: this, acc, record
    // EFFECTS:  removes the record in acc's log at location
    private void removeRecord(String location) {
        try {
            int pos = Integer.parseInt(location);
            if (pos >= 0 && pos < acc.getLog().size()) {
                acc.removeRecord(pos);
                closeMenu(recordMenu);
            }
        } catch (Exception except) {
            except.printStackTrace();
        }
    }

    // --------------------------------------------------------

    // Record menu helpers

    // EFFECTS:  creates a text field to input record's title
    JTextField createTitleField(JPanel form) {
        JLabel titleLabel = new JLabel("Title: ");
        titleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        form.add(titleLabel);
        JTextField title = new JTextField();
        title.setPreferredSize(new Dimension(100, 20));
        title.setBackground(Color.lightGray);
        form.add(title);
        return title;
    }

    // EFFECTS:  creates a combo box to select record's category
    private JComboBox categorySelector(JPanel form) {
        JLabel categoryLabel = new JLabel("Category: ");
        categoryLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        form.add(categoryLabel);

        CategoryInfo categoryInfo = acc.getCategories();
        ArrayList<String> categories = categoryInfo.getCategories();
        String[] categoriesList = categories.toArray(new String[0]);
        JComboBox chooseCategory = new JComboBox(categoriesList);
        chooseCategory.setSelectedIndex(6);
        chooseCategory.setPreferredSize(new Dimension(100, 20));
        form.add(chooseCategory);
        return chooseCategory;
    }

    // EFFECTS:  creates a text field to input record's value
    JTextField createValueField(JPanel form) {
        JLabel valueLabel = new JLabel("Value (>= $0.01):   $");
        valueLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        form.add(valueLabel);
        JTextField valueField = new JTextField();
        valueField.setPreferredSize(new Dimension(100, 20));
        valueField.setBackground(Color.lightGray);
        form.add(valueField);
        return valueField;
    }

    // EFFECTS:  creates a text field to input
    //           the index of the record to be chosen
    JTextField createChooseRecordField(JPanel form) {
        JLabel instruction = new JLabel("Index of record:");
        form.add(instruction);
        JTextField recordChosen = new JTextField();
        recordChosen.setPreferredSize(new Dimension(100, 20));
        recordChosen.setBackground(Color.lightGray);
        form.add(recordChosen);
        return recordChosen;
    }

    // MODIFIES: this
    // EFFECTS:  exits (deletes) the current record menu,
    //           refreshes the acc and graph of the main menu,
    //           and displays the main menu
    void closeMenu(JPanel menu) {
        frame.setTitle("Budget Planner");
        mainMenu.setVisible(true);
        frame.remove(menu);
        mainMenu.remove(barGraph);
        for (String category : acc.getCategories()) {
            acc.updateCategory(category);
        }
        barGraph = new BarGraph(acc);
        mainMenu.add(barGraph);
        mainMenu.repaint();
    }


}

