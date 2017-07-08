import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Competitor } from './competitor.model';
import { CompetitorPopupService } from './competitor-popup.service';
import { CompetitorService } from './competitor.service';

@Component({
    selector: 'jhi-competitor-delete-dialog',
    templateUrl: './competitor-delete-dialog.component.html'
})
export class CompetitorDeleteDialogComponent {

    competitor: Competitor;

    constructor(
        private competitorService: CompetitorService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.competitorService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'competitorListModification',
                content: 'Deleted an competitor'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-competitor-delete-popup',
    template: ''
})
export class CompetitorDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private competitorPopupService: CompetitorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.competitorPopupService
                .open(CompetitorDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
