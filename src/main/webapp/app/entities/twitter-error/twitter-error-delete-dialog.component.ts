import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { TwitterError } from './twitter-error.model';
import { TwitterErrorPopupService } from './twitter-error-popup.service';
import { TwitterErrorService } from './twitter-error.service';

@Component({
    selector: 'jhi-twitter-error-delete-dialog',
    templateUrl: './twitter-error-delete-dialog.component.html'
})
export class TwitterErrorDeleteDialogComponent {

    twitterError: TwitterError;

    constructor(
        private twitterErrorService: TwitterErrorService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.twitterErrorService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'twitterErrorListModification',
                content: 'Deleted an twitterError'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('socialappApp.twitterError.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-twitter-error-delete-popup',
    template: ''
})
export class TwitterErrorDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private twitterErrorPopupService: TwitterErrorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.twitterErrorPopupService
                .open(TwitterErrorDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
