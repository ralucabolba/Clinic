package client;
import java.io.*;

import java.net.Socket;
import java.util.Date;
import java.time.ZoneId;
import java.util.List;

import javax.sound.sampled.LineEvent.Type;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import np.com.ngopal.control.AutoFillTextBox;

public class Window extends Application{

	private ObjectInputStream in;
	private ObjectOutputStream out;

	private Socket socket;

	private TextField usernameField;
	private PasswordField passwordField;
	private TextField usernameUserField;
	private PasswordField passwordUserField;
	private TextField namePatientField;
	private TextField cardNoPatientField;
	private TextField cnpPatientField;
	private TextField addressPatientField;

	private Button loginButton;
	private Button patientButton;
	private Button consultButton;
	private Button addPatientButton;
	private Button addUserButton;
	private Button deleteUserButton;
	private Button logoutAdmin;
	private Button addCButton;
	private Button checkAv;
	private Button deleteCButton;
	private Button checkin;
	private Button addConsByDoctorButton;

	private Stage stage;
	private Scene scene;

	private TableView patientTable;
	private TableView userTable;
	private TableView cTable;
	private TableView doctorTable;

	private TableColumn<List<String>, String> nameCol;
	private TableColumn<List<String>, String> cardNoCol;
	private TableColumn<List<String>, String> cnpCol;
	private TableColumn<List<String>, String> birthCol;
	private TableColumn<List<String>, String> addressCol;

	private TableColumn<List<String>, String> usernameCol;
	private TableColumn<List<String>, String> typeCol;

	private TableColumn<List<String>, String> dateCol;
	
	private TableColumn<List<String>, String> statusCol;
	private TableColumn<List<String>, String> resultCol;
	
	private DatePicker birthDate;

	private ComboBox<String> types;
	private ComboBox<String> dates;
	private ComboBox<Integer> comboConsult;
	
	private ObservableList cnpData;
	private ObservableList usernameData;
	
	private AutoFillTextBox cnps;
	private AutoFillTextBox usernames;

	private ClientMain main;
	
	public Window(ClientMain main){
		this.main = main;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.stage = primaryStage;

		loginButton = new Button("Log in");
		addPatientButton = new Button("Add patient");
		addUserButton = new Button("Add user");
		deleteUserButton = new Button("Delete user");
		logoutAdmin = new Button("Log out");
		patientButton = new Button();
		consultButton = new Button();
		addCButton = new Button("Add consultation");
		checkAv = new Button("Check availability");
		deleteCButton = new Button("Cancel consultation");
		checkin = new Button("Check in");
		addConsByDoctorButton = new Button("Schedule consultation");

		patientTable = new TableView();
		userTable = new TableView();
		cTable = new TableView();
		doctorTable = new TableView<>();
		
		nameCol = new TableColumn<List<String>, String>("Name");
		cardNoCol = new TableColumn<List<String>, String>("Identity card number");
		cnpCol = new TableColumn<List<String>, String>("Cnp");
		birthCol = new TableColumn<List<String>, String>("Birth date");
		addressCol = new TableColumn<List<String>, String>("Address");
		birthDate = new DatePicker();

		usernameCol = new TableColumn<List<String>, String>("Username");
		typeCol = new TableColumn<List<String>, String>("Type user");

		dateCol = new TableColumn<List<String>, String>("Date consultation");
		
		statusCol = new TableColumn<List<String>, String>("Status");
		resultCol = new TableColumn<List<String>, String>("Result");
		
		namePatientField = new TextField();
		cardNoPatientField = new TextField();
		cnpPatientField = new TextField();
		addressPatientField = new TextField();

		usernameUserField = new TextField();
		passwordUserField = new PasswordField();

		cnpData = FXCollections.observableArrayList();
		usernameData = FXCollections.observableArrayList();
		
		types = new ComboBox<>();
		dates = new ComboBox<>();
		comboConsult = new ComboBox<>();
		
		cnps = new AutoFillTextBox(cnpData);
		usernames = new AutoFillTextBox(usernameData);

		loginWindow();
		//adminWindow();

		stage.setTitle("Clinic");
	}

	public void loginWindow(){
		VBox vboxLog = new VBox(20);
		HBox hboxUsername = new HBox(10);
		HBox hboxPassword = new HBox(10);

		vboxLog.setAlignment(Pos.CENTER);
		hboxUsername.setAlignment(Pos.CENTER);
		hboxPassword.setAlignment(Pos.CENTER);

		Label usernameLabel = new Label("Username");
		Label passwordLabel = new Label("Password");

		usernameField = new TextField();
		passwordField = new PasswordField();

		hboxUsername.getChildren().addAll(usernameLabel, usernameField);
		hboxPassword.getChildren().addAll(passwordLabel, passwordField);


		vboxLog.getChildren().addAll(hboxUsername, hboxPassword, loginButton);

		String image = Window.class.getResource("blue.jpg").toExternalForm();
		vboxLog.setStyle("-fx-background-image: url('" + image + "'); -fx-background-position: center center;");

		Scene loginScene = new Scene(vboxLog, 480, 360);

		setScene(loginScene);
	}

	public void secretaryChoiceWindow(){
		VBox vbox = new VBox(100);
		HBox hbox = new HBox(50);

		patientButton.setTooltip(new Tooltip("patient information"));
		patientButton.setPrefSize(128,  128);

		consultButton.setTooltip(new Tooltip("Consultation information"));
		consultButton.setPrefSize(128,  128);

		VBox vboxp = new VBox(10);
		vboxp.getChildren().addAll(patientButton, new Label("Patient information"));

		VBox vboxc = new VBox(10);
		vboxc.getChildren().addAll(consultButton, new Label("Consultation information"));


		hbox.getChildren().addAll(vboxp, vboxc);
		vbox.getChildren().addAll(new Label("Choose one of the following:"), hbox);

		hbox.setAlignment(Pos.CENTER);
		vbox.setAlignment(Pos.CENTER);
		vboxp.setAlignment(Pos.CENTER);
		vboxc.setAlignment(Pos.CENTER);

		String image = Window.class.getResource("blue.jpg").toExternalForm();
		vbox.setStyle("-fx-background-image: url('" + image + "'); -fx-background-position: center center;");

		image = Window.class.getResource("person.png").toExternalForm();
		patientButton.setStyle("-fx-background-image: url('" + image + "'); -fx-background-position: center center; -fx-background-color: transparent;");

		image = Window.class.getResource("consultation.png").toExternalForm();
		consultButton.setStyle("-fx-background-image: url('" + image + "'); -fx-background-position: center center; -fx-background-color: transparent;");


		Scene choiceScene = new Scene(vbox, 800, 600);

		setScene(choiceScene);

	}

	public void adminWindow(){
		VBox vboxu = new VBox(30);

		userTable.setEditable(true);
		userTable.setMinWidth(600);
		userTable.setMaxHeight(600);

		Label label = new Label("Users");

		TableColumn<List<String>, String> idCol = 
				new TableColumn<List<String>, String>("Id user");
		idCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(0));
			}
		});

		idCol.setMinWidth(50);


		usernameCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(1));
			}
		});

		usernameCol.setMinWidth(100);
		usernameCol.setCellFactory(TextFieldTableCell.forTableColumn());


		typeCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(2));
			}
		});

		typeCol.setMinWidth(100);
		//typeCol.setCellFactory(TextFieldTableCell.forTableColumn());		

		userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		userTable.getColumns().addAll(idCol, usernameCol, typeCol);

		HBox addUserBox = new HBox(20);


		usernameUserField.setPromptText("Username");
		passwordUserField.setPromptText("Password");

		types = new ComboBox<String>(FXCollections.observableArrayList("administrator", "doctor", "secretary"));
		types.setPromptText("Select type");

		addUserBox.getChildren().addAll(usernameUserField, passwordUserField, types, addUserButton);

		vboxu.getChildren().addAll(label, userTable, addUserBox, new Label("Select an user in order to delete him:"), deleteUserButton, logoutAdmin);

		vboxu.setAlignment(Pos.CENTER);
		vboxu.setPrefSize(300, 300);
		vboxu.setPadding(new Insets(50));

		String image = Window.class.getResource("blue.jpg").toExternalForm();
		vboxu.setStyle("-fx-background-image: url('" + image + "'); -fx-background-position: center center;");

		Scene adminScene = new Scene(vboxu, 800, 600);
		setScene(adminScene);
	}
	
	public void doctorWindow(){
		VBox vbox = new VBox(20);
		
		doctorTable.setEditable(true);
		doctorTable.setMinWidth(600);
		doctorTable.setMaxHeight(600);
		
		Label label = new Label("Past consultations");
		
		TableColumn<List<String>, String> idCol = new TableColumn<List<String>, String>("Id consultation");
		idCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(0));
			}
		});
		
		TableColumn<List<String>, String> namePCol = new TableColumn<List<String>, String>("Name patient");
		namePCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(1));
			}
		});
		
		TableColumn<List<String>, String> cnpPCol = new TableColumn<List<String>, String>("Cnp patient");
		cnpPCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(2));
			}
		});
		
		TableColumn<List<String>, String> dateCol = new TableColumn<List<String>, String>("Date");
		dateCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(3));
			}
		});
		
		
		statusCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(4));
			}
		});
		statusCol.setMinWidth(100);
		statusCol.setCellFactory(TextFieldTableCell.forTableColumn());

		
		resultCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(5));
			}
		});
		resultCol.setMinWidth(100);
		resultCol.setCellFactory(TextFieldTableCell.forTableColumn());
		
		doctorTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		doctorTable.getColumns().addAll(idCol, namePCol, cnpPCol, dateCol, statusCol, resultCol);

		HBox hbox = new HBox(20);
		
		hbox.getChildren().addAll(new Label("Select patient:"), cnps, dates, addConsByDoctorButton);
		
		vbox.getChildren().addAll(label, doctorTable, hbox);
		
		hbox.setAlignment(Pos.CENTER);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPrefSize(300, 300);
		vbox.setPadding(new Insets(50));

		String image = Window.class.getResource("blue.jpg").toExternalForm();
		vbox.setStyle("-fx-background-image: url('" + image + "'); -fx-background-position: center center;");


		Scene cScene = new Scene(vbox, 800, 600);
		cScene.getStylesheets().add(getClass().getResource("control.css").toExternalForm());
		setScene(cScene);
	}
	public void consultationInfoWindow(){
		VBox vbox = new VBox(20);
		
		cTable.setEditable(true);
		cTable.setMinWidth(600);
		cTable.setMaxHeight(600);
		
		Label label = new Label("Consultations");
		
		TableColumn<List<String>, String> idCol = new TableColumn<List<String>, String>("Id consultation");
		idCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(0));
			}
		});
		
		TableColumn<List<String>, String> namePCol = new TableColumn<List<String>, String>("Name patient");
		namePCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(1));
			}
		});
		
		TableColumn<List<String>, String> cnpPCol = new TableColumn<List<String>, String>("Cnp patient");
		cnpPCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(2));
			}
		});
		
		TableColumn<List<String>, String> doctorCol = new TableColumn<List<String>, String>("Username doctor");
		doctorCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(3));
			}
		});
		
		dateCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(4));
			}
		});
		
		TableColumn<List<String>, String> statusCol = new TableColumn<List<String>, String>("Status");
		statusCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(5));
			}
		});
		
		TableColumn<List<String>, String> resultCol = new TableColumn<List<String>, String>("Result");
		resultCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(6));
			}
		});

		dateCol.setMinWidth(100);
		dateCol.setCellFactory(TextFieldTableCell.forTableColumn());
		
		cTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		cTable.getColumns().addAll(idCol, namePCol, cnpPCol, doctorCol, dateCol, statusCol, resultCol);

		HBox hbox = new HBox(20);
		
		hbox.getChildren().addAll(new Label("Select patient:"), cnps, new Label("Select doctor:"), usernames, checkAv,
				dates, addCButton);
		
		comboConsult.setPromptText("Select consultation");
		
		HBox hboxcheck = new HBox(20);
		hboxcheck.getChildren().addAll(comboConsult, checkin);
		
		vbox.getChildren().addAll(label, cTable, hbox, new Label("Select a consultation in order to cancel it:"), deleteCButton, hboxcheck);
		
		hbox.setAlignment(Pos.CENTER);
		hboxcheck.setAlignment(Pos.CENTER);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPrefSize(300, 300);
		vbox.setPadding(new Insets(50));

		String image = Window.class.getResource("blue.jpg").toExternalForm();
		vbox.setStyle("-fx-background-image: url('" + image + "'); -fx-background-position: center center;");


		Scene cScene = new Scene(vbox, 800, 600);
		cScene.getStylesheets().add(getClass().getResource("control.css").toExternalForm());
		setScene(cScene);
	}
	
	public void patientInfoWindow(){
		VBox vboxp = new VBox(50);

		patientTable.setEditable(true);
		patientTable.setMinWidth(600);
		patientTable.setMaxHeight(600);

		Label label = new Label("Patients");

		TableColumn<List<String>, String> idCol = new TableColumn<List<String>, String>("Id patient");
		idCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(0));
			}
		});

		idCol.setMinWidth(50);


		nameCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(1));
			}
		});

		nameCol.setMinWidth(100);
		nameCol.setCellFactory(TextFieldTableCell.forTableColumn());


		cardNoCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(2));
			}
		});

		cardNoCol.setMinWidth(100);
		cardNoCol.setCellFactory(TextFieldTableCell.forTableColumn());		

		cnpCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(3));
			}
		});

		cnpCol.setMinWidth(100);
		cnpCol.setCellFactory(TextFieldTableCell.forTableColumn());	

		birthCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(4));
			}
		});

		birthCol.setMinWidth(100);
		birthCol.setCellFactory(TextFieldTableCell.forTableColumn());	


		addressCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(5));
			}
		});

		addressCol.setMinWidth(100);
		addressCol.setCellFactory(TextFieldTableCell.forTableColumn());	

		patientTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		patientTable.getColumns().addAll(idCol, nameCol, cardNoCol, cnpCol, birthCol, addressCol);

		HBox addpatientBox = new HBox(20);


		namePatientField.setPromptText("Name");
		cardNoPatientField.setPromptText("Identity card number");
		cnpPatientField.setPromptText("Personal numerical code");
		addressPatientField.setPromptText("Address");


		addpatientBox.getChildren().addAll(namePatientField, cardNoPatientField, cnpPatientField, birthDate, addressPatientField, addPatientButton);

		vboxp.getChildren().addAll(label, patientTable, addpatientBox);

		vboxp.setAlignment(Pos.CENTER);
		vboxp.setPrefSize(300, 300);
		vboxp.setPadding(new Insets(50));

		String image = Window.class.getResource("blue.jpg").toExternalForm();
		vboxp.setStyle("-fx-background-image: url('" + image + "'); -fx-background-position: center center;");


		Scene patientScene = new Scene(vboxp, 800, 600);

		setScene(patientScene);
	}

	public static void main(String[] args){
		launch(args);
	}

	/********************Setters and getters**************************/
	public void setScene(Scene newScene){
		scene = newScene;
		stage.setScene(scene);
		stage.show();
	}

	public String getUsername(){
		return usernameField.getText();
	}
	public String getPassword(){
		return passwordField.getText();
	}

	public void setUsersTable(List<List<String>> table){
		ObservableList<List<String>> data = FXCollections.observableArrayList(table);
		userTable.setItems(data);
	}
	
	public void setPatientsTable(List<List<String>> table){
		ObservableList<List<String>> data = FXCollections.observableArrayList(table);
		patientTable.setItems(data);
	}
	
	public void setConsultationsTable(List<List<String>> table){
		ObservableList<List<String>> data = FXCollections.observableArrayList(table);
		cTable.setItems(data);
	}
	
	public void setDoctorTable(List<List<String>> table){
		ObservableList<List<String>> data = FXCollections.observableArrayList(table);
		doctorTable.setItems(data);
	}
	
	public void setCnpData(List<String> cnps){
		this.cnpData.setAll(cnps);
	}
	
	public String getCnp(){
		return cnps.getText();
	}
	
	public String getDoctorUsername(){
		return usernames.getText();
	}
	
	public String getSelectedDate(){
		return dates.getValue();
	}
	
	public void setUsernameData(List<String> usernames){
		this.usernameData.setAll(usernames);
	}
	
	public Integer getSelectedIdConsultation(){
		return comboConsult.getValue();
	}
	
	public void setComboConsultation(List<Integer> list){
		ObservableList<Integer> olist = FXCollections.observableArrayList(list);
		comboConsult.setItems(olist);
	}
	
	public void setAvailableDate(List<String> list){
		ObservableList<String> olist = FXCollections.observableArrayList(list);
		dates.setItems(olist);
	}
	
	public String getUsernameUserField() {
		return usernameUserField.getText();
	}

	public String getPasswordUserField() {
		return passwordUserField.getText();
	}

	public String getTypes() {
		return types.getValue();
	}
	
	public List<String> getSelectedUser(){
		return (List<String>) this.userTable.getSelectionModel().getSelectedItem(); 
	}
	
	public List<String> getSelectedPatient(){
		return (List<String>) this.patientTable.getSelectionModel().getSelectedItem(); 
	}
	
	public List<String> getSelectedConsultation(){
		return (List<String>) this.cTable.getSelectionModel().getSelectedItem(); 
	}
	
	public List<String> getSelectedPastConsultation(){
		return (List<String>) this.doctorTable.getSelectionModel().getSelectedItem(); 
	}

	public void clearTables(){
		userTable.getColumns().clear();
		patientTable.getColumns().clear();
	}
	
	public String getNamePatientField(){
		return namePatientField.getText();
	}

	public String getCardNoPatientField(){
		return cardNoPatientField.getText();
	}
	public String getCnpPatientField(){
		return cnpPatientField.getText();
	}
	public Date getBirthDatePatientField(){
		if(birthDate.getValue() == null){
			return null;
		}
		Date date =  Date.from(birthDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
		return date;
	}
	public String getAddressPatientField(){
		return addressPatientField.getText();
	}
	/*******************************Message methods****************************/
	public void ErrorMessage(String title, String header, String content){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);

		alert.showAndWait();
	}

	public void SuccesMessage(String title, String header, String content){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);

		alert.showAndWait();
	}

	
	
	/**********************Listeners****************************/
	public void addListenerLoginButton(EventHandler<ActionEvent> act){
		loginButton.setOnAction(act);
	}
	public void addListenerAddUserButton(EventHandler<ActionEvent> act){
		addUserButton.setOnAction(act);
	}
	public void addListenerAddPatientButton(EventHandler<ActionEvent> act){
		addPatientButton.setOnAction(act);
	}
	public void addListenerAddConsultationButton(EventHandler<ActionEvent> act){
		addCButton.setOnAction(act);
	}
	public void addListenerDeleteUserButton(EventHandler<ActionEvent> act){
		deleteUserButton.setOnAction(act);
	}
	public void addListenerDeleteConsultationButton(EventHandler<ActionEvent> act){
		deleteCButton.setOnAction(act);
	}
	public void addListenerUsernameUser(EventHandler<CellEditEvent<List<String>, String>> act){
		usernameCol.setOnEditCommit(act);
	}
	
	public void addListenerLogout(EventHandler<ActionEvent> act){
		logoutAdmin.setOnAction(act);
	}
	
	public void addListenerPatientButton(EventHandler<ActionEvent> act){
		patientButton.setOnAction(act);
	}
	
	public void addListenerConsultButton(EventHandler<ActionEvent> act){
		consultButton.setOnAction(act);
	}
	
	public void addListenerNamePatient(EventHandler<CellEditEvent<List<String>, String>> act){
		nameCol.setOnEditCommit(act);
	}
	public void addListenerCardNoPatient(EventHandler<CellEditEvent<List<String>, String>> act){
		cardNoCol.setOnEditCommit(act);
	}
	public void addListenerCnpPatient(EventHandler<CellEditEvent<List<String>, String>> act){
		cnpCol.setOnEditCommit(act);
	}
	public void addListenerBirthDatePatient(EventHandler<CellEditEvent<List<String>, String>> act){
		birthCol.setOnEditCommit(act);
	}
	public void addListenerAddressPatient(EventHandler<CellEditEvent<List<String>, String>> act){
		addressCol.setOnEditCommit(act);
	}
	
	public void addListenerCheckAvailability(EventHandler<ActionEvent> act){
		checkAv.setOnAction(act);
	}
	
	public void addListenerCheckin(EventHandler<ActionEvent> act){
		checkin.setOnAction(act);
	}
	
	public void addListenerAddConsByDoctor(EventHandler<ActionEvent> act){
		addConsByDoctorButton.setOnAction(act);
	}
	
	public void addListenerStatus(EventHandler<CellEditEvent<List<String>, String>> act){
		statusCol.setOnEditCommit(act);
	}
	
	public void addListenerResult(EventHandler<CellEditEvent<List<String>, String>> act){
		resultCol.setOnEditCommit(act);
	}
	
}
