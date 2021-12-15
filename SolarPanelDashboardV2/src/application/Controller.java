package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.event.ActionListener;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

import javax.swing.Timer;

public class Controller {
	
	//FXML objects to inject into FXML file , literally all things on screen
	@FXML private ProgressBar tempbar;
	@FXML private ProgressBar pvampsbar;
	@FXML private ProgressBar pvvoltsbar;
	@FXML private ProgressBar lightbar;
	@FXML private ProgressBar battcurrentbar;
	@FXML private ProgressBar battvoltagebar;
	
	@FXML private Text temperature;
	@FXML private Text filelabel;
	@FXML private Text pvinputamps;
	@FXML private Text pvinputvolts;
	@FXML private Text irradiance;
	@FXML private Text battchargecurrent;
	@FXML private Text battvoltage;
	@FXML private Text datelabel;
	@FXML private Text timestamplabel;
	@FXML private Text boardidlabel;
	
	
	
	//variables needed on functionality , adjust values at David/Peter's discretion
	//all these values have been +5 and -5 to their max/min respectivley
	//this is done so for ones there is wiggle room and two , the progress bar wont be completely empty
	double mintemp = -15; //Temperature
	double maxtemp = 40;
	
	double minpvvolt = -5; //PV Input Volts
	double maxpvvolt = 105;
	
	double minpvamp = -10; //PV Input Amps
	double maxpvamp = 35;
	
	double minirradiance = -5; //Irradiance
	double maxirradiance = 1100;
	
	double minbattchargecurrent = -5; //Battery charging current
	double maxbattchargecurrent = 35;
	
	double minbattvolt = -5; //Battery voltage
	double maxbattvolt = 35;
		
	
	boolean listenerflag = false;
	
	private static DecimalFormat decformat = new DecimalFormat("#.##"); 
	//to format floats and doubles to 2 places
	
	private File selectedfile;
	//the file object itself
	
	private Timer readtimer;
	private ActionListener getFileData;
	

	public void chooseFileBtnClick(ActionEvent e)
	{
		
		getFilePath();
		//tempbarChange(40);
		
	}
	
	public void appInfoBtnClick(ActionEvent e)
	{
		try {
			Parent root = FXMLLoader.load(getClass().getResource("AppInfoStyle.fxml"));
			Scene scene = new Scene(root,600,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage appinfostage = new Stage();
			appinfostage.getIcons().add(new Image(Main.class.getResourceAsStream("panelimages/sunPillarLogo.png")));
			appinfostage.setScene(scene);
			appinfostage.setTitle("Solar Panel Dashboard V2");
			appinfostage.setResizable(false);
			appinfostage.show();
		} catch(Exception except) {
			except.printStackTrace();
		}
	}
	
	public void tempbarChange(double num)
	{
		temperature.setText(decformat.format(num) + " �C");
		//handles if the num is below or above my manually set range
		if(num < mintemp)
			tempbar.setProgress(0);
		else if(num > maxtemp)
			tempbar.setProgress(1);
		else
		{
		double result = (num - mintemp) / (maxtemp-mintemp); 
		//this beautiful formula right here takes care of converting the data 
		//to between 0 and 1
		
		tempbar.setProgress(result);
		
		
		}
	}
	
	public void pvInputAmpsChange(double num)
	{
		pvinputamps.setText(decformat.format(num) + " Amp");
		if(num < minpvamp)
			pvampsbar.setProgress(0);
		else if(num > maxpvamp)
			pvampsbar.setProgress(1);
		else
		{
			double result = (num - minpvamp) / (maxpvamp - minpvamp);
			
			pvampsbar.setProgress(result);
		}
	}
	
	public void pvInputVoltsChange(double num)
	{
		pvinputvolts.setText(decformat.format(num) + " V");
		if(num < minpvvolt)
			pvvoltsbar.setProgress(0);
		else if (num > maxpvvolt)
			pvvoltsbar.setProgress(1);
		else
		{
			double result = (num - minpvvolt) / (maxpvvolt - minpvvolt);
			
			pvvoltsbar.setProgress(result);
		}
	}
	
	public void irradianceChange(double num)
	{
		irradiance.setText(decformat.format(num) + " Lum");
		if(num < minirradiance)
			lightbar.setProgress(0);
		else if (num > maxirradiance)
			lightbar.setProgress(1);
		else
		{
			double result = (num - minirradiance) / (maxirradiance - minirradiance);
			
			lightbar.setProgress(result);
		}
	}
	
	public void batteryChargeCurrentChange(double num)
	{
		battchargecurrent.setText(decformat.format(num) + " Amp");
		if(num < minbattchargecurrent)
			battcurrentbar.setProgress(0);
		else if(num > maxbattchargecurrent)
			battcurrentbar.setProgress(1);
		else
		{
			double result = (num - minbattchargecurrent) / (maxbattchargecurrent - minbattchargecurrent);
			
			battcurrentbar.setProgress(result);
		}
	}
	
	public void batteryVoltageChange(double num)
	{
		battvoltage.setText(decformat.format(num) + " V");
		if(num < minbattvolt)
			battvoltagebar.setProgress(0);
		else if (num > maxbattvolt)
			battvoltagebar.setProgress(1);
		else
		{
			double result = (num - minbattvolt) / (maxbattvolt - minbattvolt);
			
			battvoltagebar.setProgress(result);
		}
	}
	
	public void getFilePath()
	{
		
		FileChooser fc = new FileChooser();
		selectedfile = fc.showOpenDialog(null);
		
		String path = selectedfile.getAbsolutePath();
		filelabel.setText(selectedfile.getName());
		System.out.println(path);
		int dotindex = path.indexOf(".");
		String filetype = path.substring(dotindex, dotindex + 4);
		System.out.println(filetype);
		
		
		if(filetype.equals(".csv"))
		{
			if(listenerflag)
			{
				readtimer.stop();
				readtimer = null;
			}
			
			System.out.println("yoinked the csv");
			
			readFile(path);
			
			getFileData = new ActionListener() {
				
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					readFile(path);
				}
			};
			
			readtimer = new Timer(5000,getFileData);
			listenerflag = true;
			readtimer.start();
			
			//TODO after here have to implement read file
		}
		else
		{
			System.out.println("no csv sadge");
			// display an Alert window and ask to select again
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No csv file selected");
			alert.setGraphic(new ImageView(this.getClass().getResource("panelimages/erroricon.png").toString()));
			alert.setHeaderText("ERROR, No valid file was selected");
			alert.setContentText("Please select a .csv file sent by the board "
					+ "\n to have the dashboard start reading the file");

			Optional<ButtonType> ok = alert.showAndWait();
			if (ok.get() == ButtonType.OK) {
				alert.close();
			}
		}
		
	}
	
	public void readFile(String path) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// save the file path
				String line = "";
				String csvSplitBy = ",";
				
								
				// try-with-resources to close the file automatically
				try (FileReader in = new FileReader(path);
						BufferedReader br = new BufferedReader(in);) {

					String[] rows = null;
					
					// Read only up to the last line
					// The data in the last line will be read and then move onto
					// the next step below
					while ((line = br.readLine()) != null) {

						rows = line.split(csvSplitBy);
						//with the rows because of how java splits element 1 is actually first element
						//rows[0] is empty , rows[1] is date and so on
					}
					
				
					
					System.out.println("Date: " + rows[1] + " Time: " + rows[2] + " Board Id: " + rows[11]);
					
					//this variance variable is only for testing purposes to make sure the UI works
					// if testing UI , just add it in after double parse
					double variance = Math.random();
					tempbarChange(Double.valueOf(rows[3]));
					pvInputAmpsChange(Double.valueOf(rows[5]));
					pvInputVoltsChange(Double.valueOf(rows[6]));
					irradianceChange(Double.valueOf(rows[4]));
					batteryChargeCurrentChange(Double.valueOf(rows[7]));
					batteryVoltageChange(Double.valueOf(rows[8]));
					
					datelabel.setText(String.valueOf(rows[1]));
					timestamplabel.setText(String.valueOf(rows[2]));
					boardidlabel.setText(String.valueOf(rows[11]));
					
				}
				
				catch (ArrayIndexOutOfBoundsException | IOException
						| NoSuchElementException | NullPointerException e) {
					e.printStackTrace();
					readtimer.stop(); // Stop reading the file over and over due to error
				}
			}
		});
	}	
}
