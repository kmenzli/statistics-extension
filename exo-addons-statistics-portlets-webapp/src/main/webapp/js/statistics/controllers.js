define( "statisticsControllers", [ "SHARED/jquery", "SHARED/juzu-ajax" ], function ( $ )
{

  var statisticsCtrl = function($scope, $http,statisticsService) {

    var statisticsContainer = $('#statistics');


      /**********************************************************************/
      /*                 POPULATE FICTIVE STAT (populate/unpopulate)        */
      /**********************************************************************/


      /** Build the populate URL*/
      $("#populator_div").live('click', function(event){


          $scope.populate('push');

      });

      // Populate method to invoke the Controller side
      $scope.populate = function(perform) {
          $http.post(statisticsContainer.jzURL('StatisticsApplication.populate') + '&perform='+perform).success(function (data) {
              $scope.setResultMessage(data, "success");
              $scope.perform =  false;


          }).error(function (data) {
                  $scope.setResultMessage(data, "error");
              });
      };

      /**********************************************************************/
      /*                 FIN POPULATE        */
      /**********************************************************************/

      $scope.resultMessage = "";
      $scope.resultMessageClass = "alert-info";

      // function which set the result message with the given style
      $scope.setResultMessage = function(text, type) {
          $scope.resultMessageClass = "alert-" + type;
          $scope.resultMessage = text;
      };



    $scope.scope = "User";

    $scope.changeScope = function(scope) {
      $scope.scope = scope;
      if(scope == 'User') {

        $scope.loadStatistics();

      } else if (scope == 'Category') {

      } else if (scope == 'CategoryId') {

      } else {

      }
    };
      /** Load stat servers **/
      $scope.loadStatistics = function() {
        $http.get(statisticsContainer.jzURL('StatisticsApplication.getStatistics')).success(function (data) {
          $scope.statistics = data.statisticBeanList;
        });

      };

      $scope.displaySearchForm = function() {
      };

      // Search action
      $scope.search = function() {

          var paramsOptions = "content=rien&category=cateoryA";
          var userParam = "&user=khemais";
          $http({
              method: 'POST',
              url: statisticsContainer.jzURL('StatisticsApplication.search'),
              data: paramsOptions + userParam,
              headers: {'Content-Type': 'application/x-www-form-urlencoded'}
          }).success(function (data) {
                  $scope.setResultMessage(data, "success");
              }).error(function (data) {
                  $scope.setResultMessage(data, "error");
              });

      };

      /**********************************************************************/
      /*                            FACETS                              */
      /**********************************************************************/

      // facets types
      $scope.facets = [];
      $scope.facetsType = [];
      $scope.isShown = false;

      $scope.loadingFacetsTree = true;

      $http.get(statisticsContainer.jzURL('StatisticsApplication.getFacets')).success(function (data) {
          $scope.facets = data.facets;
          $scope.loadingFacetsTree = false;
      });

      $scope.toggleFacetSelection = function(selectedFacet) {
          $scope.isShown = true;


      };

      // Filter action
      $scope.search = function() {

      };
  };

  return statisticsCtrl;
});