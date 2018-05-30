(function () {

    'use strict';

    angular.module("todoApp")
        .controller("TodoEditCtrl", TodoEditCtrl);

    TodoEditCtrl.$inject = ['$route','$location', '$scope', '$routeParams', 'TodoService'];

    function TodoEditCtrl($route, $location, $scope, $routeParams, TodoService) {
        TodoService.get($routeParams.todoId)
            .then(function (res) {
                $scope.todo = res.data;
            }, function () {
                $scope.todo = {};
            });
         $scope.edit = function() {
            console.log('The todo is: ');
            console.log($scope.todo);

            TodoService.put($scope.todo.id, $scope.todo.name, $scope.todo.owner, $scope.todo.priority);

            $location.path("/");
            $route.reload();

         }
    }

})();