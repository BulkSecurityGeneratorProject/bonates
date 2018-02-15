(function() {
    'use strict';
    angular
        .module('esaveApp')
        .factory('Conta', Conta);

    Conta.$inject = ['$resource', 'DateUtils'];

    function Conta ($resource, DateUtils) {
        var resourceUrl =  'api/contas/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.ultimoRecolhimentoIR = DateUtils.convertLocalDateFromServer(data.ultimoRecolhimentoIR);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.ultimoRecolhimentoIR = DateUtils.convertLocalDateToServer(copy.ultimoRecolhimentoIR);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.ultimoRecolhimentoIR = DateUtils.convertLocalDateToServer(copy.ultimoRecolhimentoIR);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
