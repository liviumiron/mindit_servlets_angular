(function () {

    'use strict';

    angular.module("todoApp")
        .controller("TodoEditCtrl", TodoEditCtrl);

    TodoEditCtrl.$inject = ['$scope', '$routeParams', 'TodoService'];

    function TodoEditCtrl($scope, $routeParams, TodoService) {
        TodoService.get($routeParams.todoId)
            .then(function (res) {
                $scope.todo = res.data;
            }, function () {
                $scope.todo = {};
            });
    }

})();