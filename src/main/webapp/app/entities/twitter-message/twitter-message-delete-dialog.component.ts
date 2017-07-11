import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TwitterMessage } from './twitter-message.model';
import { TwitterMessagePopupService } from './twitter-message-popup.service';
import { TwitterMessageService } from './twitter-message.service';

@Component({
    selector: 'jhi-twitter-message-delete-dialog',
    templateUrl: './twitter-message-delete-dialog.component.html'
})
export class TwitterMessageDeleteDialogComponent {

    twitterMessage: TwitterMessage;

    constructor(
        private twitterMessageService: TwitterMessageService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.twitterMessageService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'twitterMessageListModification',
                content: 'Deleted an twitterMessage'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-twitter-message-delete-popup',
    template: ''
})
export class TwitterMessageDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private twitterMessagePopupService: TwitterMessagePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.twitterMessagePopupService
                .open(TwitterMessageDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
