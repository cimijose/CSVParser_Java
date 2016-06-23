package com.survey.fileparser;

import com.survey.config.Configuration;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.commons.csv.CSVRecord;

/**
 * This class is to parse the survey questions and survey response which are in csv format
 * This class will give the average rating for each each rating question, 
 * percentage of participation and total participation
 * 				
 * @author Cimi
 * @version version 1.1
 * @since 13-05-2016
 */
public class SurveyParser {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main (String[] args){

		String rootPath = System.getProperty("user.dir");
		Scanner scannerFile = null;
		String inputFileExt = "";
		String inputFile = "";

		ArrayList<String> surRQuesList = new ArrayList<String>();
		ArrayList<CSVRecord> surResList = new ArrayList<CSVRecord>();

		int totalPart = 0;
		int totalEmp = 0;
		float perPart = 0;
		Configuration config = new Configuration();
		Logger logger = Logger.getLogger(SurveyParser.class);
		String dataFilePath = "";
		String resFileSuf = "";
		String resFileExt = "";

		try {
			config.loadLogProperties();
			dataFilePath = config.getProperty("DATA_FOLDER");
			resFileSuf = config.getProperty("SURVEY_RESPONSE_FILE_SUFFIX");
			resFileExt = config.getProperty("SURVEY_RESPONSE_FILE_EXT");
			scannerFile = new Scanner(System.in);
			
			/*Read the file name from console*/
			System.out.print("Enter the survey file name: ");
			inputFileExt = scannerFile.nextLine();
			logger.debug("Entered the survey file name. Filename is "+inputFileExt);
			/*Getting the survey filename without extension */
			inputFile = FilenameUtils.getBaseName(inputFileExt);
			logger.debug("Filename base name is "+inputFile);

			/*Setting the filename of question and response files with path*/
			String surveyQuesFile = rootPath+dataFilePath+inputFileExt;
			String surveyResFile = rootPath+dataFilePath+inputFile+resFileSuf+resFileExt;
			logger.debug("Survey Question file with path "+surveyQuesFile);
			logger.debug("Survey Response file with path "+surveyResFile);

			/*Creating instance for surveyQuestion and surveyResponse classes*/
			SurveyQuestion surQues = new SurveyQuestion();
			SurveyResponse surRes = new SurveyResponse();

			/* Parsing the survey question csv file and getting the list of rating questions*/
			surQues.parseSurveyRatQues(surveyQuesFile);
			surRQuesList = surQues.getSurRQuesList();
			logger.debug("Survey Response Rating questions list "+surRQuesList);

			/* Parsing the response csv file and getting list of participants with responses*/
			surRes.parseSurveySubmittedResponse(surveyResFile);
			surResList = surRes.getSurResList();
			logger.debug("Survey Response Rating questions list "+surResList);

			/* Finding total employees*/
			surRes.totalEmployees(surveyResFile);
			totalEmp = surRes.getTotalEmp();
			logger.debug("Total Employees "+totalEmp);

			/* Calculating the total actual participants*/
			surRes.totalParticipants(surResList);
			totalPart = surRes.getTotalPart();
			logger.debug("Total Participants "+totalPart);

			/* Calculating the percentage of participation*/
			surRes.percentParticipants(totalPart, totalEmp);
			perPart = surRes.getPerPart();
			logger.debug("Percentage Participants "+perPart);

			/* Calculating the average rate of each survey rating question by iterating through each questing and corresponding response by each participant */
			surRes.printAvgRating(surRQuesList,surResList);

			/* Printing the percentage participation (with 2 decimal point format) and total participation*/

			System.out.println("");
			System.out.format("Percentage of participation is %.2f%%",perPart);
			System.out.println("");
			System.out.println("Total Participation is "+totalPart);

		} catch (FileNotFoundException e1) {
			System.out.println("Invalid file name");
			logger.error("Invalid file name \n"+e1);
		} catch (IOException e2) {
			logger.error("Input Output Exception\n"+e2);
		}finally{
			if(scannerFile!=null)
				scannerFile.close();
		}

	}


}
