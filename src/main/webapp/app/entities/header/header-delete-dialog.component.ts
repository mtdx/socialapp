import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { Header } from './header.model';
import { HeaderPopupService } from './header-popup.service';
import { HeaderService } from './header.service';

@Component({
    selector: 'jhi-header-delete-dialog',
    templateUrl: './header-delete-dialog.component.html'
})
export class HeaderDeleteDialogComponent {

    header: Header;

    constructor(
        private headerService: HeaderService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.headerService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'headerListModification',
                content: 'Deleted an header'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('socialappApp.header.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-header-delete-popup',
    template: ''
})
export class HeaderDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private headerPopupService: HeaderPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.headerPopupService
                .open(HeaderDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
