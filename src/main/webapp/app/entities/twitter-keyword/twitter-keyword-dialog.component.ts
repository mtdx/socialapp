import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TwitterKeyword } from './twitter-keyword.model';
import { TwitterKeywordPopupService } from './twitter-keyword-popup.service';
import { TwitterKeywordService } from './twitter-keyword.service';

@Component({
    selector: 'jhi-twitter-keyword-dialog',
    templateUrl: './twitter-keyword-dialog.component.html'
})
export class TwitterKeywordDialogComponent implements OnInit {

    twitterKeyword: TwitterKeyword;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private twitterKeywordService: TwitterKeywordService,
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
        if (this.twitterKeyword.id !== undefined) {
            this.subscribeToSaveResponse(
                this.twitterKeywordService.update(this.twitterKeyword));
        } else {
            this.subscribeToSaveResponse(
                this.twitterKeywordService.create(this.twitterKeyword));
        }
    }

    private subscribeToSaveResponse(result: Observable<TwitterKeyword>) {
        result.subscribe((res: TwitterKeyword) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: TwitterKeyword) {
        this.eventManager.broadcast({ name: 'twitterKeywordListModification', content: 'OK'});
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
    selector: 'jhi-twitter-keyword-popup',
    template: ''
})
export class TwitterKeywordPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private twitterKeywordPopupService: TwitterKeywordPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.twitterKeywordPopupService
                    .open(TwitterKeywordDialogComponent, params['id']);
            } else {
                this.modalRef = this.twitterKeywordPopupService
                    .open(TwitterKeywordDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
