/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projectoop;

/**
 *
 * @author Abdul Hadi
 */
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SportCourtApp extends Application {
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Court> courts = new ArrayList<>();
    private ArrayList<Booking> bookings = new ArrayList<>();
    private User currentUser;
    private Admin admin;
    private Stage primaryStage;
    private Booking pendingBooking;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        users = DataManager.loadUsers();
        courts = DataManager.loadCourts();
        bookings = DataManager.loadBookings(users, courts);
        
        admin = new Admin("ADM001", "Admin", "admin@courtify.com", "admin123");
        
        if (courts.isEmpty()) {
            initializeCourts();
        }

        primaryStage.setTitle("Courtify - Sport Court Management System");
        showLoginScreen();
        primaryStage.show();
    }

    private void initializeCourts() {
        // Badminton - 10 courts
        for (int i = 1; i <= 10; i++) {
            courts.add(new Court("BAD" + String.format("%02d", i), 
                               "Badminton Court " + i, "Badminton", "Available"));
        }
        // Futsal - 2 courts
        for (int i = 1; i <= 2; i++) {
            courts.add(new Court("FUT" + String.format("%02d", i), 
                               "Futsal Court " + i, "Futsal", "Available"));
        }
        // Tennis - 4 courts
        for (int i = 1; i <= 4; i++) {
            courts.add(new Court("TEN" + String.format("%02d", i), 
                               "Tennis Court " + i, "Tennis", "Available"));
        }
        // Pickleball - 4 courts
        for (int i = 1; i <= 4; i++) {
            courts.add(new Court("PCK" + String.format("%02d", i), 
                               "Pickleball Court " + i, "Pickleball", "Available"));
        }
        // Ping Pong - 10 courts (FIXED: Changed from "PingPong" to "Ping Pong")
        for (int i = 1; i <= 10; i++) {
            courts.add(new Court("PNG" + String.format("%02d", i), 
                               "Ping Pong Table " + i, "Ping Pong", "Available"));
        }
        DataManager.saveCourts(courts);
    }

    private void showLoginScreen() {
        VBox root = new VBox(25);
        root.setPadding(new Insets(50));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);");

        // Courtify Logo
        Label logoLabel = new Label("🎾 COURTIFY");
        logoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 42));
        logoLabel.setTextFill(Color.TEAL);
        logoLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 3);");

        Label subtitleLabel = new Label("Your Ultimate Court Booking Experience");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        subtitleLabel.setTextFill(Color.web("#111827"));

        VBox formBox = new VBox(15);
        formBox.setMaxWidth(350);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(30));
        formBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); " +
                        "-fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);");

        TextField emailField = new TextField();
        emailField.setPromptText("Email Address");
        emailField.setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8;");

        Button loginButton = new Button("LOGIN");
        loginButton.setStyle("-fx-background-color: #667eea; -fx-text-fill: white; " +
                           "-fx-font-size: 15px; -fx-font-weight: bold; -fx-min-width: 290px; " +
                           "-fx-min-height: 45px; -fx-background-radius: 8; -fx-cursor: hand; " +
                           "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.4), 8, 0, 0, 2);");
        loginButton.setOnMouseEntered(e -> loginButton.setStyle(loginButton.getStyle() + 
                                      "-fx-scale-x: 1.02; -fx-scale-y: 1.02;"));
        loginButton.setOnMouseExited(e -> loginButton.setStyle(loginButton.getStyle().replace(
                                     "-fx-scale-x: 1.02; -fx-scale-y: 1.02;", "")));

        Button registerButton = new Button("CREATE ACCOUNT");
        registerButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #667eea; " +
                              "-fx-font-size: 13px; -fx-font-weight: bold; -fx-min-width: 290px; " +
                              "-fx-border-color: #667eea; -fx-border-width: 2; -fx-border-radius: 8; " +
                              "-fx-background-radius: 8; -fx-min-height: 45px; -fx-cursor: hand;");

        Hyperlink adminLink = new Hyperlink("Admin Login");
        adminLink.setStyle("-fx-text-fill: black; -fx-font-size: 13px; -fx-underline: true;");

        Label messageLabel = new Label();
        messageLabel.setTextFill(Color.web("#ff6b6b"));
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(290);

        loginButton.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            
            if (email.isEmpty() || password.isEmpty()) {
                messageLabel.setText("⚠ Please fill in all fields!");
                return;
            }

            for (User user : users) {
                if (user.getEmail().equals(email)) {
                    if (user.isSuspended()) {
                        messageLabel.setText("⛔ Account suspended. Contact admin.");
                        return;
                    }
                    if (user.login(email, password)) {
                        currentUser = user;
                        showUserDashboard();
                        return;
                    }
                }
            }
            messageLabel.setText("❌ Invalid credentials!");
        });

        registerButton.setOnAction(e -> showRegisterScreen());
        adminLink.setOnAction(e -> showAdminLoginScreen());

        formBox.getChildren().addAll(emailField, passwordField, loginButton, 
                                     registerButton, messageLabel);

        root.getChildren().addAll(logoLabel, subtitleLabel, formBox, adminLink);

        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setScene(scene);
    }

    private void showRegisterScreen() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);");

        Label titleLabel = new Label("🎾 CREATE YOUR ACCOUNT");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.TEAL);

        VBox formBox = new VBox(12);
        formBox.setMaxWidth(400);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(30));
        formBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); " +
                        "-fx-background-radius: 15;");

        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");
        nameField.setStyle("-fx-font-size: 14px; -fx-padding: 10; -fx-background-radius: 8;");

        TextField emailField = new TextField();
        emailField.setPromptText("Email Address");
        emailField.setStyle("-fx-font-size: 14px; -fx-padding: 10; -fx-background-radius: 8;");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number (e.g., 0123456789)");
        phoneField.setStyle("-fx-font-size: 14px; -fx-padding: 10; -fx-background-radius: 8;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle("-fx-font-size: 14px; -fx-padding: 10; -fx-background-radius: 8;");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        confirmPasswordField.setStyle("-fx-font-size: 14px; -fx-padding: 10; -fx-background-radius: 8;");

        Button registerButton = new Button("REGISTER NOW");
        registerButton.setStyle("-fx-background-color: #f5576c; -fx-text-fill: white; " +
                              "-fx-font-size: 14px; -fx-font-weight: bold; -fx-min-width: 340px; " +
                              "-fx-min-height: 45px; -fx-background-radius: 8; -fx-cursor: hand;");

        Button backButton = new Button("← BACK TO LOGIN");
        backButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #666; " +
                          "-fx-font-size: 12px; -fx-cursor: hand;");

        Label messageLabel = new Label();
        messageLabel.setTextFill(Color.web("#f5576c"));
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(340);

        registerButton.setOnAction(e -> {
            if (nameField.getText().isEmpty() || emailField.getText().isEmpty() || 
                phoneField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                messageLabel.setText("⚠ Please fill in all fields!");
                return;
            }

            if (!passwordField.getText().equals(confirmPasswordField.getText())) {
                messageLabel.setText("❌ Passwords do not match!");
                return;
            }

            String userID = "U" + String.format("%04d", users.size() + 1);
            User newUser = new User(userID, nameField.getText(), emailField.getText(), 
                                  passwordField.getText(), phoneField.getText());
            users.add(newUser);
            DataManager.saveUsers(users);
            
            showSuccessDialog("Registration Successful!", 
                            "Welcome to Courtify! You can now login with your credentials.");
            showLoginScreen();
        });

        backButton.setOnAction(e -> showLoginScreen());

        formBox.getChildren().addAll(nameField, emailField, phoneField, 
                                     passwordField, confirmPasswordField, 
                                     registerButton, messageLabel);

        root.getChildren().addAll(titleLabel, formBox, backButton);

        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setScene(scene);
    }

    private void showAdminLoginScreen() {
        VBox root = new VBox(25);
        root.setPadding(new Insets(50));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);");

        Label titleLabel = new Label("🔐 ADMIN ACCESS");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleLabel.setTextFill(Color.TEAL);

        VBox formBox = new VBox(15);
        formBox.setMaxWidth(350);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(30));
        formBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); " +
                        "-fx-background-radius: 15;");

        TextField emailField = new TextField();
        emailField.setPromptText("Admin Email");
        emailField.setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Admin Password");
        passwordField.setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8;");

        Button loginButton = new Button("LOGIN AS ADMIN");
        loginButton.setStyle("-fx-background-color: #fa709a; -fx-text-fill: white; " +
                           "-fx-font-size: 14px; -fx-font-weight: bold; -fx-min-width: 290px; " +
                           "-fx-min-height: 45px; -fx-background-radius: 8; -fx-cursor: hand;");

        Button backButton = new Button("← BACK");
        backButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #666; " +
                          "-fx-font-size: 12px; -fx-cursor: hand;");

        Label messageLabel = new Label();
        messageLabel.setTextFill(Color.web("#e74c3c"));
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        loginButton.setOnAction(e -> {
            if (admin.login(emailField.getText(), passwordField.getText())) {
                showAdminDashboard();
            } else {
                messageLabel.setText("❌ Invalid admin credentials!");
            }
        });

        backButton.setOnAction(e -> showLoginScreen());

        formBox.getChildren().addAll(emailField, passwordField, loginButton, messageLabel);
        root.getChildren().addAll(titleLabel, formBox, backButton);

        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setScene(scene);
    }

    private void showUserDashboard() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f7fa;");

        // Top Navigation Bar
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(20));
        topBar.setStyle("-fx-background: linear-gradient(to right, #667eea, #764ba2); " +
                       "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");
        topBar.setAlignment(Pos.CENTER_LEFT);

        Label logoLabel = new Label("DASHBOARD");
        logoLabel.setTextFill(Color.BLACK);
        logoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Label welcomeLabel = new Label("Welcome, " + currentUser.getName() + "!");
        welcomeLabel.setTextFill(Color.BLACK);
        welcomeLabel.setFont(Font.font("Arial", 16));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutButton = new Button("LOGOUT");
        logoutButton.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: black; " +
                            "-fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 20; " +
                            "-fx-cursor: hand; -fx-border-color: black; -fx-border-radius: 20;");
        logoutButton.setOnAction(e -> {
            currentUser = null;
            showLoginScreen();
        });

        topBar.getChildren().addAll(logoLabel, welcomeLabel, spacer, logoutButton);

        // Dashboard Content
        VBox centerBox = new VBox(40);
        centerBox.setPadding(new Insets(50));
        centerBox.setAlignment(Pos.CENTER);

        Label dashboardTitle = new Label("🎾 COURTIFY");
        dashboardTitle.setFont(Font.font("Arial", FontWeight.BOLD, 56));
        dashboardTitle.setTextFill(Color.web("#008080"));

        GridPane cardsGrid = new GridPane();
        cardsGrid.setHgap(30);
        cardsGrid.setVgap(30);
        cardsGrid.setAlignment(Pos.CENTER);

        VBox bookCard = createDashboardCard("🏸", "BOOK COURT", 
            "Reserve your preferred court", "#667eea", e -> showSelectSportScreen());
        VBox myBookingsCard = createDashboardCard("📋", "MY BOOKINGS", 
            "View & manage reservations", "#f093fb", e -> showMyBookingsScreen());
        VBox courtsCard = createDashboardCard("🎯", "VIEW COURTS", 
            "See all available courts", "#4facfe", e -> showAvailableCourtsScreen());

        cardsGrid.add(bookCard, 0, 0);
        cardsGrid.add(myBookingsCard, 1, 0);
        cardsGrid.add(courtsCard, 2, 0);

        centerBox.getChildren().addAll(dashboardTitle, cardsGrid);

        root.setTop(topBar);
        root.setCenter(centerBox);

        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setScene(scene);
    }

    private VBox createDashboardCard(String icon, String title, String description, 
                                    String color, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(25));
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 5); " +
                     "-fx-cursor: hand;");
        card.setPrefSize(250, 220);

        card.setOnMouseEntered(e -> card.setStyle(card.getStyle() + 
                              "-fx-translate-y: -5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 20, 0, 0, 10);"));
        card.setOnMouseExited(e -> card.setStyle(card.getStyle().replace(
                             "-fx-translate-y: -5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 20, 0, 0, 10);", 
                             "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 5);")));

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(48));

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.web(color));

        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Arial", 13));
        descLabel.setTextFill(Color.web("#718096"));
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(210);
        descLabel.setAlignment(Pos.CENTER);
        descLabel.setStyle("-fx-text-alignment: center;");

        Button actionButton = new Button("GO");
        actionButton.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                            "-fx-font-weight: bold; -fx-min-width: 120px; -fx-min-height: 35px; " +
                            "-fx-background-radius: 20; -fx-cursor: hand;");
        actionButton.setOnAction(action);

        card.getChildren().addAll(iconLabel, titleLabel, descLabel, actionButton);
        card.setOnMouseClicked(e -> actionButton.fire());
        
        return card;
    }

    // In showSelectSportScreen() method, replace the sports array:
    
    private void showSelectSportScreen() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f7fa;");

        VBox topSection = createTopSection("SELECT SPORT TYPE", "#667eea", () -> showUserDashboard());

        VBox contentBox = new VBox(30);
        contentBox.setPadding(new Insets(40));
        contentBox.setAlignment(Pos.CENTER);

        Label infoLabel = new Label("Choose your preferred sport to view available courts");
        infoLabel.setFont(Font.font("Arial", 16));
        infoLabel.setTextFill(Color.web("#718096"));

        GridPane sportsGrid = new GridPane();
        sportsGrid.setHgap(25);
        sportsGrid.setVgap(25);
        sportsGrid.setAlignment(Pos.CENTER);

        // FIXED: Use consistent naming - "Ping Pong" with space
        String[][] sports = {
            {"🏸", "Badminton", "RM14-24/hr", "10 courts", "#667eea"},
            {"⚽", "Futsal", "RM70-100/hr", "2 courts", "#f093fb"},
            {"🎾", "Tennis", "RM50-70/hr", "4 courts", "#4facfe"},
            {"🏓", "Pickleball", "RM50-70/hr", "4 courts", "#f5576c"},
            {"🏓", "Ping Pong", "RM10-14/hr", "10 courts", "#2ecc71"}  // Changed from "Ping Pong" to match database
        };

        int col = 0, row = 0;
        for (String[] sport : sports) {
            VBox sportCard = createSportCard(sport[0], sport[1], sport[2], sport[3], sport[4]);
            sportsGrid.add(sportCard, col, row);
            col++;
            if (col > 2) {
                col = 0;
                row++;
            }
        }

        contentBox.getChildren().addAll(infoLabel, sportsGrid);

        root.setTop(topSection);
        root.setCenter(contentBox);

        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setScene(scene);
    }

    private VBox createSportCard(String icon, String sport, String price, String courts, String color) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(25));
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: ; -fx-background-radius: 15; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3); " +
                     "-fx-cursor: hand;");
        card.setPrefSize(220, 200);

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(42));

        Label sportLabel = new Label(sport);
        sportLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        sportLabel.setTextFill(Color.web(color));

        Label priceLabel = new Label(price);
        priceLabel.setFont(Font.font("Arial", 13));
        priceLabel.setTextFill(Color.web("#718096"));

        Label courtsLabel = new Label(courts + " available");
        courtsLabel.setFont(Font.font("Arial", 12));
        courtsLabel.setTextFill(Color.web("#a0aec0"));

        card.setOnMouseEntered(e -> card.setStyle(card.getStyle() + 
                              "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"));
        card.setOnMouseExited(e -> card.setStyle(card.getStyle().replace(
                             "-fx-scale-x: 1.05; -fx-scale-y: 1.05;", "")));

        card.setOnMouseClicked(e -> showBookingScreen(sport));

        card.getChildren().addAll(iconLabel, sportLabel, priceLabel, courtsLabel);
        return card;
    }

    private void showBookingScreen(String sportType) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f7fa;");

        VBox topSection = createTopSection("BOOK " + sportType.toUpperCase() + " COURT", 
                                          "#667eea", () -> showSelectSportScreen());

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        VBox formBox = new VBox(20);
        formBox.setPadding(new Insets(40));
        formBox.setMaxWidth(600);
        formBox.setAlignment(Pos.TOP_CENTER);
        formBox.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 5);");

        // Date Selection
        Label dateLabel = new Label("📅 Select Date");
        dateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
        datePicker.setPrefWidth(540);
        datePicker.setStyle("-fx-font-size: 14px; -fx-padding: 10;");

        // Court Selection
        Label courtLabel = new Label("🏸 Select Court");
        courtLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        ComboBox<Court> courtCombo = new ComboBox<>();
        for (Court court : courts) {
            // Case-insensitive comparison for court type
            String courtType = court.getCourtType().toLowerCase().replace(" ", "");
            String selectedType = sportType.toLowerCase().replace(" ", "");
            
            if (courtType.equals(selectedType) && 
                court.checkAvailability() && 
                !court.getStatus().equals("Under Maintenance")) {
                courtCombo.getItems().add(court);
            }
        }
        courtCombo.setPromptText("Choose a court");
        courtCombo.setPrefWidth(540);
        courtCombo.setStyle("-fx-font-size: 14px; -fx-padding: 10;");

        // Duration Selection (moved before time selection)
        Label durationLabel = new Label("⏱ Duration (hours)");
        durationLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Spinner<Integer> durationSpinner = new Spinner<>(1, 8, 1);
        durationSpinner.setPrefWidth(540);
        durationSpinner.setStyle("-fx-font-size: 14px;");

        // Time Selection
        Label timeLabel = new Label("🕐 Select Time (9 AM - 2 AM)");
        timeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        ComboBox<String> timeCombo = new ComboBox<>();
        timeCombo.setPromptText("Choose start time");
        timeCombo.setPrefWidth(540);
        timeCombo.setStyle("-fx-font-size: 14px; -fx-padding: 10;");

        // Info label for available slots
        Label availabilityLabel = new Label("Select court, date, and duration to see available times");
        availabilityLabel.setFont(Font.font("Arial", 12));
        availabilityLabel.setTextFill(Color.web("#718096"));
        availabilityLabel.setWrapText(true);
        availabilityLabel.setMaxWidth(540);

        // Update available time slots when court, date, or duration changes
        Runnable updateAvailableSlots = () -> {
            timeCombo.getItems().clear();
            
            if (courtCombo.getValue() != null && datePicker.getValue() != null) {
                int duration = durationSpinner.getValue();
                String selectedDate = datePicker.getValue().toString();
                Court selectedCourt = courtCombo.getValue();
                
                ArrayList<String> availableSlots = BookingValidator.getAvailableTimeSlots(
                    bookings, selectedCourt, selectedDate, duration);
                
                if (availableSlots.isEmpty()) {
                    availabilityLabel.setText("❌ No available slots for " + duration + " hour(s) on this date");
                    availabilityLabel.setTextFill(Color.web("#e74c3c"));
                } else {
                    timeCombo.getItems().addAll(availableSlots);
                    availabilityLabel.setText("✓ " + availableSlots.size() + " time slot(s) available");
                    availabilityLabel.setTextFill(Color.web("#48bb78"));
                }
            }
        };

        courtCombo.setOnAction(e -> updateAvailableSlots.run());
        datePicker.setOnAction(e -> updateAvailableSlots.run());
        durationSpinner.valueProperty().addListener((obs, old, val) -> updateAvailableSlots.run());

        // Price Display
        Label priceDisplayLabel = new Label("Total Price: RM 0.00");
        priceDisplayLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        priceDisplayLabel.setTextFill(Color.web("#667eea"));
        priceDisplayLabel.setStyle("-fx-padding: 15; -fx-background-color: #f7fafc; " +
                                   "-fx-background-radius: 10;");

        // Calculate price on changes
        Runnable calculatePrice = () -> {
            if (courtCombo.getValue() != null && timeCombo.getValue() != null) {
                int startHour = Integer.parseInt(timeCombo.getValue().split(":")[0]);
                int duration = durationSpinner.getValue();
                double price = courtCombo.getValue().calculatePrice(startHour, duration);
                priceDisplayLabel.setText(String.format("Total Price: RM %.2f", price));
            }
        };

        courtCombo.setOnAction(e -> {
            updateAvailableSlots.run();
            calculatePrice.run();
        });
        timeCombo.setOnAction(e -> calculatePrice.run());
        durationSpinner.valueProperty().addListener((obs, old, val) -> {
            updateAvailableSlots.run();
            calculatePrice.run();
        });

        Button proceedButton = new Button("PROCEED TO PAYMENT");
        proceedButton.setStyle("-fx-background-color: #667eea; -fx-text-fill: white; " +
                             "-fx-font-size: 15px; -fx-font-weight: bold; -fx-min-width: 540px; " +
                             "-fx-min-height: 50px; -fx-background-radius: 10; -fx-cursor: hand;");

        Label messageLabel = new Label();
        messageLabel.setTextFill(Color.web("#e74c3c"));
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(540);

        proceedButton.setOnAction(e -> {
            if (courtCombo.getValue() == null || datePicker.getValue() == null || 
                timeCombo.getValue() == null) {
                messageLabel.setText("⚠ Please fill in all fields!");
                return;
            }

            Court selectedCourt = courtCombo.getValue();
            String selectedDate = datePicker.getValue().toString();
            String selectedTime = timeCombo.getValue();
            int duration = durationSpinner.getValue();

            // CHECK IF TIME IS IN THE PAST
            if (BookingValidator.isPastTime(selectedDate, selectedTime)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Time");
                alert.setHeaderText("Cannot Book Past Time");
                alert.setContentText("The selected time has already passed.\n\n" +
                                   "Please select a current or future time slot.");
                alert.showAndWait();
                
                messageLabel.setText("❌ Cannot book past times! Select a future time slot.");
                return;
            }

            // CHECK FOR CONFLICTS
            if (BookingValidator.hasConflict(bookings, selectedCourt, selectedDate, 
                                            selectedTime, duration)) {
                ArrayList<Booking> conflicts = BookingValidator.getConflictingBookings(
                    bookings, selectedCourt, selectedDate, selectedTime, duration);
                
                StringBuilder conflictMsg = new StringBuilder("❌ BOOKING CONFLICT!\n\n");
                conflictMsg.append(selectedCourt.getCourtName()).append(" is already booked:\n");
                
                for (Booking conflict : conflicts) {
                    conflictMsg.append("• ").append(BookingValidator.formatConflictMessage(conflict))
                              .append("\n");
                }
                
                conflictMsg.append("\nPlease choose a different time slot.");
                
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Booking Conflict");
                alert.setHeaderText("This time slot is not available");
                alert.setContentText(conflictMsg.toString());
                alert.showAndWait();
                
                messageLabel.setText("❌ Time slot already booked! Choose another time.");
                return;
            }

            String bookingID = "B" + String.format("%05d", bookings.size() + 1);
            
            pendingBooking = new Booking(bookingID, currentUser, selectedCourt, 
                                       selectedDate, selectedTime, duration);
            
            showPaymentScreen(pendingBooking);
        });

        formBox.getChildren().addAll(
            dateLabel, datePicker,
            courtLabel, courtCombo,
            durationLabel, durationSpinner,
            timeLabel, timeCombo,
            availabilityLabel,
            priceDisplayLabel,
            proceedButton, messageLabel
        );

        VBox centerWrapper = new VBox(formBox);
        centerWrapper.setPadding(new Insets(30));
        centerWrapper.setAlignment(Pos.TOP_CENTER);
        
        scrollPane.setContent(centerWrapper);
        root.setTop(topSection);
        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setScene(scene);
    }
    
    private void showPaymentScreen(Booking booking) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f7fa;");

        VBox topSection = createTopSection("PAYMENT", "#667eea", 
                                          () -> showSelectSportScreen());

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        VBox contentBox = new VBox(25);
        contentBox.setPadding(new Insets(40));
        contentBox.setMaxWidth(700);
        contentBox.setAlignment(Pos.TOP_CENTER);

        // Booking Summary
        VBox summaryBox = new VBox(15);
        summaryBox.setPadding(new Insets(25));
        summaryBox.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");

        Label summaryTitle = new Label("📋 BOOKING SUMMARY");
        summaryTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        summaryTitle.setTextFill(Color.web("#2d3748"));

        String summaryText = String.format(
            "Court: %s\nDate: %s\nTime: %s\nDuration: %d hour(s)\n\nTotal Amount: RM %.2f",
            booking.getCourt().getCourtName(),
            booking.getDate(),
            booking.getTimeSlot(),
            booking.getDuration(),
            booking.getTotalAmount()
        );

        Label summaryLabel = new Label(summaryText);
        summaryLabel.setFont(Font.font("Arial", 14));
        summaryLabel.setTextFill(Color.web("#4a5568"));
        summaryLabel.setStyle("-fx-line-spacing: 5;");

        summaryBox.getChildren().addAll(summaryTitle, new Separator(), summaryLabel);

        // Payment Method Selection
        VBox paymentBox = new VBox(15);
        paymentBox.setPadding(new Insets(25));
        paymentBox.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");

        Label paymentTitle = new Label("💳 SELECT PAYMENT METHOD");
        paymentTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        paymentTitle.setTextFill(Color.web("#2d3748"));

        ToggleGroup paymentGroup = new ToggleGroup();

        RadioButton ewalletRadio = new RadioButton("E-Wallet (Touch 'n Go, GrabPay, Boost)");
        ewalletRadio.setToggleGroup(paymentGroup);
        ewalletRadio.setFont(Font.font("Arial", 14));
        ewalletRadio.setStyle("-fx-cursor: hand;");

        RadioButton bankRadio = new RadioButton("Online Banking");
        bankRadio.setToggleGroup(paymentGroup);
        bankRadio.setFont(Font.font("Arial", 14));
        bankRadio.setStyle("-fx-cursor: hand;");

        RadioButton cardRadio = new RadioButton("Credit/Debit Card");
        cardRadio.setToggleGroup(paymentGroup);
        cardRadio.setFont(Font.font("Arial", 14));
        cardRadio.setStyle("-fx-cursor: hand;");

        // E-wallet options
        VBox ewalletBox = new VBox(10);
        ewalletBox.setPadding(new Insets(10, 0, 0, 30));
        ewalletBox.setVisible(false);
        ewalletBox.setManaged(false);

        ComboBox<String> ewalletCombo = new ComboBox<>();
        ewalletCombo.getItems().addAll("Touch 'n Go", "GrabPay", "Boost", "ShopeePay", "MAE");
        ewalletCombo.setPromptText("Select E-Wallet");
        ewalletCombo.setPrefWidth(300);

        ewalletBox.getChildren().add(ewalletCombo);

        // Bank options
        VBox bankBox = new VBox(10);
        bankBox.setPadding(new Insets(10, 0, 0, 30));
        bankBox.setVisible(false);
        bankBox.setManaged(false);

        ComboBox<String> bankCombo = new ComboBox<>();
        bankCombo.getItems().addAll("Maybank", "CIMB Bank", "Public Bank", "RHB Bank", 
                                   "Hong Leong Bank", "AmBank", "Bank Islam", "Bank Rakyat");
        bankCombo.setPromptText("Select Bank");
        bankCombo.setPrefWidth(300);

        bankBox.getChildren().add(bankCombo);

        ewalletRadio.setOnAction(e -> {
            ewalletBox.setVisible(true);
            ewalletBox.setManaged(true);
            bankBox.setVisible(false);
            bankBox.setManaged(false);
        });

        bankRadio.setOnAction(e -> {
            bankBox.setVisible(true);
            bankBox.setManaged(true);
            ewalletBox.setVisible(false);
            ewalletBox.setManaged(false);
        });

        cardRadio.setOnAction(e -> {
            ewalletBox.setVisible(false);
            ewalletBox.setManaged(false);
            bankBox.setVisible(false);
            bankBox.setManaged(false);
        });

        Button confirmButton = new Button("CONFIRM PAYMENT");
        confirmButton.setStyle("-fx-background-color: #48bb78; -fx-text-fill: white; " +
                             "-fx-font-size: 16px; -fx-font-weight: bold; -fx-min-width: 640px; " +
                             "-fx-min-height: 55px; -fx-background-radius: 10; -fx-cursor: hand;");

        Label messageLabel = new Label();
        messageLabel.setTextFill(Color.web("#e74c3c"));
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));

        confirmButton.setOnAction(e -> {
            if (paymentGroup.getSelectedToggle() == null) {
                messageLabel.setText("⚠ Please select a payment method!");
                return;
            }

            String paymentMethod = "";
            String paymentDetails = "";

            if (ewalletRadio.isSelected()) {
                if (ewalletCombo.getValue() == null) {
                    messageLabel.setText("⚠ Please select an E-Wallet!");
                    return;
                }
                paymentMethod = "E-Wallet";
                paymentDetails = ewalletCombo.getValue();
            } else if (bankRadio.isSelected()) {
                if (bankCombo.getValue() == null) {
                    messageLabel.setText("⚠ Please select a bank!");
                    return;
                }
                paymentMethod = "Online Banking";
                paymentDetails = bankCombo.getValue();
            } else {
                paymentMethod = "Credit/Debit Card";
                paymentDetails = "Card Payment";
            }

            processPayment(booking, paymentMethod, paymentDetails);
        });

        paymentBox.getChildren().addAll(paymentTitle, new Separator(),
                                       ewalletRadio, ewalletBox,
                                       bankRadio, bankBox,
                                       cardRadio,
                                       confirmButton, messageLabel);

        contentBox.getChildren().addAll(summaryBox, paymentBox);

        VBox wrapper = new VBox(contentBox);
        wrapper.setPadding(new Insets(20));
        wrapper.setAlignment(Pos.TOP_CENTER);
        scrollPane.setContent(wrapper);

        root.setTop(topSection);
        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setScene(scene);
    }

    private void processPayment(Booking booking, String method, String details) {
        booking.createBooking();
        bookings.add(booking);
        
        String paymentID = "P" + booking.getBookingID();
        Payment payment = new Payment(paymentID, booking, booking.getTotalAmount());
        payment.setPaymentMethod(method, details);
        payment.makePayment();
        
        DataManager.saveBookings(bookings);
        DataManager.saveCourts(courts);

        showReceiptDialog(payment);
    }

    private void showReceiptDialog(Payment payment) {
        Stage receiptStage = new Stage();
        receiptStage.initModality(Modality.APPLICATION_MODAL);
        receiptStage.setTitle("Payment Receipt");

        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: white;");

        Label successIcon = new Label("✅");
        successIcon.setFont(Font.font(64));

        Label successLabel = new Label("PAYMENT SUCCESSFUL!");
        successLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        successLabel.setTextFill(Color.web("#48bb78"));

        TextArea receiptArea = new TextArea(payment.generateReceipt());
        receiptArea.setEditable(false);
        receiptArea.setPrefSize(500, 400);
        receiptArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12px;");

        Label savedLabel = new Label("✓ Receipt saved to: receipt_" + payment.getPaymentID() + ".txt");
        savedLabel.setFont(Font.font("Arial", 12));
        savedLabel.setTextFill(Color.web("#48bb78"));

        Button closeButton = new Button("DONE");
        closeButton.setStyle("-fx-background-color: #667eea; -fx-text-fill: white; " +
                           "-fx-font-weight: bold; -fx-min-width: 200px; -fx-min-height: 40px; " +
                           "-fx-background-radius: 20; -fx-cursor: hand;");
        closeButton.setOnAction(e -> {
            receiptStage.close();
            showUserDashboard();
        });

        root.getChildren().addAll(successIcon, successLabel, receiptArea, savedLabel, closeButton);

        Scene scene = new Scene(root, 600, 650);
        receiptStage.setScene(scene);
        receiptStage.showAndWait();
    }

    private void showMyBookingsScreen() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f7fa;");

        VBox topSection = createTopSection("MY BOOKINGS", "#f093fb", () -> showUserDashboard());

        VBox contentBox = new VBox(20);
        contentBox.setPadding(new Insets(30));

        TableView<Booking> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        TableColumn<Booking, String> idCol = new TableColumn<>("Booking ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("bookingID"));
        idCol.setPrefWidth(120);

        TableColumn<Booking, String> courtCol = new TableColumn<>("Court");
        courtCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getCourt().getCourtName()));
        courtCol.setPrefWidth(180);

        TableColumn<Booking, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setPrefWidth(120);

        TableColumn<Booking, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("timeSlot"));
        timeCol.setPrefWidth(100);

        TableColumn<Booking, String> durationCol = new TableColumn<>("Duration");
        durationCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getDuration() + "h"));
        durationCol.setPrefWidth(80);

        TableColumn<Booking, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(
                String.format("RM %.2f", data.getValue().getTotalAmount())));
        amountCol.setPrefWidth(100);

        TableColumn<Booking, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        statusCol.setPrefWidth(120);

        TableColumn<Booking, Void> actionCol = new TableColumn<>("Action");
        actionCol.setPrefWidth(100);
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button cancelBtn = new Button("Cancel");
            {
                cancelBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
                                 "-fx-cursor: hand; -fx-font-weight: bold; " +
                                 "-fx-background-radius: 5;");
                cancelBtn.setOnAction(e -> {
                    Booking booking = getTableView().getItems().get(getIndex());
                    
                    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmAlert.setTitle("Cancel Booking");
                    confirmAlert.setHeaderText("Cancel Booking?");
                    
                    String refundMsg = booking.canRefund() ? 
                        "You will receive a FULL REFUND (cancelled 2+ hours before)." :
                        "NO REFUND will be issued (less than 2 hours to booking time).";
                    
                    confirmAlert.setContentText("Booking ID: " + booking.getBookingID() + 
                                              "\n\n" + refundMsg + 
                                              "\n\nDo you want to continue?");
                    
                    if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                        booking.cancelBooking();
                        
                        if (booking.getPaymentStatus().equals("REFUNDED")) {
                            String paymentID = "P" + booking.getBookingID();
                            Payment refund = new Payment(paymentID, booking, booking.getTotalAmount());
                            refund.refundPayment();
                        }
                        
                        bookings.remove(booking);
                        DataManager.saveBookings(bookings);
                        DataManager.saveCourts(courts);
                        showMyBookingsScreen();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(cancelBtn);
                }
            }
        });

        table.getColumns().addAll(idCol, courtCol, dateCol, timeCol, 
                                 durationCol, amountCol, statusCol, actionCol);

        for (Booking booking : bookings) {
            if (booking.getUser().getUserID().equals(currentUser.getUserID())) {
                table.getItems().add(booking);
            }
        }

        if (table.getItems().isEmpty()) {
            VBox emptyBox = new VBox(20);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(60));
            
            Label emptyIcon = new Label("📭");
            emptyIcon.setFont(Font.font(64));
            
            Label emptyLabel = new Label("No bookings yet");
            emptyLabel.setFont(Font.font("Arial", 18));
            emptyLabel.setTextFill(Color.web("#a0aec0"));
            
            Button bookNowBtn = new Button("BOOK NOW");
            bookNowBtn.setStyle("-fx-background-color: #667eea; -fx-text-fill: white; " +
                              "-fx-font-weight: bold; -fx-min-width: 150px; " +
                              "-fx-min-height: 40px; -fx-background-radius: 20; -fx-cursor: hand;");
            bookNowBtn.setOnAction(e -> showSelectSportScreen());
            
            emptyBox.getChildren().addAll(emptyIcon, emptyLabel, bookNowBtn);
            contentBox.getChildren().add(emptyBox);
        } else {
            contentBox.getChildren().add(table);
        }

        root.setTop(topSection);
        root.setCenter(contentBox);

        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setScene(scene);
    }

    private void showAvailableCourtsScreen() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f7fa;");

        VBox topSection = createTopSection("AVAILABLE COURTS", "#4facfe", 
                                          () -> showUserDashboard());

        VBox contentBox = new VBox(20);
        contentBox.setPadding(new Insets(30));

        // Filter controls
        HBox filterBox = new HBox(15);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        filterBox.setPadding(new Insets(15));
        filterBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        Label filterLabel = new Label("Filter by Sport:");
        filterLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        ComboBox<String> filterCombo = new ComboBox<>();
        filterCombo.getItems().addAll("All Sports", "Badminton", "Futsal", 
                                     "Tennis", "Pickleball", "PingPong");
        filterCombo.setValue("All Sports");
        filterCombo.setPrefWidth(150);

        Label dateLabel = new Label("Check Date:");
        dateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        DatePicker checkDatePicker = new DatePicker(LocalDate.now());
        checkDatePicker.setPrefWidth(150);

        Label timeLabel = new Label("Check Time:");
        timeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        ComboBox<String> timeCombo = new ComboBox<>();
        for (int hour = 9; hour < 24; hour++) {
            timeCombo.getItems().add(String.format("%02d:00", hour));
        }
        timeCombo.getItems().add("00:00");
        timeCombo.getItems().add("01:00");
        timeCombo.setValue("09:00");
        timeCombo.setPrefWidth(100);

        Button checkButton = new Button("Check Availability");
        checkButton.setStyle("-fx-background-color: #4facfe; -fx-text-fill: white; " +
                           "-fx-font-weight: bold; -fx-cursor: hand; " +
                           "-fx-background-radius: 8; -fx-padding: 8 15;");

        filterBox.getChildren().addAll(filterLabel, filterCombo, dateLabel, 
                                       checkDatePicker, timeLabel, timeCombo, checkButton);

        TableView<Court> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        TableColumn<Court, String> idCol = new TableColumn<>("Court ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("courtID"));
        idCol.setPrefWidth(120);

        TableColumn<Court, String> nameCol = new TableColumn<>("Court Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("courtName"));
        nameCol.setPrefWidth(200);

        TableColumn<Court, String> typeCol = new TableColumn<>("Sport Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("courtType"));
        typeCol.setPrefWidth(120);

        TableColumn<Court, String> dayPriceCol = new TableColumn<>("Day Rate");
        dayPriceCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(
                String.format("RM %.2f/hr", data.getValue().getPriceDay())));
        dayPriceCol.setPrefWidth(100);

        TableColumn<Court, String> nightPriceCol = new TableColumn<>("Night Rate");
        nightPriceCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(
                String.format("RM %.2f/hr", data.getValue().getPriceNight())));
        nightPriceCol.setPrefWidth(100);

        TableColumn<Court, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(130);
        statusCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.equals("Available")) {
                        setStyle("-fx-text-fill: #48bb78; -fx-font-weight: bold;");
                    } else if (item.equals("Under Maintenance")) {
                        setStyle("-fx-text-fill: #f59e0b; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    }
                }
            }
        });

        // Real-time availability column
        TableColumn<Court, String> availCol = new TableColumn<>("Availability");
        availCol.setPrefWidth(150);
        availCol.setCellValueFactory(data -> {
            Court court = data.getValue();
            String date = checkDatePicker.getValue().toString();
            String time = timeCombo.getValue();
            
            if (court.getStatus().equals("Under Maintenance")) {
                return new javafx.beans.property.SimpleStringProperty("Under Maintenance");
            }
            
            boolean hasConflict = BookingValidator.hasConflict(bookings, court, date, time, 1);
            return new javafx.beans.property.SimpleStringProperty(
                hasConflict ? "Booked at " + time : "Available at " + time);
        });
        availCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.startsWith("Available")) {
                        setStyle("-fx-text-fill: #48bb78; -fx-font-weight: bold;");
                    } else if (item.startsWith("Booked")) {
                        setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #f59e0b; -fx-font-weight: bold;");
                    }
                }
            }
        });

        table.getColumns().addAll(idCol, nameCol, typeCol, dayPriceCol, 
                                  nightPriceCol, statusCol, availCol);

        // Initial load
        table.getItems().addAll(courts);

        // Filter and refresh functionality
        Runnable refreshTable = () -> {
            table.getItems().clear();
            String filter = filterCombo.getValue();
            for (Court court : courts) {
                if (filter.equals("All Sports") || 
                    court.getCourtType().equalsIgnoreCase(filter)) {
                    table.getItems().add(court);
                }
            }
            table.refresh(); // Force refresh to update availability column
        };

        filterCombo.setOnAction(e -> refreshTable.run());
        checkButton.setOnAction(e -> refreshTable.run());
        checkDatePicker.setOnAction(e -> refreshTable.run());
        timeCombo.setOnAction(e -> refreshTable.run());

        contentBox.getChildren().addAll(filterBox, table);

        root.setTop(topSection);
        root.setCenter(contentBox);

        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setScene(scene);
    }
    
    // ADMIN DASHBOARD AND MANAGEMENT FUNCTIONS
// Add these methods to SportCourtApp class

    private void showAdminDashboard() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f7fa;");

        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(20));
        topBar.setStyle("-fx-background: linear-gradient(to right, #fa709a, #fee140); " +
                       "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");
        topBar.setAlignment(Pos.CENTER_LEFT);

        Label logoLabel = new Label("🎾 COURTIFY ADMIN");
        logoLabel.setTextFill(Color.BLACK);
        logoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutButton = new Button("LOGOUT");
        logoutButton.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: black; " +
                            "-fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 20; " +
                            "-fx-cursor: hand; -fx-border-color: black; -fx-border-radius: 20;");
        logoutButton.setOnAction(e -> showLoginScreen());

        topBar.getChildren().addAll(logoLabel, spacer, logoutButton);

        VBox centerBox = new VBox(40);
        centerBox.setPadding(new Insets(50));
        centerBox.setAlignment(Pos.CENTER);

        Label dashboardTitle = new Label("ADMIN CONTROL PANEL");
        dashboardTitle.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        dashboardTitle.setTextFill(Color.web("#2d3748"));

        GridPane cardsGrid = new GridPane();
        cardsGrid.setHgap(25);
        cardsGrid.setVgap(25);
        cardsGrid.setAlignment(Pos.CENTER);

        VBox manageCourtsCard = createDashboardCard("🏟", "MANAGE COURTS", 
            "Add/Remove/Maintain courts", "#fa709a", e -> showManageCourtsScreen());
        VBox viewUsersCard = createDashboardCard("👥", "VIEW USERS", 
            "Manage user accounts", "#667eea", e -> showViewUsersScreen());
        VBox allBookingsCard = createDashboardCard("📊", "ALL BOOKINGS", 
            "View all bookings", "#4facfe", e -> showAllBookingsScreen());
        VBox revenueCard = createDashboardCard("💰", "REVENUE", 
            "View monthly income", "#48bb78", e -> showRevenueScreen());

        cardsGrid.add(manageCourtsCard, 0, 0);
        cardsGrid.add(viewUsersCard, 1, 0);
        cardsGrid.add(allBookingsCard, 0, 1);
        cardsGrid.add(revenueCard, 1, 1);

        centerBox.getChildren().addAll(dashboardTitle, cardsGrid);

        root.setTop(topBar);
        root.setCenter(centerBox);

        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setScene(scene);
    }

    private void showManageCourtsScreen() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f7fa;");

        VBox topSection = createTopSection("MANAGE COURTS", "#fa709a", 
                                          () -> showAdminDashboard());

        VBox contentBox = new VBox(20);
        contentBox.setPadding(new Insets(30));

        // Add Court Form
        HBox addCourtBox = new HBox(15);
        addCourtBox.setAlignment(Pos.CENTER_LEFT);
        addCourtBox.setPadding(new Insets(15));
        addCourtBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        TextField courtNameField = new TextField();
        courtNameField.setPromptText("Court Name");
        courtNameField.setPrefWidth(200);
        courtNameField.setStyle("-fx-padding: 8;");

        ComboBox<String> courtTypeCombo = new ComboBox<>();
        courtTypeCombo.getItems().addAll("Badminton", "Futsal", "Tennis", 
                                        "Pickleball", "PingPong");
        courtTypeCombo.setPromptText("Court Type");
        courtTypeCombo.setPrefWidth(150);

        Button addButton = new Button("+ ADD COURT");
        addButton.setStyle("-fx-background-color: #48bb78; -fx-text-fill: white; " +
                         "-fx-font-weight: bold; -fx-cursor: hand; " +
                         "-fx-background-radius: 8; -fx-padding: 8 20;");

        Label messageLabel = new Label();
        messageLabel.setTextFill(Color.web("#48bb78"));
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        addButton.setOnAction(e -> {
            if (courtNameField.getText().isEmpty() || courtTypeCombo.getValue() == null) {
                messageLabel.setText("⚠ Please fill all fields!");
                messageLabel.setTextFill(Color.web("#e74c3c"));
                return;
            }

            String courtID = generateCourtID(courtTypeCombo.getValue());
            Court newCourt = new Court(courtID, courtNameField.getText(), 
                                     courtTypeCombo.getValue(), "Available");
            courts.add(newCourt);
            DataManager.saveCourts(courts);

            messageLabel.setText("✓ Court added successfully!");
            messageLabel.setTextFill(Color.web("#48bb78"));
            
            courtNameField.clear();
            courtTypeCombo.setValue(null);
            
            showManageCourtsScreen();
        });

        addCourtBox.getChildren().addAll(courtNameField, courtTypeCombo, 
                                        addButton, messageLabel);

        // Courts Table
        TableView<Court> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        TableColumn<Court, String> idCol = new TableColumn<>("Court ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("courtID"));
        idCol.setPrefWidth(120);

        TableColumn<Court, String> nameCol = new TableColumn<>("Court Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("courtName"));
        nameCol.setPrefWidth(220);

        TableColumn<Court, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("courtType"));
        typeCol.setPrefWidth(150);

        TableColumn<Court, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(150);

        TableColumn<Court, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setPrefWidth(270);
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button maintenanceBtn = new Button("Maintenance");
            private final Button deleteBtn = new Button("Delete");
            private final HBox buttons = new HBox(10, maintenanceBtn, deleteBtn);
            
            {
                maintenanceBtn.setStyle("-fx-background-color: #f59e0b; -fx-text-fill: white; " +
                                      "-fx-cursor: hand; -fx-font-weight: bold; " +
                                      "-fx-background-radius: 5; -fx-padding: 5 15;");
                deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
                                 "-fx-cursor: hand; -fx-font-weight: bold; " +
                                 "-fx-background-radius: 5; -fx-padding: 5 15;");
                
                maintenanceBtn.setOnAction(e -> {
                    Court court = getTableView().getItems().get(getIndex());
                    if (court.getStatus().equals("Under Maintenance")) {
                        court.updateStatus("Available");
                    } else {
                        court.updateStatus("Under Maintenance");
                    }
                    DataManager.saveCourts(courts);
                    showManageCourtsScreen();
                });

                deleteBtn.setOnAction(e -> {
                    Court court = getTableView().getItems().get(getIndex());
                    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmAlert.setTitle("Delete Court");
                    confirmAlert.setContentText("Are you sure you want to delete " + 
                                              court.getCourtName() + "?");
                    
                    if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                        courts.remove(court);
                        DataManager.saveCourts(courts);
                        showManageCourtsScreen();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Court court = getTableView().getItems().get(getIndex());
                    if (court.getStatus().equals("Under Maintenance")) {
                        maintenanceBtn.setText("Set Available");
                        maintenanceBtn.setStyle("-fx-background-color: #48bb78; -fx-text-fill: white; " +
                                              "-fx-cursor: hand; -fx-font-weight: bold; " +
                                              "-fx-background-radius: 5; -fx-padding: 5 15;");
                    } else {
                        maintenanceBtn.setText("Maintenance");
                        maintenanceBtn.setStyle("-fx-background-color: #f59e0b; -fx-text-fill: white; " +
                                              "-fx-cursor: hand; -fx-font-weight: bold; " +
                                              "-fx-background-radius: 5; -fx-padding: 5 15;");
                    }
                    setGraphic(buttons);
                }
            }
        });

        table.getColumns().addAll(idCol, nameCol, typeCol, statusCol, actionCol);
        table.getItems().addAll(courts);

        contentBox.getChildren().addAll(addCourtBox, table);

        root.setTop(topSection);
        root.setCenter(contentBox);

        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setScene(scene);
    }

    private void showViewUsersScreen() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f7fa;");

        VBox topSection = createTopSection("USER MANAGEMENT", "#667eea", 
                                          () -> showAdminDashboard());

        VBox contentBox = new VBox(20);
        contentBox.setPadding(new Insets(30));

        TableView<User> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        TableColumn<User, String> idCol = new TableColumn<>("User ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
        idCol.setPrefWidth(100);

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(180);

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setPrefWidth(220);

        TableColumn<User, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phoneCol.setPrefWidth(130);

        TableColumn<User, String> statusCol = new TableColumn<>("Status");
        statusCol.setPrefWidth(100);
        statusCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().isSuspended() ? "Suspended" : "Active"));
        statusCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.equals("Active")) {
                        setStyle("-fx-text-fill: #48bb78; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    }
                }
            }
        });

        TableColumn<User, Void> actionCol = new TableColumn<>("Action");
        actionCol.setPrefWidth(180);
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button suspendBtn = new Button("Suspend");
            {
                suspendBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
                                  "-fx-cursor: hand; -fx-font-weight: bold; " +
                                  "-fx-background-radius: 5; -fx-padding: 5 20;");
                suspendBtn.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    user.setSuspended(!user.isSuspended());
                    DataManager.saveUsers(users);
                    showViewUsersScreen();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    User user = getTableView().getItems().get(getIndex());
                    if (user.isSuspended()) {
                        suspendBtn.setText("Activate");
                        suspendBtn.setStyle("-fx-background-color: #48bb78; -fx-text-fill: white; " +
                                          "-fx-cursor: hand; -fx-font-weight: bold; " +
                                          "-fx-background-radius: 5; -fx-padding: 5 20;");
                    } else {
                        suspendBtn.setText("Suspend");
                        suspendBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
                                          "-fx-cursor: hand; -fx-font-weight: bold; " +
                                          "-fx-background-radius: 5; -fx-padding: 5 20;");
                    }
                    setGraphic(suspendBtn);
                }
            }
        });

        table.getColumns().addAll(idCol, nameCol, emailCol, phoneCol, 
                                  statusCol, actionCol);
        table.getItems().addAll(users);

        Label statsLabel = new Label(String.format("Total Users: %d | Active: %d | Suspended: %d",
            users.size(),
            users.stream().filter(u -> !u.isSuspended()).count(),
            users.stream().filter(User::isSuspended).count()));
        statsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        statsLabel.setTextFill(Color.web("#4a5568"));
        statsLabel.setPadding(new Insets(10));

        contentBox.getChildren().addAll(statsLabel, table);

        root.setTop(topSection);
        root.setCenter(contentBox);

        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setScene(scene);
    }

    private void showAllBookingsScreen() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f7fa;");

        VBox topSection = createTopSection("ALL BOOKINGS", "#4facfe", 
                                          () -> showAdminDashboard());

        VBox contentBox = new VBox(20);
        contentBox.setPadding(new Insets(30));

        TableView<Booking> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        TableColumn<Booking, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("bookingID"));
        idCol.setPrefWidth(100);

        TableColumn<Booking, String> userCol = new TableColumn<>("User");
        userCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getUser().getName()));
        userCol.setPrefWidth(140);

        TableColumn<Booking, String> courtCol = new TableColumn<>("Court");
        courtCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getCourt().getCourtName()));
        courtCol.setPrefWidth(150);

        TableColumn<Booking, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setPrefWidth(110);

        TableColumn<Booking, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("timeSlot"));
        timeCol.setPrefWidth(80);

        TableColumn<Booking, String> durationCol = new TableColumn<>("Hours");
        durationCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getDuration() + "h"));
        durationCol.setPrefWidth(70);

        TableColumn<Booking, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(
                String.format("RM %.2f", data.getValue().getTotalAmount())));
        amountCol.setPrefWidth(100);

        TableColumn<Booking, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        statusCol.setPrefWidth(150);

        table.getColumns().addAll(idCol, userCol, courtCol, dateCol, timeCol, 
                                 durationCol, amountCol, statusCol);
        table.getItems().addAll(bookings);

        double totalRevenue = bookings.stream()
            .filter(b -> b.getPaymentStatus().equals("PAID"))
            .mapToDouble(Booking::getTotalAmount)
            .sum();

        Label statsLabel = new Label(String.format(
            "Total Bookings: %d | Paid: %d | Total Revenue: RM %.2f",
            bookings.size(),
            bookings.stream().filter(b -> b.getPaymentStatus().equals("PAID")).count(),
            totalRevenue));
        statsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        statsLabel.setTextFill(Color.web("#4a5568"));
        statsLabel.setPadding(new Insets(10));

        contentBox.getChildren().addAll(statsLabel, table);

        root.setTop(topSection);
        root.setCenter(contentBox);

        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setScene(scene);
    }

    private void showRevenueScreen() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f7fa;");

        VBox topSection = createTopSection("REVENUE REPORT", "#48bb78", 
                                          () -> showAdminDashboard());

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        VBox contentBox = new VBox(25);
        contentBox.setPadding(new Insets(40));
        contentBox.setAlignment(Pos.TOP_CENTER);

        // Calculate revenue by sport
        Map<String, Double> revenueByType = new HashMap<>();
        for (Booking booking : bookings) {
            if (booking.getPaymentStatus().equals("PAID")) {
                String type = booking.getCourt().getCourtType();
                revenueByType.put(type, 
                    revenueByType.getOrDefault(type, 0.0) + booking.getTotalAmount());
            }
        }

        // Create revenue cards
        GridPane revenueGrid = new GridPane();
        revenueGrid.setHgap(20);
        revenueGrid.setVgap(20);
        revenueGrid.setAlignment(Pos.CENTER);

        String[] sports = {"Badminton", "Futsal", "Tennis", "Pickleball", "PingPong"};
        String[] colors = {"#667eea", "#f093fb", "#4facfe", "#f5576c", "#48bb78"};
        String[] icons = {"🏸", "⚽", "🎾", "🏓", "🏓"};

        int col = 0, row = 0;
        for (int i = 0; i < sports.length; i++) {
            double revenue = revenueByType.getOrDefault(sports[i], 0.0);
            VBox card = createRevenueCard(icons[i], sports[i], revenue, colors[i]);
            revenueGrid.add(card, col, row);
            col++;
            if (col > 2) {
                col = 0;
                row++;
            }
        }

        double totalRevenue = revenueByType.values().stream()
            .mapToDouble(Double::doubleValue).sum();

        VBox totalCard = new VBox(15);
        totalCard.setPadding(new Insets(30));
        totalCard.setAlignment(Pos.CENTER);
        totalCard.setMaxWidth(600);
        totalCard.setStyle("-fx-background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); " +
                          "-fx-background-radius: 15; " +
                          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);");

        Label totalLabel = new Label("💰 TOTAL MONTHLY REVENUE");
        totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        totalLabel.setTextFill(Color.BLACK);

        Label amountLabel = new Label(String.format("RM %.2f", totalRevenue));
        amountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 42));
        amountLabel.setTextFill(Color.BLACK);

        totalCard.getChildren().addAll(totalLabel, amountLabel);

        contentBox.getChildren().addAll(totalCard, revenueGrid);

        scrollPane.setContent(contentBox);
        root.setTop(topSection);
        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setScene(scene);
    }

    private VBox createRevenueCard(String icon, String sport, double revenue, String color) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(25));
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        card.setPrefSize(200, 160);

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(36));

        Label sportLabel = new Label(sport);
        sportLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        sportLabel.setTextFill(Color.web(color));

        Label revenueLabel = new Label(String.format("RM %.2f", revenue));
        revenueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        revenueLabel.setTextFill(Color.web("#2d3748"));

        card.getChildren().addAll(iconLabel, sportLabel, revenueLabel);
        return card;
    }

    // HELPER METHODS

    private VBox createTopSection(String title, String color, Runnable backAction) {
        VBox topSection = new VBox(10);
        topSection.setPadding(new Insets(20));
        topSection.setStyle("-fx-background-color: " + color + "; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");

        Button backButton = new Button("← BACK");
        backButton.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: white; " +
                          "-fx-cursor: hand; -fx-font-weight: bold; -fx-border-color: white; " +
                          "-fx-border-radius: 15; -fx-background-radius: 15; " +
                          "-fx-padding: 8 20;");
        backButton.setOnAction(e -> backAction.run());

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        titleLabel.setTextFill(Color.WHITE);

        topSection.getChildren().addAll(backButton, titleLabel);
        return topSection;
    }

    private String generateCourtID(String type) {
        String prefix = "";
        switch (type.toLowerCase()) {
            case "badminton": prefix = "BAD"; break;
            case "futsal": prefix = "FUT"; break;
            case "tennis": prefix = "TEN"; break;
            case "pickleball": prefix = "PCK"; break;
            case "pingpong": prefix = "PNG"; break;
        }
        
        int count = (int) courts.stream()
            .filter(c -> c.getCourtType().equalsIgnoreCase(type))
            .count() + 1;
            
        return prefix + String.format("%02d", count);
    }

    private void showSuccessDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}