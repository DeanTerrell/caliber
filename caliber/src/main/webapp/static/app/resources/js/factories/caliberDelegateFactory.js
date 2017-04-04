/**
 * Delegates API calls to the appropriate API factory
 * @param $log
 * @param trainerFactory
 * @param vpFactory
 * @param qcFactory
 * @param allFactory
 * @param aggFactory
 * @returns {{}}
 */
angular.module("delegate").factory("caliberDelegate",
    function ($log, trainerFactory, vpFactory, qcFactory, allFactory, aggFactory) {
        $log.debug("Booted Delegate Factory");

        var delegate = {};

        delegate.all = {};
        delegate.trainer = {};
        delegate.qc = {};
        delegate.vp = {};
        delegate.agg = {};

        /**************************** All *****************************/
        delegate.all.createBatch = function (batchObj) {
            return allFactory.createBatch(batchObj);
        };

        delegate.all.updateBatch = function (batchObj) {
            return allFactory.updateBatch(batchObj);
        };

        delegate.all.deleteBatch = function (batchId) {
            return allFactory.deleteBatch(batchId);
        };

        delegate.all.createTrainee = function (traineeObj) {
            return allFactory.createTrainee(traineeObj);
        };

        delegate.all.updateTrainee = function (traineeObj) {
            return allFactory.updateTrainee(traineeObj);
        };

        delegate.all.deleteTrainee = function (traineeId) {
            return allFactory.deleteTrainee(traineeId);
        };

        delegate.all.getGrades = function (traineeId) {
            return allFactory.getGrades(traineeId);
        };

        delegate.all.getAllTrainers = function () {
            return allFactory.getAllTrainers();
        };
        
        /************************* Enum constants ****************************/
        
        delegate.all.enumCommonLocations = function() {
			return allFactory.enumCommonLocations();
		};
        
        delegate.all.enumAssessmentType = function () {
            return allFactory.enumAssessmentType();
        };
        delegate.all.enumNoteType = function () {
            return allFactory.enumNoteType();
        };
        delegate.all.enumQCStatus = function () {
            return allFactory.enumQCStatus();
        };
        delegate.all.enumSkillType = function () {
            return allFactory.enumSkillType();
        };
        delegate.all.enumTrainingStatus = function () {
            return allFactory.enumTrainingStatus();
        };
        delegate.all.enumTrainingType = function () {
            return allFactory.enumTrainingType();
        };

        /************************* Trainer ****************************/
        delegate.trainer.getAllBatches = function () {
            return trainerFactory.getAllBatches();
        };

        delegate.trainer.createWeek = function (batchId) {
            return trainerFactory.createWeek(batchId);
        };

        delegate.trainer.addGrade = function (gradeObj) {
            return trainerFactory.addGrade(gradeObj);
        };

        delegate.trainer.updateGrade = function (gradeObj) {
            return trainerFactory.updateGrade(gradeObj);
        };

        delegate.trainer.createAssessment = function (assessmentObj) {
            return trainerFactory.createAssessment(assessmentObj);
        };

        delegate.trainer.getAllAssessments = function (batchId, week) {
            return trainerFactory.getAllAssessments(batchId, week);
        };

        delegate.trainer.updateAssessment = function (assessmentObj) {
            return trainerFactory.updateAssessment(assessmentObj);
        };

        delegate.trainer.deleteAssessment = function (assessmentId) {
            return trainerFactory.deleteAssessment(assessmentId);
        };

        delegate.trainer.createNote = function (noteObj) {
            return trainerFactory.createNote(noteObj);
        };

        delegate.trainer.updateNote = function (noteObj) {
            return trainerFactory.updateNote(noteObj);
        };

        /*************************** VP ***************************/
        delegate.vp.getAllBatches = function () {
            return vpFactory.getAllBatches();
        };

        delegate.vp.getAllCurrentBatches = function () {
            return vpFactory.getAllCurrentBatches();
        };

        /************************** QC *****************************/
        delegate.qc.getAllBatches = function () {
            return qcFactory.getAllBatches();
        };

        delegate.qc.addGrade = function () {
            return qcFactory.addGrade();
        };

        delegate.qc.updateGrade = function (gradeObj) {
            return qcFactory.updateGrade(gradeObj);
        };

        delegate.qc.createAssessment = function (assessmentObj) {
            return qcFactory.createAssessment(assessmentObj);
        };

        delegate.qc.getAllAssessments = function (weekId) {
            return qcFactory.getAllAssessments(weekId);
        };

        delegate.qc.deleteAssessment = function (assessmentId) {
            return qcFactory.deleteAssessment(assessmentId);
        };

        delegate.qc.createNote = function (noteObj) {
            return qcFactory.createNote(noteObj);
        };

        delegate.qc.updateNote = function (noteObj) {
            return qcFactory.updateNote(noteObj);
        };

        /************************** Aggregate *****************************/
        delegate.agg.getAggTechTrainee = function (traineeId) {
            return aggFactory.techTrainee(traineeId);
        };

        delegate.agg.getAggWeekTrainee = function (traineeId) {
            return aggFactory.weekTrainee(traineeId);
        };

        delegate.agg.getAggTechBatch = function (batchId) {
            return aggFactory.techBatch(batchId);
        };

        delegate.agg.getAggWeekBatch = function (batchId) {
            return aggFactory.weekBatch(batchId);
        };

        delegate.agg.getAggTechAllBatch = function () {
            return aggFactory.techAllBatch();
        };

        delegate.agg.getAggBatchAllTrainer = function (trainerId) {
            return aggFactory.batchTrainer(trainerId);
        };

        return delegate;
    });
