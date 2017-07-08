import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TwitterAccount } from './twitter-account.model';
import { TwitterAccountPopupService } from './twitter-account-popup.service';
import { TwitterAccountService } from './twitter-account.service';

@Component({
    selector: 'jhi-twitter-account-delete-dialog',
    templateUrl: './twitter-account-delete-dialog.component.html'
})
export class TwitterAccountDeleteDialogComponent {

    twitterAccount: TwitterAccount;

    constructor(
        private twitterAccountService: TwitterAccountService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.twitterAccountService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'twitterAccountListModification',
                content: 'Deleted an twitterAccount'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-twitter-account-delete-popup',
    template: ''
})
export class TwitterAccountDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private twitterAccountPopupService: TwitterAccountPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.twitterAccountPopupService
                .open(TwitterAccountDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
