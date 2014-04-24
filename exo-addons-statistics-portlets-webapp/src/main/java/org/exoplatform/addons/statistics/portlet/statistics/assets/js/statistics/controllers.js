define( "statisticsControllers", [ "SHARED/jquery", "SHARED/juzu-ajax" ], function ( $ )
{

  var statisticsCtrl = function($scope, $http,statisticsService) {
    var statisticsContainer = $('#statistics');

    $scope.stat = "happy";

    $scope.loadStat = function(stat) {
      $scope.stat = stat;
      if(stat == 'crazy') {

        $scope.loadStatistics();
      } else if (stat == 'freaky') {
          alert("I'm freaky");
      } else {
          alert("I'm happy");
      }
    };
      /** Load stat servers **/
      $scope.loadStatistics = function() {
        $http.get(statisticsContainer.jzURL('StatisticsApplication.getStatistics')).success(function (data) {
          $scope.globalStatistics = data.statisticBeanList;
        });

      };




  };

  return statisticsCtrl;
});