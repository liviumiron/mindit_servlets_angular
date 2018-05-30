(function () {

    'use strict';

    angular.module("todoApp")
        .service("TodoService", TodoService);

    TodoService.$inject = ['$http'];

    function TodoService($http) {
        return {
            list: function () {
                return $http.get('todos');
            },

            get: function (id) {
                var requestConfig = {
                    params: {id: id}
                };
                return $http.get('todos', requestConfig);
            },

            post: function (name, owner, priority) {
                var requestConfig = {
                    params: {
                        name: name,
                        owner: owner,
                        priority: priority
                    }
                };
                return $http.post('todos', requestConfig);
            },


            put: function (id, name, owner, priority) {
                  var requestConfig = {
                       params: {
                            id: id,
                            name: name,
                            owner: owner,
                            priority: priority
                       }
                   };
                   return $http.post('todos', requestConfig);
             },
            remove: function (id) {
               var requestConfig = {
                    params: {
                        id: id,
                        delete: 'true'
                    }
                };
                return $http.post('todos', requestConfig);
            }
        };
    }

})();