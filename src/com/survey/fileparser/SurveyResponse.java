package com.survey.fileparser;

import com.survey.config.Configuration;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

/**
 * This class is to parse survey response, find the total participants, percentage participants, average rating of each question
 * @author Cimi
 * @version 1.1
 * @since 12-05-2016
 */
public class SurveyResponse {

	private ArrayList<CSVRecord> surResList;
	private int totalEmp;
	private int totalPart;
	private float perPart;
	
	Configuration config;
	Logger logger;
	
	/**
	 * Constructor, loads logger
	 * @throws IOException
	 */
	public SurveyResponse() throws IOException {
		Configuration conf = new Configuration();
		conf.loadLogProperties();
		Logger log = Logger.getLogger(SurveyQuestion.class);
		this.config = conf;
		this.logger = log;
		
	}

	/**
	 * This method will parse the survey response file and set the list of submitted survey response
	 * @param fileName
	 * @throws IOException
	 */
	public void  parseSurveySubmittedResponse(String fileName) throws IOException {
		logger.debug("Inside SurveyResponse.parseSurveySubmittedResponse method");
		Configuration config = new Configuration();
		int subDateCol = Integer.parseInt(config.getProperty("SUBMITTED_DATE_COL"));
		FileReader reader = new FileReader(fileName);
		ArrayList<CSVRecord> resList= new ArrayList<CSVRecord>();
		CSVParser csvParser = new CSVParser(reader, CSVFormat.RFC4180);
		List<CSVRecord> csvRecords = csvParser.getRecords();
		int recIn;
		Iterator<CSVRecord> csvRecIterator = csvRecords.iterator();
		Iterator<String> csvRecItemIterator;
		String csvRecItem = "";
		boolean isSubmitted = false;
		while(csvRecIterator.hasNext()){
			CSVRecord csvRec = csvRecIterator.next();
			logger.debug("Survey Response records "+csvRec);
			csvRecItemIterator = csvRec.iterator();
			recIn = 0;
			isSubmitted = false;
			while(csvRecItemIterator.hasNext()){
				csvRecItem = csvRecItemIterator.next();
				logger.debug("Survey Response records item"+csvRecItem);
				recIn++;
				if(recIn==subDateCol){
					if (csvRecItem!=null && (!csvRecItem.equals(""))){
						isSubmitted = true;
						logger.debug("Is submitted response"+isSubmitted);
					}
				}
			}
			if(isSubmitted){
				resList.add(csvRec);
			}
		}
		logger.debug("submitted responses list"+resList);
		setSurResList(resList);
		if(csvParser!=null)
			csvParser.close();
	}


	/**
	 * This method counts the total number of employees from the survey response file
	 * @param fileName
	 * @throws IOException
	 */
	public void totalEmployees(String fileName) throws IOException {
		logger.debug("Inside SurveyResponse.totalEmployees method");
		FileReader reader = new FileReader(fileName);
		CSVParser csvParser = new CSVParser(reader, CSVFormat.RFC4180);
		List<CSVRecord> csvRecords = csvParser.getRecords();
		logger.debug("Total Employees"+csvRecords.size());
		setTotalEmp(csvRecords.size());
		if(csvParser!=null)
			csvParser.close();

	}

	/**
	 * This methods calculates the total participants of the survey from the survey response list
	 * @param resList
	 */
	public void totalParticipants(ArrayList<CSVRecord> resList) {
		logger.debug("Inside SurveyResponse.totalParticipants method");
		logger.debug("Total Participants"+resList.size());
		setTotalPart(resList.size());
	}

	/**
	 * This methods calculates the percentage of participants of the survey from the total participants and total employees in the survey response
	 * @param totalPart
	 * @param totalEmp
	 */
	public void percentParticipants(int totalPart,int totalEmp) {
		logger.debug("Inside SurveyResponse.percentParticipants method");
		float percent = ((float)totalPart/(float)totalEmp)*100;
		logger.debug("Percentage of Participants"+percent);
		setPerPart(percent);
	}

	/**
	 * This methods calculates the prints average rating of each survey question by the participants
	 * @param surRQuesList
	 * @param surResList
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public void printAvgRating(ArrayList<String> surRQuesList,ArrayList<CSVRecord> surResList) throws NumberFormatException, IOException {
		logger.debug("Inside SurveyResponse.printAvgRating method");
		Iterator<String> surQuesListIterator = surRQuesList.iterator();
		Iterator<CSVRecord> surResListIterator ;
		int totalPart = surResList.size();
		int ratStartCol = Integer.parseInt(config.getProperty("RATING_START_COL"));
		int j = ratStartCol-1;
		int totRat = 0;
		float avgRat = 0;
		CSVRecord surResListItVal;
		String surQuesListItVal;
		System.out.println("AVERAGE RATING OF EACH QUESTION");
		System.out.println("---------------------------------");
		if(totalPart>0){
			/* Iterating through each rating question */
			while(surQuesListIterator.hasNext()){
				surQuesListItVal = surQuesListIterator.next();
				logger.debug("Rating Questions "+surQuesListItVal);
				surResListIterator = surResList.iterator();
				totRat = 0;
				/* Iterating through ratings of each participant */
				while(surResListIterator.hasNext()){
					surResListItVal = surResListIterator.next();
					logger.debug("Rating Questions rate"+surResListItVal);
					/* Calculating the total rating for each question, rating response starts with 4th column, so j starts from 3 */
					if(surResListItVal.get(j)!=null && (!surResListItVal.get(j).equals(""))){
						totRat += Integer.parseInt(surResListItVal.get(j));
					}
				}
				j++;
				/* Calculating the total rating for each question */
				avgRat = (float)totRat/(float)totalPart;
				logger.debug("Avg Rating of Questions"+avgRat);
				/* Printing the Average with 2 decimal place format*/
				System.out.format(surQuesListItVal+" = %.2f",avgRat);
				System.out.println("");
			}
		}else{
			System.out.println("No one participated in the survey");
		}
	}

	/**
	 * @return the surResList
	 */
	public ArrayList<CSVRecord> getSurResList() {
		return surResList;
	}

	/**
	 * @param surResList the surResList to set
	 */
	public void setSurResList(ArrayList<CSVRecord> surResList) {
		this.surResList = surResList;
	}

	/**
	 * @return the totalEmp
	 */
	public int getTotalEmp() {
		return totalEmp;
	}

	/**
	 * @param totalEmp the totalEmp to set
	 */
	public void setTotalEmp(int totalEmp) {
		this.totalEmp = totalEmp;
	}

	/**
	 * @return the totalPart
	 */
	public int getTotalPart() {
		return totalPart;
	}

	/**
	 * @param totalPart the totalPart to set
	 */
	public void setTotalPart(int totalPart) {
		this.totalPart = totalPart;
	}

	/**
	 * @return the perPart
	 */
	public float getPerPart() {
		return perPart;
	}

	/**
	 * @param perPart the perPart to set
	 */
	public void setPerPart(float perPart) {
		this.perPart = perPart;
	}

}
