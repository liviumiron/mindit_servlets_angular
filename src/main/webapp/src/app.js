(function () {
    'use strict';

    angular.module('todoApp', ['ngRoute'])
        .config(Config);

    Config.$inject = ['$routeProvider', '$locationProvider'];

    function Config($routeProvider, $locationProvider) {
        $locationProvider.hashPrefix('');

        $routeProvider
            .when('/',
            {
                templateUrl: 'src/pages/todo/list/list.html',
                controller: 'TodoListCtrl'
            })
            .when('/todoDetail/:todoId',
                {
                    templateUrl: 'src/pages/todo/details/detail.html',
                    controller: 'TodoDetailCtrl'
                })
            .when('/todoEdit/:todoId',
                {
                    templateUrl: 'src/pages/todo/edit/edit.html',
                    controller: 'TodoEditCtrl'
                })
            .otherwise({redirectTo: '/'});
    }

})();
