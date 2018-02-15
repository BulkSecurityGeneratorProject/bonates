(function() {
    'use strict';

    angular
        .module('esaveApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('conta', {
            parent: 'entity',
            url: '/conta?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'esaveApp.conta.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/conta/contas.html',
                    controller: 'ContaController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('conta');
                    $translatePartialLoader.addPart('situacao');
                    $translatePartialLoader.addPart('tipoConta');
                    $translatePartialLoader.addPart('tipoInvestimento');
                    $translatePartialLoader.addPart('periodicidade');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('conta-detail', {
            parent: 'conta',
            url: '/conta/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'esaveApp.conta.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/conta/conta-detail.html',
                    controller: 'ContaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('conta');
                    $translatePartialLoader.addPart('situacao');
                    $translatePartialLoader.addPart('tipoConta');
                    $translatePartialLoader.addPart('tipoInvestimento');
                    $translatePartialLoader.addPart('periodicidade');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Conta', function($stateParams, Conta) {
                    return Conta.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'conta',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('conta-detail.edit', {
            parent: 'conta-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/conta/conta-dialog.html',
                    controller: 'ContaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Conta', function(Conta) {
                            return Conta.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('conta.new', {
            parent: 'conta',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/conta/conta-dialog.html',
                    controller: 'ContaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                conta: null,
                                situacao: null,
                                tipo: null,
                                tipoInvestimento: null,
                                vencimento: null,
                                melhorCompra: null,
                                anoMesFatura: null,
                                intervaloIR: null,
                                periodicidadeIR: null,
                                ultimoRecolhimentoIR: null,
                                aliquotaIR: null,
                                rendimento: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('conta', null, { reload: 'conta' });
                }, function() {
                    $state.go('conta');
                });
            }]
        })
        .state('conta.edit', {
            parent: 'conta',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/conta/conta-dialog.html',
                    controller: 'ContaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Conta', function(Conta) {
                            return Conta.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('conta', null, { reload: 'conta' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('conta.delete', {
            parent: 'conta',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/conta/conta-delete-dialog.html',
                    controller: 'ContaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Conta', function(Conta) {
                            return Conta.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('conta', null, { reload: 'conta' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
