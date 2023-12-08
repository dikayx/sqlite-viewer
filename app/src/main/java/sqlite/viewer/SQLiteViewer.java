package sqlite.viewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Objects;

public class SQLiteViewer extends JFrame {
    public SQLiteViewer() {
        // Basic window settings
        String osName = System.getProperty("os.name").toLowerCase();
        int width = osName.contains("win") ? 540 : 525;
        int height = 700;
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setTitle("SQLite Viewer");
        setResizable(false);
        // Same look and feel for all operating systems (using Nimbus)
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            showErrorDialog("UI error", e.getMessage());
        }
        // Initialize main window components
        initComponents();
        // Render window content
        setVisible(true);
    }

    private void initComponents() {
        // Components
        JTextField fileNameTextField = new JTextField();
        fileNameTextField.setName("FileNameTextField");
        fileNameTextField.setColumns(36);

        JButton connectDbButton = new JButton("Connect");
        connectDbButton.setName("ConnectDbButton");

        JComboBox<String> tablesComboBox = new JComboBox<>();
        tablesComboBox.setName("TablesComboBox");

        JTextArea queryTextArea = new JTextArea();
        queryTextArea.setName("QueryTextArea");
        queryTextArea.setRows(8);
        queryTextArea.setColumns(36);
        queryTextArea.setEnabled(false);
        JScrollPane queryTextScroll = new JScrollPane(queryTextArea);

        JButton executeButton = new JButton("Execute");
        executeButton.setName("ExecuteQueryButton");
        executeButton.setEnabled(false);

        JTable table = new JTable();
        table.setName("Table");
        table.setFillsViewportHeight(true);
        JScrollPane tableScrollPane = new JScrollPane(table);

        // Menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // File menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F); // Only works on win32

        JMenuItem loadMenuItem = new JMenuItem("Load file...");
        loadMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));

        JMenuItem quitMenuItem = new JMenuItem("Quit");
        quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        quitMenuItem.addActionListener(actionEvent -> System.exit(0));
        quitMenuItem.setName("MenuExit");

        // Add file menu components
        fileMenu.add(loadMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(quitMenuItem);

        // SQL selection menu
        JMenu sqlMenu = new JMenu("Select");

        JCheckBoxMenuItem sqliteMenuItem = new JCheckBoxMenuItem("sqlite");
        sqliteMenuItem.setState(true);

        sqlMenu.add(sqliteMenuItem);

        // Add to menu bar
        menuBar.add(fileMenu);
        menuBar.add(sqlMenu);

        // Place components
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        add(topPanel, BorderLayout.PAGE_START);

        JPanel selectionPanel = new JPanel(new GridLayout(2, 1));

        JPanel selectFilePanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        selectFilePanel.add(new JLabel("Locate the SQLite *.db file:"));
        selectFilePanel.add(fileNameTextField);
        selectFilePanel.add(connectDbButton);
        selectFilePanel.setBorder(BorderFactory.createTitledBorder("Select file"));

        JPanel selectTablePanel = new JPanel(new GridLayout(2, 0));
        selectTablePanel.add(new JLabel("Please select a table from the database:"));
        selectTablePanel.add(tablesComboBox);
        selectTablePanel.setBorder(BorderFactory.createTitledBorder("Select table"));

        selectionPanel.add(selectFilePanel);
        selectionPanel.add(selectTablePanel);

        JPanel centerPanel = new JPanel(new GridLayout(1, 0));
        add(centerPanel, BorderLayout.CENTER);

        JPanel queryPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        queryPanel.add(queryTextScroll);
        queryPanel.add(executeButton);
        queryPanel.setBorder(BorderFactory.createTitledBorder("Run query"));

        JPanel tablePanel = new JPanel(new GridLayout(1, 0));
        tablePanel.add(tableScrollPane);
        tablePanel.setBorder(BorderFactory.createTitledBorder("Results"));

        // Add panels to main window
        topPanel.add(selectionPanel);
        topPanel.add(queryPanel);
        centerPanel.add(tablePanel);

        // Action Listeners
        loadMenuItem.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                fileNameTextField.setText(""); // Clear text
                fileNameTextField.setText(fileChooser.getSelectedFile().toString());
            }
        });
        connectDbButton.addActionListener(actionEvent -> {
            Path filePath = Paths.get(fileNameTextField.getText());
            if (!Objects.equals(fileNameTextField.getText(), "") && Files.exists(filePath)) {
                try (Driver driver = new Driver(filePath.toAbsolutePath().toString())) {
                    tablesComboBox.removeAllItems();
                    driver.getAllTables().forEach(tablesComboBox::addItem);
                    queryTextArea.setText(String.format(Driver.SQL_ALL_ROWS, tablesComboBox.getSelectedItem()));
                    // Enable buttons
                    queryTextArea.setEnabled(true);
                    executeButton.setEnabled(true);
                } catch (Exception e) {
                    showErrorDialog("SQL connection error", e.getMessage());
                }
            } else {
                tablesComboBox.removeAllItems();
                queryTextArea.setText(null);
                queryTextArea.setEnabled(false);
                executeButton.setEnabled(false);
                showErrorDialog("File error", "File not found");
            }
        });
        tablesComboBox.addItemListener(actionEvent ->
                queryTextArea.setText(String.format(Driver.SQL_ALL_ROWS, actionEvent.getItem().toString())));
        executeButton.addActionListener(actionEvent -> {
            try (Driver driver = new Driver(fileNameTextField.getText())) {
                DataTableModel tableModel = driver.runQuery(queryTextArea.getText());
                table.setModel(tableModel);
            } catch (SQLException e) {
                showErrorDialog("SQL execution error", e.getMessage());
            } catch (Exception e) {
                showErrorDialog("Application error", e.getMessage());
            }
        });
    }

    private void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(
                new Frame(),
                message,
                title,
                JOptionPane.ERROR_MESSAGE
        );
    }
}
