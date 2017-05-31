import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { TwitterAccount } from './twitter-account.model';
import { TwitterAccountPopupService } from './twitter-account-popup.service';
import { TwitterAccountService } from './twitter-account.service';
import { Avatar, AvatarService } from '../avatar';
import { Header, HeaderService } from '../header';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-twitter-account-dialog',
    templateUrl: './twitter-account-dialog.component.html'
})
export class TwitterAccountDialogComponent implements OnInit {

    twitterAccount: TwitterAccount;
    authorities: any[];
    isSaving: boolean;

    avatars: Avatar[];

    headers: Header[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private twitterAccountService: TwitterAccountService,
        private avatarService: AvatarService,
        private headerService: HeaderService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.avatarService.query()
            .subscribe((res: ResponseWrapper) => { this.avatars = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.headerService.query()
            .subscribe((res: ResponseWrapper) => { this.headers = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.twitterAccount.id !== undefined) {
            this.subscribeToSaveResponse(
                this.twitterAccountService.update(this.twitterAccount), false);
        } else {
            this.subscribeToSaveResponse(
                this.twitterAccountService.create(this.twitterAccount), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<TwitterAccount>, isCreated: boolean) {
        result.subscribe((res: TwitterAccount) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: TwitterAccount, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'socialappApp.twitterAccount.created'
            : 'socialappApp.twitterAccount.updated',
            { param : result.id }, null);

        this.eventManager.broadcast({ name: 'twitterAccountListModification', content: 'OK'});
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

    trackAvatarById(index: number, item: Avatar) {
        return item.id;
    }

    trackHeaderById(index: number, item: Header) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-twitter-account-popup',
    template: ''
})
export class TwitterAccountPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private twitterAccountPopupService: TwitterAccountPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.twitterAccountPopupService
                    .open(TwitterAccountDialogComponent, params['id']);
            } else {
                this.modalRef = this.twitterAccountPopupService
                    .open(TwitterAccountDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
