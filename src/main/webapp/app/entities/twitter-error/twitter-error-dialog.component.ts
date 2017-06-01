import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { TwitterError } from './twitter-error.model';
import { TwitterErrorPopupService } from './twitter-error-popup.service';
import { TwitterErrorService } from './twitter-error.service';

@Component({
    selector: 'jhi-twitter-error-dialog',
    templateUrl: './twitter-error-dialog.component.html'
})
export class TwitterErrorDialogComponent implements OnInit {

    twitterError: TwitterError;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private twitterErrorService: TwitterErrorService,
        private eventManager: EventManager
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
        if (this.twitterError.id !== undefined) {
            this.subscribeToSaveResponse(
                this.twitterErrorService.update(this.twitterError), false);
        } else {
            this.subscribeToSaveResponse(
                this.twitterErrorService.create(this.twitterError), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<TwitterError>, isCreated: boolean) {
        result.subscribe((res: TwitterError) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: TwitterError, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'socialappApp.twitterError.created'
            : 'socialappApp.twitterError.updated',
            { param : result.id }, null);

        this.eventManager.broadcast({ name: 'twitterErrorListModification', content: 'OK'});
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
    selector: 'jhi-twitter-error-popup',
    template: ''
})
export class TwitterErrorPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private twitterErrorPopupService: TwitterErrorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.twitterErrorPopupService
                    .open(TwitterErrorDialogComponent, params['id']);
            } else {
                this.modalRef = this.twitterErrorPopupService
                    .open(TwitterErrorDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
