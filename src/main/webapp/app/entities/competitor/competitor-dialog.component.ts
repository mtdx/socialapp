import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Competitor } from './competitor.model';
import { CompetitorPopupService } from './competitor-popup.service';
import { CompetitorService } from './competitor.service';

@Component({
    selector: 'jhi-competitor-dialog',
    templateUrl: './competitor-dialog.component.html'
})
export class CompetitorDialogComponent implements OnInit {

    competitor: Competitor;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private competitorService: CompetitorService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.competitor.id !== undefined) {
            this.subscribeToSaveResponse(
                this.competitorService.update(this.competitor), false);
        } else {
            this.subscribeToSaveResponse(
                this.competitorService.create(this.competitor), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Competitor>, isCreated: boolean) {
        result.subscribe((res: Competitor) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Competitor, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'socialappApp.competitor.created'
            : 'socialappApp.competitor.updated',
            { param : result.id }, null);

        this.eventManager.broadcast({ name: 'competitorListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-competitor-popup',
    template: ''
})
export class CompetitorPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private competitorPopupService: CompetitorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.competitorPopupService
                    .open(CompetitorDialogComponent, params['id']);
            } else {
                this.modalRef = this.competitorPopupService
                    .open(CompetitorDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
