import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { Proxy } from './proxy.model';
import { ProxyPopupService } from './proxy-popup.service';
import { ProxyService } from './proxy.service';

@Component({
    selector: 'jhi-proxy-delete-dialog',
    templateUrl: './proxy-delete-dialog.component.html'
})
export class ProxyDeleteDialogComponent {

    proxy: Proxy;

    constructor(
        private proxyService: ProxyService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.proxyService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'proxyListModification',
                content: 'Deleted an proxy'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('socialappApp.proxy.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-proxy-delete-popup',
    template: ''
})
export class ProxyDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private proxyPopupService: ProxyPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.proxyPopupService
                .open(ProxyDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
