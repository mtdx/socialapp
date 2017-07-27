import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { RetweetAccount } from './retweet-account.model';
import { RetweetAccountPopupService } from './retweet-account-popup.service';
import { RetweetAccountService } from './retweet-account.service';

@Component({
    selector: 'jhi-retweet-account-delete-dialog',
    templateUrl: './retweet-account-delete-dialog.component.html'
})
export class RetweetAccountDeleteDialogComponent {

    retweetAccount: RetweetAccount;

    constructor(
        private retweetAccountService: RetweetAccountService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.retweetAccountService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'retweetAccountListModification',
                content: 'Deleted an retweetAccount'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-retweet-account-delete-popup',
    template: ''
})
export class RetweetAccountDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private retweetAccountPopupService: RetweetAccountPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.retweetAccountPopupService
                .open(RetweetAccountDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
