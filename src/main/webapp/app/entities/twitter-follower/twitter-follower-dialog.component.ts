import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { TwitterFollower } from './twitter-follower.model';
import { TwitterFollowerPopupService } from './twitter-follower-popup.service';
import { TwitterFollowerService } from './twitter-follower.service';
import { Competitor, CompetitorService } from '../competitor';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-twitter-follower-dialog',
    templateUrl: './twitter-follower-dialog.component.html'
})
export class TwitterFollowerDialogComponent implements OnInit {

    twitterFollower: TwitterFollower;
    authorities: any[];
    isSaving: boolean;

    competitors: Competitor[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private twitterFollowerService: TwitterFollowerService,
        private competitorService: CompetitorService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.competitorService.query()
            .subscribe((res: ResponseWrapper) => { this.competitors = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.twitterFollower.id !== undefined) {
            this.subscribeToSaveResponse(
                this.twitterFollowerService.update(this.twitterFollower), false);
        } else {
            this.subscribeToSaveResponse(
                this.twitterFollowerService.create(this.twitterFollower), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<TwitterFollower>, isCreated: boolean) {
        result.subscribe((res: TwitterFollower) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: TwitterFollower, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'socialappApp.twitterFollower.created'
            : 'socialappApp.twitterFollower.updated',
            { param : result.id }, null);

        this.eventManager.broadcast({ name: 'twitterFollowerListModification', content: 'OK'});
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

    trackCompetitorById(index: number, item: Competitor) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-twitter-follower-popup',
    template: ''
})
export class TwitterFollowerPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private twitterFollowerPopupService: TwitterFollowerPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.twitterFollowerPopupService
                    .open(TwitterFollowerDialogComponent, params['id']);
            } else {
                this.modalRef = this.twitterFollowerPopupService
                    .open(TwitterFollowerDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
