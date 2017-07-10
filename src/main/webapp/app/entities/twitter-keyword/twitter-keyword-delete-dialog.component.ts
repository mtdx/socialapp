import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TwitterKeyword } from './twitter-keyword.model';
import { TwitterKeywordPopupService } from './twitter-keyword-popup.service';
import { TwitterKeywordService } from './twitter-keyword.service';

@Component({
    selector: 'jhi-twitter-keyword-delete-dialog',
    templateUrl: './twitter-keyword-delete-dialog.component.html'
})
export class TwitterKeywordDeleteDialogComponent {

    twitterKeyword: TwitterKeyword;

    constructor(
        private twitterKeywordService: TwitterKeywordService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.twitterKeywordService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'twitterKeywordListModification',
                content: 'Deleted an twitterKeyword'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-twitter-keyword-delete-popup',
    template: ''
})
export class TwitterKeywordDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private twitterKeywordPopupService: TwitterKeywordPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.twitterKeywordPopupService
                .open(TwitterKeywordDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
