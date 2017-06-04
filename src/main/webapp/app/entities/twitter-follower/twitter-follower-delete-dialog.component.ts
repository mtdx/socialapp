import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { TwitterFollower } from './twitter-follower.model';
import { TwitterFollowerPopupService } from './twitter-follower-popup.service';
import { TwitterFollowerService } from './twitter-follower.service';

@Component({
    selector: 'jhi-twitter-follower-delete-dialog',
    templateUrl: './twitter-follower-delete-dialog.component.html'
})
export class TwitterFollowerDeleteDialogComponent {

    twitterFollower: TwitterFollower;

    constructor(
        private twitterFollowerService: TwitterFollowerService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.twitterFollowerService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'twitterFollowerListModification',
                content: 'Deleted an twitterFollower'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('socialappApp.twitterFollower.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-twitter-follower-delete-popup',
    template: ''
})
export class TwitterFollowerDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private twitterFollowerPopupService: TwitterFollowerPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.twitterFollowerPopupService
                .open(TwitterFollowerDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
