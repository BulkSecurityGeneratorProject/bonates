(function() {
    'use strict';

    angular
        .module('esaveApp')
        .controller('ContaDeleteController',ContaDeleteController);

    ContaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Conta'];

    function ContaDeleteController($uibModalInstance, entity, Conta) {
        var vm = this;

        vm.conta = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Conta.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
