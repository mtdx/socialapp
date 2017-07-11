import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TwitterMessage } from './twitter-message.model';
import { TwitterMessagePopupService } from './twitter-message-popup.service';
import { TwitterMessageService } from './twitter-message.service';

@Component({
    selector: 'jhi-twitter-message-dialog',
    templateUrl: './twitter-message-dialog.component.html'
})
export class TwitterMessageDialogComponent implements OnInit {

    twitterMessage: TwitterMessage;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private twitterMessageService: TwitterMessageService,
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
        if (this.twitterMessage.id !== undefined) {
            this.subscribeToSaveResponse(
                this.twitterMessageService.update(this.twitterMessage));
        } else {
            this.subscribeToSaveResponse(
                this.twitterMessageService.create(this.twitterMessage));
        }
    }

    private subscribeToSaveResponse(result: Observable<TwitterMessage>) {
        result.subscribe((res: TwitterMessage) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: TwitterMessage) {
        this.eventManager.broadcast({ name: 'twitterMessageListModification', content: 'OK'});
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
    selector: 'jhi-twitter-message-popup',
    template: ''
})
export class TwitterMessagePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private twitterMessagePopupService: TwitterMessagePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.twitterMessagePopupService
                    .open(TwitterMessageDialogComponent, params['id']);
            } else {
                this.modalRef = this.twitterMessagePopupService
                    .open(TwitterMessageDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
