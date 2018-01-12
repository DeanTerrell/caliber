package com.revature.caliber.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.revature.caliber.beans.QCStatus;
import com.revature.caliber.services.ReportingService;

/**
 * Exclusively used to generate data for charts
 * 
 * @author Patrick Walsh
 * 
 *         Team !Uncharted
 * @author Pier Yos
 * @author Hossain Yahya
 * @author Yanilda Peralta
 * @author Igor Gluskin
 * @author Ateeb Khawaja
 * 
 *
 */
@RestController
@PreAuthorize("isAuthenticated()")
@CrossOrigin(origins = "http://ec2-54-163-132-124.compute-1.amazonaws.com")
public class ReportingController {

	private static final Logger log = Logger.getLogger(ReportingController.class);
	private ReportingService reportingService;

	@Autowired
	public void setReportingService(ReportingService reportingService) {
		this.reportingService = reportingService;
	}

	/**************************************************************************
	 * Batch Average Comparison
	 *************************************************************************
	 */

	@RequestMapping(value = "/all/reports/compare/skill/{skill}/training/{training}/date/{startDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING','PANEL')")
	public ResponseEntity<Double> getBatchComparisonAvg(@PathVariable String skill, @PathVariable String training,
			@PathVariable Date startDate) {
		log.info("http://localhost:8080/all/reports/compare/skill/"+skill+"/training/"+training+"/date/"+startDate);
		log.info("YAYAYAYAYAYAYYAYAYAYAYAYAYAYAYAYATEZXRDCYTFUVGBJHLNKJSFSD " + startDate + skill + training);
		log.info(" getBatchComparisonAvg ===> " + reportingService.getBatchComparisonAvg(skill, training, startDate));
		Double result = reportingService.getBatchComparisonAvg(skill, training, startDate);
		if(!result.isNaN()){
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
		else{
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
		}
	}

	/*
	 *******************************************************
	 * Doughnut / Pie Charts
	 *******************************************************
	 */
	@RequestMapping(value = "/all/reports/batch/{batchId}/week/{weekId}/pie", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING', 'PANEL')")
	public ResponseEntity<Map<QCStatus, Integer>> getBatchWeekPieChart(@PathVariable Integer batchId,
			@PathVariable Integer weekId) {
		log.info("getBatchWeekPieChart   ===>   /all/reports/batch/{batchId}/week/{weekId}/pie");
		
		return new ResponseEntity<>(reportingService.getBatchWeekPieChart(batchId, weekId), HttpStatus.OK);
	}

	@RequestMapping(value = "/all/reports/batch/{batchId}/pie", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING', 'PANEL')")
	public ResponseEntity<Map<QCStatus, Integer>> getPieChartCurrentWeekQCStatus(@PathVariable Integer batchId) {
		log.info("getPieChartCurrentWeekQCStatus ===> /all/reports/batch/{batchId}/pie");
		Map<QCStatus, Integer> results = reportingService.pieChartCurrentWeekQCStatus(batchId);
		if(results.size() > 0) {
			log.info(results);
			return new ResponseEntity<>(results, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(new HashMap<>(), HttpStatus.NOT_FOUND);
		}
	}

	/*
	 *******************************************************
	 * Stacked Bar Charts
	 *******************************************************
	 */
	@RequestMapping(value = "/all/reports/batch/week/stacked-bar-current-week", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'STAGING', 'PANEL')")
	public ResponseEntity<List<Object>> getAllBatchesCurrentWeekQCStackedBarChart() {
		log.info("getAllBatchesCurrentWeekQCStats   ===>   /all/reports/batch/week/stacked-bar-current-week");
		return new ResponseEntity<>(reportingService.getAllBatchesCurrentWeekQCStackedBarChart(), HttpStatus.OK);
	}

	/*
	 *******************************************************
	 * Bar Charts
	 *******************************************************
	 */
	@RequestMapping(value = "/all/reports/batch/{batchId}/week/{week}/bar-batch-week-avg", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING', 'PANEL')")
	public ResponseEntity<Map<String, Double[]>> getBatchWeekAvgBarChart(@PathVariable int batchId,
			@PathVariable int week) {
		log.info("getBatchWeekAvgBarChart   ===>   /all/reports/batch/{batchId}/week/{week}/bar-batch-week-avg");
		return new ResponseEntity<>(reportingService.getBatchWeekAvgBarChart(batchId, week), HttpStatus.OK);
	}

	@RequestMapping(value = "/all/reports/batch/{batchId}/week/{week}/bar-batch-weekly-sorted", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING', 'PANEL')")
	public ResponseEntity<Map<String, Double>> getBatchWeekSortedBarChart(@PathVariable int batchId,
			@PathVariable int week) {
		log.info("getBatchWeekAvgBarChart   ===>   /all/reports/batch/{batchId}/week/{week}/bar-batch-week-avg");
		return new ResponseEntity<>(reportingService.getBatchWeekSortedBarChart(batchId, week), HttpStatus.OK);
	}

	@RequestMapping(value = "/all/reports/batch/{batchId}/overall/trainee/{traineeId}/bar-batch-overall-trainee", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING', 'PANEL')")
	public ResponseEntity<Map<String, Double[]>> getBatchOverallTraineeBarChart(@PathVariable Integer batchId,
			@PathVariable Integer traineeId) {
		log.info(
				"getBatchOverallTraineeBarChart   ===>   /all/reports/batch/{batchId}/overall/trainee/{traineeId}/bar-batch-overall-trainee");
		Map<String, Double[]> result = reportingService.getBatchOverallTraineeBarChart(batchId, traineeId);
		if(result.isEmpty()){
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "{batchId}/overall/bar-batch-overall", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING', 'PANEL')")
	public ResponseEntity<Map<String, Double>> getBatchOverallBarChart(@PathVariable Integer batchId) {
		log.info("getBatchOverallBarChart   ===>   /all/reports/batch/{batchId}/overall/bar-batch-overall");
			Map<String, Double> result = reportingService.getBatchOverallBarChart(batchId);
		if(result.isEmpty()) {
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/all/reports/batch/{batchId}/week/{weekId}/trainee/{traineeId}/bar-batch-week-trainee", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING', 'PANEL')")
	public ResponseEntity<Map<String, Double[]>> getBatchWeekTraineeBarChart(@PathVariable Integer batchId,
			@PathVariable Integer weekId, @PathVariable Integer traineeId) {
		log.info(
				"getBatchWeekTraineeBarChart   ===>   /all/reports/batch/{batchId}/week/{weekId}/trainee/{traineeId}/bar-batch-week-trainee");
		Map<String, Double[]> result = reportingService.getBatchWeekTraineeBarChart(batchId, traineeId, weekId);
		if(result.isEmpty()){
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}

	/*
	 *******************************************************
	 * Line Charts
	 *******************************************************
	 */
	@RequestMapping(value = "/all/reports/batch/{batchId}/week/{week}/trainee/{traineeId}/line-trainee-up-to-week", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING', 'PANEL')")
	public ResponseEntity<Map<Integer, Double[]>> getTraineeUpToWeekLineChart(@PathVariable int batchId,
			@PathVariable int week, @PathVariable int traineeId) {
		log.info(
				"getTraineeUpToWeekLineChart   ===>   /all/reports/week/{week}/trainee/{traineeId}/line-trainee-up-to-week");
		Map<Integer, Double[]> result = reportingService.getTraineeUpToWeekLineChart(batchId, week, traineeId);
		if(result.isEmpty()){
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/all/reports/batch/{batchId}/overall/trainee/{traineeId}/line-trainee-overall", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING', 'PANEL')")
	public ResponseEntity<Map<Integer, Double[]>> getTraineeOverallLineChart(@PathVariable Integer batchId,
			@PathVariable Integer traineeId) {
		log.info(
				"getTraineeOverallLineChart   ===>   /all/reports/batch/{batchId}/overall/trainee/{traineeId}/line-trainee-overall");
		Map<Integer, Double[]> results = reportingService.getTraineeOverallLineChart(batchId, traineeId);
		if(results.size() > 0) {
			return new ResponseEntity<>(results, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(new HashMap<>(), HttpStatus.NOT_FOUND);
		}
		}

	@RequestMapping(value = "/all/reports/batch/{batchId}/overall/line-batch-overall", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING', 'PANEL')")
	public ResponseEntity<Map<Integer, Double>> getBatchOverallLineChart(@PathVariable int batchId) {
		log.info("getBatchOverallLineChart   ===>   /all/reports/batch/{batchId}/overall/line-batch-overall");
		return new ResponseEntity<>(reportingService.getBatchOverallLineChart(batchId), HttpStatus.OK);
	}

	@RequestMapping(value = "/all/reports/dashboard", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'STAGING', 'PANEL')")
	public ResponseEntity<List<Object>> getCurrentBatchesLineChart() {
		log.info("getCurrentBatchesLineChart   ===>  /all/reports/dashboard");
		List<Object> resp = reportingService.getAllCurrentBatchesLineChart();
		if(resp.isEmpty()){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/all/reports/biweeklyPanelResults", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'STAGING', 'PANEL')")
	public ResponseEntity<List<Object>> getCurrentPanelsLineChart() {
		log.info("getCurrentPanelsLineChart   ===>  /all/reports/biweeklyPanelResults");
		List<Object> resp = reportingService.getAllCurrentPanelsLineChart();
		if(resp.isEmpty()){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}

	/*
	 *******************************************************
	 * Radar Charts
	 *******************************************************
	 */

	@RequestMapping(value = "/all/reports/week/{week}/trainee/{traineeId}/radar-trainee-up-to-week", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING', 'PANEL')")
	public ResponseEntity<Map<String, Double>> getTraineeUpToWeekRadarChart(@PathVariable Integer traineeId,
			@PathVariable Integer week) {
		log.info(
				"getTraineeUpToWeekRadarChart   ===>   /all/reports/week/{week}/trainee/{traineeId}/radar-trainee-up-to-week");
		return new ResponseEntity<>(reportingService.getTraineeUpToWeekRadarChart(traineeId, week), HttpStatus.OK);
	}

	@RequestMapping(value = "/all/reports/trainee/{traineeId}/radar-trainee-overall", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING', 'PANEL')")
	public ResponseEntity<Map<String, Double>> getTraineeOverallRadarChart(@PathVariable Integer traineeId) {
		log.info("getTraineeOverallRadarChart   ===>   /all/reports/trainee/{traineeId}/radar-trainee-overall");
		return new ResponseEntity<>(reportingService.getTraineeOverallRadarChart(traineeId), HttpStatus.OK);
	}

	@RequestMapping(value = "/all/reports/batch/{batchId}/overall/radar-batch-overall", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING', 'PANEL')")
	public ResponseEntity<Map<String, Double>> getBatchOverallRadarChart(@PathVariable Integer batchId) {
		log.info("getBatchOverallRadarChart   ===>   /all/reports/batch/{batchId}/overall/radar-batch-overall");
		return new ResponseEntity<>(reportingService.getBatchOverallRadarChart(batchId), HttpStatus.OK);
	}

	@RequestMapping(value = "/all/reports/batch/{batchId}/radar-batch-all-trainees", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING', 'PANEL')")
	public ResponseEntity<Map<String, Map<String, Double>>> getBatchAllTraineesRadarChart(
			@PathVariable Integer batchId) {
		log.info("getBatchOverallRadarChart   ===>   /all/reports/batch/{batchId}/overall/radar-batch-overall");
		return new ResponseEntity<>(reportingService.getBatchAllTraineesOverallRadarChart(batchId), HttpStatus.OK);
	}

	/*
	 *******************************************************
	 * Misc.
	 *******************************************************
	 */
	@RequestMapping(value = "/all/assessments/average/{batchId}/{week}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING', 'PANEL')")
	public ResponseEntity<Double> getBatchWeekAverageValue(@PathVariable Integer batchId, @PathVariable Integer week) {
		log.info("getBatchWeekAverageValue   ===>   /all/reports/batch/{batchId}/overall/line-batch-overall");
		return new ResponseEntity<>(reportingService.getAvgBatchWeekValue(batchId, week), HttpStatus.OK);
	}

	@RequestMapping(value = "/all/assessments/categories/batch/{batchId}/week/{week}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING', 'PANEL')")
	public ResponseEntity<Set<String>> getTechnologiesForTheWeek(@PathVariable Integer batchId,
			@PathVariable Integer week) {
		log.info("getBatchWeekAverageValue   ===>   /all/reports/batch/{batchId}/overall/line-batch-overall");
		return new ResponseEntity<>(reportingService.getTechnologiesForTheWeek(batchId, week), HttpStatus.OK);
	}
	
}
