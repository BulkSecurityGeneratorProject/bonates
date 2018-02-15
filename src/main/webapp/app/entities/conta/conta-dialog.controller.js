(function() {
    'use strict';

    angular
        .module('esaveApp')
        .controller('ContaDialogController', ContaDialogController);

    ContaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Conta'];

    function ContaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Conta) {
        var vm = this;

        vm.conta = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.conta.id !== null) {
                Conta.update(vm.conta, onSaveSuccess, onSaveError);
            } else {
                Conta.save(vm.conta, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('esaveApp:contaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.ultimoRecolhimentoIR = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
