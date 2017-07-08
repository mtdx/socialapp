import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TwitterAccount } from './twitter-account.model';
import { TwitterAccountPopupService } from './twitter-account-popup.service';
import { TwitterAccountService } from './twitter-account.service';
import { Avatar, AvatarService } from '../avatar';
import { Header, HeaderService } from '../header';
import { Proxy, ProxyService } from '../proxy';
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

    proxies: Proxy[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private twitterAccountService: TwitterAccountService,
        private avatarService: AvatarService,
        private headerService: HeaderService,
        private proxyService: ProxyService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.avatarService.query()
            .subscribe((res: ResponseWrapper) => { this.avatars = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.headerService.query()
            .subscribe((res: ResponseWrapper) => { this.headers = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.proxyService.queryrestrict()
            .subscribe((res: ResponseWrapper) => { this.proxies = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.twitterAccount.id !== undefined) {
            this.subscribeToSaveResponse(
                this.twitterAccountService.update(this.twitterAccount));
        } else {
            this.subscribeToSaveResponse(
                this.twitterAccountService.create(this.twitterAccount));
        }
    }

    private subscribeToSaveResponse(result: Observable<TwitterAccount>) {
        result.subscribe((res: TwitterAccount) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: TwitterAccount) {
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

    trackProxyById(index: number, item: Proxy) {
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
