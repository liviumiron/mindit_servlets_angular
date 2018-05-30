(function () {

    'use strict';

    angular.module("todoApp")
        .controller("TodoListCtrl", TodoListCtrl);

    TodoListCtrl.$inject = ['$location', '$route','$scope', 'TodoService'];

    function TodoListCtrl($location, $route, $scope, TodoService) {

        TodoService.list()
            .then(function (res) {
                $scope.todos = res.data;
            }, function () {
                $scope.todos = [];
            });


        $scope.remove = function(id) {
            console.log('The todo is: ');
            console.log($scope.todo);

            TodoService.remove(id);

            $location.path("/");
            $route.reload();

         }
    }



})();