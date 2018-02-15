(function() {
    'use strict';

    angular
        .module('esaveApp')
        .controller('ContaDetailController', ContaDetailController);

    ContaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Conta'];

    function ContaDetailController($scope, $rootScope, $stateParams, previousState, entity, Conta) {
        var vm = this;

        vm.conta = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('esaveApp:contaUpdate', function(event, result) {
            vm.conta = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
