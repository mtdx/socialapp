import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Proxy } from './proxy.model';
import { ProxyPopupService } from './proxy-popup.service';
import { ProxyService } from './proxy.service';

@Component({
    selector: 'jhi-proxy-dialog',
    templateUrl: './proxy-dialog.component.html'
})
export class ProxyDialogComponent implements OnInit {

    proxy: Proxy;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private proxyService: ProxyService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.proxy.id !== undefined) {
            this.subscribeToSaveResponse(
                this.proxyService.update(this.proxy));
        } else {
            this.subscribeToSaveResponse(
                this.proxyService.create(this.proxy));
        }
    }

    private subscribeToSaveResponse(result: Observable<Proxy>) {
        result.subscribe((res: Proxy) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Proxy) {
        this.eventManager.broadcast({ name: 'proxyListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-proxy-popup',
    template: ''
})
export class ProxyPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private proxyPopupService: ProxyPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.proxyPopupService
                    .open(ProxyDialogComponent, params['id']);
            } else {
                this.modalRef = this.proxyPopupService
                    .open(ProxyDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
