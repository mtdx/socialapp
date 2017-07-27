import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { RetweetAccount } from './retweet-account.model';
import { RetweetAccountPopupService } from './retweet-account-popup.service';
import { RetweetAccountService } from './retweet-account.service';

@Component({
    selector: 'jhi-retweet-account-dialog',
    templateUrl: './retweet-account-dialog.component.html'
})
export class RetweetAccountDialogComponent implements OnInit {

    retweetAccount: RetweetAccount;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private retweetAccountService: RetweetAccountService,
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
        if (this.retweetAccount.id !== undefined) {
            this.subscribeToSaveResponse(
                this.retweetAccountService.update(this.retweetAccount));
        } else {
            this.subscribeToSaveResponse(
                this.retweetAccountService.create(this.retweetAccount));
        }
    }

    private subscribeToSaveResponse(result: Observable<RetweetAccount>) {
        result.subscribe((res: RetweetAccount) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: RetweetAccount) {
        this.eventManager.broadcast({ name: 'retweetAccountListModification', content: 'OK'});
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
    selector: 'jhi-retweet-account-popup',
    template: ''
})
export class RetweetAccountPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private retweetAccountPopupService: RetweetAccountPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.retweetAccountPopupService
                    .open(RetweetAccountDialogComponent, params['id']);
            } else {
                this.modalRef = this.retweetAccountPopupService
                    .open(RetweetAccountDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
