angular
		.module("qc")
		.controller(
				"qcAssessController",
				function($log, $scope, $timeout, $rootScope, $filter, chartsDelegate,
						caliberDelegate, qcFactory, allBatches) {
					$log.debug("Booted Trainer Assess Controller");

					$scope.bnote = null;
					$scope.faces = [];
					$scope.weeks = [];
					$scope.batchesByYear = [];
					$scope.categories = [];
					// used to block user processes to wait for server's response
					$scope.processingNote = false; 
			

					var now = Number(Date.now());
					var twoMonthsAgo = (now-5259492000);
					var relaventBatches = [];
					
					var relBatchesCount = 0;
					for(var i=0;i<allBatches.length;i++){
						var endDate = allBatches[i].endDate;
						var startDate = allBatches[i].startDate;
						if ((endDate>twoMonthsAgo && endDate<now) || (startDate<now && endDate>now)){
							relaventBatches[relBatchesCount]=allBatches[i];
							relBatchesCount++;
						}
					}
					
					$scope.batches = relaventBatches;
					
					// Note object
					function Note(noteId, content, status, week, batch,
							trainee, maxVisibility, type, qcFeedback) {
						this.noteId = noteId;
						this.content = content;
						this.week = week;
						this.batch = batch;
						this.trainee = trainee;
						this.maxVisibility = maxVisibility;
						this.type = type;
						this.qcFeedback = qcFeedback;
						this.qcStatus = status;
					}

					// Used to sort trainees in batch alphabetically
					function compare(a, b) {
						if (a.name < b.name)
							return -1;
						if (a.name > b.name)
							return 1;
						return 0;
					}
					//Used to sort trainees in batch randomly
					function random(){
					    return 0.5 - Math.random();
					};
					/**
					 * ***************************************** UI
					 * **********************************************
					 */
		
					
					// function to get notes
					$scope.getNotes = function() {
						// Check if there are no weeks
						if ($scope.currentWeek !== undefined
								&& $scope.currentBatch !== undefined
								&& $scope.currentBatch !== null) {
							// Get qc batch notes for selected batch
							caliberDelegate.qc
									.batchNote($scope.currentBatch.batchId,
											$scope.currentWeek)
									.then(
											function(notes) {
												// If no batch note found create
												// empty note object to be used
												if (notes === "") {
													$log.debug("EMPTY!");
													$scope.bnote = new Note(
															null,
															null,
															null,
															$scope.currentWeek,
															$scope.currentBatch,
															null, "ROLE_QC",
															"QC_BATCH", true);
												}
												// If note found set the note
												// object to note content and
												// face
												else {
													$scope.bnote = notes;
													$scope.qcBatchAssess = notes.qcStatus;
												}
											});
							// Get qc notes for trainees in selected batch for
							// the week
							caliberDelegate.qc
									.traineeNote($scope.currentBatch.batchId,
											$scope.currentWeek)
									.then(
											function(notes) {
												$log.debug(notes);
												// Iterate through trainees
												for (var i = 0; i < $scope.currentBatch.trainees.length; i++) {
													var content = null;
													var status = null;
													var id = null;
													// Set note content, status
													// and
													// id to note in database if
													// found
													for (var j = 0; j < notes.length; j++) {
														if ($scope.currentBatch.trainees[i].name === notes[j].trainee.name) {
															content = notes[j].content;
															status = notes[j].qcStatus;
															id = notes[j].noteId;
															break;
														}
													}
													// Push note object into
													// array, batch set to null
													// for trainee note always
													$scope.faces
															.push(new Note(
																	id,
																	content,
																	status,
																	$scope.currentWeek,
																	null,
																	$scope.currentBatch.trainees[i],
																	"ROLE_QC",
																	"QC_TRAINEE",
																	true));
												}
											});
							// If there are no weeks
						} else if ($scope.currentBatch !== undefined
								&& $scope.currentBatch !== null) {
							$scope.bnote = null;
							for (var i = 0; i < $scope.currentBatch.trainees.length; i++) {
								$scope.faces.push(new Note(null, null, null,
										$scope.currentWeek,
										$scope.currentBatch,
										$scope.currentBatch.trainees[i],
										"ROLE_QC", "QC_TRAINEE", true));
							}
						}
					}
					//Set flags to color in database
					$scope.init = function(trainee, index){
						var flagElement = document.getElementsByClassName("glyphicon-flag")[index];
						var flagColor = trainee.flagStatus;
						if(flagColor === 'RED'){
							flagElement.setAttribute("class","glyphicon glyphicon-flag color-red");
						}else if(flagColor === 'GREEN'){
							flagElement.setAttribute("class","glyphicon glyphicon-flag color-green");
						}else if(flagColor === 'TRAINER'){
							flagElement.setAttribute("class","glyphicon glyphicon-flag color-orange");
						}else{
							flagElement.setAttribute("class","glyphicon glyphicon-flag color-white");
						}
					}

					// Used to pick face for batch
					$scope.pickOverallStatus = function(batch, pick) {
						$scope.qcBatchAssess = pick;
						$log.debug(batch.trainingName + " " + pick);
						$log.debug("bnote");
						$log.debug($scope.bnote);
						// Set batch note to pick and save it
						$scope.bnote.qcStatus = pick;
						$scope.saveQCNotes();
					};

					// starting scope vars
					$log.debug($scope.$parent.currentBatch);
					// If in reports get reports current batch
					if ($scope.$parent.currentBatch !== undefined) {
						// Set batch to batch selected in reports if available
						$scope.currentBatch = $scope.$parent.currentBatch;
					} else {
						// Set batch to batch selected on assess page
						$scope.currentBatch = $scope.batches[0];
					}

					// create an array of numbers for number of weeks in the
					// batch selected
					if ($scope.currentBatch) {
						for (var y = 1; y <= $scope.currentBatch.weeks; y++) {
							$scope.weeks.push(y);
						}
					}

					// Start function for reports to use and assess
					function start() {
						if ($scope.batches[0]) {
							$scope.trainingNameDate = $scope.batches[0].trainer.name
									+ " - " + $filter('date')($scope.batches[0].startDate, 'shortDate');
						}
						var curYear = new Date();
						$scope.selectedYear = curYear.getFullYear();
						batchYears();

						// Sort trainees randomly
						if ($scope.currentBatch) {
							$scope.currentBatch.trainees.sort(random);
						}

						// Set current week to first week
						// If reports week is selected
						if ($scope.$parent.reportCurrentWeek !== undefined
								&& $scope.$parent.reportCurrentWeek !== "(All)") {
							$log.debug("Got report week");
							// Set current week to week selected in report
							$scope.currentWeek = $scope.$parent.reportCurrentWeek;
						} else {
							$log.debug("No report week");
							// Set week to first week in batch
							$scope.currentWeek = $scope.weeks[$scope.weeks.length - 1];
						}

						// get status types
						$scope.qcStatusTypes = [];
						caliberDelegate.all.enumQCStatus().then(
								function(types) {
									$log.debug(types);
									$scope.qcStatusTypes = types;
								});

						
						// load note types
						caliberDelegate.all.enumNoteType().then(
								function(noteTypes) {
									$log.debug(noteTypes);
									// do something with note type
								});
						// Reset notes to empty array
						$scope.faces = [];
						// Get notes
						$scope.getNotes();
						categories();
					}
			

					// Function for individual qc feedback for trainee note
					$scope.pickIndividualStatus = function(trainee, status,
							index) {
						var weekBefore;
						//Get QC Trainee notes for previous week and make sure it's not the first week 
						if($scope.currentWeek-1 !== 1){
							caliberDelegate.qc
								.traineeNote($scope.currentBatch.batchId,
										$scope.currentWeek-1)
								.then(
										function(notes) {
											weekBefore = notes;
											var element = document.getElementsByClassName("glyphicon-flag")[index];
											var color = trainee.flagStatus;
											for(var t = 0; t < weekBefore.length; t++){
												if(weekBefore[t].trainee.traineeId === trainee.traineeId){
													if(weekBefore[t] !== undefined){
														//red flag if recently there are 2 red weeks consecutively
														if(status === 'Poor' && weekBefore[t].qcStatus === 'Poor'){
															element.setAttribute("class","glyphicon glyphicon-flag color-red");
															color = 'RED';
															trainee.flagNotes = "Trainee received two consecutive weeks of negative QC feedback";
														}//if no change, keep it to previous flag
													}
													else{
														if(color === 'RED'){
															element.setAttribute("class","glyphicon glyphicon-flag color-red");
														}else if(color === 'GREEN'){
															element.setAttribute("class","glyphicon glyphicon-flag color-green");
														}else if(color === 'TRAINER'){
															element.setAttribute("class","glyphicon glyphicon-flag color-orange");
														}else{
															element.setAttribute("class","glyphicon glyphicon-flag color-white");
														}
													}
													//add onto trainee object for update
													trainee.batch = {
															batchId : $scope.currentBatch.batchId
														};
													trainee.flagStatus = color;
													//update trainee with flag color 
													var updateTrainee = function() {
															caliberDelegate.all
																	.updateTrainee(trainee)
													};
													updateTrainee();
													// Set individual note to status selected
													$scope.faces[index].qcStatus = status;
													// Save note
													$scope.saveTraineeNote(index);
													
													$log.debug($scope.faces[index]);
													//Calculate batch average for QC week
													caliberDelegate.qc.calculateQCAverage($scope.bnote)
													.then(
															function(batchNote){
																$scope.bnote = batchNote;
																$scope.qcBatchAssess = batchNote.qcStatus;
															}
													);
													break;
												}
											}
										});
						}
					
					};

					// default -- view assessments table
					$scope.currentView = true;

					// back button
					$scope.back = function() {
						$scope.currentView = true;
					};

					/**
					 * Batch drop down select Select batches from current year
					 */
					$scope.selectCurrentBatch = function(index) {
						$log.debug("SELECTED DIFFERENT BATCH");
						if ($scope.$parent.currentBatch !== undefined) {
							// Set current batch to batch selected in report
							$scope.currentBatch = $scope.$parent.currentBatch;
						} else {
							// Set current batch to first batch in the year
							$scope.currentBatch = $scope.batchesByYear[index];
						}
						// Sort trainees
						$scope.currentBatch.trainees.sort(random);
						// Create week array for batch selected
						$scope.weeks = [];
						for (var i = 1; i <= $scope.currentBatch.weeks; i++) {
							$scope.weeks.push(i);
						}
						// Set current week to first week
						$scope.currentWeek = $scope.weeks[$scope.weeks.length - 1];
						// Retrieve notes
						$scope.getNotes();
						// Reset qc status
						wipeFaces();
						categories();
						$scope.trainingNameDate = $scope.currentBatch.trainer.name
								+ " - " + $filter('date')($scope.currentBatch.startDate, 'shortDate');
					};

					// Select week
					$scope.selectWeek = function(index) {
						// If reports week is selected
						if ($scope.$parent.reportCurrentWeek !== undefined
								&& $scope.$parent.reportCurrentWeek !== "(All)") {
							// Set current week to week selected in reports
							$scope.currentWeek = $scope.$parent.reportCurrentWeek;
						} else {
							// Set week selected in assess page
							$scope.currentWeek = $scope.weeks[index];
						}
						// Get notes
						$scope.getNotes();
						// Reset qc status
						wipeFaces();
						categories();
					};

					// Show week
					$scope.showActiveWeek = function(index) {
						if ($scope.currentWeek === $scope.weeks[index])
							return "active";
					}

					// Function to add week
					$scope.createWeek = function() {

						caliberDelegate.trainer.createWeek(
								$scope.currentBatch.batchId).then(function() {
							$scope.currentBatch.weeks += 1;
							$scope.weeks.push($scope.currentBatch.weeks);
							$scope.showActiveWeek($scope.currentBatch.weeks);
							// Select the index of the week
							$scope.selectWeek($scope.currentBatch.weeks - 1);
						});
					};

					// ///// wipe faces ;) and selections ///////
					function wipeFaces() {
						$scope.faces = [];
						$scope.qcBatchAssess = null;
						$scope.finalQCBatchNote = null;
					}

					// Get categories for the week
					function categories() {
						if ($scope.currentBatch) {
							caliberDelegate.qc.getAllAssessmentCategories(
									$scope.currentBatch.batchId,
									$scope.currentWeek).then(
									function(response) {
										$scope.categories = response;
									});
						}
					}

					/**
					 * ************************************************ GETTING
					 * NOTES ON TRAINEE
					 * *********************************************
					 */
					// Note for trainee
					$scope.noteOnTrainee = function(index) {
						return $scope.faces[index];
					};

					/**
					 * ******************************************* QCFeedBack
					 * **********************************************************
					 */
					// Save trainee note for ng-blur
					$scope.saveTraineeNote = function(index) {
						$scope.processingNote = true;
						$log.debug($scope.faces[index]);
						// Create note if noteId is null
						if ($scope.faces[index].noteId === null
								|| $scope.faces[index].noteId === undefined) {
							$log.debug("create");
							//check if QC status is unpicked, default to undefined status if true
							if($scope.faces[index].qcStatus == null){
								$scope.faces[index].qcStatus = $scope.qcStatusTypes[4];
							}
							caliberDelegate.qc.createNote($scope.faces[index])
									.then(function(id) {
										$scope.faces[index].noteId = id;
										$scope.processingNote = false;
									});
						}
						// Update if note has a noteId
						else {
							$log.debug("update");
							caliberDelegate.qc.updateNote($scope.faces[index]);
							$scope.processingNote = false;
						}
					};

					// Save batch note for ng-blur
					$scope.saveQCNotes = function() {
						$scope.processingNote = true;
						// Create note if noteId is null
						if ($scope.bnote.noteId === null
								|| $scope.bnote.noteId === undefined) {
							caliberDelegate.qc.createNote($scope.bnote).then(
							// Set id to created notes id
							function(id) {
								$scope.bnote.noteId = id;
								$scope.processingNote = false;
							});
						}
						// Update existing note
						else {
							$log
									.debug(document
											.getElementById("qcBatchNotes").value);
							$log.debug(caliberDelegate.qc
									.updateNote($scope.bnote));
							$scope.processingNote = false;
						}
					}
					

					/***********************************************************
					 * Save Button **
					 **********************************************************/

					$scope.showSaving = false;
					$scope.showCheck = false;
					$scope.showFloppy = true;
					$scope.saveQCandTrainee = function() {
						$scope.showFloppy = false
						$timeout(function() {
							$scope.showSaving = true;
						}, 480).then(function() {
							$timeout(function() {
								$scope.showSaving = false;
							}, 1000).then(function() {
								$scope.showCheck = true;
								$timeout(function() {
									$scope.showCheck = false;
								}, 2000).then(function() {
									$scope.showFloppy = true;
								});
							});
						});
					}
					// Call start function
					start();
					// Call start function when on reports page and batch and
					// week selected
					$rootScope.$on('qcBatchOverall', function() {
						$log.debug("QCBATCHOVERALL");
						start();
					});

					/**
					 * **************************************************
					 * Duplicate code from trainerManageController.js
					 * **************************************************
					 */

					/** Filter batches by year * */
					$scope.years = addYears();
					function addYears() {
						var currentYear = new Date().getFullYear();
						$scope.selectedYear = currentYear;

						var data = [];
						// List all years from (current year - 1) --> (current
						// year + 1)
						for (var y = currentYear + 1; y >= currentYear - 1; y--) {
							data.push(y)
						}
						return data;
					}

					$scope.selectYear = function(index) {
						$scope.selectedYear = $scope.years[index];
						sortByDate($scope.selectedYear);
						batchYears();
						$scope.currentBatch = $scope.batchesByYear[0];

						// Create week array for batch selected
						$scope.weeks = [];
						if ($scope.currentBatch !== null
								&& $scope.currentBatch !== undefined
								&& $scope.currentBatch.weeks !== null
								&& $scope.currentBatch.weeks !== undefined) {
							for (var i = 1; i <= $scope.currentBatch.weeks; i++) {
								$scope.weeks.push(i);
							}
						}

						$scope.currentWeek = $scope.weeks[$scope.weeks.length - 1];

						if ($scope.batchesByYear.length === 0) {
							$scope.noBatches = true;
							$scope.noBatchesMessage = "No Batches were found for this year.";
						} else {
							$scope.noBatches = false;
							$scope.selectedYear = $scope.years[index];
							sortByDate($scope.selectedYear);

							if ($scope.batchesByYear.length > 0) {
								$scope.trainingNameDate = $scope.batchesByYear[0].trainer.name
										+ " - "
										+ $filter('date')($scope.batchesByYear[0].startDate, 'shortDate');
								$scope.thereAreBatches = true;
							} else {
								/**
								 * If no batches are available, display that
								 * there are no batches
								 */

								$scope.trainingNameDate = "No Batch Found";
								$scope.currentView = false;
							}

							$log.debug($scope.batchesByYear);
						}
						$scope.getNotes();
						categories();
						wipeFaces();
					};

					function sortByDate(currentYear) {
						$scope.selectedBatches = [];
						for (var i = 0; i < $scope.batches.length; i++) {
							var date = new Date($scope.batches[i].startDate);
							if (date.getFullYear() === currentYear) {
								$scope.selectedBatches.push($scope.batches[i]);
							}
						}
					}

					/**
					 * ********************************************************************
					 * Display Batch By Years
					 * ********************************************************************
					 */

					/**
					 * Store batch object(s) according to selected year into an
					 * array
					 */
					function batchYears() {
						$scope.batchesByYear = [];

						for (var i = 0; i < $scope.batches.length; i++) {
							
							var date = new Date($scope.batches[i].startDate);

							if ($scope.selectedYear === parseInt(date.getFullYear())) {
								$scope.batchesByYear.push($scope.batches[i]);
							}
						}
					}
					
					/**
					 * ********************************************************************
					 * Flag Control
					 * ********************************************************************
					 */
					
					/** creates a function triggered by a click on the trainee's name, that toggles the color of the flag
					 *  and opens an input box to comment on the color change 
					 */
					
					var status = null;
					$scope.toggleColor = function(trainee, index) {
						var flagElement = document.getElementsByClassName("glyphicon-flag")[index];
						var initialStatus = trainee.flagStatus;
				        if (flagElement.getAttribute("class") === "glyphicon glyphicon-flag color-white") {
				        		status = "RED";
				        		flagElement.setAttribute("class","glyphicon glyphicon-flag color-red");
				        } else if (flagElement.getAttribute("class") === "glyphicon glyphicon-flag color-red") {
				        		status = "GREEN";
				        		flagElement.setAttribute("class","glyphicon glyphicon-flag color-green");
				        } else if (flagElement.getAttribute("class") === "glyphicon glyphicon-flag color-green") {
				        		status = "TRAINER";
				        		flagElement.setAttribute("class","glyphicon glyphicon-flag color-orange");
				        } else if (flagElement.getAttribute("class") === "glyphicon glyphicon-flag color-orange") {
				        		status = "NONE";
				        		flagElement.setAttribute("class","glyphicon glyphicon-flag color-white");
				        }
				        if(initialStatus !== status){
				        		commentBox(flagElement, status, initialStatus, index, trainee);
				        } else {
				        		flagElement.nextSibling.nextSibling.setAttribute("style","display:none;");
				        }
				    }
					
					/** opens up a comment form box when the flag color changes 
					 */
					function commentBox(flag, status, initialStatus, index, trainee){
						flag.nextSibling.nextSibling.removeAttribute("style");
						flag.nextSibling.nextSibling.setAttribute("style","display:inline-block; position:absolute; padding:5px; border-radius:5px; margin-left:5px; background-color: white; border: solid #ccc 1px; z-index: 1");
						$scope.closeComment = function(){
							document.getElementsByClassName("commentForm")[index].setAttribute("style","display:none;");
							if(initialStatus === "RED"){
								flag.setAttribute("class","glyphicon glyphicon-flag color-red");
							} else if (initialStatus === "GREEN"){
								flag.setAttribute("class","glyphicon glyphicon-flag color-green");
							} else if (initialStatus === "TRAINER"){
								flag.setAttribute("class","glyphicon glyphicon-flag color-orange");
							} else {
								flag.setAttribute("class","glyphicon glyphicon-flag color-white");
							}
						}
						trainee.batch = {
	                            batchId : $scope.currentBatch.batchId
	                        };
					}
					
					/** saves changes the flag status in the javascript object and persists it back to the database
					 *  upon submission of the comment form and closes the form
					 */
					$scope.updateFlag = function(trainee, index){
						trainee.flagStatus = status;
						caliberDelegate.all
                        	.updateTrainee(trainee);
						document.getElementsByClassName("commentForm")[index].setAttribute("style","display:none;");
					}
					
					//show flagNotes when hovering over flag
					$scope.showNotes = function(index){
						if($scope.currentBatch.trainees[index].flagNotes != null){
							document.getElementsByClassName("notes")[index].setAttribute("style",
							"z-index: 1; display:inline-block; position:absolute; padding:5px; " +
							"border: 1px solid #CCC; border-radius: 5px; background-color: white");
						}
					}
					 					
 					//hide flagNotes when no there is no flag hover 
 					$scope.hideNotes = function(index){
							document.getElementsByClassName("notes")[index].setAttribute("style", "display: none");
		 			}
 					
				});
