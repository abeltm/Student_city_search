import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class StudentCitySearch {

    private JFrame frame;
    private JTextField cityInput;
    private JTextArea resultsTextArea;
    private JScrollPane scrollPane;
    private JButton searchButton;
    private Connection connection;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                StudentCitySearch window = new StudentCitySearch();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public StudentCitySearch() {
        connectToDatabase();
        initialize();
    }

    private void connectToDatabase() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@199.212.26.208:1521:SQLD", "COMP228_W23_sy_195", "password");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        frame = new JFrame("Student City Search");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.getContentPane().setLayout(new BorderLayout());

        cityInput = new JTextField();
        frame.getContentPane().add(cityInput, BorderLayout.NORTH);

        resultsTextArea = new JTextArea();
        resultsTextArea.setEditable(false);
        scrollPane = new JScrollPane(resultsTextArea);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchStudentsByCity());
        frame.getContentPane().add(searchButton, BorderLayout.SOUTH);
    }

    private void searchStudentsByCity() {
        String city = cityInput.getText();
        String query = "SELECT * FROM Students WHERE city = ?";
        StringBuilder resultBuilder = new StringBuilder();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, city);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                resultBuilder.append(resultSet.getString("studentID")).append("\t")
                        .append(resultSet.getString("firstName")).append("\t")
                        .append(resultSet.getString("lastName")).append("\t")
                        .append(resultSet.getString("address")).append("\t")
                        .append(resultSet.getString("city")).append("\t")
                        .append(resultSet.getString("province")).append("\t")
                        .append(resultSet.getString("postalCode")).append("\n");
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        resultsTextArea.setText(resultBuilder.toString());
    }
}
