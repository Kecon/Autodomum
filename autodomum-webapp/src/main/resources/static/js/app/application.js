var application = angular.module('application', [
  'ngRoute',
  'ngSanitize',
  'ApplicationControllers'
]);

application.config(['$routeProvider', function($routeProvider) {
    $routeProvider.
        when('/', {
            templateUrl: 'views/overview.html',
            controller: 'OverviewController'
        }).
        otherwise({
            redirectTo: '/'
        });
}]);
